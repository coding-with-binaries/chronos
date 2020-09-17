import { all, call, put, takeLatest } from 'redux-saga/effects';
import * as Actions from '../actions/time-zones/ActionConstants';
import {
  addTimeZoneFailed,
  addTimeZoneSuccess,
  deleteTimeZoneFailed,
  deleteTimeZoneSuccess,
  editTimeZoneFailed,
  editTimeZoneSuccess,
  getAllTimeZonesFailed,
  getAllTimeZonesSuccess
} from '../actions/time-zones/Actions';
import {
  AddTimeZone,
  DeleteTimeZone,
  EditTimeZone,
  GetAllTimeZones
} from '../actions/time-zones/ActionTypes';
import TimeZoneApi from '../api/time-zone/TimeZone';
import { ErrorResponse } from '../types/Common';
import { TimeZone } from '../types/TimeZones';

function* getAllTimeZonesSaga(action: GetAllTimeZones) {
  try {
    const timeZones: TimeZone[] = yield call(TimeZoneApi.getAllTimeZones);
    yield put(getAllTimeZonesSuccess(timeZones));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(getAllTimeZonesFailed(errorResponse));
  }
}

function* watchForGetAllTimeZones() {
  yield takeLatest(Actions.GET_ALL_TIME_ZONES, getAllTimeZonesSaga);
}

function* addTimeZoneSaga(action: AddTimeZone) {
  try {
    const timeZone: TimeZone = yield call(
      TimeZoneApi.addTimeZone,
      action.payload.timeZone
    );
    yield put(addTimeZoneSuccess(timeZone));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(addTimeZoneFailed(errorResponse));
  }
}

function* watchForAddTimeZone() {
  yield takeLatest(Actions.ADD_TIME_ZONE, addTimeZoneSaga);
}

function* editTimeZoneSaga(action: EditTimeZone) {
  try {
    const { uid, timeZone } = action.payload;
    const updatedTimeZone: TimeZone = yield call(
      TimeZoneApi.editTimeZone,
      uid,
      timeZone
    );
    yield put(editTimeZoneSuccess(updatedTimeZone));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(editTimeZoneFailed(errorResponse));
  }
}

function* watchForEditTimeZone() {
  yield takeLatest(Actions.EDIT_TIME_ZONE, editTimeZoneSaga);
}

function* deleteTimeZoneSaga(action: DeleteTimeZone) {
  try {
    const { uid } = action.payload;
    yield call(TimeZoneApi.deleteTimeZone, uid);
    yield put(deleteTimeZoneSuccess(uid));
  } catch (e) {
    const errorResponse: ErrorResponse = e.response.data;
    yield put(deleteTimeZoneFailed(errorResponse));
  }
}

function* watchForDeleteTimeZone() {
  yield takeLatest(Actions.DELETE_TIME_ZONE, deleteTimeZoneSaga);
}

export default function* timeZonesSaga() {
  yield all([
    watchForGetAllTimeZones(),
    watchForAddTimeZone(),
    watchForEditTimeZone(),
    watchForDeleteTimeZone()
  ]);
}
