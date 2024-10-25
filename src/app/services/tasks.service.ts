import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class TasksService {

  constructor(private http: HttpClient) {
  }

  Add(mission_id:any,data:any) {
    return lastValueFrom(this.http.post<any>(environment.serverUrl + `/api/v1/tasks/add_task/${mission_id}`,data))
  }

  UpdateSTatus(task_id:any,data:any) {
    return lastValueFrom(this.http.patch<any>(environment.serverUrl + `/api/v1/tasks/update_task_status/${task_id}`,data))
  }

  Progress(task_id:any,data:any) {
    return lastValueFrom(this.http.patch<any>(environment.serverUrl + `/api/v1/tasks/update_task_realisation_percentage/${task_id}`,data))
  }

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

  Tasks(page: any, size: any, idTeamLead?: any, startdate?: any, enddate?: any, status?: any, title?: any) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (idTeamLead) {
      params = params.set('idTeamLead', idTeamLead.toString());
    }
    if (startdate) {
      params = params.set('startdate', this.formatDate(startdate));
    }
    if (enddate) {
      params = params.set('enddate', this.formatDate(enddate));
    }
    if (status) {
      params = params.set('status', status);
    }
    if (title) {
      params = params.set('title', title);
    }

    return lastValueFrom(this.http.get<any>(environment.serverUrl + '/api/v1/tasks/all_tasks_pg', { params }));
  }


  MyTasks(page: any, size: any, idTeamLead?: any, startdate?: any, enddate?: any, status?: any, title?: any) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (idTeamLead) {
      params = params.set('idTeamLead', idTeamLead.toString());
    }
    if (startdate) {
      params = params.set('startdate', this.formatDate(startdate));
    }
    if (enddate) {
      params = params.set('enddate', this.formatDate(enddate));
    }
    if (status) {
      params = params.set('status', status);
    }
    if (title) {
      params = params.set('title', title);
    }

    return lastValueFrom(this.http.get<any>(environment.serverUrl + '/api/v1/tasks/my_tasks_pg', { params }));
  }
}
