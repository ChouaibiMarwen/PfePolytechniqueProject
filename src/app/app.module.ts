import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';

import {AppComponent} from './app.component';
import {AdminLayoutComponent} from './layouts/admin-layout/admin-layout.component';
import {AuthLayoutComponent} from './layouts/auth-layout/auth-layout.component';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {AppRoutingModule} from './app.routing';
import {ComponentsModule} from './components/components.module';
import {MissionsComponent} from './pages/missions/missions.component';
import {HttpInterceptor} from "./interceptors/http.interceptor";
import { AddTechnicienComponent } from './pages/tables/add-technicien/add-technicien.component';
import {BrowserModule} from "@angular/platform-browser";
import {MatDialogModule} from "@angular/material/dialog";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { NewMissionComponent } from './pages/missions/new-mission/new-mission.component';
import { WazeMapComponent } from './pages/missions/new-mission/waze-map/waze-map.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import { UpdateParticipantComponent } from './pages/missions/update-participant/update-participant.component';
import { UpdateMissionComponent } from './pages/missions/update-mission/update-mission.component';
import { BudgetsComponent } from './pages/budgets/budgets.component';
import { TransactionsComponent } from './pages/transactions/transactions.component';

import {TechLayoutComponent} from "./layouts/tech-layout/tech-layout.component";
import {CalendarModule} from "angular-calendar";
import { MymissionsComponent } from './pages/mymissions/mymissions.component';
import { DemandBudgetComponent } from './pages/mymissions/demand-budget/demand-budget.component';
import { MyBudgetRequestsComponent } from './pages/my-budget-requests/my-budget-requests.component';
import { MyTransactionsComponent } from './pages/my-transactions/my-transactions.component';
import { AddTransactionComponent } from './pages/mymissions/add-transaction/add-transaction.component';
import { MissionsTransactionsComponent } from './pages/mymissions/missions-transactions/missions-transactions.component';




@NgModule({
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ComponentsModule,
    NgbModule,
    RouterModule,
    AppRoutingModule,
    BrowserModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    MatTooltipModule
  ],
  declarations: [
    TechLayoutComponent,
    AppComponent,
    AdminLayoutComponent,
    AuthLayoutComponent,
    MissionsComponent,
    AddTechnicienComponent,
    NewMissionComponent,
    WazeMapComponent,
    UpdateParticipantComponent,
    UpdateMissionComponent,
    BudgetsComponent,
    TransactionsComponent,
    MymissionsComponent,
    DemandBudgetComponent,
    MyBudgetRequestsComponent,
    MyTransactionsComponent,
    AddTransactionComponent,
    MissionsTransactionsComponent
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: HttpInterceptor, multi: true},],
  bootstrap: [AppComponent],
})
export class AppModule {
}
