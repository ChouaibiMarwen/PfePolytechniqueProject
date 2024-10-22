import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MissionsTransactionsComponent } from './missions-transactions.component';

describe('MissionsTransactionsComponent', () => {
  let component: MissionsTransactionsComponent;
  let fixture: ComponentFixture<MissionsTransactionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MissionsTransactionsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MissionsTransactionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
