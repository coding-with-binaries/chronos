export enum AsyncState {
  Completed = 'Completed',
  Failed = 'Failed',
  Fetching = 'Fetching',
  NotStarted = 'NotStarted'
}

export interface ErrorResponse {
  status: string;
  statusCode: number;
  message: string;
}
