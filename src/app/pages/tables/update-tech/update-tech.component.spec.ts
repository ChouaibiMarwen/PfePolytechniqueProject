import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateTechComponent } from './update-tech.component';

describe('UpdateTechComponent', () => {
  let component: UpdateTechComponent;
  let fixture: ComponentFixture<UpdateTechComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateTechComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateTechComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
