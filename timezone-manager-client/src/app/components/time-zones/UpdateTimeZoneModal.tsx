import { Form, Input, Modal, Select } from 'antd';
import React from 'react';
import { useDispatch } from 'react-redux';
import { Dispatch } from 'redux';
import { addTimeZone, editTimeZone } from '../../actions/time-zones/Actions';
import { TimeZoneAction } from '../../actions/time-zones/ActionTypes';
import { allTimeZones } from '../../constants/TimeZones';
import { UpdateTimeZoneDto } from '../../types/TimeZones';

interface Props {
  visible: boolean;
  mode: 'ADD' | 'EDIT';
  uid?: string;
  initialValues: UpdateTimeZoneDto;
  onDismiss: () => void;
}

const UpdateTimeZoneModal: React.FC<Props> = props => {
  const { visible, uid, mode, initialValues, onDismiss } = props;
  const dispatch = useDispatch<Dispatch<TimeZoneAction>>();

  const [form] = Form.useForm();

  const onSubmit = async () => {
    try {
      console.log(uid);
      const formValues = await form.validateFields();
      const updateTimeZoneDto: UpdateTimeZoneDto = {
        timeZoneName: formValues.timeZoneName,
        locationName: formValues.locationName,
        differenceFromGmt: formValues.differenceFromGmt
      };
      if (mode === 'ADD') {
        dispatch(addTimeZone(updateTimeZoneDto));
        form.resetFields();
        onDismiss();
      } else if (mode === 'EDIT' && !!uid) {
        dispatch(editTimeZone(uid, updateTimeZoneDto));
        onDismiss();
      }
    } catch (e) {
      console.error('Error while updating time zone: ', e);
    }
  };

  return (
    <Modal
      visible={visible}
      centered
      title="Add Time Zone"
      cancelText="Cancel"
      okText={mode === 'EDIT' ? 'Update' : 'Add'}
      onCancel={onDismiss}
      onOk={onSubmit}
      width={800}
    >
      <Form form={form} layout="vertical" initialValues={initialValues}>
        <Form.Item
          name="timeZoneName"
          label="Time Zone Name"
          rules={[
            { required: true, message: 'Please input the time zone name!' }
          ]}
        >
          <Input placeholder="Enter Time Zone Name here" />
        </Form.Item>
        <Form.Item
          name="locationName"
          label="City Name"
          rules={[{ required: true, message: 'Please input the city name!' }]}
        >
          <Input placeholder="Enter City Name here" />
        </Form.Item>
        <Form.Item
          name="differenceFromGmt"
          label="Difference From GMT"
          rules={[
            {
              required: true,
              message: 'Please select the difference from GMT!'
            }
          ]}
        >
          <Select placeholder="Select a Time Zone" allowClear>
            {allTimeZones.map(t => (
              <Select.Option key={t} value={t}>
                {t} GMT
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default UpdateTimeZoneModal;
