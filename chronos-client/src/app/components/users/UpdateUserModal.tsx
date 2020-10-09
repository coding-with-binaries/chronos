import { Alert, Form, Input, Modal, Select } from 'antd';
import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Dispatch } from 'redux';
import { v4 as uuid } from 'uuid';
import { addUser, editUser } from '../../actions/users/Actions';
import { UserAction } from '../../actions/users/ActionTypes';
import { USERNAME_REGEX } from '../../constants/Regex';
import { ROLE_MAPPING } from '../../constants/Roles';
import { StoreState } from '../../types';
import { AuthStore, RoleType } from '../../types/Auth';
import { AsyncState, ErrorResponse } from '../../types/Common';
import { RegisterUserDto, User, UsersStore } from '../../types/Users';
import { hasAdminRoles } from '../../utils/auth-utils';

interface Props {
  visible: boolean;
  mode: 'ADD' | 'EDIT';
  uid?: string;
  initialValues: RegisterUserDto;
  onDismiss: () => void;
}

const UpdateUserModal: React.FC<Props> = props => {
  const { visible, uid, mode, initialValues, onDismiss } = props;
  const { authUser } = useSelector<StoreState, AuthStore>(s => s.authStore);
  const { users } = useSelector<StoreState, UsersStore>(s => s.usersStore);
  const dispatch = useDispatch<Dispatch<UserAction>>();

  const [form] = Form.useForm();

  const user = users.find(u => u.uid === uid);

  useEffect(() => {
    if (mode === 'EDIT' && user) {
      form.setFieldsValue({ username: user.username });
    }
  }, [mode, user, form]);

  const [asyncState, setAsyncState] = useState<AsyncState>(
    AsyncState.NotStarted
  );
  const [errorResponse, setErrorResponse] = useState<ErrorResponse>();

  const onSubmit = async () => {
    try {
      const formValues = await form.validateFields();
      if (mode === 'ADD') {
        try {
          setAsyncState(AsyncState.Fetching);
          const userExist = !!users.find(
            u => u.username === formValues.username
          );
          if (userExist) {
            setErrorResponse({
              message: `User already exists with username: ${formValues.username}`,
              status: 'CONFLICT',
              statusCode: 409
            });
            setAsyncState(AsyncState.Failed);
          } else {
            const user: User = {
              createdBy: authUser?.username || '-',
              enabled: true,
              lastModifiedBy: authUser?.username || '-',
              role: {
                createdBy: 'administrator',
                lastModifiedBy: 'administrator',
                type: formValues.role,
                uid: uuid()
              },
              uid: uuid(),
              username: formValues.username
            };
            dispatch(addUser(user));
            form.resetFields();
            onDismiss();
            setAsyncState(AsyncState.Completed);
          }
        } catch (e) {
          setErrorResponse(e.response.data);
          setAsyncState(AsyncState.Failed);
        }
      } else if (mode === 'EDIT' && !!uid) {
        try {
          setAsyncState(AsyncState.Fetching);
          const updatedUser: User = {
            createdBy: user?.createdBy || '-',
            enabled: true,
            lastModifiedBy: authUser?.username || '-',
            role: {
              createdBy: 'administrator',
              lastModifiedBy: 'administrator',
              type: formValues.role,
              uid: uuid()
            },
            uid,
            username: user?.username || '-'
          };
          dispatch(editUser(updatedUser));
          form.resetFields();
          onDismiss();
          setAsyncState(AsyncState.Completed);
        } catch (e) {
          setErrorResponse(e.response.data);
          setAsyncState(AsyncState.Failed);
        }
      }
    } catch (e) {
      console.error('Error while updating user: ', e);
    }
  };

  const isAdmin = !!authUser && hasAdminRoles(authUser.role);
  const allowedRoles = Object.entries(ROLE_MAPPING).filter(
    ([roleType]) => isAdmin || roleType !== RoleType.admin
  );
  return (
    <Modal
      visible={visible}
      centered
      title="Add User"
      cancelText="Cancel"
      okText={mode === 'EDIT' ? 'Update' : 'Add'}
      onCancel={onDismiss}
      onOk={onSubmit}
      width={800}
    >
      {asyncState === AsyncState.Failed && (
        <Alert
          type="error"
          message={
            errorResponse
              ? errorResponse.message
              : 'Something went wrong!. Please check your connectivity and try again'
          }
          style={{ marginBottom: '16px' }}
        />
      )}
      <Form form={form} layout="vertical" initialValues={initialValues}>
        <Form.Item
          name="username"
          label="Username"
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
          <Input disabled={mode === 'EDIT'} placeholder="Enter Username here" />
        </Form.Item>
        {mode === 'ADD' && (
          <Form.Item
            label="Password"
            name="password"
            hasFeedback
            rules={[
              { required: true, message: 'Please input the password!' },
              {
                min: 6,
                message: 'Password should contain minimum 6 characters!'
              }
            ]}
          >
            <Input.Password type="password" placeholder="Password" />
          </Form.Item>
        )}
        <Form.Item
          name="role"
          label="Role"
          rules={[
            {
              required: true,
              message: 'Please select role!'
            }
          ]}
        >
          <Select placeholder="Select Role" allowClear>
            {allowedRoles.map(([roleType, roleDisplay]) => (
              <Select.Option key={roleType} value={roleType}>
                {roleDisplay}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default UpdateUserModal;
