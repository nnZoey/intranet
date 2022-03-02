import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IJobHistory, JobHistory } from '../job-history.model';

import { JobHistoryService } from './job-history.service';

describe('JobHistory Service', () => {
  let service: JobHistoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IJobHistory;
  let expectedResult: IJobHistory | IJobHistory[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(JobHistoryService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      employeeCode: 'AAAAAAA',
      effectiveDate: currentDate,
      salary: 0,
      commissionPct: 0,
      contractNumber: 'AAAAAAA',
      contractStartDate: currentDate,
      contractEndDate: currentDate,
      contractType: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          effectiveDate: currentDate.format(DATE_TIME_FORMAT),
          contractStartDate: currentDate.format(DATE_TIME_FORMAT),
          contractEndDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a JobHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          effectiveDate: currentDate.format(DATE_TIME_FORMAT),
          contractStartDate: currentDate.format(DATE_TIME_FORMAT),
          contractEndDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          effectiveDate: currentDate,
          contractStartDate: currentDate,
          contractEndDate: currentDate,
        },
        returnedFromService
      );

      service.create(new JobHistory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a JobHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employeeCode: 'BBBBBB',
          effectiveDate: currentDate.format(DATE_TIME_FORMAT),
          salary: 1,
          commissionPct: 1,
          contractNumber: 'BBBBBB',
          contractStartDate: currentDate.format(DATE_TIME_FORMAT),
          contractEndDate: currentDate.format(DATE_TIME_FORMAT),
          contractType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          effectiveDate: currentDate,
          contractStartDate: currentDate,
          contractEndDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a JobHistory', () => {
      const patchObject = Object.assign(
        {
          effectiveDate: currentDate.format(DATE_TIME_FORMAT),
          commissionPct: 1,
        },
        new JobHistory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          effectiveDate: currentDate,
          contractStartDate: currentDate,
          contractEndDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of JobHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employeeCode: 'BBBBBB',
          effectiveDate: currentDate.format(DATE_TIME_FORMAT),
          salary: 1,
          commissionPct: 1,
          contractNumber: 'BBBBBB',
          contractStartDate: currentDate.format(DATE_TIME_FORMAT),
          contractEndDate: currentDate.format(DATE_TIME_FORMAT),
          contractType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          effectiveDate: currentDate,
          contractStartDate: currentDate,
          contractEndDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a JobHistory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addJobHistoryToCollectionIfMissing', () => {
      it('should add a JobHistory to an empty array', () => {
        const jobHistory: IJobHistory = { id: 123 };
        expectedResult = service.addJobHistoryToCollectionIfMissing([], jobHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jobHistory);
      });

      it('should not add a JobHistory to an array that contains it', () => {
        const jobHistory: IJobHistory = { id: 123 };
        const jobHistoryCollection: IJobHistory[] = [
          {
            ...jobHistory,
          },
          { id: 456 },
        ];
        expectedResult = service.addJobHistoryToCollectionIfMissing(jobHistoryCollection, jobHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a JobHistory to an array that doesn't contain it", () => {
        const jobHistory: IJobHistory = { id: 123 };
        const jobHistoryCollection: IJobHistory[] = [{ id: 456 }];
        expectedResult = service.addJobHistoryToCollectionIfMissing(jobHistoryCollection, jobHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jobHistory);
      });

      it('should add only unique JobHistory to an array', () => {
        const jobHistoryArray: IJobHistory[] = [{ id: 123 }, { id: 456 }, { id: 60154 }];
        const jobHistoryCollection: IJobHistory[] = [{ id: 123 }];
        expectedResult = service.addJobHistoryToCollectionIfMissing(jobHistoryCollection, ...jobHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const jobHistory: IJobHistory = { id: 123 };
        const jobHistory2: IJobHistory = { id: 456 };
        expectedResult = service.addJobHistoryToCollectionIfMissing([], jobHistory, jobHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jobHistory);
        expect(expectedResult).toContain(jobHistory2);
      });

      it('should accept null and undefined values', () => {
        const jobHistory: IJobHistory = { id: 123 };
        expectedResult = service.addJobHistoryToCollectionIfMissing([], null, jobHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jobHistory);
      });

      it('should return initial array if no JobHistory is added', () => {
        const jobHistoryCollection: IJobHistory[] = [{ id: 123 }];
        expectedResult = service.addJobHistoryToCollectionIfMissing(jobHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(jobHistoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
