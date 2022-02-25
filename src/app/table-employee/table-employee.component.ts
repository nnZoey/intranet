import { Component, OnInit } from '@angular/core';
import { NbSortDirection, NbSortRequest, NbTreeGridDataSource, NbTreeGridDataSourceBuilder } from '@nebular/theme';
import dayjs from 'rxjs';

interface TreeNode<T> {
  data: T;
  children?: TreeNode<T>;
  expanded?: boolean;
}

interface IEmployee {
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

@Component({
  selector: 'app-table-employee',
  templateUrl: './table-employee.component.html',
  styleUrls: ['./table-employee.component.scss']
})
export class TableEmployeeComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
