import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FundBasketComponent } from './fund-basket.component';

describe('FundBasketComponent', () => {
  let component: FundBasketComponent;
  let fixture: ComponentFixture<FundBasketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FundBasketComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FundBasketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
