import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ScientificPaperService {

  url = 'https://localhost:9991/scientificPaper/';

  constructor(private httpClient: HttpClient) { }

  startProcess() {
    return this.httpClient.get<any>(this.url + 'startProcess');
  }

  pushFileToStorage(file: File, processInstance: String): Observable<HttpEvent<{}>> {
    const formdata: FormData = new FormData();

    formdata.append('processInstance', processInstance as string);
    formdata.append('file', file);


    return this.httpClient.post<any>( this.url + 'post', formdata);
  }

  downloadFileFromStorage(url: string) {
    return this.httpClient.get<any>(url);
  }

  getAllPapersByIssue(id: number) {
    return this.httpClient.get<any>(this.url + 'issue/' + id);
  }

}
