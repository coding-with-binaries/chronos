import React from 'react';
import { Route, Switch } from 'react-router-dom';
import Main from './components/Main';
import SignIn from './components/sign-in/SignIn';
import SignUp from './components/sign-up/SignUp';
import './index.css';

const App: React.FC = () => {
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
