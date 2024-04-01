import {
  NavigationProp,
  RouteProp,
  useFocusEffect,
  useNavigation,
  useRoute,
} from '@react-navigation/native';
import React, {useEffect, useState} from 'react';
import {Dimensions, FlatList, View} from 'react-native';
import {FlashMobStackParams} from '../../interfaces/router/flashMob/FlashMobStackParams';
import {useAppDispatch, useAppSelector} from '../../store/hooks';
import {setDisplay} from '../../store/slices/tabState';
import {
  ButtonView,
  ChatRoomDetails,
  ChatRoomItem,
  ChatRoomTitle,
  Container,
  MeetingInfo,
  ProfileImage,
  Title,
} from './AllFlashStyle';
import axios from 'axios';
import getToken from '../../hooks/getToken';
import AppButton from '../../components/common/AppButton';
import {MakeDeleteButton, MakeFlashButton} from '../../constants/AppButton';

interface FlashMobProp {
  flashmob_id: number;
  master_id: number;
  master_image_url: string;
  flashmob_title: string;
  flashmob_start_at: string;
  flashmob_max_count: number;
  flashmob_current_count: number;
  attraction_name: string;
  status: string;
}

const FlashList = () => {
  const dispatch = useAppDispatch();
  const navigation = useNavigation<NavigationProp<FlashMobStackParams>>();

  const {id} = useRoute<RouteProp<FlashMobStackParams, 'FlashCreate'>>().params;
  const user_id = useAppSelector(state => state.user.user.member_id);
  const [myFlashmobs, setMyFlashmobs] = useState<FlashMobProp[]>([]);
  const [allFlashmobs, setAllFlashmobs] = useState<FlashMobProp[]>([]);
  const [refreshKey, setRefreshKey] = useState(0);

  useFocusEffect(() => {
    dispatch(setDisplay(true));
  });

  useEffect(() => {
    const fetchData = async () => {
      const {access_token} = await getToken();
      try {
        const response = await axios.get(
          `https://j10a309.p.ssafy.io/api/attraction/v1/attractions/${id}/flashmobs`,
          {
            headers: {
              Authorization: `Bearer ${access_token}`,
            },
          },
        );
        const fetchedFlashmobs = response.data.data.flashmobs;
        const myFlashmobs = fetchedFlashmobs.filter(
          (flashmob: FlashMobProp) => flashmob.master_id === user_id,
        );
        setMyFlashmobs(myFlashmobs);
        const allFlashmobs = fetchedFlashmobs.filter(
          (flashmob: FlashMobProp) => flashmob.master_id !== user_id,
        );
        setAllFlashmobs(allFlashmobs);
      } catch (error) {
        console.log('Error fetching data:', error);
      }
    };

    fetchData();
  }, [id, user_id, refreshKey]);

  const handlePressChat = (id: number) => {
    navigation.navigate('ChatRoom', {flashmob_id: id});
  };

  const handlePressJoin = async (item: FlashMobProp) => {
    const {access_token} = await getToken();
    if (!item.status) {
      try {
        const response = await axios.post(
          `https://j10a309.p.ssafy.io/api/flashmob/v1/flashmobs/${item.flashmob_id}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${access_token}`,
            },
          },
        );
        console.log(response.data);
        setRefreshKey(prevKey => prevKey + 1);
      } catch (error) {
        console.log('Error fetching data:', error);
      }
    } else if (item.status === 'WAIT') {
      try {
        console.log(item.flashmob_id, user_id);
        const response = await axios.delete(
          `https://j10a309.p.ssafy.io/api/flashmob/v1/flashmobs/${item.flashmob_id}`,
          {
            headers: {
              Authorization: `Bearer ${access_token}`,
            },
          },
        );
        console.log(response.data);
        setRefreshKey(prevKey => prevKey + 1);
      } catch (error) {
        console.log('Error fetching data:', error);
      }
    }
  };

  const renderButton = (item: any) => {
    if (!item.status) {
      return (
        <ButtonView>
          <AppButton
            text="참가 신청"
            style={MakeFlashButton}
            onPress={() => handlePressJoin(item)}
          />
        </ButtonView>
      );
    } else if (item.status === 'WAIT') {
      return (
        <ButtonView>
          <AppButton
            text="신청 취소"
            style={MakeDeleteButton}
            onPress={() => handlePressJoin(item)}
          />
        </ButtonView>
      );
    } else if (item.status === 'ATTEND') {
      return (
        <ButtonView>
          <AppButton
            text="참가 중"
            style={MakeFlashButton}
            disabled={true}
            onPress={() => handlePressJoin(item)}
          />
        </ButtonView>
      );
    }
  };

  const renderItem = ({item}: any) => (
    <ChatRoomItem onPress={() => handlePressChat(item.flashmob_id)}>
      <ProfileImage source={item.master_image_url} />
      <ChatRoomDetails>
        <ChatRoomTitle>{item.flashmob_title}</ChatRoomTitle>
        <MeetingInfo>{item.flashmob_start_at}</MeetingInfo>
        <MeetingInfo>{item.attraction_name}</MeetingInfo>
      </ChatRoomDetails>
    </ChatRoomItem>
  );

  const renderFullItem = ({item}: any) => (
    <ChatRoomItem>
      <ProfileImage source={item.master_image_url} />
      <ChatRoomDetails>
        <ChatRoomTitle>{item.flashmob_title}</ChatRoomTitle>
        <MeetingInfo>{item.flashmob_start_at}</MeetingInfo>
        <MeetingInfo>{item.attraction_name}</MeetingInfo>
      </ChatRoomDetails>
      {renderButton(item)}
    </ChatRoomItem>
  );

  const screenHeight = Dimensions.get('window').height;
  const halfScreenHeight = screenHeight / 3;

  return (
    <Container>
      <View style={{height: halfScreenHeight}}>
        <Title>내 모임</Title>
        <FlatList
          data={myFlashmobs}
          renderItem={renderItem}
          keyExtractor={item => item.flashmob_id.toString()}
        />
      </View>

      <View style={{height: halfScreenHeight * 2}}>
        <Title>전체 모임</Title>
        <FlatList
          data={allFlashmobs}
          renderItem={renderFullItem}
          keyExtractor={item => item.flashmob_id.toString()}
        />
      </View>
    </Container>
  );
};

export default FlashList;
