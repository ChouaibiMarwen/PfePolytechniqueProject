import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {TechnicianService} from "../../../services/technician.service";
import {DataService} from "../../../services/data.service";
import {Participant} from "../../../interfaces/missions";

@Component({
  selector: 'app-update-tech',
  templateUrl: './update-tech.component.html',
  styleUrls: ['./update-tech.component.scss']
})
export class UpdateTechComponent implements OnInit {
  CurrentUser: Participant;
  technicianForm: FormGroup;
  private _snackBar = inject(MatSnackBar);
  softSkills: { name: string; value: string }[] = [
    {name: 'Active Listening', value: 'ACTIVE_LISTENING'},
    {name: 'Adaptability', value: 'ADAPTABILITY'},
    {name: 'Attention to Detail', value: 'ATTENTION_TO_DETAIL'},
    {name: 'Collaboration', value: 'COLLABORATION'},
    {name: 'Communication', value: 'COMMUNICATION'},
    {name: 'Conflict Resolution', value: 'CONFLICT_RESOLUTION'},
    {name: 'Creativity', value: 'CREATIVITY'},
    {name: 'Critical Thinking', value: 'CRITICAL_THINKING'},
    {name: 'Decision Making', value: 'DECISION_MAKING'},
    {name: 'Emotional Intelligence', value: 'EMOTIONAL_INTELLIGENCE'},
    {name: 'Interpersonal Skills', value: 'INTERPERSONAL_SKILLS'},
    {name: 'Leadership', value: 'LEADERSHIP'},
    {name: 'Negotiation', value: 'NEGOTIATION'},
    {name: 'Patience', value: 'PATIENCE'},
    {name: 'Problem Solving', value: 'PROBLEM_SOLVING'},
    {name: 'Self-Motivation', value: 'SELF_MOTIVATION'},
    {name: 'Stress Management', value: 'STRESS_MANAGEMENT'},
    {name: 'Teamwork', value: 'TEAMWORK'},
    {name: 'Time Management', value: 'TIME_MANAGEMENT'},
    {name: 'Work Ethic', value: 'WORK_ETHIC'}
  ];
  selectedSoftSkills: string[] = [];

  constructor(private dataService: DataService, private router: Router, private fb: FormBuilder, private technicianService: TechnicianService,) {
    this.dataService.data$.subscribe(data => {
      this.CurrentUser = data;
      console.log(data)
      data.softskills.forEach((res) => {
        this.selectedSoftSkills.push(res);
      })
    });
  }

  toggleSkill(skillValue: string): void {
    const index = this.selectedSoftSkills.indexOf(skillValue);
    if (index === -1) {
      // Skill not selected, add it
      this.selectedSoftSkills.push(skillValue);
    } else {
      // Skill already selected, remove it
      this.selectedSoftSkills.splice(index, 1);
    }

    // Update the form control with the selected skills
    this.technicianForm.patchValue({softSkills: this.selectedSoftSkills});
  }

  ngOnInit(): void {
    this.technicianForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      informationRequest: this.fb.group({
        firstnameen: ['', Validators.required],
        lastnameen: ['', Validators.required],
      }),
      password: ['', [Validators.required]],
      phonenumber: ['', Validators.required],
      softSkills: [[], Validators.required],
    });
  }

  Error: any;
  Warning: any;
  Success: any;

  onSubmit(): void {
    const data = new FormData();
    data.append('newskills', this.selectedSoftSkills.join(', '));
    this.technicianService.Update_Skills(this.CurrentUser.id,data).then((response) => {
      console.log(response);
      this.Success = "Technician updated successfully!"
      setTimeout(() => {
        this.Success = null;
        this.router.navigate(['/Technician']);
      }, 2000);
    }).catch((error) => {
      if (error.status === 409) {
        if (error.error.includes('email')) {
          this.Warning = 'Email is already in use. Please try another one.';
        } else if (error.error.includes('phone-number')) {
          this.Warning = 'Phone number is already in use. Please try another one.';
        } else {
          this.Warning = 'Conflict error. Please try again.';
        }
      } else {
        this.Error = 'Failed to add technician. Please try again.';
      }
      setTimeout(() => {
        this.Error = null;
        this.Warning = null;
      }, 1000);
    })
  }


  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 100000, // Duration in milliseconds
      horizontalPosition: 'center', // Position of the snack bar
      verticalPosition: 'top', // Position of the snack bar
    });
  }

}
