import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class StatAdminService {

  constructor(private http: HttpClient) {
  }

  Missions_Count() {
    return lastValueFrom(this.http.get<any>(environment.serverUrl + `/api/v1/stats/total_mission_count`))
  }

  Missions_Budget_Count() {
    return lastValueFrom(this.http.get<any>(environment.serverUrl + `/api/v1/stats/total_mission_budget`))
  }

  Request_Count() {
    return lastValueFrom(this.http.get<any>(environment.serverUrl + `/api/v1/stats/total_requestes_count`))
  }

  Tech_Count() {
    return lastValueFrom(this.http.get<any>(environment.serverUrl + `/api/v1/stats/total_technicians_count`))
  }

  Trans_Count() {
    return lastValueFrom(this.http.get<any>(environment.serverUrl + `/api/v1/stats/total_transactions_count`))
  }

  total_missions_by_status() {
    return lastValueFrom(this.http.get<any>(environment.serverUrl + `/api/v1/stats/total_missions_by_status`))
  }

  total_requests_by_status() {
    return lastValueFrom(this.http.get<any>(environment.serverUrl + `/api/v1/stats/total_requests_by_status`))
  }

  total_transactions_by_status() {
    return lastValueFrom(this.http.get<any>(environment.serverUrl + `/api/v1/stats/total_transactions_by_status`))
  }
}
