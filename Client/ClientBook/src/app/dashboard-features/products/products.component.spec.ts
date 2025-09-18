import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashBoardProductsComponent } from './products.component';

describe('ProductsComponent', () => {
  let component: DashBoardProductsComponent;
  let fixture: ComponentFixture<DashBoardProductsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashBoardProductsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashBoardProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
