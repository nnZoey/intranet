import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBankAccount, BankAccount } from '../bank-account.model';
import { BankAccountService } from '../service/bank-account.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-bank-account-update',
  templateUrl: './bank-account-update.component.html',
})
export class BankAccountUpdateComponent implements OnInit {
  isSaving = false;

  employeeAccountsCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    bankAccount: [null, [Validators.required]],
    bankCode: [null, [Validators.required]],
    employeeAccount: [null, Validators.required],
  });

  constructor(
    protected bankAccountService: BankAccountService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankAccount }) => {
      this.updateForm(bankAccount);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankAccount = this.createFromForm();
    if (bankAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.bankAccountService.update(bankAccount));
    } else {
      this.subscribeToSaveResponse(this.bankAccountService.create(bankAccount));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankAccount>>): void {
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

  protected updateForm(bankAccount: IBankAccount): void {
    this.editForm.patchValue({
      id: bankAccount.id,
      bankAccount: bankAccount.bankAccount,
      bankCode: bankAccount.bankCode,
      employeeAccount: bankAccount.employeeAccount,
    });

    this.employeeAccountsCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeeAccountsCollection,
      bankAccount.employeeAccount
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query({ filter: 'bankaccount-is-null' })
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employeeAccount')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeeAccountsCollection = employees));
  }

  protected createFromForm(): IBankAccount {
    return {
      ...new BankAccount(),
      id: this.editForm.get(['id'])!.value,
      bankAccount: this.editForm.get(['bankAccount'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      employeeAccount: this.editForm.get(['employeeAccount'])!.value,
    };
  }
}
