import { all, call, put, takeLatest } from 'redux-saga/effects';
import * as Actions from '../actions/users/ActionConstants';
import {
  addUserFailed,
  addUserSuccess,
  deleteUserFailed,
  deleteUserSuccess,
  editUserFailed,
  editUserSuccess,
  getAllUsersFailed,
  getAllUsersSuccess
} from '../actions/users/Actions';
import {
  AddUser,
  DeleteUser,
  EditUser,
  GetAllUsers
} from '../actions/users/ActionTypes';
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

function* addUserSaga(action: AddUser) {
  try {
    const user: User = yield call(UserApi.registerUser, action.payload.user);
    yield put(addUserSuccess(user));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(addUserFailed(errorResponse));
  }
}

function* watchForAddUser() {
  yield takeLatest(Actions.ADD_USER, addUserSaga);
}

function* editUserSaga(action: EditUser) {
  try {
    const { uid, user } = action.payload;
    const updatedUser: User = yield call(UserApi.updateUser, uid, user);
    yield put(editUserSuccess(updatedUser));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(editUserFailed(errorResponse));
  }
}

function* watchForEditUser() {
  yield takeLatest(Actions.EDIT_USER, editUserSaga);
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
  yield all([
    watchForGetAllUsers(),
    watchForAddUser(),
    watchForEditUser(),
    watchForDeleteUser()
  ]);
}
