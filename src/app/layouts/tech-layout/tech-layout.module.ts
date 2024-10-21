import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {TechLayoutRoutes, TechLayoutRoutingModule} from './tech-layout-routing.module';
import {RouterModule} from "@angular/router";
import {AdminLayoutRoutes} from "../admin-layout/admin-layout.routing";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {ClipboardModule} from "ngx-clipboard";
import {ComponentsModule} from "../../components/components.module";
import {CalendarComponent} from "../../pages/calendar/calendar.component";


@NgModule({
  declarations: [
    CalendarComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(TechLayoutRoutes),
    FormsModule,
    HttpClientModule,
    NgbModule,
    ClipboardModule,
    ComponentsModule,
    ReactiveFormsModule
  ],
})
export class TechLayoutModule {
}
