import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { TokenService } from '../services/token.service';


const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private _token: TokenService) {}

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
  {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
];
