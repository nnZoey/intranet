export interface IJob {
  id?: number;
  jobTitle?: string;
  minSalary?: number;
  maxSalary?: number;
}

export class Job implements IJob {
  constructor(public id?: number, public jobTitle?: string, public minSalary?: number, public maxSalary?: number) {}
}

export function getJobIdentifier(job: IJob): number | undefined {
  return job.id;
}
