import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MissionService {

  constructor(private http: HttpClient) { }

  New_Mission(data:any) {
    return lastValueFrom(this.http.post<any>(environment.serverUrl + `/api/v1/mission/add_mission`,data))
  }

  UpdateMissionStatus(mission_id:any,data:any) {
    return lastValueFrom(this.http.patch<any>(environment.serverUrl + `/api/v1/mission/update_mission_status/${mission_id}`,data))
  }

  updateMissionParticipant(mission_id:any,data:any) {
    return lastValueFrom(this.http.patch<any>(environment.serverUrl + `/api/v1/mission/update_mission_participants/${mission_id}`,data))
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

  getMissions(page: any, size: any, idTeamLead?: any, startdate?: any, enddate?: any, status?: any, title?: any) {
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

    return lastValueFrom(this.http.get<any>(environment.serverUrl + '/api/v1/mission/all_missions_pg', { params }));
  }
}
