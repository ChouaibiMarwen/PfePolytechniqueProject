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

export const AdminLayoutRoutes: Routes = [
  {path: 'dashboard', component: DashboardComponent},
  {path: 'user-profile', component: UserProfileComponent},
  {path: 'Technician', component: TablesComponent},
  {path: 'Technician/add', component: AddTechnicienComponent},
  {path: 'missions', component: MissionsComponent},
  {path: 'missions/add', component: NewMissionComponent},
  {path: 'missions/update', component: UpdateMissionComponent},
  {path: 'missions/update/participant', component: UpdateParticipantComponent},
  {path: 'transaction', component: TransactionsComponent},
  {path: 'Budget Requests', component: BudgetsComponent},
  {path: 'icons', component: IconsComponent},
  {path: 'maps', component: MapsComponent}
];
