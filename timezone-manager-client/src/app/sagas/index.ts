import { all } from 'redux-saga/effects';
import authSaga from './Auth';
import timeZonesSaga from './TimeZones';

export default function* rootSaga() {
  yield all([authSaga(), timeZonesSaga()]);
}
