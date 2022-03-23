import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployee } from '../employee.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { EmployeeService } from '../service/employee.service';
import { EmployeeDeleteDialogComponent } from '../delete/employee-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { AccountService } from 'app/core/auth/account.service';
import { ActivatedRoute } from '@angular/router';
import { NbSearchService } from '@nebular/theme';
import { ConfirmationService } from 'primeng/api';
import { MessageService } from 'primeng/api';


@Component({
  selector: 'leap-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.scss'],
  styles: [`
        :host ::ng-deep .p-dialog .product-image {
            width: 150px;
            margin: 0 auto 2rem auto;
            display: block;
        }
    `],
})
export class EmployeeComponent implements OnInit {
  employees: IEmployee[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  employeeDialog: boolean | undefined;
  employeeUserId: IEmployee | null = null;
  value = '';
  selectedProducts: IEmployee[] | null = null;

  submitted: boolean | undefined;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    protected employeeService: EmployeeService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private searchService: NbSearchService,
    private messageService: MessageService, 
    private confirmationService: ConfirmationService,
  ) {
    this.employees = [];
    this.employeeUserId = {};
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.searchService.onSearchSubmit()
      .subscribe((data: any) => {
        this.value = data.term;
      })
  }

  
  editProduct(employee: IEmployee): void {
    this.employeeUserId = {...employee};
    this.employeeDialog = true;
  }

  openNew(): void {
    this.employeeUserId = {};
    this.submitted = false;
    this.employeeDialog = true;
}

  deleteSelectedProducts(): void {
    this.confirmationService.confirm({
        message: 'Are you sure you want to delete the selected products?',
        header: 'Confirm',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
            this.employees = this.employees.filter(val => !this.selectedProducts?.includes(val));
            this.selectedProducts = null;
            this.messageService.add({severity:'success', summary: 'Successful', detail: 'Products Deleted', life: 3000});
        }
    });
  }

  deleteProduct(employee: IEmployee): void {
    this.confirmationService.confirm({
        // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
        message: 'Are you sure you want to delete ' + employee.firstName + '?',
        header: 'Confirm',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
            this.employees = this.employees.filter((val: any) => val.id !== employee.id);
            this.employeeUserId = {};
            this.messageService.add({severity:'success', summary: 'Successful', detail: 'Product Deleted', life: 3000});
        }
    });
  }

  hideDialog(): void {
    this.employeeDialog = false;
    this.submitted = false;
  }

  findIndexById(id: number): number {
    let index = -1;
    for (let i = 0; i < this.employees.length; i++) {
        if (this.employees[i].id === id) {
            index = i;
            break;
        }
    }

    return index;
  }

  loadAll(): void {
    this.isLoading = true;
    this.accountService.getAuthenticationState().subscribe(account => {
      
      if (account) {
        

        this.employeeService.findByUserId(account.id)
        .subscribe({
          next: (res: HttpResponse<IEmployee>) => {
            console.warn(res);
            
            this.paginateEmployee(res.body, res.headers);
          
          },
          error: () => {
            this.isLoading = false;
          },
    
        })
      }
    
    });
  
      
      this.employeeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IEmployee[]>) => {
          this.isLoading = false;
          this.paginateEmployees(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  previousState(): void {
    window.history.back();
  }

  reset(): void {
    this.page = 0;
    this.employees = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
      this.loadAll();
  }

  trackId(index: number, item: IEmployee): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  delete(employee: IEmployee): void {
    const modalRef = this.modalService.open(EmployeeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.employee = employee;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
        this.messageService.add({severity:'success', summary: 'Successful', detail: 'Products Deleted', life: 3000});
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEmployees(data: IEmployee[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.employees.push(d);
      }
    }
  }

  protected paginateEmployee(data: IEmployee | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
        this.employeeUserId = data;
      }
    }
  }

