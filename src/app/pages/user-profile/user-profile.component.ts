import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {Participant} from "../../interfaces/missions";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  LoggedCurrentUser: Participant;
  Readonly:boolean = true
  constructor(private CurrentUser: UserService, private fb: FormBuilder) {
  }

  update: FormGroup = this.fb.group({
    firstname: ['', Validators.required],
    lastname: ['', Validators.required],
    phonenumber: [null, [Validators.required]],
  });

  ngOnInit() {
    this.Load_Data()
  }

  Load_Data(){
    this.CurrentUser.getProfile().then((res) => {
      this.LoggedCurrentUser = res;
      console.log(res)
      console.log(res?.personalinformation?.firstnameen)
      this.update.patchValue({
        firstname:res?.personalinformation?.firstnameen,
        lastname:res?.personalinformation?.lastnameen,
        phonenumber:res?.phonenumber
      })
    })
  }

  Update(){
    const formData = new FormData();
    formData.append('firstnameen', this.update.value.firstname);
    formData.append('lastnameen', this.update.value.lastname);
    formData.append('phonenumber', this.update.value.phonenumber);

    this.CurrentUser.updateProfile(formData).then(res=>{
      this.Load_Data()
    })
  }

}
