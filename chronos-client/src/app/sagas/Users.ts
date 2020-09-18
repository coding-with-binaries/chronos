import { all, call, put, takeLatest } from 'redux-saga/effects';
import * as Actions from '../actions/users/ActionConstants';
import {
  deleteUserFailed,
  deleteUserSuccess,
  getAllUsersFailed,
  getAllUsersSuccess
} from '../actions/users/Actions';
import { DeleteUser, GetAllUsers } from '../actions/users/ActionTypes';
import UserApi from '../api/user/User';
import { ErrorResponse } from '../types/Common';
import { User } from '../types/Users';

function* getAllUsersSaga(action: GetAllUsers) {
  try {
    const users: User[] = yield call(UserApi.getAllUsers);
    yield put(getAllUsersSuccess(users));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(getAllUsersFailed(errorResponse));
  }
}

function* watchForGetAllUsers() {
  yield takeLatest(Actions.GET_ALL_USERS, getAllUsersSaga);
}

function* deleteUserSaga(action: DeleteUser) {
  try {
    const { uid } = action.payload;
    yield call(UserApi.deleteUser, uid);
    yield put(deleteUserSuccess(uid));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(deleteUserFailed(errorResponse));
  }
}

function* watchForDeleteUser() {
  yield takeLatest(Actions.DELETE_USER, deleteUserSaga);
}

export default function* usersSaga() {
  yield all([watchForGetAllUsers(), watchForDeleteUser()]);
}
