import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { Sex } from 'app/entities/enumerations/sex.model';

export interface IPerson {
  id?: number;
  firstName?: string;
  lastName?: string;
  sex?: Sex;
  dob?: dayjs.Dayjs;
  placeOfBirth?: string | null;
  personalPhoneNumber?: string;
  personalEmail?: string;
  permanentAddress?: string;
  temporaryAddress?: string;
  idNumber?: string;
  idIssuedDate?: dayjs.Dayjs;
  idIssuedLocation?: string;
  socialInsuranceNumber?: string | null;
  taxIdentificationNumber?: string | null;
  qualification?: string | null;
  employee?: IEmployee;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public sex?: Sex,
    public dob?: dayjs.Dayjs,
    public placeOfBirth?: string | null,
    public personalPhoneNumber?: string,
    public personalEmail?: string,
    public permanentAddress?: string,
    public temporaryAddress?: string,
    public idNumber?: string,
    public idIssuedDate?: dayjs.Dayjs,
    public idIssuedLocation?: string,
    public socialInsuranceNumber?: string | null,
    public taxIdentificationNumber?: string | null,
    public qualification?: string | null,
    public employee?: IEmployee
  ) {}
}

export function getPersonIdentifier(person: IPerson): number | undefined {
  return person.id;
}
