import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent, HttpErrorResponse,
} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {Router} from "@angular/router";
import {environment} from "../../environments/environment";
import {SharedService} from "../services/shared.service";
import {AuthService} from "../services/auth.service";
import {GlobalService} from "../global.service";

@Injectable()
export class HttpInterceptor implements HttpInterceptor {
  token: string = '';
  user: any | undefined
  constructor(private authService: AuthService  , private router: Router, private glovar: GlobalService, private shared: SharedService) {
    shared.currentUser.subscribe((res) => {
      this.user = res
    })
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.token = this.authService.getToken();
    let url = req.url
    let ispublic = url.includes(environment.serverUrl + '/api/v1/public/')
    let other = !url.includes(environment.serverUrl)
    if (!ispublic && !other && this.token) {
      const tokenizedReq = req.clone({ headers: req.headers.set('Authorization', 'Bearer ' + this.token) });
      return next.handle(tokenizedReq).pipe(
        tap({
          error: (err) => {
            if (err instanceof HttpErrorResponse) {
              this.glovar.handleHttpError(err,req)
            }
          }
        }));
    }
    return next.handle(req).pipe(
      tap({
          error: (err) => {
            if (err instanceof HttpErrorResponse) {
              this.glovar.handleHttpError(err,req)
            }
          }
        }
      ))
  }

}
