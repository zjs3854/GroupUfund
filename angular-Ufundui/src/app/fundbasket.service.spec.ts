import { TestBed } from '@angular/core/testing';

import { FundBasketService } from './fundbasket.service';

describe('FundbasketService', () => {
  let service: FundBasketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FundBasketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
