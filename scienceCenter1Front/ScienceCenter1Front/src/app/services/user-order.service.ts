import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserOrderService {

  orderUrl = 'https://localhost:8080/orders/';

  subUrl = 'https://localhost:8080/subscription/create';

  constructor(private httpClient: HttpClient) { }

  public sendOrder(order: UserOrder) {
    return this.httpClient.post<any>(this.orderUrl + 'create', order);
  }

  public getAllPurchasedItems() {
    return this.httpClient.get<any>(this.orderUrl + 'user');
  }
}

export class UserOrder {
  constructor(public type: string, public ids: number[]) {}
}