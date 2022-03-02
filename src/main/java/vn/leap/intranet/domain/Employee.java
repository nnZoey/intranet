package vn.leap.intranet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private ZonedDateTime effectiveDate;

    @NotNull
    @Column(name = "employee_code", nullable = false)
    private String employeeCode;

    @NotNull
    @Column(name = "professional_email", nullable = false, unique = true)
    private String professionalEmail;

    @NotNull
    @Column(name = "professional_phone_number", nullable = false, unique = true)
    private String professionalPhoneNumber;

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

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "project", "employee" }, allowSetters = true)
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "supervisor", "job", "employee" }, allowSetters = true)
    private Set<JobHistory> jobHistories = new HashSet<>();

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
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Department department;

    @OneToMany(mappedBy = "supervisor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "assignments", "jobHistories", "supervisor", "job", "department", "teamMembers", "teamMembersInHistories" },
        allowSetters = true
    )
    private Set<Employee> teamMembers = new HashSet<>();

    @OneToMany(mappedBy = "supervisor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "supervisor", "job", "employee" }, allowSetters = true)
    private Set<JobHistory> teamMembersInHistories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getEffectiveDate() {
        return this.effectiveDate;
    }

    public Employee effectiveDate(ZonedDateTime effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public void setEffectiveDate(ZonedDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getEmployeeCode() {
        return this.employeeCode;
    }

    public Employee employeeCode(String employeeCode) {
        this.setEmployeeCode(employeeCode);
        return this;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getProfessionalEmail() {
        return this.professionalEmail;
    }

    public Employee professionalEmail(String professionalEmail) {
        this.setProfessionalEmail(professionalEmail);
        return this;
    }

    public void setProfessionalEmail(String professionalEmail) {
        this.professionalEmail = professionalEmail;
    }

    public String getProfessionalPhoneNumber() {
        return this.professionalPhoneNumber;
    }

    public Employee professionalPhoneNumber(String professionalPhoneNumber) {
        this.setProfessionalPhoneNumber(professionalPhoneNumber);
        return this;
    }

    public void setProfessionalPhoneNumber(String professionalPhoneNumber) {
        this.professionalPhoneNumber = professionalPhoneNumber;
    }

    public Long getSalary() {
        return this.salary;
    }

    public Employee salary(Long salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getCommissionPct() {
        return this.commissionPct;
    }

    public Employee commissionPct(Long commissionPct) {
        this.setCommissionPct(commissionPct);
        return this;
    }

    public void setCommissionPct(Long commissionPct) {
        this.commissionPct = commissionPct;
    }

    public String getContractNumber() {
        return this.contractNumber;
    }

    public Employee contractNumber(String contractNumber) {
        this.setContractNumber(contractNumber);
        return this;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public ZonedDateTime getContractStartDate() {
        return this.contractStartDate;
    }

    public Employee contractStartDate(ZonedDateTime contractStartDate) {
        this.setContractStartDate(contractStartDate);
        return this;
    }

    public void setContractStartDate(ZonedDateTime contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public ZonedDateTime getContractEndDate() {
        return this.contractEndDate;
    }

    public Employee contractEndDate(ZonedDateTime contractEndDate) {
        this.setContractEndDate(contractEndDate);
        return this;
    }

    public void setContractEndDate(ZonedDateTime contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getContractType() {
        return this.contractType;
    }

    public Employee contractType(String contractType) {
        this.setContractType(contractType);
        return this;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        if (this.assignments != null) {
            this.assignments.forEach(i -> i.setEmployee(null));
        }
        if (assignments != null) {
            assignments.forEach(i -> i.setEmployee(this));
        }
        this.assignments = assignments;
    }

    public Employee assignments(Set<Assignment> assignments) {
        this.setAssignments(assignments);
        return this;
    }

    public Employee addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.setEmployee(this);
        return this;
    }

    public Employee removeAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
        assignment.setEmployee(null);
        return this;
    }

    public Set<JobHistory> getJobHistories() {
        return this.jobHistories;
    }

    public void setJobHistories(Set<JobHistory> jobHistories) {
        if (this.jobHistories != null) {
            this.jobHistories.forEach(i -> i.setEmployee(null));
        }
        if (jobHistories != null) {
            jobHistories.forEach(i -> i.setEmployee(this));
        }
        this.jobHistories = jobHistories;
    }

    public Employee jobHistories(Set<JobHistory> jobHistories) {
        this.setJobHistories(jobHistories);
        return this;
    }

    public Employee addJobHistory(JobHistory jobHistory) {
        this.jobHistories.add(jobHistory);
        jobHistory.setEmployee(this);
        return this;
    }

    public Employee removeJobHistory(JobHistory jobHistory) {
        this.jobHistories.remove(jobHistory);
        jobHistory.setEmployee(null);
        return this;
    }

    public Employee getSupervisor() {
        return this.supervisor;
    }

    public void setSupervisor(Employee employee) {
        this.supervisor = employee;
    }

    public Employee supervisor(Employee employee) {
        this.setSupervisor(employee);
        return this;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Employee job(Job job) {
        this.setJob(job);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Set<Employee> getTeamMembers() {
        return this.teamMembers;
    }

    public void setTeamMembers(Set<Employee> employees) {
        if (this.teamMembers != null) {
            this.teamMembers.forEach(i -> i.setSupervisor(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setSupervisor(this));
        }
        this.teamMembers = employees;
    }

    public Employee teamMembers(Set<Employee> employees) {
        this.setTeamMembers(employees);
        return this;
    }

    public Employee addTeamMembers(Employee employee) {
        this.teamMembers.add(employee);
        employee.setSupervisor(this);
        return this;
    }

    public Employee removeTeamMembers(Employee employee) {
        this.teamMembers.remove(employee);
        employee.setSupervisor(null);
        return this;
    }

    public Set<JobHistory> getTeamMembersInHistories() {
        return this.teamMembersInHistories;
    }

    public void setTeamMembersInHistories(Set<JobHistory> jobHistories) {
        if (this.teamMembersInHistories != null) {
            this.teamMembersInHistories.forEach(i -> i.setSupervisor(null));
        }
        if (jobHistories != null) {
            jobHistories.forEach(i -> i.setSupervisor(this));
        }
        this.teamMembersInHistories = jobHistories;
    }

    public Employee teamMembersInHistories(Set<JobHistory> jobHistories) {
        this.setTeamMembersInHistories(jobHistories);
        return this;
    }

    public Employee addTeamMembersInHistory(JobHistory jobHistory) {
        this.teamMembersInHistories.add(jobHistory);
        jobHistory.setSupervisor(this);
        return this;
    }

    public Employee removeTeamMembersInHistory(JobHistory jobHistory) {
        this.teamMembersInHistories.remove(jobHistory);
        jobHistory.setSupervisor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", employeeCode='" + getEmployeeCode() + "'" +
            ", professionalEmail='" + getProfessionalEmail() + "'" +
            ", professionalPhoneNumber='" + getProfessionalPhoneNumber() + "'" +
            ", salary=" + getSalary() +
            ", commissionPct=" + getCommissionPct() +
            ", contractNumber='" + getContractNumber() + "'" +
            ", contractStartDate='" + getContractStartDate() + "'" +
            ", contractEndDate='" + getContractEndDate() + "'" +
            ", contractType='" + getContractType() + "'" +
            "}";
    }
}
