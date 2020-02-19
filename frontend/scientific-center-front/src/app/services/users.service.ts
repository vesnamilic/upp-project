import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  authenticationService = 'https://localhost:9991/auth/';

  constructor(private httpClient: HttpClient) { }

  public getUsers() {
    return this.httpClient.get<any>(this.authenticationService + 'users/all');
  }

  public getGroups() {
    return this.httpClient.get<any>(this.authenticationService + 'users/groups');
  }

  public getGroupMembers(groupId: string) {
    return this.httpClient.get<any>(this.authenticationService + 'users/groups/' + groupId);
  }
}
