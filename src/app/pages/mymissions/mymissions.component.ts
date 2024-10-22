import { Component, OnInit } from '@angular/core';
import {PaginatedMissions} from "../../interfaces/missions";
import {DataService} from "../../services/data.service";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {MissionService} from "../../services/mission.service";

@Component({
  selector: 'app-mymissions',
  templateUrl: './mymissions.component.html',
  styleUrls: ['./mymissions.component.scss']
})
export class MymissionsComponent implements OnInit {
  paginatedMissions: PaginatedMissions = {size: 5, nb_elements: 0, page: -1, elements: [], pages: 1}
  idTeamLead: any;
  startdate: any;
  enddate: any;
  status: any = null;
  title: any;
  statuses = ['CANCELLED', 'COMPLETED', 'IN_PROGRESS', 'OVERDUE', 'PENDING'];
  constructor(private dataService: DataService,private router: Router,private User:UserService,private mission:MissionService) { }
  ngOnInit(): void {
    this.getAllMissions(0)
  }



  getTitle(status: string): string {
    switch (status) {
      case 'CANCELLED':
        return 'Make The Mission Canceled';
      case 'COMPLETED':
        return 'Make The Mission Complete';
      case 'IN_PROGRESS':
        return 'Make The Mission In Process';
      case 'OVERDUE':
        return 'Make The Mission Overdue';
      case 'PENDING':
        return 'Make The Mission Pending';
      default:
        return '';
    }
  }

  NewMission(){
    this.router.navigate(['/missions/add'])
  }


  statusOptions: { value: string, label: string }[] = [
    { value: 'CANCELLED', label: 'Cancelled' },
    { value: 'COMPLETED', label: 'Completed' },
    { value: 'IN_PROGRESS', label: 'In Progress' },
    { value: 'OVERDUE', label: 'Overdue' },
    { value: 'PENDING', label: 'Pending' }
  ];

  getAllMissions(page: any) {
    this.mission.getMyMissions(page,this.paginatedMissions.size,this.idTeamLead,this.startdate,this.enddate,this.status,this.title).then((res) => {
      this.paginatedMissions.nb_elements = res.totalItems
      this.paginatedMissions.page = res.currentPage
      this.paginatedMissions.pages = res.totalPages
      this.paginatedMissions.elements = res.result
    }).finally(() => {
    })
  }




  sendData(data:any,navigate:any) {
    this.dataService.setData(data);
    this.router.navigate([navigate])
  }




}
