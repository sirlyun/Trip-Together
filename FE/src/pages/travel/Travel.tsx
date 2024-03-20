import React from 'react';
import {Dimensions} from 'react-native';
import {Wrapper} from './TravelStyle';
import {useAppDispatch} from '../../store/hooks';
import {useFocusEffect} from '@react-navigation/native';
import {setDisplay} from '../../store/slices/tabState';
import Plans from '../../components/travel/Plans';
import Package from '../../components/travel/Package';
import Icons from '../../components/travel/Icons';

function Travel() {
  const screenWidth = Math.round(Dimensions.get('window').width);
  const dispatch = useAppDispatch();

  useFocusEffect(() => {
    dispatch(setDisplay(true));
  });

  const PAGES = [
    {
      num: 1,
      color: '#86E3CE',
    },
    {
      num: 2,
      color: '#D0E6A5',
    },
    {
      num: 3,
      color: '#FFDD94',
    },
    {
      num: 4,
      color: '#FA897B',
    },
    {
      num: 5,
      color: '#CCABD8',
    },
  ];

  return (
    <Wrapper>
      <Plans />
      <Icons pages={PAGES} gap={15} offset={35} pageWidth={screenWidth - 51} />
      <Package
        pages={PAGES}
        gap={15}
        offset={35}
        pageWidth={screenWidth - (16 + 36) * 4.7}
      />
    </Wrapper>
  );
}

export default Travel;