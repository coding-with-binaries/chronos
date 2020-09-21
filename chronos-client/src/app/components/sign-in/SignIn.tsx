import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Alert, Button, Form, Input } from 'antd';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, Redirect, useHistory, useLocation } from 'react-router-dom';
import { Dispatch } from 'redux';
import { authenticateUser } from '../../actions/auth/Actions';
import { AuthAction } from '../../actions/auth/ActionTypes';
import { StoreState } from '../../types';
import { AuthRequestDto, AuthStore } from '../../types/Auth';
import './SignIn.css';

const SignIn: React.FC = () => {
  const { search } = useLocation();
  const history = useHistory();
  const isRegistered = search === '?registered=true';

  const { authUser, error } = useSelector<StoreState, AuthStore>(
    s => s.authStore
  );

  const dispatch = useDispatch<Dispatch<AuthAction>>();

  const onFinish = (authenticationRequest: AuthRequestDto) => {
    onCloseRegisteredAlert();
    dispatch(authenticateUser(authenticationRequest));
  };

  const onCloseRegisteredAlert = () => {
    history.replace('/sign-in');
  };

  if (authUser) {
    return <Redirect to="/" />;
  }

  return (
    <div id="chronos-signin">
      <div id="signin-container">
        {error && (
          <Alert
            message={
              error.message ||
              'Something went wrong! Please check your connection and try again!'
            }
            type="error"
            showIcon
            closable
          />
        )}
        {isRegistered && (
          <Alert
            message="User registered successfully!!"
            type="success"
            showIcon
            closable
            onClose={onCloseRegisteredAlert}
          />
        )}
        <h2>Sign in to Chronos</h2>
        <Form
          name="normal_signin"
          className="signin-form"
          initialValues={{}}
          onFinish={onFinish}
        >
          <Form.Item
            name="username"
            rules={[{ required: true, message: 'Please input your Username!' }]}
          >
            <Input prefix={<UserOutlined />} placeholder="Username" />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[{ required: true, message: 'Please input your Password!' }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Password" />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="signin-form-button"
            >
              Sign In
            </Button>
            Or <Link to="/sign-up">sign up now!</Link>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default SignIn;
