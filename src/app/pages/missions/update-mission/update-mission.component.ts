import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UserService} from "../../../services/user.service";
import {Router} from "@angular/router";
import {MissionService} from "../../../services/mission.service";
import {DataService} from "../../../services/data.service";
import {Missions} from "../../../interfaces/missions";

@Component({
  selector: 'app-update-mission',
  templateUrl: './update-mission.component.html',
  styleUrls: ['./update-mission.component.scss']
})
export class UpdateMissionComponent implements OnInit {
  CurrentMission: Missions;
  missionForm: FormGroup;
  ids:number[]=[];
  private _snackBar = inject(MatSnackBar);

  constructor(private dataService: DataService,private user: UserService, private router: Router, private fb: FormBuilder, private missionService: MissionService) {
  }



  ngOnInit(): void {
    this.missionForm = this.fb.group({
      title: ['', Validators.required],
      address: ['', Validators.required],
      budget: [null, [Validators.required, Validators.min(0)]],
      startdate: ['', Validators.required],
      enddate: ['', Validators.required],
      locationname: ['', Validators.required],
      locationLatitude: [null, Validators.required],
      locationLongitude: [null, Validators.required],
      idTeamLead: [null, Validators.required],
      idTechniciens: ['', Validators.required],
    });

    this.dataService.data$.subscribe(data => {
      this.CurrentMission = data;

      this.Patch();
    });

  }

  Error: any;
  Warning: any;
  Success: any;


  lang:any;
  lat:any;
  Patch(){
    this.CurrentMission.participants.forEach((res=>{
      this.ids.push(res.id)
    }))
    this.missionForm.patchValue({
      title: this.CurrentMission.title,
      address: this.CurrentMission.address,
      budget: this.CurrentMission.budget,
      startdate: this.formatDate(this.CurrentMission.startdate),
      enddate: this.formatDate(this.CurrentMission.enddate),
      locationname: this.CurrentMission.location.name,
      locationLatitude: this.CurrentMission.location.latitude,
      locationLongitude: this.CurrentMission.location.longitude,
      idTeamLead:this.CurrentMission.teamLead.id,
      idTechniciens:this.ids
    })
    this.lang = this.CurrentMission.location.longitude
    this.lat = this.CurrentMission.location.latitude
  }

  formatDate(timestamp) {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }








}
