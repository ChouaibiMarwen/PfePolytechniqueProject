import { Component, OnInit } from '@angular/core';
import Chart from 'chart.js';

// core components
import {
  chartOptions,
  parseOptions,
  chartExample1,
  chartExample2, chartExample3
} from "../../variables/charts";
import {StatAdminService} from "../../services/stat-admin.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  missionCount: number = 0;
  budgetCount: number = 0;
  requestCount: number = 0;
  technicianCount: number = 0;
  Trans_Count: number = 0;

  MissionByStatus:any
  RequestByStatus:any
  transactionsByStatus:any

  public datasets: any;
  public data: any;
  public salesChart;
  public ordersChart;
  public Transaction;

  constructor(private statAdminService: StatAdminService) {}



  ngOnInit() {
    this.loadStatistics();
    this.datasets = [
      [0, 20, 10, 30, 15, 40, 20, 60, 60],
      [0, 20, 5, 25, 10, 30, 15, 40, 40]
    ];
    this.data = this.datasets[0];


    var chartOrders = document.getElementById('chart-orders');

    parseOptions(Chart, chartOptions());


    this.ordersChart = new Chart(chartOrders, {
      type: 'bar',
      options: chartExample2.options,
      data: chartExample2.data
    });

    var chartSales = document.getElementById('chart-sales');

    this.salesChart = new Chart(chartSales, {
			type: 'bar',
			options: chartExample1.options,
			data: chartExample1.data
		});


    var chartTran = document.getElementById('chart-trans');

    this.Transaction = new Chart(chartTran, {
			type: 'bar',
			options: chartExample3.options,
			data: chartExample3.data
		});
  }

  private async loadStatistics() {
    try {
      this.missionCount = await this.statAdminService.Missions_Count();
      this.budgetCount = await this.statAdminService.Missions_Budget_Count();
      this.requestCount = await this.statAdminService.Request_Count();
      this.technicianCount = await this.statAdminService.Tech_Count();
      this.Trans_Count = await this.statAdminService.Trans_Count();


      this.MissionByStatus = await this.statAdminService.total_missions_by_status();
      this.RequestByStatus = await this.statAdminService.total_requests_by_status();
      this.transactionsByStatus = await this.statAdminService.total_transactions_by_status();
      const statusList = this.MissionByStatus.map(mission => mission.status);
      const totalList = this.MissionByStatus.map(mission => mission.total);
      chartExample1.data.labels = statusList;
      chartExample1.data.datasets[0].data = totalList;
      const statusList2 = this.RequestByStatus.map(mission => mission.status);
      const totalList2 = this.RequestByStatus.map(mission => mission.amount);
      chartExample2.data.labels = statusList2;
      chartExample2.data.datasets[0].data = totalList2;
      const statusList3 = this.RequestByStatus.map(mission => mission.status);
      const totalList3 = this.RequestByStatus.map(mission => mission.amount);
      chartExample3.data.labels = statusList3;
      chartExample3.data.datasets[0].data = totalList3;
      this.salesChart.update();
      this.ordersChart.update();
      this.Transaction.update();
    } catch (error) {
      console.error('Error loading statistics:', error);
    }
  }
}
