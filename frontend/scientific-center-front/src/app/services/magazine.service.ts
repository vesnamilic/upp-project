import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MagazineService {

magazineService = 'http://localhost:8080/magazine/';

constructor(private httpClient: HttpClient) { }

public createMagazine(magazineDTO, taskId) {
  return this.httpClient.post<any>(this.magazineService + 'create/' + taskId, magazineDTO);
}

startProcess(processName: String) {
  return this.httpClient.get<any>(this.magazineService + 'startProcess');
}

}
