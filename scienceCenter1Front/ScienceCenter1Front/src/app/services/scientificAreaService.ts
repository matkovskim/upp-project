import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ScientificAreaService {

  constructor(private httpClient: HttpClient, private http : Http) { }

  getAllScientificAreas(){ return this.httpClient.get('http://localhost:8080/scientificArea') as Observable<any> }

}
