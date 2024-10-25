import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UserService} from "../../../services/user.service";
import {Router} from "@angular/router";
import {MissionService} from "../../../services/mission.service";
import {TasksService} from "../../../services/tasks.service";
import {Missions} from "../../../interfaces/missions";
import {DataService} from "../../../services/data.service";

@Component({
  selector: 'app-new-task',
  templateUrl: './new-task.component.html',
  styleUrls: ['./new-task.component.css']
})
export class NewTaskComponent implements OnInit {
  CurrentMission: Missions;
  missionForm: FormGroup;
  private _snackBar = inject(MatSnackBar);

  constructor(private dataService:DataService,private user: UserService, private router: Router, private fb: FormBuilder, private missionService: TasksService) {

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
    });

  }

  Error: any;
  Warning: any;
  Success: any;

  formatDate(date: Date): string {
    const options: Intl.DateTimeFormatOptions = {
      weekday: 'short',
      year: 'numeric',
      month: 'short',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      timeZone: 'GMT',
      hour12: false
    };
    return new Intl.DateTimeFormat('en-US', options).format(date).replace(',', '') + ' GMT';
  }

  onSubmit(): void {
    console.log(this.missionForm.value.startdate)
    const startDate = new Date(this.missionForm.value.startdate);
    const endDate = new Date(this.missionForm.value.enddate);
    const formData = new FormData();
    formData.append('description', this.missionForm.value.address);
    formData.append('Title', this.missionForm.value.title);
    formData.append('fileurl', this.missionForm.value.budget);
    formData.append('startdate', this.formatDate(startDate));
    formData.append('enddate', this.formatDate(endDate));
    formData.append('idParticipants', this.missionForm.value.idTechniciens); // Append IDs as an array
    this.missionService.Add(this.CurrentMission.id,formData).then((response) => {
      this.Success = "Task added successfully!";
      setTimeout(() => {
        this.Success = null;
        this.router.navigate(['/missions']);
      }, 2000);
    }).catch((error) => {
      if(error.status === 406) {
        this.Warning = error.error;
      }else {
        this.Error = 'Failed to add mission. Please try again.';
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



  teamLeads = [];

  technicians = [];



  selectedTechnicians: any[] = [];
  filteredTeamLeads: any[] = [];


  onTechniciansChange() {
    // Get selected technicians' IDs
    const selectedIds = this.missionForm.value.idTechniciens;
    this.selectedTechnicians = this.technicians.filter(technician => selectedIds.includes(technician.id));
  }

}
