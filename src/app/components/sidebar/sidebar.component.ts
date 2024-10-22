import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from "../../services/user.service";
import {CookieService} from "ngx-cookie-service";
import {Participant} from "../../interfaces/missions";

declare interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}

export const ROUTES: RouteInfo[] = [
  {path: '/dashboard', title: 'Dashboard', icon: 'ni-tv-2 text-primary', class: ''},
  {path: '/Technician', title: 'Technician Management', icon: 'ni-badge text-red', class: ''},
  {path: '/missions', title: 'Mission Management', icon: 'ni-briefcase-24 text-orange', class: ''},
  {path: '/transaction', title: 'Transaction Management', icon: 'ni-money-coins text-info', class: ''},
  {path: '/Budget_Requests', title: 'Budget Requests', icon: 'ni-bullet-list-67 text-yellow', class: ''},
  {path: '/user-profile', title: 'User profile', icon: 'ni-single-02  text-pink', class: ''},
];

export const Tech_ROUTES: RouteInfo[] = [
  {path: '/Calendar', title: 'Calendar', icon: 'ni-calendar-grid-58 text-primary', class: ''},
  {path: '/My_Missions', title: 'My Missions', icon: 'ni-badge text-red', class: ''},
  {path: '/My_Budget_Requests', title: 'My Budget Requests', icon: 'ni-bullet-list-67 text-yellow', class: ''},
  {path: '/My_Transactions', title: 'My Transactions', icon: 'ni-money-coins text-info', class: ''},
  {path: '/user-profile', title: 'User profile', icon: 'ni-single-02  text-pink', class: ''},
];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  public menuItems: any[];
  public isCollapsed = false;
  CurrentUser:Participant
  constructor(private user: UserService,private router: Router, private userService: UserService, private cookieService: CookieService) {
    if (!this.cookieService.check('TOKEN_DASH_ADMIN_TT') || this.cookieService.get('TOKEN_DASH_ADMIN_TT').length === 0) {

      this.router.navigate(['/login']);
    }
    user.getProfile().then((res)=>{
      this.CurrentUser = res;
    })
    // console.log(this.cookieService.get('TOKEN_DASH_ADMIN_TT'))
  }

  ngOnInit() {
    this.user.getProfile().then((res)=>{
      if(res.role.role === 'ROLE_TECHNICIEN'){
        this.menuItems = Tech_ROUTES.filter(menuItem => menuItem);
      }else{
        this.menuItems = ROUTES.filter(menuItem => menuItem);
      }
    })

    this.router.events.subscribe((event) => {
      this.isCollapsed = true;
    });
  }

  Logout() {

    this.userService.logout();
    this.deleteAllCookies()
    this.router.navigate(['/login'])
  }

  deleteAllCookies() {
    var cookies = document.cookie.split("; ");
    //console.log('cookies' , cookies);
    for (var i = 0; i < cookies.length; i++) {
      var cookie = cookies[i];
      var eqPos = cookie.indexOf("=");
      var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
      document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/";
    }
  }

  toggleSidebar() {
    this.isCollapsed = !this.isCollapsed;
  }
}
