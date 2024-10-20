import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  /**********USER**********/
  private userSource = new BehaviorSubject<any | null>(null);
  currentUser = this.userSource.asObservable();

  changeUser(user: any | null) {
    this.userSource.next(user)
  }

  getUser() {
    return this.userSource.getValue()
  }
}
