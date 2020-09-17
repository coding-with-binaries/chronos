import { RoleType } from '../types/Auth';

export function hasAdminRoles(role: RoleType) {
  return role === RoleType.admin;
}

export function hasUserManagerRoles(role: RoleType) {
  return role === RoleType.admin || role === RoleType.user_manager;
}
