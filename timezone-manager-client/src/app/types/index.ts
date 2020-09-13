import { AuthStore } from './Auth';
import { TimeZonesStore } from './TimeZones';

export interface StoreState {
  authStore: AuthStore;
  timeZonesStore: TimeZonesStore;
}
