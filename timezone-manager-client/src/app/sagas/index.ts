import { all } from 'redux-saga/effects';
import authSaga from './Auth';

export default function* rootSaga() {
  yield all([authSaga()]);
}
