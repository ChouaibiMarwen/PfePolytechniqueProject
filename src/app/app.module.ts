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
    TransactionsComponent
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: HttpInterceptor, multi: true},],
  bootstrap: [AppComponent],
})
export class AppModule {
}
