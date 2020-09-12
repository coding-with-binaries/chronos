import { AuthRequestDto, AuthUser } from '../../types/Auth';
import { ErrorResponse } from '../../types/Common';
import * as Actions from './ActionConstants';
import {
  AuthenticateUser,
  AuthenticateUserFailed,
  AuthenticateUserSuccess,
  GetCurrentUser,
  GetCurrentUserFailed,
  GetCurrentUserSuccess
} from './ActionTypes';

export const authenticateUser = (
  authRequestDto: AuthRequestDto
): AuthenticateUser => ({
  type: Actions.AUTHENTICATE_USER,
  payload: { authRequestDto }
});

export const authenticateUserFailed = (
  errorResponse: ErrorResponse
): AuthenticateUserFailed => ({
  type: Actions.AUTHENTICATE_USER_FAILED,
  payload: { errorResponse }
});

export const authenticateUserSuccess = (
  authUser: AuthUser
): AuthenticateUserSuccess => ({
  type: Actions.AUTHENTICATE_USER_SUCCESS,
  payload: { authUser }
});

export const getCurrentUser = (): GetCurrentUser => ({
  type: Actions.GET_CURRENT_USER
});

export const getCurrentUserFailed = (
  errorResponse: ErrorResponse
): GetCurrentUserFailed => ({
  type: Actions.GET_CURRENT_USER_FAILED,
  payload: { errorResponse }
});

export const getCurrentUserSuccess = (
  authUser: AuthUser
): GetCurrentUserSuccess => ({
  type: Actions.GET_CURRENT_USER_SUCCESS,
  payload: { authUser }
});
