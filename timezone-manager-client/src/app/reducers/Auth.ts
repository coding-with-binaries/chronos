import produce from 'immer';
import * as Actions from '../actions/auth/ActionConstants';
import { AuthAction } from '../actions/auth/ActionTypes';
import { initialState } from '../constants/InitialState';
import { Auth } from '../types/Auth';
import { AsyncState } from '../types/Common';

const authReducer = (
  state: Auth = initialState.auth,
  action: AuthAction
): Auth => {
  return produce<Auth, Auth>(state, draft => {
    switch (action.type) {
      case Actions.AUTHENTICATE_USER:
      case Actions.REGISTER_USER:
      case Actions.GET_CURRENT_USER: {
        draft.error = undefined;
        draft.asyncState = AsyncState.Fetching;
        break;
      }

      case Actions.AUTHENTICATE_USER_FAILED:
      case Actions.REGISTER_USER_FAILED:
      case Actions.GET_CURRENT_USER_FAILED: {
        const { errorResponse } = action.payload;
        draft.error = errorResponse;
        draft.asyncState = AsyncState.Failed;
        break;
      }

      case Actions.AUTHENTICATE_USER_SUCCESS:
      case Actions.GET_CURRENT_USER_SUCCESS: {
        const { authUser } = action.payload;
        draft.authUser = authUser;
        draft.asyncState = AsyncState.Completed;
        break;
      }

      case Actions.REGISTER_USER_SUCCESS: {
        draft.asyncState = AsyncState.Completed;
        break;
      }

      case Actions.SIGN_OUT_USER: {
        draft.authUser = null;
        break;
      }
      // no default
    }
  });
};

export default authReducer;
