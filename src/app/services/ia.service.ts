import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class IaService {

  constructor(private http: HttpClient) {
  }

  analyse_overdue_missions_and_advices() {
    return lastValueFrom(this.http.post<any>(environment.serverUrl + `/api/v1/iatool/analyse_overdue_missions_and_advices`, {}))
  }

  decide_Mission_Participants_and_Budget(data: any) {
    return lastValueFrom(this.http.post<any>(environment.serverUrl + `/api/v1/iatool/decide_Mission_Participants_and_Budget`, data))
  }

  extra_skills_needed_for_future_missions_with_advices_to_hire() {
    return lastValueFrom(this.http.post<any>(environment.serverUrl + `/api/v1/iatool/extra_skills_needed_for_future_missions_with_advices_to_hire`, {}))
  }
}
