import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest } from '../authorization/loginRequest';
import { LoginResponse } from '../authorization/loginResponse';
import { Registration } from '../authorization/registrationRequest';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  authenticationService = 'https://localhost:9991/auth/';

  constructor(private httpClient: HttpClient) { }

  startProcess() {
    return this.httpClient.get<any>(this.authenticationService + 'startProcess');
  }

  public attemptRegistration(user, taskId) {
    return this.httpClient.post(this.authenticationService + 'register/' + taskId, user);
  }

  public attemtAuthentication(credentials: LoginRequest): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(this.authenticationService + 'login', credentials, httpOptions);
  }

  public registerEditor(user: Registration) {
    return this.httpClient.post<any>(this.authenticationService + 'adminRegistration', user);
  }

}
