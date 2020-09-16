import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Alert, Button, Form, Input } from 'antd';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { Dispatch } from 'redux';
import { registerUser } from '../../actions/auth/Actions';
import { AuthAction } from '../../actions/auth/ActionTypes';
import { USERNAME_REGEX } from '../../constants/Regex';
import { StoreState } from '../../types';
import { AuthRequestDto, AuthStore } from '../../types/Auth';
import './SignUp.css';

const SignIn: React.FC = () => {
  const { authUser, error } = useSelector<StoreState, AuthStore>(
    s => s.authStore
  );

  const dispatch = useDispatch<Dispatch<AuthAction>>();

  const onFinish = (registrationRequest: AuthRequestDto) => {
    dispatch(registerUser(registrationRequest));
  };

  if (authUser) {
    return <Redirect to="/" />;
  }

  return (
    <div id="timezone-manager-signup">
      <div id="signup-container">
        {error && error.statusCode !== 401 && (
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
        <h2>Sign up to Timezone Manager</h2>
        <Form
          name="normal_signup"
          className="signup-form"
          initialValues={{}}
          onFinish={onFinish}
        >
          <Form.Item
            name="username"
            hasFeedback
            rules={[
              { required: true, message: 'Please input your Username!' },
              {
                min: 6,
                message: 'Username must contain minimum 6 characters!'
              },
              {
                max: 30,
                message: 'Username must contain maximum 30 characters!'
              },
              {
                pattern: USERNAME_REGEX,
                message:
                  'Only (.) and (_) are allowed as special characters! These cannot be together!'
              }
            ]}
          >
            <Input prefix={<UserOutlined />} placeholder="Username" />
          </Form.Item>
          <Form.Item
            name="password"
            hasFeedback
            rules={[
              { required: true, message: 'Please input your Password!' },
              {
                min: 6,
                message: 'Password must contain minimum 6 characters!'
              }
            ]}
          >
            <Input
              prefix={<LockOutlined />}
              type="password"
              placeholder="Password"
            />
          </Form.Item>

          <Form.Item
            name="confirmPassword"
            hasFeedback
            rules={[
              {
                required: true,
                message: 'Please confirm your password!'
              },
              ({ getFieldValue }) => ({
                validator(rule, value) {
                  if (!value || getFieldValue('password') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(
                    'Passwords that you entered do not match!'
                  );
                }
              })
            ]}
          >
            <Input
              prefix={<LockOutlined />}
              type="password"
              placeholder="Confirm Password"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="signup-form-button"
            >
              Sign Up
            </Button>
            Or <Link to="/sign-in">sign in now!</Link>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default SignIn;
