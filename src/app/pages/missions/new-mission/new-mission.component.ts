import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {MissionService} from "../../../services/mission.service";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'app-new-mission',
  templateUrl: './new-mission.component.html',
  styleUrls: ['./new-mission.component.scss']
})
export class NewMissionComponent implements OnInit {

  missionForm: FormGroup;
  private _snackBar = inject(MatSnackBar);

  constructor(private user: UserService, private router: Router, private fb: FormBuilder, private missionService: MissionService) {
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
    this.LoadTechnicians();

    this.missionForm.get('idTechniciens')?.valueChanges.subscribe(selectedValues => {
      this.selectedTechnicians = selectedValues;
      this.filterTeamLeads();
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
    formData.append('title', this.missionForm.value.title);
    formData.append('address', this.missionForm.value.address);
    formData.append('budget', this.missionForm.value.budget);
    formData.append('startdate', this.formatDate(startDate));
    formData.append('enddate', this.formatDate(endDate));
    formData.append('locationname', this.missionForm.value.locationname);
    formData.append('locationLatitude', this.missionForm.value.locationLatitude);
    formData.append('locationLongitude', this.missionForm.value.locationLongitude);
    formData.append('idTeamLead', this.missionForm.value.idTeamLead);
    formData.append('idTechniciens', this.missionForm.value.idTechniciens); // Append IDs as an array
    this.missionService.New_Mission(formData).then((response) => {
      this.Success = "Mission added successfully!";
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


  LoadTechnicians() {
    const roles = ['ROLE_TECHNICIEN'];
    this.user.getUsersByRoles(roles).then((res) => {
      this.teamLeads = res;
      this.technicians = res;
    })
  }

  teamLeads = [];

  technicians = [];



  selectedTechnicians: any[] = [];
  filteredTeamLeads: any[] = [];


  onTechniciansChange() {
    // Get selected technicians' IDs
    const selectedIds = this.missionForm.value.idTechniciens;
    this.selectedTechnicians = this.technicians.filter(technician => selectedIds.includes(technician.id));
    this.filterTeamLeads();
  }

  filterTeamLeads() {
    // Logic to filter team leads based on selected technicians
    if (this.selectedTechnicians.length > 0) {
      // Assuming you have a logic to decide which leads are available based on selected technicians
      this.filteredTeamLeads = this.teamLeads.filter(lead => {
        // Custom logic to filter leads based on selected technicians
        return this.selectedTechnicians.some(technician => {
          // Replace this with your actual condition
          return lead.id !== technician.id; // Example: a lead cannot be one of the selected technicians
        });
      });
    } else {
      this.filteredTeamLeads = this.teamLeads; // Reset to all team leads if no technicians are selected
    }
  }

}
