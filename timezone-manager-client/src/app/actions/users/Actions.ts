import { ErrorResponse } from '../../types/Common';
import { RegisterUserDto, UpdateUserDto, User } from '../../types/Users';
import * as Actions from './ActionConstants';
import {
  AddUser,
  AddUserFailed,
  AddUserSuccess,
  DeleteUser,
  DeleteUserFailed,
  DeleteUserSuccess,
  EditUser,
  EditUserFailed,
  EditUserSuccess,
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

export const addUser = (user: RegisterUserDto): AddUser => ({
  type: Actions.ADD_USER,
  payload: { user }
});

export const addUserFailed = (errorResponse: ErrorResponse): AddUserFailed => ({
  type: Actions.ADD_USER_FAILED,
  payload: { errorResponse }
});

export const addUserSuccess = (user: User): AddUserSuccess => ({
  type: Actions.ADD_USER_SUCCESS,
  payload: { user }
});

export const editUser = (uid: string, user: UpdateUserDto): EditUser => ({
  type: Actions.EDIT_USER,
  payload: { uid, user }
});

export const editUserFailed = (
  errorResponse: ErrorResponse
): EditUserFailed => ({
  type: Actions.EDIT_USER_FAILED,
  payload: { errorResponse }
});

export const editUserSuccess = (user: User): EditUserSuccess => ({
  type: Actions.EDIT_USER_SUCCESS,
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
