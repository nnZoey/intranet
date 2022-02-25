import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableJobComponent } from './table-job.component';

describe('TableJobComponent', () => {
  let component: TableJobComponent;
  let fixture: ComponentFixture<TableJobComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TableJobComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TableJobComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
