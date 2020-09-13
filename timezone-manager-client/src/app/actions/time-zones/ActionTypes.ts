import { ErrorResponse } from '../../types/Common';
import { TimeZone, UpdateTimeZoneDto } from '../../types/TimeZones';
import * as Actions from './ActionConstants';

export interface GetAllTimeZones {
  type: Actions.GET_ALL_TIME_ZONES;
}

export interface GetAllTimeZonesFailed {
  type: Actions.GET_ALL_TIME_ZONES_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface GetAllTimeZonesSuccess {
  type: Actions.GET_ALL_TIME_ZONES_SUCCESS;
  payload: {
    timeZones: TimeZone[];
  };
}

export interface AddTimeZone {
  type: Actions.ADD_TIME_ZONE;
  payload: {
    timeZone: UpdateTimeZoneDto;
  };
}

export interface AddTimeZoneFailed {
  type: Actions.ADD_TIME_ZONE_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface AddTimeZoneSuccess {
  type: Actions.ADD_TIME_ZONE_SUCCESS;
  payload: {
    timeZone: TimeZone;
  };
}

export interface EditTimeZone {
  type: Actions.EDIT_TIME_ZONE;
  payload: {
    uid: string;
    timeZone: UpdateTimeZoneDto;
  };
}

export interface EditTimeZoneFailed {
  type: Actions.EDIT_TIME_ZONE_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface EditTimeZoneSuccess {
  type: Actions.EDIT_TIME_ZONE_SUCCESS;
  payload: {
    timeZone: TimeZone;
  };
}

export interface DeleteTimeZone {
  type: Actions.DELETE_TIME_ZONE;
  payload: {
    uid: string;
  };
}

export interface DeleteTimeZoneFailed {
  type: Actions.DELETE_TIME_ZONE_FAILED;
  payload: {
    errorResponse: ErrorResponse;
  };
}

export interface DeleteTimeZoneSuccess {
  type: Actions.DELETE_TIME_ZONE_SUCCESS;
  payload: {
    uid: string;
  };
}

export type TimeZoneAction =
  | GetAllTimeZones
  | GetAllTimeZonesFailed
  | GetAllTimeZonesSuccess
  | AddTimeZone
  | AddTimeZoneFailed
  | AddTimeZoneSuccess
  | EditTimeZone
  | EditTimeZoneFailed
  | EditTimeZoneSuccess
  | DeleteTimeZone
  | DeleteTimeZoneFailed
  | DeleteTimeZoneSuccess;
