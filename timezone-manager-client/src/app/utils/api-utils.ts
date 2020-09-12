export function getAuthorizationHeader() {
  const authToken = localStorage.getItem('auth-token');
  if (authToken) {
    return `token:${authToken}`;
  }
  return null;
}

export function setAuthorizationToken(token: string) {
  localStorage.setItem('auth-token', token);
}

export function clearAuthorizationToken() {
  localStorage.removeItem('auth-token');
}
