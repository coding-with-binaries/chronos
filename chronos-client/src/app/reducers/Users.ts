import produce from 'immer';
import * as AuthActions from '../actions/auth/ActionConstants';
import { AuthAction } from '../actions/auth/ActionTypes';
import * as Actions from '../actions/users/ActionConstants';
import { UserAction } from '../actions/users/ActionTypes';
import { initialState } from '../constants/InitialState';
import { AsyncState } from '../types/Common';
import { UsersStore } from '../types/Users';

const userReducer = (
  state: UsersStore = initialState.usersStore,
  action: UserAction | AuthAction
): UsersStore => {
  return produce<UsersStore, UsersStore>(state, draft => {
    switch (action.type) {
      case Actions.GET_ALL_USERS:
      case Actions.DELETE_USER: {
        draft.error = undefined;
        draft.asyncState = AsyncState.Fetching;
        break;
      }

      case Actions.GET_ALL_USERS_FAILED:
      case Actions.DELETE_USER_FAILED: {
        const { errorResponse } = action.payload;
        draft.error = errorResponse;
        draft.asyncState = AsyncState.Failed;
        break;
      }

      case Actions.GET_ALL_USERS_SUCCESS: {
        const { users } = action.payload;
        draft.users = users;
        draft.asyncState = AsyncState.Completed;
        break;
      }

      case Actions.ADD_USER: {
        const { user } = action.payload;
        draft.users.push(user);
        draft.asyncState = AsyncState.Completed;
        break;
      }

      case Actions.EDIT_USER: {
        const { user } = action.payload;
        const index = draft.users.findIndex(t => t.uid === user.uid);
        draft.users.splice(index, 1, user);
        draft.asyncState = AsyncState.Completed;
        break;
      }

      case Actions.DELETE_USER_SUCCESS: {
        const { uid } = action.payload;
        const index = draft.users.findIndex(t => t.uid === uid);
        draft.users.splice(index, 1);
        draft.asyncState = AsyncState.Completed;
        break;
      }

      case AuthActions.SIGN_OUT_USER: {
        draft.users = [];
        break;
      }
      // no default
    }
  });
};

export default userReducer;
