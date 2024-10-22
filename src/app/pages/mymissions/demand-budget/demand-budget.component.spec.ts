import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandBudgetComponent } from './demand-budget.component';

describe('DemandBudgetComponent', () => {
  let component: DemandBudgetComponent;
  let fixture: ComponentFixture<DemandBudgetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DemandBudgetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemandBudgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
