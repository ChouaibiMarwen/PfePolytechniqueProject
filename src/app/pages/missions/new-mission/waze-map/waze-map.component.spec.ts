import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WazeMapComponent } from './waze-map.component';

describe('WazeMapComponent', () => {
  let component: WazeMapComponent;
  let fixture: ComponentFixture<WazeMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WazeMapComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WazeMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
