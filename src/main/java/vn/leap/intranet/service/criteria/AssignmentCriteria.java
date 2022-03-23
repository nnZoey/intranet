package vn.leap.intranet.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;
import vn.leap.intranet.domain.enumeration.ProjectRole;

/**
 * Criteria class for the {@link vn.leap.intranet.domain.Assignment} entity. This class is used
 * in {@link vn.leap.intranet.web.rest.AssignmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assignments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssignmentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProjectRole
     */
    public static class ProjectRoleFilter extends Filter<ProjectRole> {

        public ProjectRoleFilter() {}

        public ProjectRoleFilter(ProjectRoleFilter filter) {
            super(filter);
        }

        @Override
        public ProjectRoleFilter copy() {
            return new ProjectRoleFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter startDate;

    private ProjectRoleFilter projectRole;

    private LongFilter projectId;

    private LongFilter employeeId;

    private Boolean distinct;

    public AssignmentCriteria() {}

    public AssignmentCriteria(AssignmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.projectRole = other.projectRole == null ? null : other.projectRole.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssignmentCriteria copy() {
        return new AssignmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getStartDate() {
        return startDate;
    }

    public ZonedDateTimeFilter startDate() {
        if (startDate == null) {
            startDate = new ZonedDateTimeFilter();
        }
        return startDate;
    }

    public void setStartDate(ZonedDateTimeFilter startDate) {
        this.startDate = startDate;
    }

    public ProjectRoleFilter getProjectRole() {
        return projectRole;
    }

    public ProjectRoleFilter projectRole() {
        if (projectRole == null) {
            projectRole = new ProjectRoleFilter();
        }
        return projectRole;
    }

    public void setProjectRole(ProjectRoleFilter projectRole) {
        this.projectRole = projectRole;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public LongFilter projectId() {
        if (projectId == null) {
            projectId = new LongFilter();
        }
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            employeeId = new LongFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssignmentCriteria that = (AssignmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(projectRole, that.projectRole) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, projectRole, projectId, employeeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (projectRole != null ? "projectRole=" + projectRole + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
