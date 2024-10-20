import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MessagingService {
  currentToken = new BehaviorSubject<string | null>(null);
  token = this.currentToken.asObservable();
  constructor() { }


}
