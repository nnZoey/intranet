import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableAssignmentComponent } from './table-assignment.component';

describe('TableAssignmentComponent', () => {
  let component: TableAssignmentComponent;
  let fixture: ComponentFixture<TableAssignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TableAssignmentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TableAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
