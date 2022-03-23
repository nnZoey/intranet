import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IAssignment } from 'app/entities/assignment/assignment.model';
import { IJob } from 'app/entities/job/job.model';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { Sex } from 'app/entities/enumerations/sex.model';

export interface IEmployee {

  id?: number;
  employeeCode?: string;
  firstName?: string;
  lastName?: string;
  imageContentType?: string | null;
  image?: string | null;
  effectiveDate?: dayjs.Dayjs;
  slogan?: string | null;
  professionalEmail?: string;
  professionalPhoneNumber?: string;
  commissionPct?: number;
  hiredDate?: dayjs.Dayjs;
  contractNumber?: string;
  contractStartDate?: dayjs.Dayjs;
  contractEndDate?: dayjs.Dayjs | null;
  contractType?: ContractType;
  contractFileContentType?: string | null;
  contractFile?: string | null;
  salary?: number;
  sex?: Sex;
  dob?: dayjs.Dayjs;
  placeOfBirth?: string | null;
  personalPhoneNumber?: string;
  permanentAddress?: string;
  temporaryAddress?: string;
  idNumber?: string;
  idIssuedDate?: dayjs.Dayjs;
  idIssuedLocation?: string;
  socialInsuranceNumber?: string | null;
  taxIdentificationNumber?: string | null;
  qualification?: string | null;
  bankAccount?: string;
  bankCode?: string;
  userId?: IUser | null;
  assignments?: IAssignment[] | null;
  supervisor?: IEmployee | null;
  job?: IJob | null;
  teamMembers?: IEmployee[] | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public employeeCode?: string,
    public firstName?: string,
    public lastName?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public effectiveDate?: dayjs.Dayjs,
    public slogan?: string | null,
    public professionalEmail?: string,
    public professionalPhoneNumber?: string,
    public commissionPct?: number,
    public hiredDate?: dayjs.Dayjs,
    public contractNumber?: string,
    public contractStartDate?: dayjs.Dayjs,
    public contractEndDate?: dayjs.Dayjs | null,
    public contractType?: ContractType,
    public contractFileContentType?: string | null,
    public contractFile?: string | null,
    public salary?: number,
    public sex?: Sex,
    public dob?: dayjs.Dayjs,
    public placeOfBirth?: string | null,
    public personalPhoneNumber?: string,
    public permanentAddress?: string,
    public temporaryAddress?: string,
    public idNumber?: string,
    public idIssuedDate?: dayjs.Dayjs,
    public idIssuedLocation?: string,
    public socialInsuranceNumber?: string | null,
    public taxIdentificationNumber?: string | null,
    public qualification?: string | null,
    public bankAccount?: string,
    public bankCode?: string,
    public userId?: IUser | null,
    public assignments?: IAssignment[] | null,
    public supervisor?: IEmployee | null,
    public job?: IJob | null,
    public teamMembers?: IEmployee[] | null
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
