import { Form, Input, Modal, Select } from 'antd';
import React, { useCallback, useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { Dispatch } from 'redux';
import { addUser, editUser } from '../../actions/users/Actions';
import { UserAction } from '../../actions/users/ActionTypes';
import UserApi from '../../api/user/User';
import { ROLE_MAPPING } from '../../constants/Roles';
import { RegisterUserDto, UpdateUserDto, User } from '../../types/Users';

interface Props {
  visible: boolean;
  mode: 'ADD' | 'EDIT';
  uid?: string;
  initialValues: RegisterUserDto;
  onDismiss: () => void;
}

const UpdateUserModal: React.FC<Props> = props => {
  const { visible, uid, mode, initialValues, onDismiss } = props;
  const dispatch = useDispatch<Dispatch<UserAction>>();

  const [form] = Form.useForm();

  const [user, setUser] = useState<User>();

  useEffect(() => {
    if (mode === 'EDIT' && user) {
      form.setFieldsValue({ username: user.username });
    }
  }, [mode, user, form]);

  const getUser = useCallback(async () => {
    if (uid) {
      const res = await UserApi.getUser(uid);
      setUser(res);
    }
  }, [uid]);

  useEffect(() => {
    getUser();
  }, [getUser]);

  const onSubmit = async () => {
    try {
      const formValues = await form.validateFields();
      if (mode === 'ADD') {
        const registerUserDto: RegisterUserDto = {
          username: formValues.username,
          password: formValues.password,
          roles: formValues.roles
        };
        dispatch(addUser(registerUserDto));
        form.resetFields();
        onDismiss();
      } else if (mode === 'EDIT' && !!uid) {
        const updateUserDto: UpdateUserDto = {
          password: formValues.password,
          roles: formValues.roles
        };
        dispatch(editUser(uid, updateUserDto));
        onDismiss();
      }
    } catch (e) {
      console.error('Error while updating user: ', e);
    }
  };

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
      <Form form={form} layout="vertical" initialValues={initialValues}>
        <Form.Item
          name="username"
          label="Username"
          rules={[{ required: true, message: 'Please input the username!' }]}
        >
          <Input disabled={mode === 'EDIT'} placeholder="Enter Username here" />
        </Form.Item>
        <Form.Item
          label="Password"
          name="password"
          hasFeedback
          rules={[
            { required: mode === 'ADD', message: 'Please input the username!' },
            {
              min: 6,
              message: 'Password should contain minimum 6 characters!'
            }
          ]}
        >
          <Input.Password type="password" placeholder="Password" />
        </Form.Item>
        <Form.Item
          name="roles"
          label="Roles"
          rules={[
            {
              required: true,
              message: 'Please select roles!'
            }
          ]}
        >
          <Select mode="multiple" placeholder="Select Roles" allowClear>
            {Object.entries(ROLE_MAPPING).map(([roleType, roleDisplay]) => (
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
