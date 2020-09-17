import { ErrorResponse } from '../../types/Common';
import { TimeZone, UpdateTimeZoneDto } from '../../types/TimeZones';
import * as Actions from './ActionConstants';
import {
  AddTimeZone,
  AddTimeZoneFailed,
  AddTimeZoneSuccess,
  DeleteTimeZone,
  DeleteTimeZoneFailed,
  DeleteTimeZoneSuccess,
  EditTimeZone,
  EditTimeZoneFailed,
  EditTimeZoneSuccess,
  GetAllTimeZones,
  GetAllTimeZonesFailed,
  GetAllTimeZonesSuccess
} from './ActionTypes';

export const getAllTimeZones = (): GetAllTimeZones => ({
  type: Actions.GET_ALL_TIME_ZONES
});

export const getAllTimeZonesFailed = (
  errorResponse: ErrorResponse
): GetAllTimeZonesFailed => ({
  type: Actions.GET_ALL_TIME_ZONES_FAILED,
  payload: { errorResponse }
});

export const getAllTimeZonesSuccess = (
  timeZones: TimeZone[]
): GetAllTimeZonesSuccess => ({
  type: Actions.GET_ALL_TIME_ZONES_SUCCESS,
  payload: { timeZones }
});

export const addTimeZone = (timeZone: UpdateTimeZoneDto): AddTimeZone => ({
  type: Actions.ADD_TIME_ZONE,
  payload: { timeZone }
});

export const addTimeZoneFailed = (
  errorResponse: ErrorResponse
): AddTimeZoneFailed => ({
  type: Actions.ADD_TIME_ZONE_FAILED,
  payload: { errorResponse }
});

export const addTimeZoneSuccess = (timeZone: TimeZone): AddTimeZoneSuccess => ({
  type: Actions.ADD_TIME_ZONE_SUCCESS,
  payload: { timeZone }
});

export const editTimeZone = (
  uid: string,
  timeZone: UpdateTimeZoneDto
): EditTimeZone => ({
  type: Actions.EDIT_TIME_ZONE,
  payload: { uid, timeZone }
});

export const editTimeZoneFailed = (
  errorResponse: ErrorResponse
): EditTimeZoneFailed => ({
  type: Actions.EDIT_TIME_ZONE_FAILED,
  payload: { errorResponse }
});

export const editTimeZoneSuccess = (
  timeZone: TimeZone
): EditTimeZoneSuccess => ({
  type: Actions.EDIT_TIME_ZONE_SUCCESS,
  payload: { timeZone }
});

export const deleteTimeZone = (uid: string): DeleteTimeZone => ({
  type: Actions.DELETE_TIME_ZONE,
  payload: { uid }
});

export const deleteTimeZoneFailed = (
  errorResponse: ErrorResponse
): DeleteTimeZoneFailed => ({
  type: Actions.DELETE_TIME_ZONE_FAILED,
  payload: { errorResponse }
});

export const deleteTimeZoneSuccess = (uid: string): DeleteTimeZoneSuccess => ({
  type: Actions.DELETE_TIME_ZONE_SUCCESS,
  payload: { uid }
});
