import { AuthRequestDto, AuthResponseDto, AuthUser } from '../../types/Auth';
import { clearAuthorizationToken } from '../../utils/api-utils';
import Http from '../Http';
import { SIGN_IN_URI, SIGN_UP_URI, WHO_AM_I_URI } from './Uri';

export default class UserApi {
  public static async authenticateUser(authRequestDto: AuthRequestDto) {
    const response = await Http.post<AuthResponseDto>(
      SIGN_IN_URI,
      authRequestDto
    );
    return response.data;
  }

  public static async registerUser(authRequestDto: AuthRequestDto) {
    await Http.post(SIGN_UP_URI, authRequestDto);
  }

  public static async signOutUser() {
    clearAuthorizationToken();
  }

  public static async whoAmI() {
    const response = await Http.get<AuthUser>(WHO_AM_I_URI);
    return response.data;
  }
}
