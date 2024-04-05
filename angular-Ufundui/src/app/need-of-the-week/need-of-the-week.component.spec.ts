import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedOfTheWeekComponent } from './need-of-the-week.component';

describe('NeedOfTheWeekComponent', () => {
  let component: NeedOfTheWeekComponent;
  let fixture: ComponentFixture<NeedOfTheWeekComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NeedOfTheWeekComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NeedOfTheWeekComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
