export function getUserCreatedBy(username: string, createdBy: string) {
  if (createdBy === 'system') {
    return username;
  }
  return createdBy;
}
