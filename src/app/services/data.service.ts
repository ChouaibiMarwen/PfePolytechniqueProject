import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private dataSubject = new BehaviorSubject<any>(this.getDataFromLocalStorage());
  data$ = this.dataSubject.asObservable();

  private getDataFromLocalStorage(): any {
    const data = localStorage.getItem('appData');
    return data ? JSON.parse(data) : null;
  }

  setData(data: any) {
    localStorage.setItem('appData', JSON.stringify(data));
    this.dataSubject.next(data);
  }

  clearData() {
    localStorage.removeItem('appData');
    this.dataSubject.next(null);
  }
}
