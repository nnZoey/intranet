import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BankAccountService } from '../service/bank-account.service';
import { IBankAccount, BankAccount } from '../bank-account.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { BankAccountUpdateComponent } from './bank-account-update.component';

describe('BankAccount Management Update Component', () => {
  let comp: BankAccountUpdateComponent;
  let fixture: ComponentFixture<BankAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bankAccountService: BankAccountService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BankAccountUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BankAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BankAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bankAccountService = TestBed.inject(BankAccountService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call employeeAccount query and add missing value', () => {
      const bankAccount: IBankAccount = { id: 456 };
      const employeeAccount: IEmployee = { id: 54223 };
      bankAccount.employeeAccount = employeeAccount;

      const employeeAccountCollection: IEmployee[] = [{ id: 40056 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeAccountCollection })));
      const expectedCollection: IEmployee[] = [employeeAccount, ...employeeAccountCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bankAccount });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeAccountCollection, employeeAccount);
      expect(comp.employeeAccountsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bankAccount: IBankAccount = { id: 456 };
      const employeeAccount: IEmployee = { id: 92395 };
      bankAccount.employeeAccount = employeeAccount;

      activatedRoute.data = of({ bankAccount });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bankAccount));
      expect(comp.employeeAccountsCollection).toContain(employeeAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BankAccount>>();
      const bankAccount = { id: 123 };
      jest.spyOn(bankAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bankAccount }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bankAccountService.update).toHaveBeenCalledWith(bankAccount);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BankAccount>>();
      const bankAccount = new BankAccount();
      jest.spyOn(bankAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bankAccount }));
      saveSubject.complete();

      // THEN
      expect(bankAccountService.create).toHaveBeenCalledWith(bankAccount);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BankAccount>>();
      const bankAccount = { id: 123 };
      jest.spyOn(bankAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bankAccountService.update).toHaveBeenCalledWith(bankAccount);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEmployeeById', () => {
      it('Should return tracked Employee primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmployeeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
