import { TestBed } from '@angular/core/testing';

import { MemoryCommunicationService } from './memory-communication.service';

describe('MemoryCommunicationService', () => {
  let service: MemoryCommunicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MemoryCommunicationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
