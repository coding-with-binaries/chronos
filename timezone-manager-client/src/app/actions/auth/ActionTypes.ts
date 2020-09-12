import { AuthRequestDto, AuthUser } from '../../types/Auth';
import { ErrorResponse } from '../../types/Common';
import * as Actions from './ActionConstants';

export interface AuthenticateUser {
  type: Actions.AUTHENTICATE_USER;
  payload: {
    authRequestDto: AuthRequestDto;
  };
}

export interface AuthenticateUserFailed {
  type: Actions.AUTHENTICATE_USER_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface AuthenticateUserSuccess {
  type: Actions.AUTHENTICATE_USER_SUCCESS;
  payload: {
    accessToken: string;
    authUser: AuthUser;
  };
}

export interface RegisterUser {
  type: Actions.REGISTER_USER;
  payload: {
    authRequestDto: AuthRequestDto;
  };
}

export interface RegisterUserFailed {
  type: Actions.REGISTER_USER_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface RegisterUserSuccess {
  type: Actions.REGISTER_USER_SUCCESS;
}

export interface GetCurrentUser {
  type: Actions.GET_CURRENT_USER;
}

export interface GetCurrentUserFailed {
  type: Actions.GET_CURRENT_USER_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface GetCurrentUserSuccess {
  type: Actions.GET_CURRENT_USER_SUCCESS;
  payload: {
    authUser: AuthUser;
  };
}

export interface SignOutUser {
  type: Actions.SIGN_OUT_USER;
}

export interface SignOutUserSuccess {
  type: Actions.SIGN_OUT_USER_SUCCESS;
}

export type AuthAction =
  | AuthenticateUser
  | AuthenticateUserFailed
  | AuthenticateUserSuccess
  | RegisterUser
  | RegisterUserFailed
  | RegisterUserSuccess
  | GetCurrentUser
  | GetCurrentUserFailed
  | GetCurrentUserSuccess
  | SignOutUser
  | SignOutUserSuccess;
