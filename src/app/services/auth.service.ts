import { Injectable } from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {BehaviorSubject, lastValueFrom} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private emailTable = new BehaviorSubject<any>(null);
  email = this.emailTable.asObservable();
  private resetCodeTable = new BehaviorSubject<any>(null);
  resetCode = this.resetCodeTable.asObservable();
  private isLoggedSource = new BehaviorSubject(this.isLoggedIn());
  currentIsLogged = this.isLoggedSource.asObservable()
  Root = environment.serverUrl
  isLogin = false;
  roleAs?: string | null;
  token?: string | null;
  constructor(private cookieService: CookieService,private http:HttpClient) { }

  getToken() {
    this.token = this.cookieService.get('TOKEN_DASH_ADMIN_TT');
    return this.token;
  }

  getIpAddress() {
    return lastValueFrom(this.http.get<any>('https://api.ipify.org/?format=json'))
  }

  signIn(user: any) {
    return lastValueFrom(this.http.post<any>(environment.serverUrl + '/api/v1/auth/signin', user))
  }

  logoutUser(data: any) {
    return lastValueFrom(
      this.http.put(environment.serverUrl + `/api/v1/users/logout`, data, { responseType: 'text' })
    );
  }

  isLoggedIn() {
    const loggedIn = this.cookieService.get('STATE_DASH_ADMIN_TT');
    const token = this.cookieService.get('TOKEN_DASH_ADMIN_TT');
    if (loggedIn == 'true' && token != '') {
      this.isLogin = true;
    } else {
      this.isLogin = false;
    }
    return this.isLogin;
  }

  changeLoggedIn(state: boolean) {
    this.isLoggedSource.next(state)
  }
}
