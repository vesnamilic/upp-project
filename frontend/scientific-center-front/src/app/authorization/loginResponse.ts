export class LoginResponse {
  token: string;
  type: string;
  username: string;
  authorities: string[];
  expiratonDate: Date;
}
