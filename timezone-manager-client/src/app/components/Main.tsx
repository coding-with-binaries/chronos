import React, { useEffect } from 'react';
import Spinner from 'react-bootstrap/Spinner';
import { useDispatch, useSelector } from 'react-redux';
import { Redirect, Route, Switch, useLocation } from 'react-router-dom';
import { Dispatch } from 'redux';
import { getCurrentUser } from '../actions/auth/Actions';
import { AuthAction } from '../actions/auth/ActionTypes';
import { StoreState } from '../types';
import { Auth } from '../types/Auth';
import { AsyncState } from '../types/Common';
import { hasUserManagerRoles } from '../utils/aut-utils';
import TimeZones from './time-zones/TimeZones';
import Users from './users/Users';

const Main: React.FC = () => {
  const { asyncState, authUser } = useSelector<StoreState, Auth>(s => s.auth);

  const dispatch = useDispatch<Dispatch<AuthAction>>();
  const location = useLocation();

  useEffect(() => {
    if (asyncState === AsyncState.NotStarted) {
      dispatch(getCurrentUser());
    }
  }, [dispatch, asyncState]);

  const renderContent = () => {
    if (
      asyncState === AsyncState.NotStarted ||
      asyncState === AsyncState.Fetching
    ) {
      return (
        <Spinner animation="border" role="status">
          <span className="sr-only">Validating Current User...</span>
        </Spinner>
      );
    }

    if (asyncState === AsyncState.Failed) {
      return (
        <Redirect
          to={{
            pathname: '/login',
            state: { from: location }
          }}
        />
      );
    }

    if (asyncState === AsyncState.Completed && authUser) {
      const { roles } = authUser;
      const availableRoutes = [
        <Route path="/time-zones">
          <TimeZones />
        </Route>
      ];
      if (hasUserManagerRoles(roles)) {
        availableRoutes.push(
          <Route path="/users">
            <Users />
          </Route>
        );
      }
      availableRoutes.push(<Redirect exact={true} path="/" to="time-zones" />);
      return <Switch>{availableRoutes}</Switch>;
    }
  };

  return <div className="timezone-manager-main">{renderContent()}</div>;
};

export default Main;
