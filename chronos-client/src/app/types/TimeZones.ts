import { AsyncState, ErrorResponse } from './Common';

export interface UpdateTimeZoneDto {
  differenceFromGmt: string;
  locationName: string;
  timeZoneName: string;
}

export interface TimeZone {
  createdBy: string;
  differenceFromGmt: string;
  lastModifiedBy: string;
  locationName: string;
  timeZoneName: string;
  uid: string;
}

export interface TimeZonesStore {
  asyncState: AsyncState;
  error?: ErrorResponse;
  timeZones: TimeZone[];
}
