import produce from 'immer';
import * as AuthActions from '../actions/auth/ActionConstants';
import { AuthAction } from '../actions/auth/ActionTypes';
import * as Actions from '../actions/time-zones/ActionConstants';
import { TimeZoneAction } from '../actions/time-zones/ActionTypes';
import { initialState } from '../constants/InitialState';
import { AsyncState } from '../types/Common';
import { TimeZonesStore } from '../types/TimeZones';

const timeZoneReducer = (
  state: TimeZonesStore = initialState.timeZonesStore,
  action: TimeZoneAction | AuthAction
): TimeZonesStore => {
  return produce<TimeZonesStore, TimeZonesStore>(state, draft => {
    switch (action.type) {
      case Actions.GET_ALL_TIME_ZONES:
      case Actions.ADD_TIME_ZONE:
      case Actions.EDIT_TIME_ZONE:
      case Actions.DELETE_TIME_ZONE: {
        draft.error = undefined;
        draft.asyncState = AsyncState.Fetching;
        break;
      }

      case Actions.GET_ALL_TIME_ZONES_FAILED:
      case Actions.ADD_TIME_ZONE_FAILED:
      case Actions.EDIT_TIME_ZONE_FAILED:
      case Actions.DELETE_TIME_ZONE_FAILED: {
        const { errorResponse } = action.payload;
        draft.error = errorResponse;
        draft.asyncState = AsyncState.Failed;
        break;
      }

      case Actions.GET_ALL_TIME_ZONES_SUCCESS: {
        const { timeZones } = action.payload;
        draft.timeZones = timeZones;
        draft.asyncState = AsyncState.Completed;
        break;
      }

      case Actions.ADD_TIME_ZONE_SUCCESS: {
        const { timeZone } = action.payload;
        draft.timeZones.push(timeZone);
        draft.asyncState = AsyncState.Completed;
        break;
      }

      case Actions.EDIT_TIME_ZONE_SUCCESS: {
        const { timeZone } = action.payload;
        const index = draft.timeZones.findIndex(t => t.uid === timeZone.uid);
        draft.timeZones.splice(index, 1, timeZone);
        draft.asyncState = AsyncState.Completed;
        break;
      }

      case Actions.DELETE_TIME_ZONE_SUCCESS: {
        const { uid } = action.payload;
        const index = draft.timeZones.findIndex(t => t.uid === uid);
        draft.timeZones.splice(index, 1);
        draft.asyncState = AsyncState.Completed;
        break;
      }

      case AuthActions.SIGN_OUT_USER: {
        draft.timeZones = [];
        break;
      }
      // no default
    }
  });
};

export default timeZoneReducer;
