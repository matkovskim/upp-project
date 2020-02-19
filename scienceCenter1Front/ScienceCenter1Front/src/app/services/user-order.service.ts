import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserOrderService {

  orderUrl = 'https://localhost:8080/orders/create';

  constructor(private httpClient: HttpClient) { }

  public sendOrder(order: UserOrder) {
    return this.httpClient.post<any>(this.orderUrl, order);
  }
}

export class UserOrder {
  constructor(public type: string, public ids: number[]) {}
}