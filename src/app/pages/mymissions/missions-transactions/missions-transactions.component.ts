import { Component, OnInit } from '@angular/core';
import {Missions, PaginatedTransaction, Participant} from "../../../interfaces/missions";
import {DataService} from "../../../services/data.service";
import {Router} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {TransactionsService} from "../../../services/transactions.service";

@Component({
  selector: 'app-missions-transactions',
  templateUrl: './missions-transactions.component.html',
  styleUrls: ['./missions-transactions.component.css']
})
export class MissionsTransactionsComponent implements OnInit {
  CurrentMission:Missions
  CurrentUser:any;
  denyReason:any;
  users:Participant[] = [];
  paginatedMissions: PaginatedTransaction = {size: 5, nb_elements: 0, page: -1, elements: [], pages: 1}
  constructor(private user: UserService,private dataService: DataService,private router: Router,private User:UserService,private tran:TransactionsService) {
    this.dataService.data$.subscribe(data => {
      this.CurrentMission = data;
    });
  }
  ngOnInit(): void {
    this.getAllTransactions(0)
    this.GetAllTechnicien()
    this.user.getProfile().then((res)=>{
      this.CurrentUser = res;
    })
  }

  getAllTransactions(page: any) {
    this.tran.GetMyTransactions(page,this.paginatedMissions.size).then((res) => {
      this.paginatedMissions.nb_elements = res.totalItems
      this.paginatedMissions.page = res.currentPage
      this.paginatedMissions.pages = res.totalPages
      this.paginatedMissions.elements = res.result
    }).finally(() => {
    })
  }

  // Reject(id:any){
  //   this.tran.Deny(id).then((res)=>{
  //     this.router.navigate(['/My_Missions']);
  //   });
  // }

  Approve(id:any){
    this.tran.Approve(id).then((res)=>{
      this.router.navigate(['/My_Missions']);
    });
  }

  GetAllTechnicien(): void {
    const roles = ['ROLE_TECHNICIEN'];
    this.User.getUsersByRoles(roles).then(
      (data) => {
        this.users = data;
      },
      (error) => {
        console.error('Error fetching users', error);
      }
    );
  }

  FindUserName(id:any){
    return this.users.find(res=> res.id === id)
  }


  budgetId: number;
  selectedStatus: string = '';

  setBudgetId(id: number) {
    this.budgetId = id;
    this.selectedStatus = ''; // Reset the reason when opening the modal
  }

  // deny(id: number) {
  //   this.tran.cancel(id).then((res)=>{
  //     this.getAllTransactions(this.paginatedMissions.page)
  //   })
  // }

  deny(id: number, reason: string) {
    const formData = new FormData();
    formData.append('reason', reason);
    this.tran.Deny(id,formData).then((res)=>{

    })
  }


  sendData(data:any,navigate:any) {
    this.dataService.setData(data);
    this.router.navigate([navigate])
  }
}
