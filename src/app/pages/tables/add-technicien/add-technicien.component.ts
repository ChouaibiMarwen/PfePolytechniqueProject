import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {TechnicianService} from "../../../services/technician.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-technicien',
  templateUrl: './add-technicien.component.html',
  styleUrls: ['./add-technicien.component.scss']
})
export class AddTechnicienComponent implements OnInit {
  technicianForm: FormGroup;
  private _snackBar = inject(MatSnackBar);

  constructor(private router: Router,private fb: FormBuilder, private technicianService: TechnicianService,) {
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
    });
  }

  Error: any;
  Warning: any;
  Success: any;

  onSubmit(): void {
    if (this.technicianForm.valid) {
      const data = {
        email: this.technicianForm.value.email,
        informationRequest: {
          firstnameen: this.technicianForm.value.informationRequest.firstnameen,
          lastnameen: this.technicianForm.value.informationRequest.lastnameen,
        },
        password: this.technicianForm.value.password,
        phonenumber: this.technicianForm.value.phonenumber,
      };
      this.technicianService.New_Tech(data).then((response) => {
        console.log(response);
        this.Success = "Technician added successfully!"
        setTimeout(() => {
          this.Success = null;
          this.router.navigate(['/Technician']);
        }, 2000);
      }).catch((error)=>{
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
    } else {
      this.Error = "Please fill in all required fields correctly.";
      setTimeout(() => {
        this.Error = null;
      }, 1000);
    }
  }


  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 100000, // Duration in milliseconds
      horizontalPosition: 'center', // Position of the snack bar
      verticalPosition: 'top', // Position of the snack bar
    });
  }

}
