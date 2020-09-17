import { RoleType } from './Auth';
import { AsyncState, ErrorResponse } from './Common';

export interface Role {
  createdBy: string;
  lastModifiedBy: string;
  type: RoleType;
  uid: string;
}

export interface User {
  createdBy: string;
  enabled: boolean;
  lastModifiedBy: string;
  role: Role;
  uid: string;
  username: string;
}

export interface RegisterUserDto {
  username: string;
  password: string;
  role: RoleType;
}

export interface UpdateUserDto {
  password: string;
  role: RoleType;
}

export interface UsersStore {
  asyncState: AsyncState;
  error?: ErrorResponse;
  users: User[];
}
