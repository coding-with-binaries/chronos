import { push } from 'connected-react-router';
import { all, call, put, takeLatest } from 'redux-saga/effects';
import * as Actions from '../actions/auth/ActionConstants';
import {
  authenticateUserFailed,
  authenticateUserSuccess,
  getCurrentUserFailed,
  getCurrentUserSuccess,
  registerUserSuccess,
  signOutUserSuccess
} from '../actions/auth/Actions';
import { AuthenticateUser, SignOutUser } from '../actions/auth/ActionTypes';
import UserApi from '../api/user/User';
import { AuthResponseDto, AuthUser, RoleType } from '../types/Auth';
import { ErrorResponse } from '../types/Common';
import { RegisterUserDto } from '../types/Users';
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
    const { accessToken, uid, username, role }: AuthResponseDto = yield call(
      UserApi.authenticateUser,
      action.payload.authRequestDto
    );
    setAuthorizationToken(accessToken);
    const authUser: AuthUser = { uid, username, role };
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
    const registerUserDto: RegisterUserDto = {
      ...action.payload.authRequestDto,
      role: RoleType.user
    };
    yield call(UserApi.registerUser, registerUserDto);
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

function* signOutUserSaga(action: SignOutUser) {
  try {
    yield call(UserApi.signOutUser);
    yield put(signOutUserSuccess());
    yield put(push('/sign-in'));
  } catch (e) {
    // Should never come here
  }
}

function* watchForUserSignOut() {
  yield takeLatest(Actions.SIGN_OUT_USER, signOutUserSaga);
}

export default function* authSaga() {
  yield all([
    watchForGetCurrentUser(),
    watchForUserAuthentication(),
    watchForUserRegistration(),
    watchForUserSignOut()
  ]);
}
