import { render } from '@testing-library/react';
import { ConnectedRouter } from 'connected-react-router';
import React from 'react';
import { Provider } from 'react-redux';
import App from './app/index';
import configureStore from './store';

describe('App', () => {
  test('renders App component', () => {
    const { store, history } = configureStore();
    render(
      <Provider store={store}>
        <ConnectedRouter history={history}>
          <App />
        </ConnectedRouter>
      </Provider>
    );
  });
});
