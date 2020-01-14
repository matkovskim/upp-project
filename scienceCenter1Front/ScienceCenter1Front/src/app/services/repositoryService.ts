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

  registerUser(user, taskId) {
    return this.httpClient.post("http://localhost:8080/auth/post/".concat(taskId), user) as Observable<any>;
  }

  loginUser(user) {
    return this.httpClient.post("http://localhost:8080/auth/login", user) as Observable<any>;
  }
  
  startProcess(){
    return this.httpClient.get('http://localhost:8080/welcome/get') as Observable<any>
  }

  getTasks(processInstance : string){
    return this.httpClient.get('http://localhost:8080/welcome/get/tasks/'.concat(processInstance)) as Observable<any>
  }

  getAllMyTasks(){
    return this.httpClient.get('http://localhost:8080/welcome/get/allMyTasks/') as Observable<any>
  }

  getTaskInfo(taskId : string){
    return this.httpClient.get('http://localhost:8080/welcome/getTask/'.concat(taskId)) as Observable<any>
  }

  completeTask(taskId : string, data){
    return this.httpClient.post('http://localhost:8080/admin/accept/'.concat(taskId), data) as Observable<any>
  }

  startMagazineProcess(){
    return this.httpClient.get('http://localhost:8080/magazine/getMagazineForm') as Observable<any>
  }

  createMagazine(magazine, taskId) {
    return this.httpClient.post("http://localhost:8080/magazine/post/".concat(taskId), magazine) as Observable<any>;
  }

  nextTasks(procesId) {
    return this.httpClient.get("http://localhost:8080/magazine/getTasks/".concat(procesId)) as Observable<any>;
  }


}
