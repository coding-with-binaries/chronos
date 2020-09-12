import { StoreState } from '../types';
import { AsyncState } from '../types/Common';

export const initialState: StoreState = {
  auth: {
    accessToken: null,
    authUser: null,
    asyncState: AsyncState.NotStarted
  }
};
