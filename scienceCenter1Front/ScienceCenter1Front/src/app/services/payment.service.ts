import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private httpClient: HttpClient, private http : Http) { }

  subscriptionForMagazines(subscriptionDTO) {
    return this.httpClient.post("https://localhost:8080/subscription/create", subscriptionDTO) as Observable<any>;
  }
}




