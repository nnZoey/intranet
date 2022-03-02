import dayjs from 'dayjs/esm';
import { IAssignment } from 'app/entities/assignment/assignment.model';
import { IJobHistory } from 'app/entities/job-history/job-history.model';
import { IJob } from 'app/entities/job/job.model';
import { IDepartment } from 'app/entities/department/department.model';

export interface IEmployee {
  id?: number;
  effectiveDate?: dayjs.Dayjs;
  employeeCode?: string;
  professionalEmail?: string;
  professionalPhoneNumber?: string;
  salary?: number;
  commissionPct?: number;
  contractNumber?: string;
  contractStartDate?: dayjs.Dayjs;
  contractEndDate?: dayjs.Dayjs;
  contractType?: string | null;
  assignments?: IAssignment[] | null;
  jobHistories?: IJobHistory[] | null;
  supervisor?: IEmployee | null;
  job?: IJob | null;
  department?: IDepartment;
  teamMembers?: IEmployee[] | null;
  teamMembersInHistories?: IJobHistory[] | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public effectiveDate?: dayjs.Dayjs,
    public employeeCode?: string,
    public professionalEmail?: string,
    public professionalPhoneNumber?: string,
    public salary?: number,
    public commissionPct?: number,
    public contractNumber?: string,
    public contractStartDate?: dayjs.Dayjs,
    public contractEndDate?: dayjs.Dayjs,
    public contractType?: string | null,
    public assignments?: IAssignment[] | null,
    public jobHistories?: IJobHistory[] | null,
    public supervisor?: IEmployee | null,
    public job?: IJob | null,
    public department?: IDepartment,
    public teamMembers?: IEmployee[] | null,
    public teamMembersInHistories?: IJobHistory[] | null
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
