import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Route, Switch } from 'react-router-dom';
import { Dispatch } from 'redux';
import { getCurrentUser } from './actions/auth/Actions';
import { AuthAction } from './actions/auth/ActionTypes';
import Main from './components/Main';
import SignIn from './components/sign-in/SignIn';
import SignUp from './components/sign-up/SignUp';
import './index.css';
import { StoreState } from './types';
import { AuthStore } from './types/Auth';
import { AsyncState } from './types/Common';

const App: React.FC = () => {
  const { asyncState } = useSelector<StoreState, AuthStore>(s => s.authStore);

  const dispatch = useDispatch<Dispatch<AuthAction>>();
  useEffect(() => {
    if (asyncState === AsyncState.NotStarted) {
      dispatch(getCurrentUser());
    }
  }, [dispatch, asyncState]);

  return (
    <div id="timezone-manager-app">
      <Switch>
        <Route path="/sign-in">
          <SignIn />
        </Route>
        <Route path="/sign-up">
          <SignUp />
        </Route>
        <Route path="/">
          <Main />
        </Route>
      </Switch>
    </div>
  );
};

export default App;
