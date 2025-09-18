export interface UserGetAllResponse {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  enabled: boolean;
  provider: string;
  roles: string[];
}
