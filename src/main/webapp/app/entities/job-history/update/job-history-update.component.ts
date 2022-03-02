import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IJobHistory, JobHistory } from '../job-history.model';
import { JobHistoryService } from '../service/job-history.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';

@Component({
  selector: 'jhi-job-history-update',
  templateUrl: './job-history-update.component.html',
})
export class JobHistoryUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];
  jobsSharedCollection: IJob[] = [];

  editForm = this.fb.group({
    id: [],
    employeeCode: [null, [Validators.required]],
    effectiveDate: [null, [Validators.required]],
    salary: [null, [Validators.required, Validators.min(1000000)]],
    commissionPct: [null, [Validators.required, Validators.min(1), Validators.max(100)]],
    contractNumber: [null, [Validators.required]],
    contractStartDate: [null, [Validators.required]],
    contractEndDate: [null, [Validators.required]],
    contractType: [],
    supervisor: [],
    job: [],
    employee: [null, Validators.required],
  });

  constructor(
    protected jobHistoryService: JobHistoryService,
    protected employeeService: EmployeeService,
    protected jobService: JobService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobHistory }) => {
      if (jobHistory.id === undefined) {
        const today = dayjs().startOf('day');
        jobHistory.effectiveDate = today;
        jobHistory.contractStartDate = today;
        jobHistory.contractEndDate = today;
      }

      this.updateForm(jobHistory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobHistory = this.createFromForm();
    if (jobHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.jobHistoryService.update(jobHistory));
    } else {
      this.subscribeToSaveResponse(this.jobHistoryService.create(jobHistory));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  trackJobById(index: number, item: IJob): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobHistory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(jobHistory: IJobHistory): void {
    this.editForm.patchValue({
      id: jobHistory.id,
      employeeCode: jobHistory.employeeCode,
      effectiveDate: jobHistory.effectiveDate ? jobHistory.effectiveDate.format(DATE_TIME_FORMAT) : null,
      salary: jobHistory.salary,
      commissionPct: jobHistory.commissionPct,
      contractNumber: jobHistory.contractNumber,
      contractStartDate: jobHistory.contractStartDate ? jobHistory.contractStartDate.format(DATE_TIME_FORMAT) : null,
      contractEndDate: jobHistory.contractEndDate ? jobHistory.contractEndDate.format(DATE_TIME_FORMAT) : null,
      contractType: jobHistory.contractType,
      supervisor: jobHistory.supervisor,
      job: jobHistory.job,
      employee: jobHistory.employee,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      jobHistory.supervisor,
      jobHistory.employee
    );
    this.jobsSharedCollection = this.jobService.addJobToCollectionIfMissing(this.jobsSharedCollection, jobHistory.job);
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(
            employees,
            this.editForm.get('supervisor')!.value,
            this.editForm.get('employee')!.value
          )
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.jobService
      .query()
      .pipe(map((res: HttpResponse<IJob[]>) => res.body ?? []))
      .pipe(map((jobs: IJob[]) => this.jobService.addJobToCollectionIfMissing(jobs, this.editForm.get('job')!.value)))
      .subscribe((jobs: IJob[]) => (this.jobsSharedCollection = jobs));
  }

  protected createFromForm(): IJobHistory {
    return {
      ...new JobHistory(),
      id: this.editForm.get(['id'])!.value,
      employeeCode: this.editForm.get(['employeeCode'])!.value,
      effectiveDate: this.editForm.get(['effectiveDate'])!.value
        ? dayjs(this.editForm.get(['effectiveDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      salary: this.editForm.get(['salary'])!.value,
      commissionPct: this.editForm.get(['commissionPct'])!.value,
      contractNumber: this.editForm.get(['contractNumber'])!.value,
      contractStartDate: this.editForm.get(['contractStartDate'])!.value
        ? dayjs(this.editForm.get(['contractStartDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      contractEndDate: this.editForm.get(['contractEndDate'])!.value
        ? dayjs(this.editForm.get(['contractEndDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      contractType: this.editForm.get(['contractType'])!.value,
      supervisor: this.editForm.get(['supervisor'])!.value,
      job: this.editForm.get(['job'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
