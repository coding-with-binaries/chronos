import { connectRouter } from 'connected-react-router';
import { History } from 'history';
import { combineReducers } from 'redux';
import auth from './Auth';

const createRootReducer = (history: History) => {
  return combineReducers({
    router: connectRouter(history),
    auth
  });
};

export default createRootReducer;
