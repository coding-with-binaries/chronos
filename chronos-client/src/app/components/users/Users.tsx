import {
  DeleteOutlined,
  EditOutlined,
  SearchOutlined
} from '@ant-design/icons';
import { Alert, Button, Input, Popconfirm, Space, Table, Tag } from 'antd';
import { ColumnsType, ColumnType } from 'antd/lib/table';
import React, { createRef, useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Dispatch } from 'redux';
import { deleteUser, getAllUsers } from '../../actions/users/Actions';
import { UserAction } from '../../actions/users/ActionTypes';
import { ROLE_MAPPING } from '../../constants/Roles';
import { StoreState } from '../../types';
import { AuthStore, RoleType } from '../../types/Auth';
import { RegisterUserDto, UsersStore } from '../../types/Users';
import { hasAdminRoles } from '../../utils/auth-utils';
import { getUserCreatedBy } from '../../utils/user-utils';
import UpdateUserModal from './UpdateUserModal';

interface UpdateModalData {
  visible: boolean;
  mode?: 'ADD' | 'EDIT';
  uid?: string;
  initialValues?: RegisterUserDto;
}

const Users: React.FC = () => {
  const { authUser } = useSelector<StoreState, AuthStore>(s => s.authStore);
  const { error, users } = useSelector<StoreState, UsersStore>(
    s => s.usersStore
  );
  const [{ visible, mode, uid, initialValues }, setUpdateModalData] = useState<
    UpdateModalData
  >({
    visible: false
  });

  const dispatch = useDispatch<Dispatch<UserAction>>();

  useEffect(() => {
    dispatch(getAllUsers());
  }, [dispatch]);

  const onConfirmDelete = (uid: string) => () => {
    dispatch(deleteUser(uid));
  };

  const dataSource = users
    .filter(u => u.username !== authUser?.username)
    .map(u => {
      return {
        key: u.uid,
        username: u.username,
        role: u.role,
        createdBy: getUserCreatedBy(u.username, u.createdBy),
        actions: u.uid
      };
    });

  type DataIndex = keyof typeof dataSource[0];

  const nodeRef = createRef<Input>();

  const getColumnSearchProps = (
    dataIndex: DataIndex
  ): ColumnType<typeof dataSource[0]> => ({
    filterDropdown: ({
      setSelectedKeys,
      selectedKeys,
      confirm,
      clearFilters
    }) => (
      <div style={{ padding: 8 }}>
        <Input
          ref={nodeRef}
          placeholder={`Search ${dataIndex}`}
          value={selectedKeys[0]}
          onChange={e =>
            setSelectedKeys(e.target.value ? [e.target.value] : [])
          }
          onPressEnter={confirm}
          style={{ width: 188, marginBottom: 8, display: 'block' }}
        />
        <Space>
          <Button
            type="primary"
            onClick={confirm}
            icon={<SearchOutlined />}
            size="small"
            style={{ width: 90 }}
          >
            Search
          </Button>
          <Button onClick={clearFilters} size="small" style={{ width: 90 }}>
            Reset
          </Button>
        </Space>
      </div>
    ),
    filterIcon: filtered => (
      <SearchOutlined style={{ color: filtered ? '#1890ff' : undefined }} />
    ),
    onFilter: (value, user) =>
      !!user[dataIndex] &&
      user[dataIndex]
        .toString()
        .toLowerCase()
        .includes(value.toString().toLowerCase()),
    onFilterDropdownVisibleChange: visible => {
      if (visible) {
        setTimeout(() => {
          if (nodeRef.current) {
            nodeRef.current.select();
          }
        }, 100);
      }
    }
  });

  const getColumns = () => {
    const columns: ColumnsType<typeof dataSource[0]> = [
      {
        title: 'Username',
        dataIndex: 'username',
        key: 'username',
        ...getColumnSearchProps('username')
      },
      {
        title: 'Roles',
        dataIndex: 'roles',
        key: 'roles',
        filters: [
          {
            text: 'Administrator',
            value: RoleType.admin
          },
          {
            text: 'User Manager',
            value: RoleType.user_manager
          },
          {
            text: 'User',
            value: RoleType.user
          }
        ],
        render: (_, { role }) => (
          <Space size="small">
            <Tag key={role.uid} color="geekblue">
              {ROLE_MAPPING[role.type] || role.type}
            </Tag>
          </Space>
        ),
        onFilter: (filterValue, user) => user.role.type === filterValue
      }
    ];
    if (authUser && hasAdminRoles(authUser.role)) {
      columns.push({
        title: 'Created By',
        dataIndex: 'createdBy',
        key: 'createdBy',
        ...getColumnSearchProps('createdBy'),
        render: createdBy => <Tag color="geekblue">{createdBy}</Tag>
      });
    }

    columns.push({
      title: 'Actions',
      dataIndex: 'actions',
      key: 'actions',
      render: (uid, user) => (
        <Space size="middle">
          <Button
            title="Edit"
            icon={<EditOutlined />}
            type="link"
            onClick={showEditUserModal(user)}
          />
          <Popconfirm
            title="This action cannot be reverted! Want to proceed?"
            okText="Delete"
            okButtonProps={{ danger: true }}
            onConfirm={onConfirmDelete(uid)}
          >
            <Button title="Delete" icon={<DeleteOutlined />} type="link" />
          </Popconfirm>
        </Space>
      )
    });
    return columns;
  };

  const showAddUserModal = () => {
    setUpdateModalData({
      visible: true,
      mode: 'ADD',
      initialValues: {
        username: '',
        password: '',
        role: RoleType.user
      }
    });
  };
  const showEditUserModal = (user: typeof dataSource[0]) => () => {
    setUpdateModalData({
      visible: true,
      mode: 'EDIT',
      uid: user.key,
      initialValues: {
        username: user.username,
        password: '',
        role: user.role.type
      }
    });
  };
  const onDismiss = () => setUpdateModalData({ visible: false });

  return (
    <div className="chronos-time-zones">
      <Button
        onClick={showAddUserModal}
        type="primary"
        style={{ marginBottom: 16 }}
      >
        Add User
      </Button>
      {error && (
        <Alert
          type="error"
          message={
            error.message ||
            'Something went wrong! Please check your connectivity and try again.'
          }
          closable
          style={{ marginBottom: 16 }}
        />
      )}
      <Table bordered columns={getColumns()} dataSource={dataSource} />
      {visible && !!mode && !!initialValues && (
        <UpdateUserModal
          visible={visible}
          mode={mode}
          uid={uid}
          initialValues={initialValues}
          onDismiss={onDismiss}
        />
      )}
    </div>
  );
};

export default Users;
