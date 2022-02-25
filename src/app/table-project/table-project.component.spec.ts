import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableProjectComponent } from './table-project.component';

describe('TableProjectComponent', () => {
  let component: TableProjectComponent;
  let fixture: ComponentFixture<TableProjectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TableProjectComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TableProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
