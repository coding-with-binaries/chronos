import { v4 as uuid } from 'uuid';
import { RoleType } from '../types/Auth';
import { User } from '../types/Users';

export const mockUsers: User[] = [
  {
    uid: uuid(),
    createdBy: 'administrator',
    enabled: true,
    lastModifiedBy: 'administrator',
    role: {
      uid: uuid(),
      createdBy: 'administrator',
      lastModifiedBy: 'administrator',
      type: RoleType.user
    },
    username: 'dummy.user'
  },
  {
    uid: uuid(),
    createdBy: 'administrator',
    enabled: true,
    lastModifiedBy: 'administrator',
    role: {
      uid: uuid(),
      createdBy: 'administrator',
      lastModifiedBy: 'administrator',
      type: RoleType.user_manager
    },
    username: 'dummy.usermanager'
  }
];
