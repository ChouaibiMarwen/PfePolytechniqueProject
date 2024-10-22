import {Component, inject, OnInit} from '@angular/core';
import {Missions} from "../../../interfaces/missions";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DataService} from "../../../services/data.service";
import {UserService} from "../../../services/user.service";
import {Router} from "@angular/router";
import {BugetService} from "../../../services/buget.service";
import {TransactionsService} from "../../../services/transactions.service";

@Component({
  selector: 'app-add-transaction',
  templateUrl: './add-transaction.component.html',
  styleUrls: ['./add-transaction.component.css']
})
export class AddTransactionComponent implements OnInit {

  CurrentMission:Missions
  missionForm: FormGroup;
  private _snackBar = inject(MatSnackBar);

  constructor(private dataService: DataService,private user: UserService, private router: Router, private fb: FormBuilder, private missionService: TransactionsService) {
    this.dataService.data$.subscribe(data => {
      this.CurrentMission = data;
    });
  }

  ngOnInit(): void {
    this.missionForm = this.fb.group({
      amount: ['', Validators.required],
      description: ['', Validators.required],
      name: ['', Validators.required],
    });
  }

  Error: any;
  Warning: any;
  Success: any;


  onSubmit(): void {
    const formData = new FormData();
    console.log(this.missionForm.value.amount)
    formData.append('amount', this.missionForm.value.amount);
    formData.append('missionId', this.CurrentMission.id.toString());
    formData.append('description', this.missionForm.value.description);
    formData.append('name', this.missionForm.value.name);
    this.missionService.add(formData).then((response) => {
      this.Success = "Transaction Requested successfully!";
      setTimeout(() => {
        this.Success = null;
        this.router.navigate(['/My_Transactions']);
      }, 2000);
    }).catch((error) => {
      if(error.status === 406) {
        this.Warning = error.error;
      }else {
        this.Error = 'Failed to requested Transaction. Please try again.';
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
