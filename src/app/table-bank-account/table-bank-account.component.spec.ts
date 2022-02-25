import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableBankAccountComponent } from './table-bank-account.component';

describe('TableBankAccountComponent', () => {
  let component: TableBankAccountComponent;
  let fixture: ComponentFixture<TableBankAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TableBankAccountComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TableBankAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
