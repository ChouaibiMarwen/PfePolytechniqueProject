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
import {
  CalendarDateFormatter,
  CalendarModule,
  CalendarMomentDateFormatter,
  DateAdapter,
  MOMENT
} from "angular-calendar";
import {adapterFactory} from "angular-calendar/date-adapters/moment";
import * as moment from 'moment';
export function momentAdapterFactory() {
  return adapterFactory(moment);
}
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
    ReactiveFormsModule,
    CalendarModule.forRoot(
      {
        provide: DateAdapter,
        useFactory: momentAdapterFactory,
      },
      {
        dateFormatter: {
          provide: CalendarDateFormatter,
          useClass: CalendarMomentDateFormatter,
        },
      }
    ),
  ],
  providers: [
    {
      provide: MOMENT,
      useValue: moment,
    },
  ],
})
export class TechLayoutModule {
}
