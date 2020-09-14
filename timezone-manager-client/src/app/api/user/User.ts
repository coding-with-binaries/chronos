import { AuthRequestDto, AuthResponseDto, AuthUser } from '../../types/Auth';
import { RegisterUserDto, UpdateUserDto, User } from '../../types/Users';
import { clearAuthorizationToken } from '../../utils/api-utils';
import Http from '../Http';
import {
  SIGN_IN_URI,
  SIGN_UP_URI,
  USERS_URI,
  USER_URI,
  WHO_AM_I_URI
} from './Uri';

export default class UserApi {
  public static async getAllUsers() {
    const response = await Http.get<User[]>(USERS_URI);
    return response.data;
  }

  public static async getUser(uid: string) {
    const response = await Http.get<User>(USER_URI(uid));
    return response.data;
  }

  public static async authenticateUser(authRequestDto: AuthRequestDto) {
    const response = await Http.post<AuthResponseDto>(
      SIGN_IN_URI,
      authRequestDto
    );
    return response.data;
  }

  public static async registerUser(registerUserDto: RegisterUserDto) {
    const response = await Http.post<User>(SIGN_UP_URI, registerUserDto);
    return response.data;
  }

  public static async updateUser(uid: string, updateUserDto: UpdateUserDto) {
    const response = await Http.put<User>(USER_URI(uid), updateUserDto);
    return response.data;
  }

  public static async deleteUser(uid: string) {
    const response = await Http.delete<User>(USER_URI(uid));
    return response.data;
  }

  public static async signOutUser() {
    clearAuthorizationToken();
  }

  public static async whoAmI() {
    const response = await Http.get<AuthUser>(WHO_AM_I_URI);
    return response.data;
  }
}
