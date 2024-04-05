import { TestBed } from '@angular/core/testing';

import { UfundControllerService } from './ufund-controller.service';

describe('UfundControllerService', () => {
  let service: UfundControllerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UfundControllerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
