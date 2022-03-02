import { IEmployee } from 'app/entities/employee/employee.model';

export interface IBankAccount {
  id?: number;
  bankAccount?: string;
  bankCode?: string;
  employeeAccount?: IEmployee;
}

export class BankAccount implements IBankAccount {
  constructor(public id?: number, public bankAccount?: string, public bankCode?: string, public employeeAccount?: IEmployee) {}
}

export function getBankAccountIdentifier(bankAccount: IBankAccount): number | undefined {
  return bankAccount.id;
}
