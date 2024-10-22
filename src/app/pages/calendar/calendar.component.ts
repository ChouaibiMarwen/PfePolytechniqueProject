import { Component, OnInit } from '@angular/core';
import { CalendarEvent, CalendarView, DAYS_OF_WEEK } from 'angular-calendar';
import moment from 'moment';
import { MissionService } from "../../services/mission.service";

// Configure moment.js for week start
moment.updateLocale('en', {
  week: {
    dow: DAYS_OF_WEEK.MONDAY,
    doy: 0,
  },
});

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnInit {
  view: CalendarView = CalendarView.Month;
  viewDate: Date = new Date();
  events: CalendarEvent[] = [];
  missions: any[];

  constructor(private Service: MissionService) {
    this.GetAllMyMiSSION();
  }

  ngOnInit(): void {}

  // Navigate to the previous month
  prevMonth(): void {
    this.viewDate = moment(this.viewDate).subtract(1, 'months').toDate();
  }

  // Navigate to the next month
  nextMonth(): void {
    this.viewDate = moment(this.viewDate).add(1, 'months').toDate();
  }

  generateRandomColor(): { primary: string; secondary: string } {
    const randomColor = () => {
      const randomValue = Math.floor(Math.random() * 256);
      return randomValue.toString(16).padStart(2, '0'); // Convert to hex
    };

    const primary = `#${randomColor()}${randomColor()}${randomColor()}`;
    const secondary = `${primary}30`; // Slightly transparent version for secondary color

    return { primary, secondary };
  }

  GetAllMyMiSSION() {
    this.Service.MyMissions().then((res) => {
      this.missions = res;
      this.events = this.missions.map(mission => ({
        start: new Date(mission.startdate),
        end: new Date(mission.enddate),
        title: mission.title,
        color: this.generateRandomColor(), // Generate a random color for each mission
      }));
    }).catch((error) => {
      console.error("Error fetching missions", error);
    });
  }
}
