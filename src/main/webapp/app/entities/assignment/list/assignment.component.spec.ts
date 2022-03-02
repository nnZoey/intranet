import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AssignmentService } from '../service/assignment.service';

import { AssignmentComponent } from './assignment.component';

describe('Assignment Management Component', () => {
  let comp: AssignmentComponent;
  let fixture: ComponentFixture<AssignmentComponent>;
  let service: AssignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AssignmentComponent],
    })
      .overrideTemplate(AssignmentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssignmentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AssignmentService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.assignments?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
