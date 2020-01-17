import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest } from '../authorization/loginRequest';
import { LoginResponse } from '../authorization/loginResponse';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  authenticationService = 'http://localhost:8080/auth/';

  constructor(private httpClient: HttpClient) { }

  public attemptRegistration(user, taskId) {
    return this.httpClient.post(this.authenticationService + 'register/' + taskId, user);
  }

 attemtAuthentication(credentials: LoginRequest): Observable<LoginResponse> {
  return this.httpClient.post<LoginResponse>(this.authenticationService + 'login', credentials, httpOptions);
}

}
