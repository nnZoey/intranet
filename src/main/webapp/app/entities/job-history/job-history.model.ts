import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IJob } from 'app/entities/job/job.model';

export interface IJobHistory {
  id?: number;
  employeeCode?: string;
  effectiveDate?: dayjs.Dayjs;
  salary?: number;
  commissionPct?: number;
  contractNumber?: string;
  contractStartDate?: dayjs.Dayjs;
  contractEndDate?: dayjs.Dayjs;
  contractType?: string | null;
  supervisor?: IEmployee | null;
  job?: IJob | null;
  employee?: IEmployee;
}

export class JobHistory implements IJobHistory {
  constructor(
    public id?: number,
    public employeeCode?: string,
    public effectiveDate?: dayjs.Dayjs,
    public salary?: number,
    public commissionPct?: number,
    public contractNumber?: string,
    public contractStartDate?: dayjs.Dayjs,
    public contractEndDate?: dayjs.Dayjs,
    public contractType?: string | null,
    public supervisor?: IEmployee | null,
    public job?: IJob | null,
    public employee?: IEmployee
  ) {}
}

export function getJobHistoryIdentifier(jobHistory: IJobHistory): number | undefined {
  return jobHistory.id;
}
