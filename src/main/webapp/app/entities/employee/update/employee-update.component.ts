import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { Sex } from 'app/entities/enumerations/sex.model';

@Component({
  selector: 'leap-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  contractTypeValues = Object.keys(ContractType);
  sexValues = Object.keys(Sex);

  usersSharedCollection: IUser[] = [];
  employeesSharedCollection: IEmployee[] = [];
  jobsSharedCollection: IJob[] = [];

  editForm = this.fb.group({
    id: [],
    employeeCode: [null, [Validators.required]],
    firstName: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    effectiveDate: [null, [Validators.required]],
    slogan: [],
    professionalEmail: [null, [Validators.required]],
    professionalPhoneNumber: [null, [Validators.required]],
    commissionPct: [null, [Validators.required, Validators.min(1), Validators.max(100)]],
    hiredDate: [null, [Validators.required]],
    contractNumber: [null, [Validators.required]],
    contractStartDate: [null, [Validators.required]],
    contractEndDate: [],
    contractType: [null, [Validators.required]],
    contractFile: [],
    contractFileContentType: [],
    salary: [null, [Validators.required, Validators.min(1000000)]],
    sex: [null, [Validators.required]],
    dob: [null, [Validators.required]],
    placeOfBirth: [],
    personalPhoneNumber: [null, [Validators.required]],
    permanentAddress: [null, [Validators.required]],
    temporaryAddress: [null, [Validators.required]],
    idNumber: [null, [Validators.required]],
    idIssuedDate: [null, [Validators.required]],
    idIssuedLocation: [null, [Validators.required]],
    socialInsuranceNumber: [],
    taxIdentificationNumber: [],
    qualification: [],
    bankAccount: [null, [Validators.required]],
    bankCode: [null, [Validators.required]],
    userId: [],
    supervisor: [],
    job: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected jobService: JobService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      if (employee.id === undefined) {
        const today = dayjs().startOf('day');
        employee.effectiveDate = today;
        employee.hiredDate = today;
        employee.contractStartDate = today;
        employee.contractEndDate = today;
        employee.dob = today;
        employee.idIssuedDate = today;
      }

      this.updateForm(employee);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('intranetApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  trackJobById(index: number, item: IJob): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      employeeCode: employee.employeeCode,
      firstName: employee.firstName,
      lastName: employee.lastName,
      image: employee.image,
      imageContentType: employee.imageContentType,
      effectiveDate: employee.effectiveDate ? employee.effectiveDate.format(DATE_TIME_FORMAT) : null,
      slogan: employee.slogan,
      professionalEmail: employee.professionalEmail,
      professionalPhoneNumber: employee.professionalPhoneNumber,
      commissionPct: employee.commissionPct,
      hiredDate: employee.hiredDate ? employee.hiredDate.format(DATE_TIME_FORMAT) : null,
      contractNumber: employee.contractNumber,
      contractStartDate: employee.contractStartDate ? employee.contractStartDate.format(DATE_TIME_FORMAT) : null,
      contractEndDate: employee.contractEndDate ? employee.contractEndDate.format(DATE_TIME_FORMAT) : null,
      contractType: employee.contractType,
      contractFile: employee.contractFile,
      contractFileContentType: employee.contractFileContentType,
      salary: employee.salary,
      sex: employee.sex,
      dob: employee.dob ? employee.dob.format(DATE_TIME_FORMAT) : null,
      placeOfBirth: employee.placeOfBirth,
      personalPhoneNumber: employee.personalPhoneNumber,
      permanentAddress: employee.permanentAddress,
      temporaryAddress: employee.temporaryAddress,
      idNumber: employee.idNumber,
      idIssuedDate: employee.idIssuedDate ? employee.idIssuedDate.format(DATE_TIME_FORMAT) : null,
      idIssuedLocation: employee.idIssuedLocation,
      socialInsuranceNumber: employee.socialInsuranceNumber,
      taxIdentificationNumber: employee.taxIdentificationNumber,
      qualification: employee.qualification,
      bankAccount: employee.bankAccount,
      bankCode: employee.bankCode,
      userId: employee.userId,
      supervisor: employee.supervisor,
      job: employee.job,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, employee.userId);
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      employee.supervisor
    );
    this.jobsSharedCollection = this.jobService.addJobToCollectionIfMissing(this.jobsSharedCollection, employee.job);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('userId')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('supervisor')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.jobService
      .query()
      .pipe(map((res: HttpResponse<IJob[]>) => res.body ?? []))
      .pipe(map((jobs: IJob[]) => this.jobService.addJobToCollectionIfMissing(jobs, this.editForm.get('job')!.value)))
      .subscribe((jobs: IJob[]) => (this.jobsSharedCollection = jobs));
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      employeeCode: this.editForm.get(['employeeCode'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      effectiveDate: this.editForm.get(['effectiveDate'])!.value
        ? dayjs(this.editForm.get(['effectiveDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      slogan: this.editForm.get(['slogan'])!.value,
      professionalEmail: this.editForm.get(['professionalEmail'])!.value,
      professionalPhoneNumber: this.editForm.get(['professionalPhoneNumber'])!.value,
      commissionPct: this.editForm.get(['commissionPct'])!.value,
      hiredDate: this.editForm.get(['hiredDate'])!.value ? dayjs(this.editForm.get(['hiredDate'])!.value, DATE_TIME_FORMAT) : undefined,
      contractNumber: this.editForm.get(['contractNumber'])!.value,
      contractStartDate: this.editForm.get(['contractStartDate'])!.value
        ? dayjs(this.editForm.get(['contractStartDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      contractEndDate: this.editForm.get(['contractEndDate'])!.value
        ? dayjs(this.editForm.get(['contractEndDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      contractType: this.editForm.get(['contractType'])!.value,
      contractFileContentType: this.editForm.get(['contractFileContentType'])!.value,
      contractFile: this.editForm.get(['contractFile'])!.value,
      salary: this.editForm.get(['salary'])!.value,
      sex: this.editForm.get(['sex'])!.value,
      dob: this.editForm.get(['dob'])!.value ? dayjs(this.editForm.get(['dob'])!.value, DATE_TIME_FORMAT) : undefined,
      placeOfBirth: this.editForm.get(['placeOfBirth'])!.value,
      personalPhoneNumber: this.editForm.get(['personalPhoneNumber'])!.value,
      permanentAddress: this.editForm.get(['permanentAddress'])!.value,
      temporaryAddress: this.editForm.get(['temporaryAddress'])!.value,
      idNumber: this.editForm.get(['idNumber'])!.value,
      idIssuedDate: this.editForm.get(['idIssuedDate'])!.value
        ? dayjs(this.editForm.get(['idIssuedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      idIssuedLocation: this.editForm.get(['idIssuedLocation'])!.value,
      socialInsuranceNumber: this.editForm.get(['socialInsuranceNumber'])!.value,
      taxIdentificationNumber: this.editForm.get(['taxIdentificationNumber'])!.value,
      qualification: this.editForm.get(['qualification'])!.value,
      bankAccount: this.editForm.get(['bankAccount'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      supervisor: this.editForm.get(['supervisor'])!.value,
      job: this.editForm.get(['job'])!.value,
    };
  }
}
