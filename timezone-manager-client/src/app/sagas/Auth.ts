import { push } from 'connected-react-router';
import { all, call, put, takeLatest } from 'redux-saga/effects';
import * as Actions from '../actions/auth/ActionConstants';
import {
  authenticateUserFailed,
  authenticateUserSuccess,
  getCurrentUserFailed,
  getCurrentUserSuccess,
  registerUserSuccess
} from '../actions/auth/Actions';
import { AuthenticateUser } from '../actions/auth/ActionTypes';
import UserApi from '../api/user/User';
import { AuthResponseDto, AuthUser } from '../types/Auth';
import { ErrorResponse } from '../types/Common';
import {
  clearAuthorizationToken,
  setAuthorizationToken
} from '../utils/api-utils';

function* getCurrentUserSaga() {
  try {
    const authUser: AuthUser = yield call(UserApi.whoAmI);
    yield put(getCurrentUserSuccess(authUser));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(getCurrentUserFailed(errorResponse));
    clearAuthorizationToken();
  }
}

function* watchForGetCurrentUser() {
  yield takeLatest(Actions.GET_CURRENT_USER, getCurrentUserSaga);
}

function* authenticateUserSaga(action: AuthenticateUser) {
  try {
    const { accessToken, username, roles }: AuthResponseDto = yield call(
      UserApi.authenticateUser,
      action.payload.authRequestDto
    );
    setAuthorizationToken(accessToken);
    const authUser: AuthUser = { username, roles };
    yield put(authenticateUserSuccess(accessToken, authUser));
    yield put(push('/'));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(authenticateUserFailed(errorResponse));
    clearAuthorizationToken();
  }
}

function* watchForUserAuthentication() {
  yield takeLatest(Actions.AUTHENTICATE_USER, authenticateUserSaga);
}

function* registerUserSaga(action: AuthenticateUser) {
  try {
    yield call(UserApi.registerUser, action.payload.authRequestDto);
    yield put(registerUserSuccess());
    yield put(push('/sign-in?registered=true'));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(authenticateUserFailed(errorResponse));
    clearAuthorizationToken();
  }
}

function* watchForUserRegistration() {
  yield takeLatest(Actions.REGISTER_USER, registerUserSaga);
}

export default function* authSaga() {
  yield all([
    watchForGetCurrentUser(),
    watchForUserAuthentication(),
    watchForUserRegistration()
  ]);
}