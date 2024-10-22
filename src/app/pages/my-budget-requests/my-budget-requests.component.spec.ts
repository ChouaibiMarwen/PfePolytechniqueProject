import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyBudgetRequestsComponent } from './my-budget-requests.component';

describe('MyBudgetRequestsComponent', () => {
  let component: MyBudgetRequestsComponent;
  let fixture: ComponentFixture<MyBudgetRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyBudgetRequestsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyBudgetRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
