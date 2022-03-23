import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssignment } from '../assignment.model';
import { AssignmentService } from '../service/assignment.service';
import { AssignmentDeleteDialogComponent } from '../delete/assignment-delete-dialog.component';

@Component({
  selector: 'leap-assignment',
  templateUrl: './assignment.component.html',
})
export class AssignmentComponent implements OnInit {
  assignments?: IAssignment[];
  isLoading = false;

  constructor(protected assignmentService: AssignmentService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.assignmentService.query().subscribe({
      next: (res: HttpResponse<IAssignment[]>) => {
        this.isLoading = false;
        this.assignments = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAssignment): number {
    return item.id!;
  }

  delete(assignment: IAssignment): void {
    const modalRef = this.modalService.open(AssignmentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.assignment = assignment;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
