import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTechnicienComponent } from './add-technicien.component';

describe('AddTechnicienComponent', () => {
  let component: AddTechnicienComponent;
  let fixture: ComponentFixture<AddTechnicienComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddTechnicienComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddTechnicienComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
