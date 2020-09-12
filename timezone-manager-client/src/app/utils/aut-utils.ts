import { Role } from '../types/Auth';

export function hasAdminRoles(roles: Role[]) {
  return roles.includes(Role.admin);
}

export function hasUserManagerRoles(roles: Role[]) {
  return roles.includes(Role.admin) || roles.includes(Role.user_manager);
}
