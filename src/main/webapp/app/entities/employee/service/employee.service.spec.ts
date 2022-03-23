import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { Sex } from 'app/entities/enumerations/sex.model';
import { IEmployee, Employee } from '../employee.model';

import { EmployeeService } from './employee.service';

describe('Employee Service', () => {
  let service: EmployeeService;
  let httpMock: HttpTestingController;
  let elemDefault: IEmployee;
  let expectedResult: IEmployee | IEmployee[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      employeeCode: 'AAAAAAA',
      firstName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      imageContentType: 'image/png',
      image: 'AAAAAAA',
      effectiveDate: currentDate,
      slogan: 'AAAAAAA',
      professionalEmail: 'AAAAAAA',
      professionalPhoneNumber: 'AAAAAAA',
      commissionPct: 0,
      hiredDate: currentDate,
      contractNumber: 'AAAAAAA',
      contractStartDate: currentDate,
      contractEndDate: currentDate,
      contractType: ContractType.DEFINITIVE,
      contractFileContentType: 'image/png',
      contractFile: 'AAAAAAA',
      salary: 0,
      sex: Sex.FEMALE,
      dob: currentDate,
      placeOfBirth: 'AAAAAAA',
      personalPhoneNumber: 'AAAAAAA',
      permanentAddress: 'AAAAAAA',
      temporaryAddress: 'AAAAAAA',
      idNumber: 'AAAAAAA',
      idIssuedDate: currentDate,
      idIssuedLocation: 'AAAAAAA',
      socialInsuranceNumber: 'AAAAAAA',
      taxIdentificationNumber: 'AAAAAAA',
      qualification: 'AAAAAAA',
      bankAccount: 'AAAAAAA',
      bankCode: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          effectiveDate: currentDate.format(DATE_TIME_FORMAT),
          hiredDate: currentDate.format(DATE_TIME_FORMAT),
          contractStartDate: currentDate.format(DATE_TIME_FORMAT),
          contractEndDate: currentDate.format(DATE_TIME_FORMAT),
          dob: currentDate.format(DATE_TIME_FORMAT),
          idIssuedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Employee', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          effectiveDate: currentDate.format(DATE_TIME_FORMAT),
          hiredDate: currentDate.format(DATE_TIME_FORMAT),
          contractStartDate: currentDate.format(DATE_TIME_FORMAT),
          contractEndDate: currentDate.format(DATE_TIME_FORMAT),
          dob: currentDate.format(DATE_TIME_FORMAT),
          idIssuedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          effectiveDate: currentDate,
          hiredDate: currentDate,
          contractStartDate: currentDate,
          contractEndDate: currentDate,
          dob: currentDate,
          idIssuedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Employee()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Employee', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employeeCode: 'BBBBBB',
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          image: 'BBBBBB',
          effectiveDate: currentDate.format(DATE_TIME_FORMAT),
          slogan: 'BBBBBB',
          professionalEmail: 'BBBBBB',
          professionalPhoneNumber: 'BBBBBB',
          commissionPct: 1,
          hiredDate: currentDate.format(DATE_TIME_FORMAT),
          contractNumber: 'BBBBBB',
          contractStartDate: currentDate.format(DATE_TIME_FORMAT),
          contractEndDate: currentDate.format(DATE_TIME_FORMAT),
          contractType: 'BBBBBB',
          contractFile: 'BBBBBB',
          salary: 1,
          sex: 'BBBBBB',
          dob: currentDate.format(DATE_TIME_FORMAT),
          placeOfBirth: 'BBBBBB',
          personalPhoneNumber: 'BBBBBB',
          permanentAddress: 'BBBBBB',
          temporaryAddress: 'BBBBBB',
          idNumber: 'BBBBBB',
          idIssuedDate: currentDate.format(DATE_TIME_FORMAT),
          idIssuedLocation: 'BBBBBB',
          socialInsuranceNumber: 'BBBBBB',
          taxIdentificationNumber: 'BBBBBB',
          qualification: 'BBBBBB',
          bankAccount: 'BBBBBB',
          bankCode: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          effectiveDate: currentDate,
          hiredDate: currentDate,
          contractStartDate: currentDate,
          contractEndDate: currentDate,
          dob: currentDate,
          idIssuedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Employee', () => {
      const patchObject = Object.assign(
        {
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          image: 'BBBBBB',
          effectiveDate: currentDate.format(DATE_TIME_FORMAT),
          slogan: 'BBBBBB',
          professionalEmail: 'BBBBBB',
          professionalPhoneNumber: 'BBBBBB',
          commissionPct: 1,
          hiredDate: currentDate.format(DATE_TIME_FORMAT),
          contractNumber: 'BBBBBB',
          contractStartDate: currentDate.format(DATE_TIME_FORMAT),
          contractEndDate: currentDate.format(DATE_TIME_FORMAT),
          contractType: 'BBBBBB',
          contractFile: 'BBBBBB',
          sex: 'BBBBBB',
          dob: currentDate.format(DATE_TIME_FORMAT),
          placeOfBirth: 'BBBBBB',
          personalPhoneNumber: 'BBBBBB',
          temporaryAddress: 'BBBBBB',
          idIssuedDate: currentDate.format(DATE_TIME_FORMAT),
          idIssuedLocation: 'BBBBBB',
          taxIdentificationNumber: 'BBBBBB',
          bankAccount: 'BBBBBB',
          bankCode: 'BBBBBB',
        },
        new Employee()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          effectiveDate: currentDate,
          hiredDate: currentDate,
          contractStartDate: currentDate,
          contractEndDate: currentDate,
          dob: currentDate,
          idIssuedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Employee', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employeeCode: 'BBBBBB',
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          image: 'BBBBBB',
          effectiveDate: currentDate.format(DATE_TIME_FORMAT),
          slogan: 'BBBBBB',
          professionalEmail: 'BBBBBB',
          professionalPhoneNumber: 'BBBBBB',
          commissionPct: 1,
          hiredDate: currentDate.format(DATE_TIME_FORMAT),
          contractNumber: 'BBBBBB',
          contractStartDate: currentDate.format(DATE_TIME_FORMAT),
          contractEndDate: currentDate.format(DATE_TIME_FORMAT),
          contractType: 'BBBBBB',
          contractFile: 'BBBBBB',
          salary: 1,
          sex: 'BBBBBB',
          dob: currentDate.format(DATE_TIME_FORMAT),
          placeOfBirth: 'BBBBBB',
          personalPhoneNumber: 'BBBBBB',
          permanentAddress: 'BBBBBB',
          temporaryAddress: 'BBBBBB',
          idNumber: 'BBBBBB',
          idIssuedDate: currentDate.format(DATE_TIME_FORMAT),
          idIssuedLocation: 'BBBBBB',
          socialInsuranceNumber: 'BBBBBB',
          taxIdentificationNumber: 'BBBBBB',
          qualification: 'BBBBBB',
          bankAccount: 'BBBBBB',
          bankCode: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          effectiveDate: currentDate,
          hiredDate: currentDate,
          contractStartDate: currentDate,
          contractEndDate: currentDate,
          dob: currentDate,
          idIssuedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Employee', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEmployeeToCollectionIfMissing', () => {
      it('should add a Employee to an empty array', () => {
        const employee: IEmployee = { id: 123 };
        expectedResult = service.addEmployeeToCollectionIfMissing([], employee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employee);
      });

      it('should not add a Employee to an array that contains it', () => {
        const employee: IEmployee = { id: 123 };
        const employeeCollection: IEmployee[] = [
          {
            ...employee,
          },
          { id: 456 },
        ];
        expectedResult = service.addEmployeeToCollectionIfMissing(employeeCollection, employee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Employee to an array that doesn't contain it", () => {
        const employee: IEmployee = { id: 123 };
        const employeeCollection: IEmployee[] = [{ id: 456 }];
        expectedResult = service.addEmployeeToCollectionIfMissing(employeeCollection, employee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employee);
      });

      it('should add only unique Employee to an array', () => {
        const employeeArray: IEmployee[] = [{ id: 123 }, { id: 456 }, { id: 78871 }];
        const employeeCollection: IEmployee[] = [{ id: 123 }];
        expectedResult = service.addEmployeeToCollectionIfMissing(employeeCollection, ...employeeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employee: IEmployee = { id: 123 };
        const employee2: IEmployee = { id: 456 };
        expectedResult = service.addEmployeeToCollectionIfMissing([], employee, employee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employee);
        expect(expectedResult).toContain(employee2);
      });

      it('should accept null and undefined values', () => {
        const employee: IEmployee = { id: 123 };
        expectedResult = service.addEmployeeToCollectionIfMissing([], null, employee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employee);
      });

      it('should return initial array if no Employee is added', () => {
        const employeeCollection: IEmployee[] = [{ id: 123 }];
        expectedResult = service.addEmployeeToCollectionIfMissing(employeeCollection, undefined, null);
        expect(expectedResult).toEqual(employeeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
