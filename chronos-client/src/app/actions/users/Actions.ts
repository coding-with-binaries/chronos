import { ErrorResponse } from '../../types/Common';
import { User } from '../../types/Users';
import * as Actions from './ActionConstants';
import {
  AddUser,
  DeleteUser,
  DeleteUserFailed,
  DeleteUserSuccess,
  EditUser,
  GetAllUsers,
  GetAllUsersFailed,
  GetAllUsersSuccess
} from './ActionTypes';

export const getAllUsers = (): GetAllUsers => ({
  type: Actions.GET_ALL_USERS
});

export const getAllUsersFailed = (
  errorResponse: ErrorResponse
): GetAllUsersFailed => ({
  type: Actions.GET_ALL_USERS_FAILED,
  payload: { errorResponse }
});

export const getAllUsersSuccess = (users: User[]): GetAllUsersSuccess => ({
  type: Actions.GET_ALL_USERS_SUCCESS,
  payload: { users }
});

export const addUser = (user: User): AddUser => ({
  type: Actions.ADD_USER,
  payload: { user }
});

export const editUser = (user: User): EditUser => ({
  type: Actions.EDIT_USER,
  payload: { user }
});

export const deleteUser = (uid: string): DeleteUser => ({
  type: Actions.DELETE_USER,
  payload: { uid }
});

export const deleteUserFailed = (
  errorResponse: ErrorResponse
): DeleteUserFailed => ({
  type: Actions.DELETE_USER_FAILED,
  payload: { errorResponse }
});

export const deleteUserSuccess = (uid: string): DeleteUserSuccess => ({
  type: Actions.DELETE_USER_SUCCESS,
  payload: { uid }
});
