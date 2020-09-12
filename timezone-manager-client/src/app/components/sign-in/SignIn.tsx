import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Alert, Button, Form, Input } from 'antd';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useHistory, useLocation } from 'react-router-dom';
import { Dispatch } from 'redux';
import { authenticateUser } from '../../actions/auth/Actions';
import { AuthAction } from '../../actions/auth/ActionTypes';
import { StoreState } from '../../types';
import { Auth, AuthRequestDto } from '../../types/Auth';
import './SignIn.css';

const SignIn: React.FC = () => {
  const { search } = useLocation();
  const history = useHistory();
  const isRegistered = search === '?registered=true';

  const { error } = useSelector<StoreState, Auth>(s => s.auth);

  const dispatch = useDispatch<Dispatch<AuthAction>>();

  const onFinish = (authenticationRequest: AuthRequestDto) => {
    onCloseRegisteredAlert();
    dispatch(authenticateUser(authenticationRequest));
  };

  const onCloseRegisteredAlert = () => {
    history.replace('/sign-in');
  };

  return (
    <div id="timezone-manager-signin">
      <div id="signin-container">
        {error && (
          <Alert message={error.message} type="error" showIcon closable />
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
        <h2>Sign in to Timezone Manager</h2>
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
