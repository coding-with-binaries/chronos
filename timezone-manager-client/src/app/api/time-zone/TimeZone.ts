import { TimeZone, UpdateTimeZoneDto } from '../../types/TimeZones';
import Http from '../Http';
import { TIME_ZONES_URI, TIME_ZONE_URI } from './Uri';

export default class TimeZoneApi {
  public static async getAllTimeZones() {
    const response = await Http.get<TimeZone[]>(TIME_ZONES_URI);
    return response.data;
  }

  public static async addTimeZone(timeZone: UpdateTimeZoneDto) {
    const response = await Http.post<TimeZone>(TIME_ZONES_URI, timeZone);
    return response.data;
  }

  public static async editTimeZone(uid: string, timeZone: UpdateTimeZoneDto) {
    const response = await Http.put<TimeZone>(TIME_ZONE_URI(uid), timeZone);
    return response.data;
  }

  public static async deleteTimeZone(uid: string) {
    await Http.delete<TimeZone>(TIME_ZONE_URI(uid));
  }
}
