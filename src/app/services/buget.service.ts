import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class BugetService {

  constructor(private http: HttpClient) { }

  Approve(request_id:any) {
    return lastValueFrom(this.http.patch<any>(environment.serverUrl + `/api/v1/request/approve_request/${request_id}`,{}))
  }
  cancel(request_id:any) {
    return lastValueFrom(this.http.patch<any>(environment.serverUrl + `/api/v1/request/cancel_request_while_pending/${request_id}`,{}))
  }

  Add(mission_id:any,data:any) {
    return lastValueFrom(this.http.post<any>(environment.serverUrl + `/api/v1/request/add_request/${mission_id}`,data))
  }
  Reject(request_id:any,data:any) {
    return lastValueFrom(this.http.patch<any>(environment.serverUrl + `/api/v1/request/reject_request/${request_id}`,data))
  }
  GetRequests(page: any, size: any) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return lastValueFrom(this.http.get<any>(environment.serverUrl + '/api/v1/request/get_all_requests_pg', { params }));
  }
  GetMyRequests(page: any, size: any) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return lastValueFrom(this.http.get<any>(environment.serverUrl + '/api/v1/request/get_all_my_requests_pg', { params }));
  }
}
