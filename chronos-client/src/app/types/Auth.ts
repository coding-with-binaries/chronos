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
  uid: string;
  username: string;
  role: RoleType;
}

export interface AuthUser {
  uid: string;
  username: string;
  role: RoleType;
}

export interface UpdatePasswordDto {
  currentPassword: string;
  newPassword: string;
}

export interface AuthStore {
  asyncState: AsyncState;
  accessToken: string | null;
  error?: ErrorResponse;
  authUser: AuthUser | null;
}
