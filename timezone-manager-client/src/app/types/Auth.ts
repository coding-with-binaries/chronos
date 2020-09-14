import { AsyncState, ErrorResponse } from './Common';

export interface AuthRequestDto {
  username: string;
  password: string;
}

export enum RoleType {
  admin = 'admin',
  user_manager = 'user_manager',
  user = 'user'
}

export interface AuthResponseDto {
  accessToken: string;
  username: string;
  roles: RoleType[];
}

export interface AuthUser {
  username: string;
  roles: RoleType[];
}

export interface AuthStore {
  asyncState: AsyncState;
  accessToken: string | null;
  error?: ErrorResponse;
  authUser: AuthUser | null;
}
