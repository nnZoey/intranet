import dayjs from 'dayjs/esm';
import { IProject } from 'app/entities/project/project.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { ProjectRole } from 'app/entities/enumerations/project-role.model';

export interface IAssignment {
  id?: number;
  startDate?: dayjs.Dayjs;
  projectRole?: ProjectRole;
  project?: IProject;
  employee?: IEmployee;
}

export class Assignment implements IAssignment {
  constructor(
    public id?: number,
    public startDate?: dayjs.Dayjs,
    public projectRole?: ProjectRole,
    public project?: IProject,
    public employee?: IEmployee
  ) {}
}

export function getAssignmentIdentifier(assignment: IAssignment): number | undefined {
  return assignment.id;
}
