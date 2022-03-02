import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAssignment, Assignment } from '../assignment.model';
import { AssignmentService } from '../service/assignment.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { ProjectRole } from 'app/entities/enumerations/project-role.model';

@Component({
  selector: 'jhi-assignment-update',
  templateUrl: './assignment-update.component.html',
})
export class AssignmentUpdateComponent implements OnInit {
  isSaving = false;
  projectRoleValues = Object.keys(ProjectRole);

  projectsSharedCollection: IProject[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    startDate: [null, [Validators.required]],
    projectRole: [null, [Validators.required]],
    project: [null, Validators.required],
    employee: [null, Validators.required],
  });

  constructor(
    protected assignmentService: AssignmentService,
    protected projectService: ProjectService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assignment }) => {
      if (assignment.id === undefined) {
        const today = dayjs().startOf('day');
        assignment.startDate = today;
      }

      this.updateForm(assignment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assignment = this.createFromForm();
    if (assignment.id !== undefined) {
      this.subscribeToSaveResponse(this.assignmentService.update(assignment));
    } else {
      this.subscribeToSaveResponse(this.assignmentService.create(assignment));
    }
  }

  trackProjectById(index: number, item: IProject): number {
    return item.id!;
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssignment>>): void {
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

  protected updateForm(assignment: IAssignment): void {
    this.editForm.patchValue({
      id: assignment.id,
      startDate: assignment.startDate ? assignment.startDate.format(DATE_TIME_FORMAT) : null,
      projectRole: assignment.projectRole,
      project: assignment.project,
      employee: assignment.employee,
    });

    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing(this.projectsSharedCollection, assignment.project);
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      assignment.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing(projects, this.editForm.get('project')!.value))
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employee')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  protected createFromForm(): IAssignment {
    return {
      ...new Assignment(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      projectRole: this.editForm.get(['projectRole'])!.value,
      project: this.editForm.get(['project'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
