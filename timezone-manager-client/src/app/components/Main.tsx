import { ClockCircleOutlined, UserOutlined } from '@ant-design/icons';
import { Layout, Menu, Spin } from 'antd';
import { MenuInfo } from 'rc-menu/lib/interface';
import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
  Redirect,
  Route,
  Switch,
  useHistory,
  useLocation
} from 'react-router-dom';
import { Dispatch } from 'redux';
import { signOutUser } from '../actions/auth/Actions';
import { AuthAction } from '../actions/auth/ActionTypes';
import { StoreState } from '../types';
import { Auth } from '../types/Auth';
import { AsyncState } from '../types/Common';
import { hasUserManagerRoles } from '../utils/auth-utils';
import './Main.css';
import TimeZones from './time-zones/TimeZones';
import Users from './users/Users';

const { Header, Content, Sider } = Layout;

const Main: React.FC = () => {
  const { asyncState, authUser } = useSelector<StoreState, Auth>(s => s.auth);

  const location = useLocation();
  const history = useHistory();

  const dispatch = useDispatch<Dispatch<AuthAction>>();

  const renderContent = () => {
    if (
      asyncState === AsyncState.NotStarted ||
      asyncState === AsyncState.Fetching
    ) {
      return <Spin size="large" />;
    }

    if (asyncState === AsyncState.Failed) {
      return (
        <Redirect
          to={{
            pathname: '/sign-in',
            state: { from: location }
          }}
        />
      );
    }

    if (asyncState === AsyncState.Completed && authUser) {
      const { roles } = authUser;
      const availableRoutes = [
        <Route key="time-zones" path="/time-zones">
          <TimeZones />
        </Route>
      ];
      if (hasUserManagerRoles(roles)) {
        availableRoutes.push(
          <Route key="users" path="/users">
            <Users />
          </Route>
        );
      }
      availableRoutes.push(
        <Redirect key="redirect" exact={true} path="/" to="time-zones" />
      );
      return <Switch>{availableRoutes}</Switch>;
    }
  };
  const initialSelection = location.pathname.includes('users')
    ? 'users'
    : 'time-zones';
  const [selectedItem, setSelectedItem] = useState(initialSelection);

  const renderMenuItems = () => {
    const menuItems = [
      <Menu.Item key="time-zones" icon={<ClockCircleOutlined />}>
        Time Zones
      </Menu.Item>
    ];
    if (authUser && hasUserManagerRoles(authUser.roles)) {
      menuItems.push(
        <Menu.Item key="users" icon={<UserOutlined />}>
          Users
        </Menu.Item>
      );
    }
    menuItems.push();
    return menuItems;
  };

  const handleMenuItemClick = (info: MenuInfo) => {
    const { key } = info;
    history.push(`/${key}`);
    setSelectedItem(key.toString());
  };

  const onSignOutClick = () => {
    dispatch(signOutUser());
  };

  return (
    <div id="timezone-manager-main">
      <Layout id="timezone-manager-layout">
        <Sider collapsible>
          <div className="logo" />
          <Menu
            theme="dark"
            selectedKeys={[selectedItem]}
            onClick={handleMenuItemClick}
            mode="inline"
          >
            {renderMenuItems()}
          </Menu>
        </Sider>
        <Layout>
          <Header>
            <div className="sign-out" onClick={onSignOutClick}>
              Sign Out
            </div>
          </Header>
          <Content id="timezone-manager-content">{renderContent()}</Content>
        </Layout>
      </Layout>
    </div>
  );
};

export default Main;
