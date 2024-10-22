import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UserService} from "../../../services/user.service";
import {Router} from "@angular/router";
import {MissionService} from "../../../services/mission.service";
import {BugetService} from "../../../services/buget.service";
import {DataService} from "../../../services/data.service";
import {Missions} from "../../../interfaces/missions";

@Component({
  selector: 'app-demand-budget',
  templateUrl: './demand-budget.component.html',
  styleUrls: ['./demand-budget.component.css']
})
export class DemandBudgetComponent implements OnInit {
  CurrentMission:Missions
  missionForm: FormGroup;
  private _snackBar = inject(MatSnackBar);

  constructor(private dataService: DataService,private user: UserService, private router: Router, private fb: FormBuilder, private missionService: BugetService) {
    this.dataService.data$.subscribe(data => {
      this.CurrentMission = data;
    });
  }

  ngOnInit(): void {
    this.missionForm = this.fb.group({
      amount: ['', Validators.required],
      reason: ['', Validators.required],
    });
  }

  Error: any;
  Warning: any;
  Success: any;


  onSubmit(): void {
    const formData = new FormData();
    console.log(this.missionForm.value.amount)
    formData.append('amount', this.missionForm.value.amount);
    formData.append('idMission', this.CurrentMission.id.toString());
    formData.append('reason', this.missionForm.value.amount);
    this.missionService.Add(this.CurrentMission.id,formData).then((response) => {
      this.Success = "Budget Requested successfully!";
      setTimeout(() => {
        this.Success = null;
        this.router.navigate(['/My_Budget_Requests']);
      }, 2000);
    }).catch((error) => {
      if(error.status === 406) {
        this.Warning = error.error;
      }else {
        this.Error = 'Failed to requested budget. Please try again.';
      }
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




}
