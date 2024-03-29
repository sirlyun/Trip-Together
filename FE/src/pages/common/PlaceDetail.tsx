import {
  NavigationProp,
  useNavigation,
  useRoute,
} from '@react-navigation/native';
import React, {useEffect, useState} from 'react';
import {FlatList, TouchableOpacity} from 'react-native';
import {StarRatingDisplay} from 'react-native-star-rating-widget';
import {PlaceDetail} from '../../assets/data/placedetail';
import {imagePath} from '../../assets/images/imagePath';
import AppButton from '../../components/common/AppButton';
import {JoinFlashButton, MakeFlashButton} from '../../constants/AppButton';
import {PlaceStackParams} from '../../interfaces/router/PlaceStackParams';
import {useAppDispatch, useAppSelector} from '../../store/hooks';
import {addItemToBag} from '../../store/slices/bag';
import {
  Address,
  Bag,
  BagImage,
  ButtonContainer,
  Container,
  DetailsContainer,
  Header,
  HeaderContainer,
  HeadersContainer,
  ImageBackground,
  Info,
  Line,
  NavButton,
  NavigationButtons,
  ProfileImage,
  ReviewContent,
  ReviewDetails,
  ReviewImage,
  ReviewItem,
  ReviewRating,
  ReviewWriter,
  ReviewsContainer,
  StarInfo,
  Title,
} from './PlaceDetailStyle';

interface RouteParams {
  theme?: string;
}

const AttractionDetailsPage = () => {
  const [show, setShow] = useState(true);
  const [currentReviewIndex, setCurrentReviewIndex] = useState(0);

  const attraction = PlaceDetail[0];
  const attraction_id = PlaceDetail[0].attraction_id;
  const PlaceBag = useAppSelector(state => state.bag.bagInfo);
  const dispatch = useAppDispatch();

  const route = useRoute();
  const {theme}: RouteParams = route.params || {};
  const navigation = useNavigation<NavigationProp<PlaceStackParams>>();

  useEffect(() => {
    PlaceBag.forEach(item => {
      if (item.attraction_id === attraction_id) {
        setShow(false);
      }
    });
  }, [PlaceBag, attraction_id]);

  const images = Array.from({length: 5}, (_, index) => ({
    id: index.toString(),
    source: imagePath.review,
  }));

  const renderImageItem = ({item}: {item: any}) => (
    <ReviewImage source={item.source} />
  );

  const renderReviewItem = ({item}: {item: any}) => (
    <ReviewItem>
      <ProfileImage source={imagePath.basicprofile} />
      <ReviewDetails>
        <ReviewWriter>{item.writer_nickname}</ReviewWriter>
        <ReviewRating>Rating: {item.rating}</ReviewRating>
        <ReviewContent>{item.content}</ReviewContent>
      </ReviewDetails>
    </ReviewItem>
  );

  const goToPreviousReview = () => {
    setCurrentReviewIndex(
      currentReviewIndex === 0
        ? attraction.reviews.length - 1
        : currentReviewIndex - 1,
    );
  };

  const goToNextReview = () => {
    setCurrentReviewIndex(
      currentReviewIndex === attraction.reviews.length - 1
        ? 0
        : currentReviewIndex + 1,
    );
  };

  const handleAddItem = () => {
    const data = {
      attraction_id: 14,
      thumbnail_image_url: '',
      name: 'La Sagrada Familia',
      address: '',
      avg_rating: 2.4,
      avg_price: 123,
    };
    dispatch(addItemToBag(data));
  };

  const keyExtractor = (item: any, index: number) => index.toString();

  const handlePressMake = () => {
    navigation.navigate('makeflash');
  };

  const handlePressAllFlash = () => {
    navigation.navigate('allflash');
  };

  return (
    <FlatList
      data={[attraction]}
      renderItem={({item}) => (
        <Container>
          <ImageBackground
            source={imagePath.sagradafamilla}
            resizeMode="cover"
          />
          <HeadersContainer>
            <Header>
              <Title>La Sagrada Familia</Title>
              <Address>
                {' '}
                C/ de Mallorca, 401, L`Eixample, 08013 Barcelona'
              </Address>
            </Header>
            {theme === 'trip' && (
              <Bag onPress={handleAddItem}>
                {show ? (
                  <BagImage source={imagePath.shopping2} resizeMode="cover" />
                ) : (
                  <></>
                )}
              </Bag>
            )}
          </HeadersContainer>

          {theme === 'flashmob' && (
            <ButtonContainer>
              <AppButton
                text="모임 생성"
                style={MakeFlashButton}
                onPress={handlePressMake}
              />
              <AppButton
                text="모임 검색"
                style={JoinFlashButton}
                onPress={handlePressAllFlash}
              />
            </ButtonContainer>
          )}

          <DetailsContainer>
            <Title>정보</Title>
            <Line />
            <StarInfo>
              <Info>평점: 4.9</Info>
              <StarRatingDisplay rating={4.9} starSize={18} />
            </StarInfo>
            <Info>평균 가격: {item.avg_price}</Info>
            <Info>
              운영 시간: {item.start_at} - {item.end_at}
            </Info>

            <ReviewsContainer>
              <Title>사진</Title>
              <Line />
              <FlatList
                data={images}
                renderItem={renderImageItem}
                horizontal
                keyExtractor={keyExtractor}
              />

              <HeaderContainer>
                <Title>리뷰</Title>
                <NavigationButtons>
                  <TouchableOpacity onPress={goToPreviousReview}>
                    <NavButton>{'<'}</NavButton>
                  </TouchableOpacity>
                  <TouchableOpacity onPress={goToNextReview}>
                    <NavButton>{'>'}</NavButton>
                  </TouchableOpacity>
                </NavigationButtons>
              </HeaderContainer>

              <Line />
              <FlatList
                data={[item.reviews[currentReviewIndex]]}
                renderItem={renderReviewItem}
                contentContainerStyle={{
                  paddingHorizontal: 20 / 2,
                }}
                horizontal
                pagingEnabled
                keyExtractor={keyExtractor}
              />
            </ReviewsContainer>
          </DetailsContainer>
        </Container>
      )}
      keyExtractor={keyExtractor}
    />
  );
};

export default AttractionDetailsPage;
