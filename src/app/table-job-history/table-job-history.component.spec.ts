import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableJobHistoryComponent } from './table-job-history.component';

describe('TableJobHistoryComponent', () => {
  let component: TableJobHistoryComponent;
  let fixture: ComponentFixture<TableJobHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TableJobHistoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TableJobHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
