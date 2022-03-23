import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssignment, getAssignmentIdentifier } from '../assignment.model';

export type EntityResponseType = HttpResponse<IAssignment>;
export type EntityArrayResponseType = HttpResponse<IAssignment[]>;

@Injectable({ providedIn: 'root' })
export class AssignmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/assignments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assignment: IAssignment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assignment);
    return this.http
      .post<IAssignment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assignment: IAssignment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assignment);
    return this.http
      .put<IAssignment>(`${this.resourceUrl}/${getAssignmentIdentifier(assignment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assignment: IAssignment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assignment);
    return this.http
      .patch<IAssignment>(`${this.resourceUrl}/${getAssignmentIdentifier(assignment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssignment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssignment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssignmentToCollectionIfMissing(
    assignmentCollection: IAssignment[],
    ...assignmentsToCheck: (IAssignment | null | undefined)[]
  ): IAssignment[] {
    const assignments: IAssignment[] = assignmentsToCheck.filter(isPresent);
    if (assignments.length > 0) {
      const assignmentCollectionIdentifiers = assignmentCollection.map(assignmentItem => getAssignmentIdentifier(assignmentItem)!);
      const assignmentsToAdd = assignments.filter(assignmentItem => {
        const assignmentIdentifier = getAssignmentIdentifier(assignmentItem);
        if (assignmentIdentifier == null || assignmentCollectionIdentifiers.includes(assignmentIdentifier)) {
          return false;
        }
        assignmentCollectionIdentifiers.push(assignmentIdentifier);
        return true;
      });
      return [...assignmentsToAdd, ...assignmentCollection];
    }
    return assignmentCollection;
  }

  protected convertDateFromClient(assignment: IAssignment): IAssignment {
    return Object.assign({}, assignment, {
      startDate: assignment.startDate?.isValid() ? assignment.startDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assignment: IAssignment) => {
        assignment.startDate = assignment.startDate ? dayjs(assignment.startDate) : undefined;
      });
    }
    return res;
  }
}
