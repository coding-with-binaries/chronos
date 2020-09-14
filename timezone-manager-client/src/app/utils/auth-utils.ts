import { RoleType } from '../types/Auth';

export function hasAdminRoles(roles: RoleType[]) {
  return roles.includes(RoleType.admin);
}

export function hasUserManagerRoles(roles: RoleType[]) {
  return (
    roles.includes(RoleType.admin) || roles.includes(RoleType.user_manager)
  );
}
