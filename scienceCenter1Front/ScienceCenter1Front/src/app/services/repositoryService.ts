import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Http, Response } from '@angular/http';


import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RepositoryService {

  constructor(private httpClient: HttpClient, private http : Http) { 

  }

  startRegistrationProcess(){
    return this.httpClient.get('http://localhost:8080/welcome/startRegistration') as Observable<any>
  }

  startMagazineProcess(){
    return this.httpClient.get('http://localhost:8080/welcome/startCreatingMagazine') as Observable<any>
  }

  postData(data, taskId) {
    return this.httpClient.post("http://localhost:8080/welcome/post/".concat(taskId), data) as Observable<any>;
  }
  
  loginUser(user) {
    return this.httpClient.post("http://localhost:8080/auth/login", user) as Observable<any>;
  }

  getAllMyTasks(){
    return this.httpClient.get('http://localhost:8080/welcome/getAllMyTasks/') as Observable<any>
  }
  
  getMyNextTask(procesId) {
    return this.httpClient.get("http://localhost:8080/welcome/getTasks/".concat(procesId)) as Observable<any>;
  }

  getTaskInfo(taskId : string){
    return this.httpClient.get('http://localhost:8080/welcome/getTask/'.concat(taskId)) as Observable<any>
  }

}