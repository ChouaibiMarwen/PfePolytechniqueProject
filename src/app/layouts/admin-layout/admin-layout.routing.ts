import {Routes} from '@angular/router';

import {DashboardComponent} from '../../pages/dashboard/dashboard.component';
import {IconsComponent} from '../../pages/icons/icons.component';
import {MapsComponent} from '../../pages/maps/maps.component';
import {UserProfileComponent} from '../../pages/user-profile/user-profile.component';
import {TablesComponent} from '../../pages/tables/tables.component';
import {MissionsComponent} from "../../pages/missions/missions.component";
import {AddTechnicienComponent} from "../../pages/tables/add-technicien/add-technicien.component";
import {NewMissionComponent} from "../../pages/missions/new-mission/new-mission.component";
import {UpdateParticipantComponent} from "../../pages/missions/update-participant/update-participant.component";
import {UpdateMissionComponent} from "../../pages/missions/update-mission/update-mission.component";
import {BudgetsComponent} from "../../pages/budgets/budgets.component";
import {TransactionsComponent} from "../../pages/transactions/transactions.component";
import {UpdateTechComponent} from "../../pages/tables/update-tech/update-tech.component";
import {TasksComponent} from "../../pages/tasks/tasks.component";
import {MyTasksComponent} from "../../pages/tasks/my-tasks/my-tasks.component";
import {AIComponent} from "../../pages/ai/ai.component";

export const AdminLayoutRoutes: Routes = [
  {path: 'dashboard', component: DashboardComponent},
  {path: 'user-profile', component: UserProfileComponent},
  {path: 'Technician', component: TablesComponent},
  {path: 'Technician/Skills/update', component: UpdateTechComponent},
  {path: 'Technician/add', component: AddTechnicienComponent},
  {path: 'missions', component: MissionsComponent},
  {path: 'missions/add', component: NewMissionComponent},
  {path: 'missions/update', component: UpdateMissionComponent},
  {path: 'missions/update/participant', component: UpdateParticipantComponent},
  {path: 'transaction', component: TransactionsComponent},
  {path: 'Budget_Requests', component: BudgetsComponent},
  {path: 'Tasks', component: TasksComponent},
  {path: 'AI', component: AIComponent},

  {path: 'icons', component: IconsComponent},
  {path: 'maps', component: MapsComponent}
];
