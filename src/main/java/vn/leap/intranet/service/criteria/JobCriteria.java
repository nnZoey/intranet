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

/**
 * Criteria class for the {@link vn.leap.intranet.domain.Job} entity. This class is used
 * in {@link vn.leap.intranet.web.rest.JobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter jobTitle;

    private LongFilter minSalary;

    private LongFilter maxSalary;

    private Boolean distinct;

    public JobCriteria() {}

    public JobCriteria(JobCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.jobTitle = other.jobTitle == null ? null : other.jobTitle.copy();
        this.minSalary = other.minSalary == null ? null : other.minSalary.copy();
        this.maxSalary = other.maxSalary == null ? null : other.maxSalary.copy();
        this.distinct = other.distinct;
    }

    @Override
    public JobCriteria copy() {
        return new JobCriteria(this);
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

    public StringFilter getJobTitle() {
        return jobTitle;
    }

    public StringFilter jobTitle() {
        if (jobTitle == null) {
            jobTitle = new StringFilter();
        }
        return jobTitle;
    }

    public void setJobTitle(StringFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LongFilter getMinSalary() {
        return minSalary;
    }

    public LongFilter minSalary() {
        if (minSalary == null) {
            minSalary = new LongFilter();
        }
        return minSalary;
    }

    public void setMinSalary(LongFilter minSalary) {
        this.minSalary = minSalary;
    }

    public LongFilter getMaxSalary() {
        return maxSalary;
    }

    public LongFilter maxSalary() {
        if (maxSalary == null) {
            maxSalary = new LongFilter();
        }
        return maxSalary;
    }

    public void setMaxSalary(LongFilter maxSalary) {
        this.maxSalary = maxSalary;
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
        final JobCriteria that = (JobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(minSalary, that.minSalary) &&
            Objects.equals(maxSalary, that.maxSalary) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobTitle, minSalary, maxSalary, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (jobTitle != null ? "jobTitle=" + jobTitle + ", " : "") +
            (minSalary != null ? "minSalary=" + minSalary + ", " : "") +
            (maxSalary != null ? "maxSalary=" + maxSalary + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
