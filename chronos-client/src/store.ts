import { routerMiddleware } from 'connected-react-router';
import { createBrowserHistory } from 'history';
import { applyMiddleware, createStore } from 'redux';
import { composeWithDevTools } from 'redux-devtools-extension';
import createSagaMiddleware from 'redux-saga';
import createRootReducer from './app/reducers';
import rootSaga from './app/sagas';
import './index.css';

const configureStore = () => {
  const history = createBrowserHistory();
  const sagaMiddleware = createSagaMiddleware();

  const store = createStore(
    createRootReducer(history),
    composeWithDevTools(
      applyMiddleware(routerMiddleware(history), sagaMiddleware)
    )
  );

  sagaMiddleware.run(rootSaga);

  return { store, history };
};

export default configureStore;
