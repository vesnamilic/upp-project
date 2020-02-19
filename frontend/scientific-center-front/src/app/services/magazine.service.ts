import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MagazineService {

  magazineService = 'https://localhost:9991/magazine/';

  constructor(private httpClient: HttpClient) { }

  public createMagazine(magazineDTO, taskId) {
    return this.httpClient.post<any>(this.magazineService + 'create/' + taskId, magazineDTO);
  }

  startProcess(processName: String) {
    return this.httpClient.get<any>(this.magazineService + 'startProcess');
  }

  public getAllMagazines() {
    return this.httpClient.get<any>(this.magazineService + 'all');
  }

  public getMagazinePaymentMethod(magazineId:number) {
    return this.httpClient.get<any>(this.magazineService + 'paymentMethod/' + magazineId);
  }

}
