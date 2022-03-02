import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ProjectRole } from 'app/entities/enumerations/project-role.model';
import { IAssignment, Assignment } from '../assignment.model';

import { AssignmentService } from './assignment.service';

describe('Assignment Service', () => {
  let service: AssignmentService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssignment;
  let expectedResult: IAssignment | IAssignment[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssignmentService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      startDate: currentDate,
      projectRole: ProjectRole.ENGINEER,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          startDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Assignment', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          startDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Assignment()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Assignment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          startDate: currentDate.format(DATE_TIME_FORMAT),
          projectRole: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Assignment', () => {
      const patchObject = Object.assign(
        {
          projectRole: 'BBBBBB',
        },
        new Assignment()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Assignment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          startDate: currentDate.format(DATE_TIME_FORMAT),
          projectRole: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Assignment', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssignmentToCollectionIfMissing', () => {
      it('should add a Assignment to an empty array', () => {
        const assignment: IAssignment = { id: 123 };
        expectedResult = service.addAssignmentToCollectionIfMissing([], assignment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assignment);
      });

      it('should not add a Assignment to an array that contains it', () => {
        const assignment: IAssignment = { id: 123 };
        const assignmentCollection: IAssignment[] = [
          {
            ...assignment,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssignmentToCollectionIfMissing(assignmentCollection, assignment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Assignment to an array that doesn't contain it", () => {
        const assignment: IAssignment = { id: 123 };
        const assignmentCollection: IAssignment[] = [{ id: 456 }];
        expectedResult = service.addAssignmentToCollectionIfMissing(assignmentCollection, assignment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assignment);
      });

      it('should add only unique Assignment to an array', () => {
        const assignmentArray: IAssignment[] = [{ id: 123 }, { id: 456 }, { id: 7971 }];
        const assignmentCollection: IAssignment[] = [{ id: 123 }];
        expectedResult = service.addAssignmentToCollectionIfMissing(assignmentCollection, ...assignmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assignment: IAssignment = { id: 123 };
        const assignment2: IAssignment = { id: 456 };
        expectedResult = service.addAssignmentToCollectionIfMissing([], assignment, assignment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assignment);
        expect(expectedResult).toContain(assignment2);
      });

      it('should accept null and undefined values', () => {
        const assignment: IAssignment = { id: 123 };
        expectedResult = service.addAssignmentToCollectionIfMissing([], null, assignment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assignment);
      });

      it('should return initial array if no Assignment is added', () => {
        const assignmentCollection: IAssignment[] = [{ id: 123 }];
        expectedResult = service.addAssignmentToCollectionIfMissing(assignmentCollection, undefined, null);
        expect(expectedResult).toEqual(assignmentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
