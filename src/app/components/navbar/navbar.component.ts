import { Component, OnInit, ElementRef } from '@angular/core';
import {ROUTES, Tech_ROUTES} from '../sidebar/sidebar.component';
import { Location, LocationStrategy, PathLocationStrategy } from '@angular/common';
import { Router } from '@angular/router';
import {DataService} from "../../services/data.service";
import {UserService} from "../../services/user.service";
import {Participant} from "../../interfaces/missions";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  public focus;
  public listTitles: any[];
  public location: Location;
  public CurrenUser: Participant;
  constructor(location: Location,  private userService: UserService, private user: UserService, private element: ElementRef, private router: Router) {
    this.location = location;
    user.getProfile().then((res)=>{
      this.CurrenUser = res;
      if(res.role.role === 'ROLE_TECHNICIEN'){
        this.listTitles = Tech_ROUTES.filter(listTitle => listTitle);
      }else{
        this.listTitles = ROUTES.filter(listTitle => listTitle);
      }
    })
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

  ngOnInit() {

  }
  getTitle(){
    var titlee = this.location.prepareExternalUrl(this.location.path());
    if(titlee.charAt(0) === '#'){
        titlee = titlee.slice( 1 );
    }

    for(var item = 0; item < this.listTitles?.length; item++){
        if(this.listTitles[item].path === titlee){
            return this.listTitles[item].title;
        }
    }
    return 'Dashboard';
  }

}
