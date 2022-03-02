package vn.leap.intranet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A JobHistory.
 */
@Entity
@Table(name = "job_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JobHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "employee_code", nullable = false)
    private String employeeCode;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private ZonedDateTime effectiveDate;

    @NotNull
    @Min(value = 1000000L)
    @Column(name = "salary", nullable = false)
    private Long salary;

    @NotNull
    @Min(value = 1L)
    @Max(value = 100L)
    @Column(name = "commission_pct", nullable = false)
    private Long commissionPct;

    @NotNull
    @Column(name = "contract_number", nullable = false, unique = true)
    private String contractNumber;

    @NotNull
    @Column(name = "contract_start_date", nullable = false)
    private ZonedDateTime contractStartDate;

    @NotNull
    @Column(name = "contract_end_date", nullable = false)
    private ZonedDateTime contractEndDate;

    @Column(name = "contract_type")
    private String contractType;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "assignments", "jobHistories", "supervisor", "job", "department", "teamMembers", "teamMembersInHistories" },
        allowSetters = true
    )
    private Employee supervisor;

    @ManyToOne
    private Job job;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "assignments", "jobHistories", "supervisor", "job", "department", "teamMembers", "teamMembersInHistories" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JobHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return this.employeeCode;
    }

    public JobHistory employeeCode(String employeeCode) {
        this.setEmployeeCode(employeeCode);
        return this;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public ZonedDateTime getEffectiveDate() {
        return this.effectiveDate;
    }

    public JobHistory effectiveDate(ZonedDateTime effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public void setEffectiveDate(ZonedDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Long getSalary() {
        return this.salary;
    }

    public JobHistory salary(Long salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getCommissionPct() {
        return this.commissionPct;
    }

    public JobHistory commissionPct(Long commissionPct) {
        this.setCommissionPct(commissionPct);
        return this;
    }

    public void setCommissionPct(Long commissionPct) {
        this.commissionPct = commissionPct;
    }

    public String getContractNumber() {
        return this.contractNumber;
    }

    public JobHistory contractNumber(String contractNumber) {
        this.setContractNumber(contractNumber);
        return this;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public ZonedDateTime getContractStartDate() {
        return this.contractStartDate;
    }

    public JobHistory contractStartDate(ZonedDateTime contractStartDate) {
        this.setContractStartDate(contractStartDate);
        return this;
    }

    public void setContractStartDate(ZonedDateTime contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public ZonedDateTime getContractEndDate() {
        return this.contractEndDate;
    }

    public JobHistory contractEndDate(ZonedDateTime contractEndDate) {
        this.setContractEndDate(contractEndDate);
        return this;
    }

    public void setContractEndDate(ZonedDateTime contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getContractType() {
        return this.contractType;
    }

    public JobHistory contractType(String contractType) {
        this.setContractType(contractType);
        return this;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Employee getSupervisor() {
        return this.supervisor;
    }

    public void setSupervisor(Employee employee) {
        this.supervisor = employee;
    }

    public JobHistory supervisor(Employee employee) {
        this.setSupervisor(employee);
        return this;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public JobHistory job(Job job) {
        this.setJob(job);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public JobHistory employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobHistory)) {
            return false;
        }
        return id != null && id.equals(((JobHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobHistory{" +
            "id=" + getId() +
            ", employeeCode='" + getEmployeeCode() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", salary=" + getSalary() +
            ", commissionPct=" + getCommissionPct() +
            ", contractNumber='" + getContractNumber() + "'" +
            ", contractStartDate='" + getContractStartDate() + "'" +
            ", contractEndDate='" + getContractEndDate() + "'" +
            ", contractType='" + getContractType() + "'" +
            "}";
    }
}
