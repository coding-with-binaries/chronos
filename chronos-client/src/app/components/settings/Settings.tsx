import { Alert, Button, Form, Input } from 'antd';
import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Dispatch } from 'redux';
import { signOutUser } from '../../actions/auth/Actions';
import { AuthAction } from '../../actions/auth/ActionTypes';
import UserApi from '../../api/user/User';
import { StoreState } from '../../types';
import { AuthStore, UpdatePasswordDto } from '../../types/Auth';
import { ErrorResponse } from '../../types/Common';
import './Settings.css';

const Settings: React.FC = props => {
  const dispatch = useDispatch<Dispatch<AuthAction>>();
  const { authUser } = useSelector<StoreState, AuthStore>(s => s.authStore);

  const [error, setError] = useState<ErrorResponse>();

  const onFinish = async (updatePasswordDto: UpdatePasswordDto) => {
    try {
      setError(undefined);
      if (authUser) {
        await UserApi.updateUserPassword(authUser.uid, updatePasswordDto);
        dispatch(signOutUser());
      }
    } catch (e) {
      setError(e.response.data);
    }
  };
  return (
    <div className="chronos-settings">
      <div className="update-password-container">
        <h2>Update Password</h2>
        <Alert
          message="After successful password update the user will be signed out!"
          type="info"
          showIcon
          closable
          style={{ marginBottom: '32px' }}
        />
        {error && (
          <Alert
            message={error.message}
            type="error"
            showIcon
            closable
            style={{ marginBottom: '16px' }}
          />
        )}
        <Form
          name="normal_signup"
          layout="vertical"
          className="signup-form"
          initialValues={{}}
          onFinish={onFinish}
        >
          <Form.Item
            label="Current Password"
            name="currentPassword"
            rules={[
              { required: true, message: 'Please input current password!' }
            ]}
          >
            <Input.Password placeholder="Enter your current password here" />
          </Form.Item>
          <Form.Item
            label="New Password"
            name="newPassword"
            hasFeedback
            rules={[
              { required: true, message: 'Please input new password!' },
              {
                min: 6,
                message: 'Password must contain minimum 6 characters!'
              }
            ]}
          >
            <Input type="password" placeholder="Enter your new password here" />
          </Form.Item>

          <Form.Item
            label="Confirm Password"
            name="confirmPassword"
            hasFeedback
            rules={[
              {
                required: true,
                message: 'Please confirm new password!'
              },
              ({ getFieldValue }) => ({
                validator(rule, value) {
                  if (!value || getFieldValue('newPassword') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(
                    'Passwords that you entered do not match!'
                  );
                }
              })
            ]}
          >
            <Input type="password" placeholder="Confirm new password here" />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="update-password-button"
            >
              Update
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default Settings;
