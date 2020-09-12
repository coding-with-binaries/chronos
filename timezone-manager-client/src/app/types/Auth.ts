import { AsyncState, ErrorResponse } from './Common';

export interface AuthRequestDto {
  username: string;
  password: string;
}

export enum Role {
  admin = 'admin',
  user_manager = 'user_manager',
  user = 'user'
}

export interface AuthResponseDto {
  accessToken: string;
  username: string;
  roles: Role[];
}

export interface AuthUser {
  username: string;
  roles: Role[];
}

export interface Auth {
  asyncState: AsyncState;
  accessToken: string | null;
  error?: ErrorResponse;
  authUser: AuthUser | null;
}
