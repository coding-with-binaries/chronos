import { all, put, select, takeLatest } from 'redux-saga/effects';
import { v4 as uuid } from 'uuid';
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
import { mockTimeZones } from '../mocks/TimeZones';
import { StoreState } from '../types';
import { ErrorResponse } from '../types/Common';
import { TimeZone } from '../types/TimeZones';

function* getAllTimeZonesSaga(_: GetAllTimeZones) {
  try {
    const timeZones: TimeZone[] = mockTimeZones;
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
  const username = yield select(
    (s: StoreState) => s.authStore.authUser?.username
  );
  try {
    const {
      timeZone: { timeZoneName, locationName, differenceFromGmt }
    } = action.payload;
    const timeZone: TimeZone = {
      differenceFromGmt,
      locationName,
      timeZoneName,
      createdBy: username,
      lastModifiedBy: username,
      uid: uuid()
    };
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
    const {
      uid,
      timeZone: { differenceFromGmt, locationName, timeZoneName }
    } = action.payload;
    const timeZones: TimeZone[] = yield select(
      (s: StoreState) => s.timeZonesStore.timeZones
    );
    const currentTimeZone = timeZones.find(t => t.uid === uid);
    const username = yield select(
      (s: StoreState) => s.authStore.authUser?.username
    );
    const updatedTimeZone: TimeZone = {
      createdBy: currentTimeZone?.createdBy || '-',
      differenceFromGmt,
      timeZoneName,
      locationName,
      uid,
      lastModifiedBy: username
    };
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
