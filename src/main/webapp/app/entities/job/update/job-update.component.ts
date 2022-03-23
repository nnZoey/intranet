import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IJob, Job } from '../job.model';
import { JobService } from '../service/job.service';

@Component({
  selector: 'leap-job-update',
  templateUrl: './job-update.component.html',
})
export class JobUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    jobTitle: [null, [Validators.required]],
    minSalary: [null, [Validators.required, Validators.min(1000000)]],
    maxSalary: [null, [Validators.required, Validators.min(1000000)]],
  });

  constructor(protected jobService: JobService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ job }) => {
      this.updateForm(job);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const job = this.createFromForm();
    if (job.id !== undefined) {
      this.subscribeToSaveResponse(this.jobService.update(job));
    } else {
      this.subscribeToSaveResponse(this.jobService.create(job));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJob>>): void {
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

  protected updateForm(job: IJob): void {
    this.editForm.patchValue({
      id: job.id,
      jobTitle: job.jobTitle,
      minSalary: job.minSalary,
      maxSalary: job.maxSalary,
    });
  }

  protected createFromForm(): IJob {
    return {
      ...new Job(),
      id: this.editForm.get(['id'])!.value,
      jobTitle: this.editForm.get(['jobTitle'])!.value,
      minSalary: this.editForm.get(['minSalary'])!.value,
      maxSalary: this.editForm.get(['maxSalary'])!.value,
    };
  }
}
