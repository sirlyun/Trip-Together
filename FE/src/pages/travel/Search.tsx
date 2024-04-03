import React, {useState} from 'react';
import {
  Wrapper,
  SearchInput,
  SearchResult,
  SearchResultBox,
} from './SearchStyle';
import {NavigationProp, useNavigation} from '@react-navigation/native';
import {SearchStackParams} from '../../interfaces/router/SearchStackParams';
import {useAppDispatch} from '../../store/hooks';
import {useFocusEffect} from '@react-navigation/native';
// import DismissKeyboardView from '../../components/common/DismissKeyboardView';
import {StyleSheet, FlatList, Text} from 'react-native';
import {setDisplay} from '../../store/slices/tabState';
import axios from 'axios';
import {setStartRegion} from '../../store/slices/trip';
import getToken from '../../hooks/getToken';
import {TRIP_API_URL} from '@env';

interface CityResult {
  region_id: number;
  nation: string;
  city_name: string;
  latitude: string;
  longitude: string;
}

const Search = () => {
  const dispatch = useAppDispatch();
  const navigation = useNavigation<NavigationProp<SearchStackParams>>();
  const [searchText, setSearchText] = useState('');
  const [searchResults, setSearchResults] = useState<CityResult[]>([]);

  useFocusEffect(() => {
    dispatch(setDisplay(false));
  });

  const handleSearchChange = async (text: string) => {
    setSearchText(text);
    const {access_token} = await getToken();

    try {
      const response = await axios.get(
        `${TRIP_API_URL}/api/attraction/v1/regions?name=${text}`,
        {
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
        },
      );
      const regions = response.data.data;
      setSearchResults(regions.regions);
    } catch (error) {
      console.error('Error fetching search results:', error);
    }
  };

  const handleSearchSubmit = (item: CityResult) => {
    dispatch(setStartRegion(item));
    navigation.navigate('calendar');
  };

  return (
    <Wrapper>
      <SearchInput
        placeholder="시작 도시를 설정해주세요"
        value={searchText}
        onChangeText={handleSearchChange}
      />
      <FlatList
        style={styles.flatList}
        data={searchResults}
        renderItem={({item}) => (
          <SearchResultBox onPress={() => handleSearchSubmit(item)}>
            <SearchResult>
              <Text>{item.city_name}</Text>
            </SearchResult>
          </SearchResultBox>
        )}
        keyExtractor={(item, index) => index.toString()}
        contentContainerStyle={styles.flatListContent}
      />
    </Wrapper>
  );
};

const styles = StyleSheet.create({
  dismissKeyboard: {
    backgroundColor: 'white',
    padding: 20,
  },
  flatList: {
    width: '100%',
  },
  flatListContent: {
    position: 'absolute',
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default Search;
