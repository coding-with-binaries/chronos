import { all } from 'redux-saga/effects';
import authSaga from './Auth';
import timeZonesSaga from './TimeZones';
import usersSaga from './Users';

export default function* rootSaga() {
  yield all([authSaga(), timeZonesSaga(), usersSaga()]);
}
