import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MagazineService {

  constructor(private httpClient: HttpClient, private http : Http) { }

  getAllMagazines(){ return this.httpClient.get('https://localhost:8080/magazine') as Observable<any> }

  getAllMagazinesPublications(magazineId){ return this.httpClient.get('https://localhost:8080/publication/get/'.concat(magazineId)) as Observable<any> }

  getAllPublicationArticles(publicationId){ return this.httpClient.get('https://localhost:8080/article/get/'.concat(publicationId)) as Observable<any> }

}
