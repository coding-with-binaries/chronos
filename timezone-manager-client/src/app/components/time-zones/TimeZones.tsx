import { DeleteOutlined, EditOutlined } from '@ant-design/icons';
import { Alert, Button, Popconfirm, Space, Table, Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Dispatch } from 'redux';
import {
  deleteTimeZone,
  getAllTimeZones
} from '../../actions/time-zones/Actions';
import { TimeZoneAction } from '../../actions/time-zones/ActionTypes';
import { StoreState } from '../../types';
import { AuthStore } from '../../types/Auth';
import { TimeZonesStore, UpdateTimeZoneDto } from '../../types/TimeZones';
import { hasAdminRoles } from '../../utils/auth-utils';
import CurrentTime from './CurrentTime';
import UpdateTimeZoneModal from './UpdateTimeZoneModal';

interface UpdateModalData {
  visible: boolean;
  mode?: 'ADD' | 'EDIT';
  uid?: string;
  initialValues?: UpdateTimeZoneDto;
}

const TimeZones: React.FC = () => {
  const { authUser } = useSelector<StoreState, AuthStore>(s => s.authStore);
  const { error, timeZones } = useSelector<StoreState, TimeZonesStore>(
    s => s.timeZonesStore
  );
  const [{ visible, mode, uid, initialValues }, setUpdateModalData] = useState<
    UpdateModalData
  >({
    visible: false
  });

  const dispatch = useDispatch<Dispatch<TimeZoneAction>>();

  useEffect(() => {
    dispatch(getAllTimeZones());
  }, [dispatch]);

  const onConfirmDelete = (uid: string) => () => {
    dispatch(deleteTimeZone(uid));
  };

  const getColumns = () => {
    const columns: ColumnsType<typeof dataSource[0]> = [
      {
        title: 'Time Zone Name',
        dataIndex: 'timeZoneName',
        key: 'timeZoneName'
      },
      {
        title: 'Time Zone City',
        dataIndex: 'locationName',
        key: 'locationName'
      },
      {
        title: 'Difference From GMT',
        dataIndex: 'differenceFromGmt',
        key: 'differenceFromGmt'
      },
      {
        title: 'Current Time in Time Zone',
        dataIndex: 'currentTime',
        key: 'currentTime',
        render: (differenceFromGmt: string) => (
          <CurrentTime differenceFromGmt={differenceFromGmt} />
        )
      }
    ];
    if (authUser && hasAdminRoles(authUser.roles)) {
      columns.push({
        title: 'Created By',
        dataIndex: 'createdBy',
        key: 'createdBy',
        render: (createdBy: string) => <Tag color="geekblue">{createdBy}</Tag>
      });
    }

    columns.push({
      title: 'Actions',
      dataIndex: 'actions',
      key: 'actions',
      render: (uid, timeZone) => (
        <Space size="middle">
          <Button
            title="Edit"
            icon={<EditOutlined />}
            type="link"
            onClick={showEditTimeZoneModal(timeZone)}
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

  const dataSource = timeZones.map(t => {
    return {
      key: t.uid,
      timeZoneName: t.timeZoneName,
      locationName: t.locationName,
      differenceFromGmt: t.differenceFromGmt,
      currentTime: t.differenceFromGmt,
      createdBy: t.createdBy,
      actions: t.uid
    };
  });

  const showAddTimeZoneModal = () => {
    setUpdateModalData({
      visible: true,
      mode: 'ADD',
      initialValues: {
        timeZoneName: '',
        locationName: '',
        differenceFromGmt: ''
      }
    });
  };
  const showEditTimeZoneModal = (timeZone: typeof dataSource[0]) => () => {
    console.log(timeZone);
    setUpdateModalData({
      visible: true,
      mode: 'EDIT',
      uid: timeZone.key,
      initialValues: {
        timeZoneName: timeZone.timeZoneName,
        locationName: timeZone.locationName,
        differenceFromGmt: timeZone.differenceFromGmt
      }
    });
  };
  const onDismiss = () => setUpdateModalData({ visible: false });

  return (
    <div className="timezone-manager-time-zones">
      <Button
        onClick={showAddTimeZoneModal}
        type="primary"
        style={{ marginBottom: 16 }}
      >
        Add Time Zone
      </Button>
      {error && (
        <Alert
          type="error"
          message={
            error.message ||
            'Something went wrong! Please check your connectivity and try again.'
          }
        />
      )}
      <Table bordered columns={getColumns()} dataSource={dataSource} />
      {visible && !!mode && !!initialValues && (
        <UpdateTimeZoneModal
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

export default TimeZones;
