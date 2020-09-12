import React from 'react';
import { Route, Switch } from 'react-router-dom';
import Login from './components/login/Login';
import Main from './components/Main';

const App: React.FC = () => {
  return (
    <div className="timezone-manager-app">
      <Switch>
        <Route path="/login">
          <Login />
        </Route>
        <Route path="/">
          <Main />
        </Route>
      </Switch>
    </div>
  );
};

export default App;
