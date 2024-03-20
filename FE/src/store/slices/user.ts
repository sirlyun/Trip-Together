import {PayloadAction, createSlice} from '@reduxjs/toolkit';
import {UserState, syncAccount} from '../../interfaces/states/UserState';

/* 더미 계좌 정보
{id: 0, nation: 'UK', nation_kr: '영국', unit: 8356, balance: 28.88},
{id: 1, nation: 'EU', nation_kr: 'EU', unit: 8364, balance: 485.88},
*/

const initialState: UserState = {
  isLoggedIn: false,
  userInfo: {
    user_id: 0,
    username: '김태규',
    nickname: '',
    image_url: '',
    description: '',
    is_pin: true,
    sync_accounts: [],
    sync_accounts_length: 0,
    trip_accounts: [],
    trip_accounts_length: 0,
  },
};

export const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    login: (state, action: PayloadAction<boolean>) => {
      state.isLoggedIn = action.payload;
    },
    setPin: (state, action: PayloadAction<boolean>) => {
      state.userInfo.is_pin = action.payload;
    },
    pushSyncAccount: (state, action: PayloadAction<syncAccount>) => {
      if (action.payload.is_main === 1) {
        for (const account of state.userInfo.sync_accounts) {
          account.is_main = 0;
        }
      }
      state.userInfo.sync_accounts.push(action.payload);
      state.userInfo.sync_accounts_length += 1;
    },
  },
});

export const {login, setPin, pushSyncAccount} = userSlice.actions;

export default userSlice.reducer;