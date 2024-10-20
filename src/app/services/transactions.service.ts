import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class TransactionsService {

  constructor(private http: HttpClient) { }

  // Approve(request_id:any) {
  //   return lastValueFrom(this.http.patch<any>(environment.serverUrl + `/api/v1/request/approve_request/${request_id}`,{}))
  // }
  UpdateStat(transaction_id:any,data:any) {
    return lastValueFrom(this.http.patch<any>(environment.serverUrl + `/api/v1/transactions/update_transaction_status/${transaction_id}`,data))
  }
  GetTransactions(page: any, size: any, missionId?: any, startdate?: any, enddate?: any, missionTitle?: any, technicianId?: any) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (missionId) {
      params = params.set('missionId', missionId);
    }
    if (startdate) {
      params = params.set('startdate', this.formatDate(startdate));
    }
    if (enddate) {
      params = params.set('endDate', this.formatDate(enddate));
    }
    if (missionTitle) {
      params = params.set('missionTitle', missionTitle);
    }
    if (technicianId) {
      params = params.set('technicianId', technicianId);
    }
    return lastValueFrom(this.http.get<any>(environment.serverUrl + '/api/v1/transactions/all_transactions_pg', { params }));
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
}
