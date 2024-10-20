import { Component, OnInit } from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AddTechnicienComponent} from "./add-technicien/add-technicien.component";
import {Route, Router} from "@angular/router";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-tables',
  templateUrl: './tables.component.html',
  styleUrls: ['./tables.component.scss']
})
export class TablesComponent implements OnInit {
  users: any[] = [];
  constructor(private router: Router,private User:UserService) { }

  ngOnInit() {
    this.GetAllTechnicien()
  }

  GetAllTechnicien(): void {
    const roles = ['ROLE_TECHNICIEN'];
    this.User.getUsersByRoles(roles).then(
      (data) => {
        this.users = data;
      },
      (error) => {
        console.error('Error fetching users', error);
      }
    );
  }

  NewTech(){
    this.router.navigate(['/Technician/add'])
  }
}
