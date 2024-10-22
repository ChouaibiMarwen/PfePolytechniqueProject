import { Component, OnInit } from '@angular/core';
import {PaginatedTransaction, Participant} from "../../interfaces/missions";
import {DataService} from "../../services/data.service";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {TransactionsService} from "../../services/transactions.service";

@Component({
  selector: 'app-my-transactions',
  templateUrl: './my-transactions.component.html',
  styleUrls: ['./my-transactions.component.css']
})
export class MyTransactionsComponent implements OnInit {
  users:Participant[] = [];
  paginatedMissions: PaginatedTransaction = {size: 5, nb_elements: 0, page: -1, elements: [], pages: 1}
  constructor(private dataService: DataService,private router: Router,private User:UserService,private tran:TransactionsService) { }
  ngOnInit(): void {
    this.getAllTransactions(0)
    this.GetAllTechnicien()
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

  deny(id: number) {
    this.tran.cancel(id).then((res)=>{
      this.getAllTransactions(this.paginatedMissions.page)
    })
  }


  sendData(data:any,navigate:any) {
    this.dataService.setData(data);
    this.router.navigate([navigate])
  }

}
