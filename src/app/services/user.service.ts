import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {BehaviorSubject, lastValueFrom, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {DeviceUUID} from 'device-uuid';
import {DeviceDetectorService} from "ngx-device-detector";
import {CookieService} from "ngx-cookie-service";
import {AuthService} from "./auth.service";
import {SharedService} from "./shared.service";
@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private shared: SharedService,private authService: AuthService,private http: HttpClient,private deviceService: DeviceDetectorService,private cookieService: CookieService) { }

  getProfile() {
    return lastValueFrom(this.http.get<any>(environment.serverUrl + '/api/v1/users/current_user'))
  }

  getUsersByRoles(roles: string[]) {
    const params = {
      role: roles
    };
    return lastValueFrom(this.http.get<any>(environment.serverUrl + '/api/v1/users/all_users_list_by_roles_list',{params}))
  }

  logout() {
    let obj = {
      deviceInfo: {
        deviceId: new DeviceUUID().get(),
        deviceType: this.deviceService.deviceType,
      },
      token: this.cookieService.get('TOKEN_DASH_ADMIN_TT')
    }
    this.authService.logoutUser(obj).then((res) => {
    }).then(() => {
    }).catch((err) => {
    }).finally(() => {
      this.authService.isLogin = false;
      this.authService.roleAs = "";
      this.cookieService.set("STATE_DASH_ADMIN_TT", "false");
      this.cookieService.set("ROLE_DASH_ADMIN_TT", "");
      this.cookieService.set("TOKEN_DASH_ADMIN_TT", "");
      this.cookieService.set("USER_TT_PRIVILEGES", "");
      this.authService.changeLoggedIn(false);
      this.shared.changeUser(null);
      return of({success: this.authService.isLogin, role: ""});

    })
  }



}
