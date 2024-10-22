import {Component, OnInit, OnDestroy, Renderer2} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {DeviceUUID} from 'device-uuid';
import {DeviceDetectorService} from "ngx-device-detector";
import {AuthService} from "../../services/auth.service";
import {MessagingService} from "../../services/messaging.service";
import {UserService} from "../../services/user.service";
import {SharedService} from "../../services/shared.service";
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
  ipAddress:any;
  see:boolean =false;
  login = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  constructor(private renderer: Renderer2,
              private router: Router,
              private shared: SharedService,
              private activatedRoute: ActivatedRoute,
              private messagingService: MessagingService,
              private authService: AuthService,
              private cookieService: CookieService,
              private deviceService: DeviceDetectorService,
              private userService: UserService,
              ) {
    if (this.cookieService.check('TOKEN_DASH_ADMIN_TT') && this.cookieService.get('TOKEN_DASH_ADMIN_TT').length != 0) {

      this.router.navigate(['/']);
    }
  }

  async ngOnInit() {
    this.ipAddress = (await this.authService.getIpAddress()).ip
  }
  ngOnDestroy() {
  }

  signin() {

    if (this.login.valid) {


      let login = {
        deviceId: new DeviceUUID().get(),
        deviceType:
          this.deviceService.browser +
          ' ' +
          this.deviceService.browser_version +
          '/' +
          this.deviceService.deviceType,
        ip: this.ipAddress,
        // tokendevice: this.messagingService.Token,
        password: this.login.get('password')?.value,
        username: (this.login.get('username')?.value)?.toLocaleLowerCase(),
      };
      // this.spinner.show();
      this.authService
        .signIn(login).then((res) => {
        const today = new Date()
        const tomorrow = new Date(today)
        tomorrow.setDate(tomorrow.getDate() + 1)
        if (res.roles === "ROLE_ADMIN" || res.roles === "ROLE_TECHNICIEN") {
          this.cookieService.set('TOKEN_DASH_ADMIN_TT', res.token!, tomorrow, '/');
          this.cookieService.set('STATE_DASH_ADMIN_TT', 'true', tomorrow, '/');
          this.cookieService.set('ROLE_DASH_ADMIN_TT', res.roles!, tomorrow, '/');
          this.cookieService.set('ROLE_DASH_ADMIN_TT_OTP', '0', tomorrow, '/');
          // this.spinner.show();
          this.userService.getProfile().then((res) => {
            this.shared.changeUser(res)
            let privileges = res.privileges.map((p) => p.name)
            this.cookieService.set('USER_TT_PRIVILEGES', privileges.join(', '), tomorrow, '/');
            // let routes = getAllowedRoutes(privileges)
            // console.log('Allowed routes', routes);
            // this.resetAttempts()
            // if (routes.includes('/home')) {
            if (res.role.role === 'ROLE_ADMIN') {
              console.log("Here")
              this.router.navigate(['/dashboard']);
            }else{
              console.log("Here")
              this.router.navigate(['/Calendar']);
            }
              // this.setAction(1)
              // this.onUserIDChange(res.id.toString());
              // this.SentOTP('SMS_OTP');
              // // this.router.navigate([`/otp/${id}`]);
              // this.otpVerificationService.setVerified(false);
            // } else {
            //   try {
            //     this.router.navigate([routes[0]]);
            //   } catch (err) {
            //     this.glovar.showMsg(this.userService.getTranslatedWord('Errors.NotAllowed') , this.userService.getTranslatedWord('Oops') , 'error_snack');
            //
            //   }
            // }
            console.log(res)

          }).catch((error) => {
            // this.glovar.showMsg(this.userService.getTranslatedWord('Errors.CntLoadProfile') , this.userService.getTranslatedWord('Oops') , 'error_snack');
            this.login.reset();
            this.userService.logout()
          }).finally(() => {
            // this.spinner.hide()
          })
        } else {
          // this.glovar.showMsg(this.userService.getTranslatedWord('Errors.NotAccess')  , this.userService.getTranslatedWord('Oops') , 'info_snack')
        }
      }).catch((error) => {
        // this.glovar.showMsg(this.userService.getTranslatedWord('Errors.WrongCred')  , this.userService.getTranslatedWord('Oops') , 'error_snack')
        this.login.reset();
        // this.attemptCount++;
        // this.updateCaptchaValidator();
        // if (this.attemptCount >= 2) {
        //   this.captchaVisible = true;
        //   this.createCaptcha()
        // }
      }).finally(() => {
        // this.spinner.hide()
      })
    } else {
      // this.glovar.checkFields(this.login)
    }
  }

}
