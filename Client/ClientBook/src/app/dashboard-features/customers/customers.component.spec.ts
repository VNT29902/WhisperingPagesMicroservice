import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashBoardCustomersComponent } from './customers.component';

describe('DashBoardCustomersComponent', () => {
  let component: DashBoardCustomersComponent;
  let fixture: ComponentFixture<DashBoardCustomersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashBoardCustomersComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashBoardCustomersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
