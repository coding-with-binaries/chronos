import { AuthStore } from './Auth';
import { TimeZonesStore } from './TimeZones';
import { UsersStore } from './Users';

export interface StoreState {
  authStore: AuthStore;
  timeZonesStore: TimeZonesStore;
  usersStore: UsersStore;
}
