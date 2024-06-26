import {AppButtonStyle} from '../interfaces/props/AppButton';
import {
  bg_danger,
  bg_light,
  bg_main,
  bg_warning,
  font_light,
  primary,
  primary_light,
  secondary,
  secondary_light,
} from './colors';

const defaultStyle = {
  button: {
    width: '80%',
    bg1: primary,
    bg2: primary_light,
    borderR: '40px',
    borderW: '0px',
    borderC: 'none',
    padding: '20px 0',
  },
  font: {
    color: font_light,
    size: '20px',
  },
};

const socialLoginButton: AppButtonStyle = {
  button: {
    bg1: 'tomato',
    bg2: 'red',
  },
};

const myWalletButton: AppButtonStyle = {
  button: {
    width: '100%',
    padding: '8px 0',
    borderR: '10px',
  },
  font: {
    size: '20px',
  },
};

const BottomButton: AppButtonStyle = {
  button: {
    width: '100%',
    bg1: primary,
    bg2: primary_light,
    borderR: '0px',
    padding: '20px 0',
  },
  font: {
    color: font_light,
    size: '20px',
  },
};

const MakeFlashButton: AppButtonStyle = {
  button: {
    width: '100%',
    padding: '8px 0',
    bg1: primary,
  },
  font: {
    color: font_light,
    size: '13px',
  },
};

const MakeDeleteButton: AppButtonStyle = {
  button: {
    width: '100%',
    padding: '8px 0',
    bg1: bg_danger,
  },
  font: {
    color: font_light,
    size: '13px',
  },
};

const JoinFlashButton: AppButtonStyle = {
  button: {
    width: '100%',
    padding: '8px 0',
    bg1: secondary,
  },
  font: {
    color: font_light,
    size: '13px',
  },
};

const profileEditButton: AppButtonStyle = {
  button: {
    width: '100%',
    bg1: bg_light,
    bg2: bg_main,
    borderC: secondary,
    borderW: '2px',
    borderR: '10px',
    padding: '5px 0',
  },
  font: {
    color: secondary,
  },
};

const reportButton: AppButtonStyle = {
  button: {
    width: '100%',
    bg1: bg_light,
    bg2: bg_main,
    borderC: bg_warning,
    borderW: '2px',
    borderR: '10px',
    padding: '5px 0',
  },
  font: {
    color: bg_warning,
  },
};

const remittanceButton: AppButtonStyle = {
  button: {
    width: '100%',
    bg1: secondary,
    bg2: secondary_light,
    borderR: '10px',
    padding: '15px 0',
  },
  font: {
    size: '24px',
  },
};

export {
  BottomButton,
  JoinFlashButton,
  MakeFlashButton,
  defaultStyle,
  myWalletButton,
  profileEditButton,
  remittanceButton,
  reportButton,
  socialLoginButton,
  MakeDeleteButton,
};
