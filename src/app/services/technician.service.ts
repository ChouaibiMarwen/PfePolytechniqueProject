import { Injectable } from '@angular/core';
import {lastValueFrom} from "rxjs";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class TechnicianService {

  constructor(private http: HttpClient) { }

  New_Tech(data:any) {
    return lastValueFrom(this.http.post<any>(environment.serverUrl + '/api/v1/technician/add_technicien',data))
  }
}
