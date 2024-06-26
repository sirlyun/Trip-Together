import styled from 'styled-components/native';
import {
  font_danger,
  font_dark,
  font_lightgray,
  primary,
} from '../../constants/colors';

const TitleView = styled.View`
  width: 100%;
  margin: 30px 0 10px 15px;
`;

const Title = styled.Text`
  font-weight: 600;
  font-size: 28px;
  color: ${font_dark};
`;

const Hightlight = styled.Text`
  color: ${primary};
`;

const HightlightRed = styled.Text`
  color: ${font_danger};
`;

const SloganView = styled.View`
  width: 100%;
  margin: 0 0 10px 15px;
`;

const Slogan = styled.Text`
  color: ${font_lightgray};
`;

const Body = styled.View`
  width: 100%;
  flex: 1;
`;

export {Body, Hightlight, HightlightRed, Slogan, SloganView, Title, TitleView};
