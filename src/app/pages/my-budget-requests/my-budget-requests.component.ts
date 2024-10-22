import { Component, OnInit } from '@angular/core';
import {PaginatedBudgets} from "../../interfaces/missions";
import {DataService} from "../../services/data.service";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {BugetService} from "../../services/buget.service";

@Component({
  selector: 'app-my-budget-requests',
  templateUrl: './my-budget-requests.component.html',
  styleUrls: ['./my-budget-requests.component.css']
})
export class MyBudgetRequestsComponent implements OnInit {

  paginatedMissions: PaginatedBudgets = {size: 5, nb_elements: 0, page: -1, elements: [], pages: 1}
  constructor(private dataService: DataService,private router: Router,private User:UserService,private budget:BugetService) { }
  ngOnInit(): void {
    this.getAllBudgets(0)
  }

  getAllBudgets(page: any) {
    this.budget.GetMyRequests(page,this.paginatedMissions.size).then((res) => {
      this.paginatedMissions.nb_elements = res.totalItems
      this.paginatedMissions.page = res.currentPage
      this.paginatedMissions.pages = res.totalPages
      this.paginatedMissions.elements = res.result
    }).finally(() => {
    })
  }

  approve(id:any){
    this.budget.Approve(id).then((res)=>{
      this.getAllBudgets(this.paginatedMissions.page)
    })
  }

  budgetId: number;
  denyReason: string = '';

  setBudgetId(id: number) {
    this.budgetId = id;
    this.denyReason = ''; // Reset the reason when opening the modal
  }

  deny(id: number, reason: string) {
    const formData = new FormData();
    formData.append('reason', reason);
    this.budget.Reject(id,formData).then((res)=>{
      this.getAllBudgets(this.paginatedMissions.page)
    })
  }

  cancel(id: number) {
    this.budget.cancel(id).then((res)=>{
      this.getAllBudgets(this.paginatedMissions.page)
    })
  }


  sendData(data:any,navigate:any) {
    this.dataService.setData(data);
    this.router.navigate([navigate])
  }

}
