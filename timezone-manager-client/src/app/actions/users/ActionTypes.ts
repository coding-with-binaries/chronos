import { ErrorResponse } from '../../types/Common';
import { RegisterUserDto, UpdateUserDto, User } from '../../types/Users';
import * as Actions from './ActionConstants';

export interface GetAllUsers {
  type: Actions.GET_ALL_USERS;
}

export interface GetAllUsersFailed {
  type: Actions.GET_ALL_USERS_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface GetAllUsersSuccess {
  type: Actions.GET_ALL_USERS_SUCCESS;
  payload: {
    users: User[];
  };
}

export interface AddUser {
  type: Actions.ADD_USER;
  payload: {
    user: RegisterUserDto;
  };
}

export interface AddUserFailed {
  type: Actions.ADD_USER_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface AddUserSuccess {
  type: Actions.ADD_USER_SUCCESS;
  payload: {
    user: User;
  };
}

export interface EditUser {
  type: Actions.EDIT_USER;
  payload: {
    uid: string;
    user: UpdateUserDto;
  };
}

export interface EditUserFailed {
  type: Actions.EDIT_USER_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface EditUserSuccess {
  type: Actions.EDIT_USER_SUCCESS;
  payload: {
    user: User;
  };
}

export interface DeleteUser {
  type: Actions.DELETE_USER;
  payload: {
    uid: string;
  };
}

export interface DeleteUserFailed {
  type: Actions.DELETE_USER_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface DeleteUserSuccess {
  type: Actions.DELETE_USER_SUCCESS;
  payload: {
    uid: string;
  };
}

export type UserAction =
  | GetAllUsers
  | GetAllUsersFailed
  | GetAllUsersSuccess
  | AddUser
  | AddUserFailed
  | AddUserSuccess
  | EditUser
  | EditUserFailed
  | EditUserSuccess
  | DeleteUser
  | DeleteUserFailed
  | DeleteUserSuccess;
