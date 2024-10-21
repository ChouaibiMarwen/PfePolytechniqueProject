import { TestBed } from '@angular/core/testing';

import { StatAdminService } from './stat-admin.service';

describe('StatAdminService', () => {
  let service: StatAdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StatAdminService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
