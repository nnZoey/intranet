import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPerson, Person } from '../../person/person.model';
import { PersonService } from '../../person/service/person.service';
import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { Sex } from 'app/entities/enumerations/sex.model';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  sexValues = Object.keys(Sex);
  employeesCollection: IEmployee[] = [];
  employeesSharedCollection: IEmployee[] = [];
  jobsSharedCollection: IJob[] = [];
  departmentsSharedCollection: IDepartment[] = [];

  editForm = this.fb.group({
    id: [],
    effectiveDate: [null, [Validators.required]],
    employeeCode: [null, [Validators.required]],
    professionalEmail: [null, [Validators.required]],
    professionalPhoneNumber: [null, [Validators.required]],
    salary: [null, [Validators.required, Validators.min(1000000)]],
    commissionPct: [null, [Validators.required, Validators.min(1), Validators.max(100)]],
    contractNumber: [null, [Validators.required]],
    contractStartDate: [null, [Validators.required]],
    contractEndDate: [null, [Validators.required]],
    contractType: [],
    supervisor: [],
    job: [],
    department: [null, Validators.required],
    firstName: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    sex: [null, [Validators.required]],
    dob: [null, [Validators.required]],
    placeOfBirth: [],
    personalPhoneNumber: [null, [Validators.required]],
    personalEmail: [null, [Validators.required]],
    permanentAddress: [null, [Validators.required]],
    temporaryAddress: [null, [Validators.required]],
    idNumber: [null, [Validators.required]],
    idIssuedDate: [null, [Validators.required]],
    idIssuedLocation: [null, [Validators.required]],
    socialInsuranceNumber: [],
    taxIdentificationNumber: [],
    qualification: [],
    employee: [null, Validators.required],
  });

  constructor(
    protected personService: PersonService,
    protected employeeService: EmployeeService,
    protected jobService: JobService,
    protected departmentService: DepartmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee, person }) => {
      if (employee.id === undefined) {
        const today = dayjs().startOf('day');
        employee.effectiveDate = today;
        employee.contractStartDate = today;
        employee.contractEndDate = today;
      }
      if (person.id === undefined) {
        const today = dayjs().startOf('day');
        person.dob = today;
        person.idIssuedDate = today;
      }

      this.updateForm(employee);
      this.updateForm1(person);
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    const person = this.createFromForm1(employee);
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee), person);
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee), person);
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  trackJobById(index: number, item: IJob): number {
    return item.id!;
  }

  trackDepartmentById(index: number, item: IDepartment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>, person: IPerson): void {
    result
      .pipe(map(() => this.personService.update(person)))
      .pipe(finalize(() => this.onSaveFinalize()))
      .subscribe({
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
      effectiveDate: employee.effectiveDate ? employee.effectiveDate.format(DATE_TIME_FORMAT) : null,
      employeeCode: employee.employeeCode,
      professionalEmail: employee.professionalEmail,
      professionalPhoneNumber: employee.professionalPhoneNumber,
      salary: employee.salary,
      commissionPct: employee.commissionPct,
      contractNumber: employee.contractNumber,
      contractStartDate: employee.contractStartDate ? employee.contractStartDate.format(DATE_TIME_FORMAT) : null,
      contractEndDate: employee.contractEndDate ? employee.contractEndDate.format(DATE_TIME_FORMAT) : null,
      contractType: employee.contractType,
      supervisor: employee.supervisor,
      job: employee.job,
      department: employee.department,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      employee.supervisor
    );
    this.jobsSharedCollection = this.jobService.addJobToCollectionIfMissing(this.jobsSharedCollection, employee.job);
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing(
      this.departmentsSharedCollection,
      employee.department
    );
  }

  protected updateForm1(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      firstName: person.firstName,
      lastName: person.lastName,
      sex: person.sex,
      dob: person.dob ? person.dob.format(DATE_TIME_FORMAT) : null,
      placeOfBirth: person.placeOfBirth,
      personalPhoneNumber: person.personalPhoneNumber,
      personalEmail: person.personalEmail,
      permanentAddress: person.permanentAddress,
      temporaryAddress: person.temporaryAddress,
      idNumber: person.idNumber,
      idIssuedDate: person.idIssuedDate ? person.idIssuedDate.format(DATE_TIME_FORMAT) : null,
      idIssuedLocation: person.idIssuedLocation,
      socialInsuranceNumber: person.socialInsuranceNumber,
      taxIdentificationNumber: person.taxIdentificationNumber,
      qualification: person.qualification,
      employee: person.employee,
    });

    this.employeesCollection = this.employeeService.addEmployeeToCollectionIfMissing(this.employeesCollection, person.employee);
  }

  protected loadRelationshipsOptions(): void {
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

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing(departments, this.editForm.get('department')!.value)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      effectiveDate: this.editForm.get(['effectiveDate'])!.value
        ? dayjs(this.editForm.get(['effectiveDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      employeeCode: this.editForm.get(['employeeCode'])!.value,
      professionalEmail: this.editForm.get(['professionalEmail'])!.value,
      professionalPhoneNumber: this.editForm.get(['professionalPhoneNumber'])!.value,
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
      department: this.editForm.get(['department'])!.value,
    };
  }

  protected createFromForm1(EMF: IPerson): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      sex: this.editForm.get(['sex'])!.value,
      dob: this.editForm.get(['dob'])!.value ? dayjs(this.editForm.get(['dob'])!.value, DATE_TIME_FORMAT) : undefined,
      placeOfBirth: this.editForm.get(['placeOfBirth'])!.value,
      personalPhoneNumber: this.editForm.get(['personalPhoneNumber'])!.value,
      personalEmail: this.editForm.get(['personalEmail'])!.value,
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
      employee: EMF,
    };
  }
}
