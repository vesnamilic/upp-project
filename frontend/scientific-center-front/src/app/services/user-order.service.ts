import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserOrder } from '../model/user-order';

@Injectable({
  providedIn: 'root'
})
export class UserOrderService {

  orderUrl = 'https://localhost:9991/orders/';

  subUrl = 'https://localhost:9991/subscription/create';

  constructor(private httpClient: HttpClient) { }

  public sendOrder(order: UserOrder) {
    return this.httpClient.post<any>(this.orderUrl + 'create', order);
  }

  public subscribeToMagazine(email: string) {
    return this.httpClient.post<any>(this.subUrl, email);
  }

  public getAllPurchasedItems() {
    return this.httpClient.get<any>(this.orderUrl + 'user');
  }
}
