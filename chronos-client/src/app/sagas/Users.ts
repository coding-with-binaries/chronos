import { all, put, takeLatest } from 'redux-saga/effects';
import * as Actions from '../actions/users/ActionConstants';
import {
  deleteUserFailed,
  deleteUserSuccess,
  getAllUsersFailed,
  getAllUsersSuccess
} from '../actions/users/Actions';
import { DeleteUser, GetAllUsers } from '../actions/users/ActionTypes';
import { mockUsers } from '../mocks/Users';
import { ErrorResponse } from '../types/Common';
import { User } from '../types/Users';

function* getAllUsersSaga(_: GetAllUsers) {
  try {
    const users: User[] = mockUsers;
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
