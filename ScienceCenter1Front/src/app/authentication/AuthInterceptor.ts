import { HTTP_INTERCEPTORS, HttpClient, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class RequestLogInterceptor implements HttpInterceptor {

  constructor(private _token: TokenStorageService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
      let authenticationRequest = req;
      const token = this.token.getToken();
      if (token != null) {
        authenticationRequest = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token)});
      }

      return next.handle(authenticationRequest);
  }
  get token() {
    return this._token;
  }
}

export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: RequestLogInterceptor, multi: true }
];



