export const USERS_URI = '/api/v1/users';

export const USER_URI = (uid: string) => `${USERS_URI}/${uid}`;

export const AUTH_URI = `${USERS_URI}/auth`;

export const SIGN_IN_URI = `${AUTH_URI}/sign-in`;

export const SIGN_UP_URI = `${AUTH_URI}/sign-up`;

export const WHO_AM_I_URI = `${AUTH_URI}/who-am-i`;
