import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PopularbookComponent } from './popularbook.component';

describe('PopularbookComponent', () => {
  let component: PopularbookComponent;
  let fixture: ComponentFixture<PopularbookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PopularbookComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PopularbookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
