import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LiveMeetComponent } from './live-meet.component';

describe('LiveMeetComponent', () => {
  let component: LiveMeetComponent;
  let fixture: ComponentFixture<LiveMeetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LiveMeetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LiveMeetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
