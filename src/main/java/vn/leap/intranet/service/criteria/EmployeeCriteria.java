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
import vn.leap.intranet.domain.enumeration.ContractType;
import vn.leap.intranet.domain.enumeration.Sex;

/**
 * Criteria class for the {@link vn.leap.intranet.domain.Employee} entity. This class is used
 * in {@link vn.leap.intranet.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContractType
     */
    public static class ContractTypeFilter extends Filter<ContractType> {

        public ContractTypeFilter() {}

        public ContractTypeFilter(ContractTypeFilter filter) {
            super(filter);
        }

        @Override
        public ContractTypeFilter copy() {
            return new ContractTypeFilter(this);
        }
    }

    /**
     * Class for filtering Sex
     */
    public static class SexFilter extends Filter<Sex> {

        public SexFilter() {}

        public SexFilter(SexFilter filter) {
            super(filter);
        }

        @Override
        public SexFilter copy() {
            return new SexFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter employeeCode;

    private StringFilter firstName;

    private StringFilter lastName;

    private ZonedDateTimeFilter effectiveDate;

    private StringFilter slogan;

    private StringFilter professionalEmail;

    private StringFilter professionalPhoneNumber;

    private LongFilter commissionPct;

    private ZonedDateTimeFilter hiredDate;

    private StringFilter contractNumber;

    private ZonedDateTimeFilter contractStartDate;

    private ZonedDateTimeFilter contractEndDate;

    private ContractTypeFilter contractType;

    private LongFilter salary;

    private SexFilter sex;

    private ZonedDateTimeFilter dob;

    private StringFilter placeOfBirth;

    private StringFilter personalPhoneNumber;

    private StringFilter permanentAddress;

    private StringFilter temporaryAddress;

    private StringFilter idNumber;

    private ZonedDateTimeFilter idIssuedDate;

    private StringFilter idIssuedLocation;

    private StringFilter socialInsuranceNumber;

    private StringFilter taxIdentificationNumber;

    private StringFilter qualification;

    private StringFilter bankAccount;

    private StringFilter bankCode;

    private LongFilter userIdId;

    private LongFilter assignmentEmployeeId;

    private LongFilter supervisorId;

    private LongFilter jobId;

    private LongFilter teamMembersId;

    private Boolean distinct;

    public EmployeeCriteria() {}

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeCode = other.employeeCode == null ? null : other.employeeCode.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.effectiveDate = other.effectiveDate == null ? null : other.effectiveDate.copy();
        this.slogan = other.slogan == null ? null : other.slogan.copy();
        this.professionalEmail = other.professionalEmail == null ? null : other.professionalEmail.copy();
        this.professionalPhoneNumber = other.professionalPhoneNumber == null ? null : other.professionalPhoneNumber.copy();
        this.commissionPct = other.commissionPct == null ? null : other.commissionPct.copy();
        this.hiredDate = other.hiredDate == null ? null : other.hiredDate.copy();
        this.contractNumber = other.contractNumber == null ? null : other.contractNumber.copy();
        this.contractStartDate = other.contractStartDate == null ? null : other.contractStartDate.copy();
        this.contractEndDate = other.contractEndDate == null ? null : other.contractEndDate.copy();
        this.contractType = other.contractType == null ? null : other.contractType.copy();
        this.salary = other.salary == null ? null : other.salary.copy();
        this.sex = other.sex == null ? null : other.sex.copy();
        this.dob = other.dob == null ? null : other.dob.copy();
        this.placeOfBirth = other.placeOfBirth == null ? null : other.placeOfBirth.copy();
        this.personalPhoneNumber = other.personalPhoneNumber == null ? null : other.personalPhoneNumber.copy();
        this.permanentAddress = other.permanentAddress == null ? null : other.permanentAddress.copy();
        this.temporaryAddress = other.temporaryAddress == null ? null : other.temporaryAddress.copy();
        this.idNumber = other.idNumber == null ? null : other.idNumber.copy();
        this.idIssuedDate = other.idIssuedDate == null ? null : other.idIssuedDate.copy();
        this.idIssuedLocation = other.idIssuedLocation == null ? null : other.idIssuedLocation.copy();
        this.socialInsuranceNumber = other.socialInsuranceNumber == null ? null : other.socialInsuranceNumber.copy();
        this.taxIdentificationNumber = other.taxIdentificationNumber == null ? null : other.taxIdentificationNumber.copy();
        this.qualification = other.qualification == null ? null : other.qualification.copy();
        this.bankAccount = other.bankAccount == null ? null : other.bankAccount.copy();
        this.bankCode = other.bankCode == null ? null : other.bankCode.copy();
        this.userIdId = other.userIdId == null ? null : other.userIdId.copy();
        this.assignmentEmployeeId = other.assignmentEmployeeId == null ? null : other.assignmentEmployeeId.copy();
        this.supervisorId = other.supervisorId == null ? null : other.supervisorId.copy();
        this.jobId = other.jobId == null ? null : other.jobId.copy();
        this.teamMembersId = other.teamMembersId == null ? null : other.teamMembersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
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

    public StringFilter getEmployeeCode() {
        return employeeCode;
    }

    public StringFilter employeeCode() {
        if (employeeCode == null) {
            employeeCode = new StringFilter();
        }
        return employeeCode;
    }

    public void setEmployeeCode(StringFilter employeeCode) {
        this.employeeCode = employeeCode;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public ZonedDateTimeFilter getEffectiveDate() {
        return effectiveDate;
    }

    public ZonedDateTimeFilter effectiveDate() {
        if (effectiveDate == null) {
            effectiveDate = new ZonedDateTimeFilter();
        }
        return effectiveDate;
    }

    public void setEffectiveDate(ZonedDateTimeFilter effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public StringFilter getSlogan() {
        return slogan;
    }

    public StringFilter slogan() {
        if (slogan == null) {
            slogan = new StringFilter();
        }
        return slogan;
    }

    public void setSlogan(StringFilter slogan) {
        this.slogan = slogan;
    }

    public StringFilter getProfessionalEmail() {
        return professionalEmail;
    }

    public StringFilter professionalEmail() {
        if (professionalEmail == null) {
            professionalEmail = new StringFilter();
        }
        return professionalEmail;
    }

    public void setProfessionalEmail(StringFilter professionalEmail) {
        this.professionalEmail = professionalEmail;
    }

    public StringFilter getProfessionalPhoneNumber() {
        return professionalPhoneNumber;
    }

    public StringFilter professionalPhoneNumber() {
        if (professionalPhoneNumber == null) {
            professionalPhoneNumber = new StringFilter();
        }
        return professionalPhoneNumber;
    }

    public void setProfessionalPhoneNumber(StringFilter professionalPhoneNumber) {
        this.professionalPhoneNumber = professionalPhoneNumber;
    }

    public LongFilter getCommissionPct() {
        return commissionPct;
    }

    public LongFilter commissionPct() {
        if (commissionPct == null) {
            commissionPct = new LongFilter();
        }
        return commissionPct;
    }

    public void setCommissionPct(LongFilter commissionPct) {
        this.commissionPct = commissionPct;
    }

    public ZonedDateTimeFilter getHiredDate() {
        return hiredDate;
    }

    public ZonedDateTimeFilter hiredDate() {
        if (hiredDate == null) {
            hiredDate = new ZonedDateTimeFilter();
        }
        return hiredDate;
    }

    public void setHiredDate(ZonedDateTimeFilter hiredDate) {
        this.hiredDate = hiredDate;
    }

    public StringFilter getContractNumber() {
        return contractNumber;
    }

    public StringFilter contractNumber() {
        if (contractNumber == null) {
            contractNumber = new StringFilter();
        }
        return contractNumber;
    }

    public void setContractNumber(StringFilter contractNumber) {
        this.contractNumber = contractNumber;
    }

    public ZonedDateTimeFilter getContractStartDate() {
        return contractStartDate;
    }

    public ZonedDateTimeFilter contractStartDate() {
        if (contractStartDate == null) {
            contractStartDate = new ZonedDateTimeFilter();
        }
        return contractStartDate;
    }

    public void setContractStartDate(ZonedDateTimeFilter contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public ZonedDateTimeFilter getContractEndDate() {
        return contractEndDate;
    }

    public ZonedDateTimeFilter contractEndDate() {
        if (contractEndDate == null) {
            contractEndDate = new ZonedDateTimeFilter();
        }
        return contractEndDate;
    }

    public void setContractEndDate(ZonedDateTimeFilter contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public ContractTypeFilter getContractType() {
        return contractType;
    }

    public ContractTypeFilter contractType() {
        if (contractType == null) {
            contractType = new ContractTypeFilter();
        }
        return contractType;
    }

    public void setContractType(ContractTypeFilter contractType) {
        this.contractType = contractType;
    }

    public LongFilter getSalary() {
        return salary;
    }

    public LongFilter salary() {
        if (salary == null) {
            salary = new LongFilter();
        }
        return salary;
    }

    public void setSalary(LongFilter salary) {
        this.salary = salary;
    }

    public SexFilter getSex() {
        return sex;
    }

    public SexFilter sex() {
        if (sex == null) {
            sex = new SexFilter();
        }
        return sex;
    }

    public void setSex(SexFilter sex) {
        this.sex = sex;
    }

    public ZonedDateTimeFilter getDob() {
        return dob;
    }

    public ZonedDateTimeFilter dob() {
        if (dob == null) {
            dob = new ZonedDateTimeFilter();
        }
        return dob;
    }

    public void setDob(ZonedDateTimeFilter dob) {
        this.dob = dob;
    }

    public StringFilter getPlaceOfBirth() {
        return placeOfBirth;
    }

    public StringFilter placeOfBirth() {
        if (placeOfBirth == null) {
            placeOfBirth = new StringFilter();
        }
        return placeOfBirth;
    }

    public void setPlaceOfBirth(StringFilter placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public StringFilter getPersonalPhoneNumber() {
        return personalPhoneNumber;
    }

    public StringFilter personalPhoneNumber() {
        if (personalPhoneNumber == null) {
            personalPhoneNumber = new StringFilter();
        }
        return personalPhoneNumber;
    }

    public void setPersonalPhoneNumber(StringFilter personalPhoneNumber) {
        this.personalPhoneNumber = personalPhoneNumber;
    }

    public StringFilter getPermanentAddress() {
        return permanentAddress;
    }

    public StringFilter permanentAddress() {
        if (permanentAddress == null) {
            permanentAddress = new StringFilter();
        }
        return permanentAddress;
    }

    public void setPermanentAddress(StringFilter permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public StringFilter getTemporaryAddress() {
        return temporaryAddress;
    }

    public StringFilter temporaryAddress() {
        if (temporaryAddress == null) {
            temporaryAddress = new StringFilter();
        }
        return temporaryAddress;
    }

    public void setTemporaryAddress(StringFilter temporaryAddress) {
        this.temporaryAddress = temporaryAddress;
    }

    public StringFilter getIdNumber() {
        return idNumber;
    }

    public StringFilter idNumber() {
        if (idNumber == null) {
            idNumber = new StringFilter();
        }
        return idNumber;
    }

    public void setIdNumber(StringFilter idNumber) {
        this.idNumber = idNumber;
    }

    public ZonedDateTimeFilter getIdIssuedDate() {
        return idIssuedDate;
    }

    public ZonedDateTimeFilter idIssuedDate() {
        if (idIssuedDate == null) {
            idIssuedDate = new ZonedDateTimeFilter();
        }
        return idIssuedDate;
    }

    public void setIdIssuedDate(ZonedDateTimeFilter idIssuedDate) {
        this.idIssuedDate = idIssuedDate;
    }

    public StringFilter getIdIssuedLocation() {
        return idIssuedLocation;
    }

    public StringFilter idIssuedLocation() {
        if (idIssuedLocation == null) {
            idIssuedLocation = new StringFilter();
        }
        return idIssuedLocation;
    }

    public void setIdIssuedLocation(StringFilter idIssuedLocation) {
        this.idIssuedLocation = idIssuedLocation;
    }

    public StringFilter getSocialInsuranceNumber() {
        return socialInsuranceNumber;
    }

    public StringFilter socialInsuranceNumber() {
        if (socialInsuranceNumber == null) {
            socialInsuranceNumber = new StringFilter();
        }
        return socialInsuranceNumber;
    }

    public void setSocialInsuranceNumber(StringFilter socialInsuranceNumber) {
        this.socialInsuranceNumber = socialInsuranceNumber;
    }

    public StringFilter getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }

    public StringFilter taxIdentificationNumber() {
        if (taxIdentificationNumber == null) {
            taxIdentificationNumber = new StringFilter();
        }
        return taxIdentificationNumber;
    }

    public void setTaxIdentificationNumber(StringFilter taxIdentificationNumber) {
        this.taxIdentificationNumber = taxIdentificationNumber;
    }

    public StringFilter getQualification() {
        return qualification;
    }

    public StringFilter qualification() {
        if (qualification == null) {
            qualification = new StringFilter();
        }
        return qualification;
    }

    public void setQualification(StringFilter qualification) {
        this.qualification = qualification;
    }

    public StringFilter getBankAccount() {
        return bankAccount;
    }

    public StringFilter bankAccount() {
        if (bankAccount == null) {
            bankAccount = new StringFilter();
        }
        return bankAccount;
    }

    public void setBankAccount(StringFilter bankAccount) {
        this.bankAccount = bankAccount;
    }

    public StringFilter getBankCode() {
        return bankCode;
    }

    public StringFilter bankCode() {
        if (bankCode == null) {
            bankCode = new StringFilter();
        }
        return bankCode;
    }

    public void setBankCode(StringFilter bankCode) {
        this.bankCode = bankCode;
    }

    public LongFilter getUserIdId() {
        return userIdId;
    }

    public LongFilter userIdId() {
        if (userIdId == null) {
            userIdId = new LongFilter();
        }
        return userIdId;
    }

    public void setUserIdId(LongFilter userIdId) {
        this.userIdId = userIdId;
    }

    public LongFilter getAssignmentEmployeeId() {
        return assignmentEmployeeId;
    }

    public LongFilter assignmentEmployeeId() {
        if (assignmentEmployeeId == null) {
            assignmentEmployeeId = new LongFilter();
        }
        return assignmentEmployeeId;
    }

    public void setAssignmentEmployeeId(LongFilter assignmentEmployeeId) {
        this.assignmentEmployeeId = assignmentEmployeeId;
    }

    public LongFilter getSupervisorId() {
        return supervisorId;
    }

    public LongFilter supervisorId() {
        if (supervisorId == null) {
            supervisorId = new LongFilter();
        }
        return supervisorId;
    }

    public void setSupervisorId(LongFilter supervisorId) {
        this.supervisorId = supervisorId;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public LongFilter jobId() {
        if (jobId == null) {
            jobId = new LongFilter();
        }
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
    }

    public LongFilter getTeamMembersId() {
        return teamMembersId;
    }

    public LongFilter teamMembersId() {
        if (teamMembersId == null) {
            teamMembersId = new LongFilter();
        }
        return teamMembersId;
    }

    public void setTeamMembersId(LongFilter teamMembersId) {
        this.teamMembersId = teamMembersId;
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
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeCode, that.employeeCode) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(effectiveDate, that.effectiveDate) &&
            Objects.equals(slogan, that.slogan) &&
            Objects.equals(professionalEmail, that.professionalEmail) &&
            Objects.equals(professionalPhoneNumber, that.professionalPhoneNumber) &&
            Objects.equals(commissionPct, that.commissionPct) &&
            Objects.equals(hiredDate, that.hiredDate) &&
            Objects.equals(contractNumber, that.contractNumber) &&
            Objects.equals(contractStartDate, that.contractStartDate) &&
            Objects.equals(contractEndDate, that.contractEndDate) &&
            Objects.equals(contractType, that.contractType) &&
            Objects.equals(salary, that.salary) &&
            Objects.equals(sex, that.sex) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(placeOfBirth, that.placeOfBirth) &&
            Objects.equals(personalPhoneNumber, that.personalPhoneNumber) &&
            Objects.equals(permanentAddress, that.permanentAddress) &&
            Objects.equals(temporaryAddress, that.temporaryAddress) &&
            Objects.equals(idNumber, that.idNumber) &&
            Objects.equals(idIssuedDate, that.idIssuedDate) &&
            Objects.equals(idIssuedLocation, that.idIssuedLocation) &&
            Objects.equals(socialInsuranceNumber, that.socialInsuranceNumber) &&
            Objects.equals(taxIdentificationNumber, that.taxIdentificationNumber) &&
            Objects.equals(qualification, that.qualification) &&
            Objects.equals(bankAccount, that.bankAccount) &&
            Objects.equals(bankCode, that.bankCode) &&
            Objects.equals(userIdId, that.userIdId) &&
            Objects.equals(assignmentEmployeeId, that.assignmentEmployeeId) &&
            Objects.equals(supervisorId, that.supervisorId) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(teamMembersId, that.teamMembersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            employeeCode,
            firstName,
            lastName,
            effectiveDate,
            slogan,
            professionalEmail,
            professionalPhoneNumber,
            commissionPct,
            hiredDate,
            contractNumber,
            contractStartDate,
            contractEndDate,
            contractType,
            salary,
            sex,
            dob,
            placeOfBirth,
            personalPhoneNumber,
            permanentAddress,
            temporaryAddress,
            idNumber,
            idIssuedDate,
            idIssuedLocation,
            socialInsuranceNumber,
            taxIdentificationNumber,
            qualification,
            bankAccount,
            bankCode,
            userIdId,
            assignmentEmployeeId,
            supervisorId,
            jobId,
            teamMembersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeCode != null ? "employeeCode=" + employeeCode + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (effectiveDate != null ? "effectiveDate=" + effectiveDate + ", " : "") +
            (slogan != null ? "slogan=" + slogan + ", " : "") +
            (professionalEmail != null ? "professionalEmail=" + professionalEmail + ", " : "") +
            (professionalPhoneNumber != null ? "professionalPhoneNumber=" + professionalPhoneNumber + ", " : "") +
            (commissionPct != null ? "commissionPct=" + commissionPct + ", " : "") +
            (hiredDate != null ? "hiredDate=" + hiredDate + ", " : "") +
            (contractNumber != null ? "contractNumber=" + contractNumber + ", " : "") +
            (contractStartDate != null ? "contractStartDate=" + contractStartDate + ", " : "") +
            (contractEndDate != null ? "contractEndDate=" + contractEndDate + ", " : "") +
            (contractType != null ? "contractType=" + contractType + ", " : "") +
            (salary != null ? "salary=" + salary + ", " : "") +
            (sex != null ? "sex=" + sex + ", " : "") +
            (dob != null ? "dob=" + dob + ", " : "") +
            (placeOfBirth != null ? "placeOfBirth=" + placeOfBirth + ", " : "") +
            (personalPhoneNumber != null ? "personalPhoneNumber=" + personalPhoneNumber + ", " : "") +
            (permanentAddress != null ? "permanentAddress=" + permanentAddress + ", " : "") +
            (temporaryAddress != null ? "temporaryAddress=" + temporaryAddress + ", " : "") +
            (idNumber != null ? "idNumber=" + idNumber + ", " : "") +
            (idIssuedDate != null ? "idIssuedDate=" + idIssuedDate + ", " : "") +
            (idIssuedLocation != null ? "idIssuedLocation=" + idIssuedLocation + ", " : "") +
            (socialInsuranceNumber != null ? "socialInsuranceNumber=" + socialInsuranceNumber + ", " : "") +
            (taxIdentificationNumber != null ? "taxIdentificationNumber=" + taxIdentificationNumber + ", " : "") +
            (qualification != null ? "qualification=" + qualification + ", " : "") +
            (bankAccount != null ? "bankAccount=" + bankAccount + ", " : "") +
            (bankCode != null ? "bankCode=" + bankCode + ", " : "") +
            (userIdId != null ? "userIdId=" + userIdId + ", " : "") +
            (assignmentEmployeeId != null ? "assignmentEmployeeId=" + assignmentEmployeeId + ", " : "") +
            (supervisorId != null ? "supervisorId=" + supervisorId + ", " : "") +
            (jobId != null ? "jobId=" + jobId + ", " : "") +
            (teamMembersId != null ? "teamMembersId=" + teamMembersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
