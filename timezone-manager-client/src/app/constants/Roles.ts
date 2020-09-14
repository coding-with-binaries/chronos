import { RoleType } from '../types/Auth';

export const ROLE_MAPPING = {
  [RoleType.admin]: 'Administrator',
  [RoleType.user_manager]: 'User Manager',
  [RoleType.user]: 'User'
};
