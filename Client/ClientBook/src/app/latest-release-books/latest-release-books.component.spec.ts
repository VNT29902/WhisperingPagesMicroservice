import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LatestReleaseBooksComponent } from './latest-release-books.component';

describe('LatestReleaseBooksComponent', () => {
  let component: LatestReleaseBooksComponent;
  let fixture: ComponentFixture<LatestReleaseBooksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LatestReleaseBooksComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LatestReleaseBooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
