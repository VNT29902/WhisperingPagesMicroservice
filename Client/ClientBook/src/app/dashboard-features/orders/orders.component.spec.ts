import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashBoardOrdersComponent } from './orders.component';

describe('OrdersComponent', () => {
  let component: DashBoardOrdersComponent;
  let fixture: ComponentFixture<DashBoardOrdersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashBoardOrdersComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashBoardOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
