import { connectRouter } from 'connected-react-router';
import { History } from 'history';
import { combineReducers } from 'redux';
import authReducer from './Auth';
import timeZoneReducer from './TimeZones';
import userReducer from './Users';

const createRootReducer = (history: History) => {
  return combineReducers({
    router: connectRouter(history),
    authStore: authReducer,
    timeZonesStore: timeZoneReducer,
    usersStore: userReducer
  });
};

export default createRootReducer;
