import React, {useState} from 'react';
import {Wrapper} from './CalendarStyle';
import CalendarView from '../../components/travel/Calendar';
import {useNavigation} from '@react-navigation/native';
import {NavigationProp} from '@react-navigation/native';
import {CalendarStackParams} from '../../interfaces/router/CalendarStackParams';
import AppButton from '../../components/common/AppButton';
import {BottomButton} from '../../constants/AppButton';
import {Alert} from 'react-native';

const Calendar = () => {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');

  const navigation = useNavigation<NavigationProp<CalendarStackParams>>();

  const handleSearchSubmit = () => {
    if (startDate !== '' && endDate !== '') {
      navigation.navigate('TripTitle');
    } else {
      Alert.alert('경고', '시작일자와 종료일자를 입력하세요');
    }
  };

  return (
    <Wrapper>
      <CalendarView
        startDate={startDate}
        endDate={endDate}
        setStartDate={setStartDate}
        setEndDate={setEndDate}
      />
      <AppButton
        style={BottomButton}
        text="다음"
        onPress={handleSearchSubmit}
      />
    </Wrapper>
  );
};

export default Calendar;
