import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class IssueService {

  issueService = 'https://localhost:9991/issue/';
  constructor(private httpClient: HttpClient) { }

  public getMagazineIssues(id: number) {
    return this.httpClient.get<any>(this.issueService + 'magazine/' + id);
  }

  public getMagazinePaymentMethod(issueId: number) {
    return this.httpClient.get<any>(this.issueService + 'paymentMethod/' + issueId);
  }

  public getMagazineIssuesEditor() {
    return this.httpClient.get<any>(this.issueService + 'editor');
  }
}
