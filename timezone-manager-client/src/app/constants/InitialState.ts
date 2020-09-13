import { StoreState } from '../types';
import { AsyncState } from '../types/Common';

export const initialState: StoreState = {
  authStore: {
    accessToken: null,
    authUser: null,
    asyncState: AsyncState.NotStarted
  },
  timeZonesStore: {
    asyncState: AsyncState.NotStarted,
    timeZones: []
  }
};
