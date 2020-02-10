import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ScientificAreasServiceService {

  authenticationService = 'https://localhost:8080/scientificAreas/';

  constructor(private httpClient: HttpClient) { }

  public getScientificAreas() {
    return this.httpClient.get<any>(this.authenticationService + 'all');
  }


}
