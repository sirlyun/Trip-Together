import {AppButtonStyle} from '../interfaces/props/AppButton';
import {font_light, primary, primary_light} from './colors';

const defaultStyle = {
  button: {
    width: '80%',
    bg1: primary,
    bg2: primary_light,
    borderR: '40px',
    padding: '20px 0',
  },
  font: {
    color: font_light,
    size: '20px',
  },
};

const socialLoginButton: AppButtonStyle = {
  button: {
    bg1: 'red',
    bg2: 'black',
  },
};

export {defaultStyle, socialLoginButton};
