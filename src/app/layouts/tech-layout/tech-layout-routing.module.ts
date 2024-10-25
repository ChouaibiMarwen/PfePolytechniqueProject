
import {Routes} from '@angular/router';
import {CalendarComponent} from "../../pages/calendar/calendar.component";
import {MymissionsComponent} from "../../pages/mymissions/mymissions.component";
import {DemandBudgetComponent} from "../../pages/mymissions/demand-budget/demand-budget.component";
import {MyBudgetRequestsComponent} from "../../pages/my-budget-requests/my-budget-requests.component";
import {MyTransactionsComponent} from "../../pages/my-transactions/my-transactions.component";
import {AddTransactionComponent} from "../../pages/mymissions/add-transaction/add-transaction.component";
import {
  MissionsTransactionsComponent
} from "../../pages/mymissions/missions-transactions/missions-transactions.component";
import {LiveMeetComponent} from "../../pages/live-meet/live-meet.component";
import {RoomComponent} from "../../pages/live-meet/room/room.component";
import {MyTasksComponent} from "../../pages/tasks/my-tasks/my-tasks.component";
import {NewTaskComponent} from "../../pages/tasks/new-task/new-task.component";

export const TechLayoutRoutes: Routes = [
  {path: 'Calendar', component: CalendarComponent},
  {path: 'My_Missions', component: MymissionsComponent},
  {path: 'My_Transactions', component: MyTransactionsComponent},
  {path: 'My_Missions/request_budget', component: DemandBudgetComponent},
  {path: 'My_Missions/add_transaction', component: AddTransactionComponent},
  {path: 'My_Missions/Transactions', component: MissionsTransactionsComponent},
  {path: 'My_Budget_Requests', component: MyBudgetRequestsComponent},
  {path: 'LiveMeet', component: LiveMeetComponent},
  {path: 'LiveMeet/Room/:roomID', component: RoomComponent},
  {path: 'MyTasks', component: MyTasksComponent},
  {path: 'NewTask', component: NewTaskComponent},
]

export class TechLayoutRoutingModule {
}
