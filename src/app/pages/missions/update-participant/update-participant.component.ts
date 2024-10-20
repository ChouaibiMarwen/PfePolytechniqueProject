import {Component, inject, OnInit} from '@angular/core';
import {Missions} from "../../../interfaces/missions";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DataService} from "../../../services/data.service";
import {UserService} from "../../../services/user.service";
import {Router} from "@angular/router";
import {MissionService} from "../../../services/mission.service";

@Component({
  selector: 'app-update-participant',
  templateUrl: './update-participant.component.html',
  styleUrls: ['./update-participant.component.scss']
})
export class UpdateParticipantComponent implements OnInit {

  CurrentMission: Missions;
  missionForm: FormGroup;
  ids:number[]=[];
  private _snackBar = inject(MatSnackBar);

  constructor(private dataService: DataService,private user: UserService, private router: Router, private fb: FormBuilder, private missionService: MissionService) {
  }



  ngOnInit(): void {
    this.missionForm = this.fb.group({

      idTechniciens: ['', Validators.required],
    });

    this.dataService.data$.subscribe(data => {
      this.CurrentMission = data;
      this.Patch();
      this.LoadTechnicians()
    });
  }

  Error: any;
  Warning: any;
  Success: any;


  Patch(){
    this.CurrentMission?.participants.forEach((res=>{
      this.ids.push(res.id)
    }))
    this.missionForm.patchValue({
      idTechniciens:this.ids
    })
  }

  onSubmit(): void {
    const formData = new FormData();
    // const ids: number[] = this.missionForm.value.idTechniciens.map(res => res.id) || [];
    formData.append('idTechniciens', this.missionForm.value.idTechniciens);
    console.log(this.missionForm.value.idTechniciens)
    this.missionService.updateMissionParticipant(this.CurrentMission.id,formData).then((response) => {
      this.Success = "Mission participants updated successfully!";
      setTimeout(() => {
        this.Success = null;
        this.router.navigate(['/missions']);
      }, 2000);
    }).catch((error) => {
        this.Error = 'Failed to update participants. Please try again.';
      setTimeout(() => {
        this.Error = null;
        this.Warning = null;
      }, 2000);
    });
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }


  LoadTechnicians() {
    const roles = ['ROLE_TECHNICIEN'];
    this.user.getUsersByRoles(roles).then((res) => {
      this.teamLeads = res;
      this.technicians = res;
    })
  }

  teamLeads = [];

  technicians = [];



}
