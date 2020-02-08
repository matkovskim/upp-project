import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthentificationService {

  constructor(private httpClient: HttpClient, private http : Http) { }

  getAllUsers(){ return this.httpClient.get('http://localhost:8080/auth') as Observable<any> }

  registerAdmin(data) {
    return this.httpClient.post("http://localhost:8080/auth/admin", data) as Observable<any>;
  }

  registerEditor(data) {
    return this.httpClient.post("http://localhost:8080/auth/editor", data) as Observable<any>;
  }

}
