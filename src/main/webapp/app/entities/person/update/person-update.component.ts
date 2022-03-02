import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPerson, Person } from '../person.model';
import { PersonService } from '../service/person.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { Sex } from 'app/entities/enumerations/sex.model';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html',
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;
  sexValues = Object.keys(Sex);

  employeesCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
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
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      if (person.id === undefined) {
        const today = dayjs().startOf('day');
        person.dob = today;
        person.idIssuedDate = today;
      }

      this.updateForm(person);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
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

  protected updateForm(person: IPerson): void {
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
      .query({ filter: 'person-is-null' })
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employee')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesCollection = employees));
  }

  protected createFromForm(): IPerson {
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
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
