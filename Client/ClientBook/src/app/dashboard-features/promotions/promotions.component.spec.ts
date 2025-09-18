import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashBoardPromotionsComponent } from './promotions.component';

describe('DashBoardPromotionsComponent', () => {
  let component: DashBoardPromotionsComponent;
  let fixture: ComponentFixture<DashBoardPromotionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashBoardPromotionsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashBoardPromotionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
