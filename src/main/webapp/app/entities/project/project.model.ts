import dayjs from 'dayjs/esm';
import { IAssignment } from 'app/entities/assignment/assignment.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IProject {
  id?: number;
  name?: string;
  description?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  status?: Status;
  assignments?: IAssignment[] | null;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs | null,
    public status?: Status,
    public assignments?: IAssignment[] | null
  ) {}
}

export function getProjectIdentifier(project: IProject): number | undefined {
  return project.id;
}
