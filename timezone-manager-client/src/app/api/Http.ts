import axios from 'axios';
import { getAuthorizationHeader } from '../utils/api-utils';

class Http {
  private getHeaders() {
    const authHeader = getAuthorizationHeader();
    return authHeader ? { Authorization: authHeader } : {};
  }

  public get<T>(url: string) {
    const headers = this.getHeaders();
    return axios.get<T>(url, { headers });
  }

  public post<T>(url: string, data?: any) {
    const headers = this.getHeaders();
    return axios.post<T>(url, data, { headers });
  }
}

export default new Http();
