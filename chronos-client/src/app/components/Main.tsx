import {
  ClockCircleOutlined,
  DownOutlined,
  LogoutOutlined,
  SettingOutlined,
  UserOutlined
} from '@ant-design/icons';
import { Dropdown, Layout, Menu, Spin } from 'antd';
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
import { AuthStore } from '../types/Auth';
import { AsyncState } from '../types/Common';
import { hasUserManagerRoles } from '../utils/auth-utils';
import './Main.css';
import Settings from './settings/Settings';
import TimeZones from './time-zones/TimeZones';
import Users from './users/Users';

const { Header, Content, Sider } = Layout;

const Main: React.FC = () => {
  const { asyncState, authUser } = useSelector<StoreState, AuthStore>(
    s => s.authStore
  );

  const location = useLocation();
  const history = useHistory();

  const dispatch = useDispatch<Dispatch<AuthAction>>();

  const initialSelection = location.pathname.includes('users')
    ? 'users'
    : 'time-zones';
  const [selectedItem, setSelectedItem] = useState(initialSelection);

  const renderNavigationMenuItems = () => {
    const menuItems = [
      <Menu.Item key="time-zones" icon={<ClockCircleOutlined />}>
        Time Zones
      </Menu.Item>
    ];
    if (authUser && hasUserManagerRoles(authUser.role)) {
      menuItems.push(
        <Menu.Item key="users" icon={<UserOutlined />}>
          Users
        </Menu.Item>
      );
    }
    menuItems.push();
    return menuItems;
  };

  const handleNavigationMenuItemClick = (info: MenuInfo) => {
    const { key } = info;
    history.push(`/${key}`);
    setSelectedItem(key.toString());
  };

  const onSignOutClick = () => {
    dispatch(signOutUser());
  };

  const handleDropdownMenuItemClick = (info: MenuInfo) => {
    const { key } = info;
    if (key === 'sign-out') {
      onSignOutClick();
    } else if (key === 'settings') {
      history.push('/settings');
    }
  };

  const dropdownMenu = (
    <Menu onClick={handleDropdownMenuItemClick}>
      <Menu.Item key="settings" icon={<SettingOutlined />}>
        User Settings
      </Menu.Item>
      <Menu.Item key="sign-out" icon={<LogoutOutlined />}>
        Sign Out
      </Menu.Item>
    </Menu>
  );

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
      const { role } = authUser;
      let availableRoutes = [
        <Route key="time-zones" path="/time-zones">
          <TimeZones />
        </Route>
      ];
      if (hasUserManagerRoles(role)) {
        availableRoutes.push(
          <Route key="users" path="/users">
            <Users />
          </Route>
        );
      }

      availableRoutes = availableRoutes.concat([
        <Route key="settings" path="/settings">
          <Settings />
        </Route>,
        <Redirect key="redirect" exact={true} path="/" to="time-zones" />
      ]);
      return (
        <Layout id="chronos-layout">
          <Sider collapsible>
            <div className="logo" />
            <Menu
              theme="dark"
              selectedKeys={[selectedItem]}
              onClick={handleNavigationMenuItemClick}
              mode="inline"
            >
              {renderNavigationMenuItems()}
            </Menu>
          </Sider>
          <Layout>
            <Header>
              <Dropdown
                overlayStyle={{ width: '120px' }}
                placement="bottomRight"
                overlay={dropdownMenu}
                trigger={['click']}
              >
                <div
                  className="user-dropdown-menu"
                  onClick={e => e.preventDefault()}
                >
                  Hello, {authUser.username}! <DownOutlined />
                </div>
              </Dropdown>
            </Header>
            <Content id="chronos-content">
              <Switch>{availableRoutes}</Switch>
            </Content>
          </Layout>
        </Layout>
      );
    }
  };

  return <div id="chronos-main">{renderContent()}</div>;
};

export default Main;
