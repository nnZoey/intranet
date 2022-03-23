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
import vn.leap.intranet.domain.enumeration.ContractType;
import vn.leap.intranet.domain.enumeration.Sex;

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
    @Column(name = "employee_code", nullable = false)
    private String employeeCode;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private ZonedDateTime effectiveDate;

    @Column(name = "slogan")
    private String slogan;

    @NotNull
    @Column(name = "professional_email", nullable = false, unique = true)
    private String professionalEmail;

    @NotNull
    @Column(name = "professional_phone_number", nullable = false, unique = true)
    private String professionalPhoneNumber;

    @NotNull
    @Min(value = 1L)
    @Max(value = 100L)
    @Column(name = "commission_pct", nullable = false)
    private Long commissionPct;

    @NotNull
    @Column(name = "hired_date", nullable = false)
    private ZonedDateTime hiredDate;

    @NotNull
    @Column(name = "contract_number", nullable = false, unique = true)
    private String contractNumber;

    @NotNull
    @Column(name = "contract_start_date", nullable = false)
    private ZonedDateTime contractStartDate;

    @Column(name = "contract_end_date")
    private ZonedDateTime contractEndDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false)
    private ContractType contractType;

    @Lob
    @Column(name = "contract_file")
    private byte[] contractFile;

    @Column(name = "contract_file_content_type")
    private String contractFileContentType;

    @NotNull
    @Min(value = 1000000L)
    @Column(name = "salary", nullable = false)
    private Long salary;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;

    @NotNull
    @Column(name = "dob", nullable = false)
    private ZonedDateTime dob;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @NotNull
    @Column(name = "personal_phone_number", nullable = false, unique = true)
    private String personalPhoneNumber;

    @NotNull
    @Column(name = "permanent_address", nullable = false)
    private String permanentAddress;

    @NotNull
    @Column(name = "temporary_address", nullable = false)
    private String temporaryAddress;

    @NotNull
    @Column(name = "id_number", nullable = false)
    private String idNumber;

    @NotNull
    @Column(name = "id_issued_date", nullable = false)
    private ZonedDateTime idIssuedDate;

    @NotNull
    @Column(name = "id_issued_location", nullable = false)
    private String idIssuedLocation;

    @Column(name = "social_insurance_number")
    private String socialInsuranceNumber;

    @Column(name = "tax_identification_number")
    private String taxIdentificationNumber;

    @Column(name = "qualification")
    private String qualification;

    @NotNull
    @Column(name = "bank_account", nullable = false)
    private String bankAccount;

    @NotNull
    @Column(name = "bank_code", nullable = false)
    private String bankCode;

    @OneToOne
    @JoinColumn(unique = true)
    private User userId;

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "project", "employee" }, allowSetters = true)
    private Set<Assignment> assignments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "userId", "assignments", "supervisor", "job", "teamMembers" }, allowSetters = true)
    private Employee supervisor;

    @ManyToOne
    private Job job;

    @OneToMany(mappedBy = "supervisor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userId", "assignments", "supervisor", "job", "teamMembers" }, allowSetters = true)
    private Set<Employee> teamMembers = new HashSet<>();

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

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Employee image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Employee imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
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

    public String getSlogan() {
        return this.slogan;
    }

    public Employee slogan(String slogan) {
        this.setSlogan(slogan);
        return this;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
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

    public ZonedDateTime getHiredDate() {
        return this.hiredDate;
    }

    public Employee hiredDate(ZonedDateTime hiredDate) {
        this.setHiredDate(hiredDate);
        return this;
    }

    public void setHiredDate(ZonedDateTime hiredDate) {
        this.hiredDate = hiredDate;
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

    public ContractType getContractType() {
        return this.contractType;
    }

    public Employee contractType(ContractType contractType) {
        this.setContractType(contractType);
        return this;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public byte[] getContractFile() {
        return this.contractFile;
    }

    public Employee contractFile(byte[] contractFile) {
        this.setContractFile(contractFile);
        return this;
    }

    public void setContractFile(byte[] contractFile) {
        this.contractFile = contractFile;
    }

    public String getContractFileContentType() {
        return this.contractFileContentType;
    }

    public Employee contractFileContentType(String contractFileContentType) {
        this.contractFileContentType = contractFileContentType;
        return this;
    }

    public void setContractFileContentType(String contractFileContentType) {
        this.contractFileContentType = contractFileContentType;
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

    public Sex getSex() {
        return this.sex;
    }

    public Employee sex(Sex sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public ZonedDateTime getDob() {
        return this.dob;
    }

    public Employee dob(ZonedDateTime dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(ZonedDateTime dob) {
        this.dob = dob;
    }

    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    public Employee placeOfBirth(String placeOfBirth) {
        this.setPlaceOfBirth(placeOfBirth);
        return this;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPersonalPhoneNumber() {
        return this.personalPhoneNumber;
    }

    public Employee personalPhoneNumber(String personalPhoneNumber) {
        this.setPersonalPhoneNumber(personalPhoneNumber);
        return this;
    }

    public void setPersonalPhoneNumber(String personalPhoneNumber) {
        this.personalPhoneNumber = personalPhoneNumber;
    }

    public String getPermanentAddress() {
        return this.permanentAddress;
    }

    public Employee permanentAddress(String permanentAddress) {
        this.setPermanentAddress(permanentAddress);
        return this;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getTemporaryAddress() {
        return this.temporaryAddress;
    }

    public Employee temporaryAddress(String temporaryAddress) {
        this.setTemporaryAddress(temporaryAddress);
        return this;
    }

    public void setTemporaryAddress(String temporaryAddress) {
        this.temporaryAddress = temporaryAddress;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public Employee idNumber(String idNumber) {
        this.setIdNumber(idNumber);
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public ZonedDateTime getIdIssuedDate() {
        return this.idIssuedDate;
    }

    public Employee idIssuedDate(ZonedDateTime idIssuedDate) {
        this.setIdIssuedDate(idIssuedDate);
        return this;
    }

    public void setIdIssuedDate(ZonedDateTime idIssuedDate) {
        this.idIssuedDate = idIssuedDate;
    }

    public String getIdIssuedLocation() {
        return this.idIssuedLocation;
    }

    public Employee idIssuedLocation(String idIssuedLocation) {
        this.setIdIssuedLocation(idIssuedLocation);
        return this;
    }

    public void setIdIssuedLocation(String idIssuedLocation) {
        this.idIssuedLocation = idIssuedLocation;
    }

    public String getSocialInsuranceNumber() {
        return this.socialInsuranceNumber;
    }

    public Employee socialInsuranceNumber(String socialInsuranceNumber) {
        this.setSocialInsuranceNumber(socialInsuranceNumber);
        return this;
    }

    public void setSocialInsuranceNumber(String socialInsuranceNumber) {
        this.socialInsuranceNumber = socialInsuranceNumber;
    }

    public String getTaxIdentificationNumber() {
        return this.taxIdentificationNumber;
    }

    public Employee taxIdentificationNumber(String taxIdentificationNumber) {
        this.setTaxIdentificationNumber(taxIdentificationNumber);
        return this;
    }

    public void setTaxIdentificationNumber(String taxIdentificationNumber) {
        this.taxIdentificationNumber = taxIdentificationNumber;
    }

    public String getQualification() {
        return this.qualification;
    }

    public Employee qualification(String qualification) {
        this.setQualification(qualification);
        return this;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getBankAccount() {
        return this.bankAccount;
    }

    public Employee bankAccount(String bankAccount) {
        this.setBankAccount(bankAccount);
        return this;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public Employee bankCode(String bankCode) {
        this.setBankCode(bankCode);
        return this;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public User getUserId() {
        return this.userId;
    }

    public void setUserId(User user) {
        this.userId = user;
    }

    public Employee userId(User user) {
        this.setUserId(user);
        return this;
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
            ", employeeCode='" + getEmployeeCode() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", slogan='" + getSlogan() + "'" +
            ", professionalEmail='" + getProfessionalEmail() + "'" +
            ", professionalPhoneNumber='" + getProfessionalPhoneNumber() + "'" +
            ", commissionPct=" + getCommissionPct() +
            ", hiredDate='" + getHiredDate() + "'" +
            ", contractNumber='" + getContractNumber() + "'" +
            ", contractStartDate='" + getContractStartDate() + "'" +
            ", contractEndDate='" + getContractEndDate() + "'" +
            ", contractType='" + getContractType() + "'" +
            ", contractFile='" + getContractFile() + "'" +
            ", contractFileContentType='" + getContractFileContentType() + "'" +
            ", salary=" + getSalary() +
            ", sex='" + getSex() + "'" +
            ", dob='" + getDob() + "'" +
            ", placeOfBirth='" + getPlaceOfBirth() + "'" +
            ", personalPhoneNumber='" + getPersonalPhoneNumber() + "'" +
            ", permanentAddress='" + getPermanentAddress() + "'" +
            ", temporaryAddress='" + getTemporaryAddress() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", idIssuedDate='" + getIdIssuedDate() + "'" +
            ", idIssuedLocation='" + getIdIssuedLocation() + "'" +
            ", socialInsuranceNumber='" + getSocialInsuranceNumber() + "'" +
            ", taxIdentificationNumber='" + getTaxIdentificationNumber() + "'" +
            ", qualification='" + getQualification() + "'" +
            ", bankAccount='" + getBankAccount() + "'" +
            ", bankCode='" + getBankCode() + "'" +
            "}";
    }
}
