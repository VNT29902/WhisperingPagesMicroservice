import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashBoardSettingsComponent } from './settings.component';

describe('DashBoardSettingsComponent', () => {
  let component: DashBoardSettingsComponent;
  let fixture: ComponentFixture<DashBoardSettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashBoardSettingsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashBoardSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
