import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AddTechnicienComponent} from "./add-technicien/add-technicien.component";
import {Route, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {Participant} from "../../interfaces/missions";

@Component({
  selector: 'app-tables',
  templateUrl: './tables.component.html',
  styleUrls: ['./tables.component.scss']
})
export class TablesComponent implements OnInit {
  users: Participant[] = [];

  constructor(private router: Router, private User: UserService) {
  }

  ngOnInit() {
    this.GetAllTechnicien()
  }

  GetAllTechnicien(): void {
    this.User.All_Users().then(
      (data) => {
        this.users = data;
      },
      (error) => {
        console.error('Error fetching users', error);
      }
    );
  }

  NewTech() {
    this.router.navigate(['/Technician/add'])
  }

  Active(id: any) {
    this.User.activated(id).then((res) => {
      this.GetAllTechnicien()
    })
  }

  Verify(id: any) {
    this.User.verified(id).then((res) => {
      this.GetAllTechnicien()
    })
  }

  Suspend(id: any) {
    this.User.suspended(id).then((res) => {
      this.GetAllTechnicien()
    })
  }
}
