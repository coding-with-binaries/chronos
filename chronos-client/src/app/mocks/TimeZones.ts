import { v4 as uuid } from 'uuid';
import { TimeZone } from '../types/TimeZones';

export const mockTimeZones: TimeZone[] = [
  {
    uid: uuid(),
    createdBy: 'administrator',
    differenceFromGmt: '+05:30',
    lastModifiedBy: 'administrator',
    locationName: 'Bengaluru',
    timeZoneName: 'India Timezone'
  },
  {
    uid: uuid(),
    createdBy: 'administrator',
    differenceFromGmt: '+05:45',
    lastModifiedBy: 'administrator',
    locationName: 'Nepal',
    timeZoneName: 'Nepal Timezone'
  }
];
