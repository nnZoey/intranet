import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableBankPersonComponent } from './table-bank-person.component';

describe('TableBankPersonComponent', () => {
  let component: TableBankPersonComponent;
  let fixture: ComponentFixture<TableBankPersonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TableBankPersonComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TableBankPersonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
