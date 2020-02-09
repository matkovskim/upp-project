import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  constructor(private httpClient: HttpClient, private http: Http) { }

  sendPDF(file: File, procesId: string): Observable<HttpEvent<{}>> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    formdata.append('procesId', procesId)
    const req = new HttpRequest('POST', '/post', formdata, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.httpClient.post('https://localhost:8080/welcome/post', formdata) as Observable<any>;
  }

  getFile(procesId: string) {
    return this.httpClient.get('https://localhost:8080/welcome/getFile/' + procesId) as Observable<any>;
  }

}
