package vn.leap.intranet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.leap.intranet.domain.enumeration.Sex;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

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
    @Column(name = "personal_email", nullable = false, unique = true)
    private String personalEmail;

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

    @JsonIgnoreProperties(
        value = { "assignments", "jobHistories", "supervisor", "job", "department", "teamMembers", "teamMembersInHistories" },
        allowSetters = true
    )
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Person id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Person firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Person lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Sex getSex() {
        return this.sex;
    }

    public Person sex(Sex sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public ZonedDateTime getDob() {
        return this.dob;
    }

    public Person dob(ZonedDateTime dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(ZonedDateTime dob) {
        this.dob = dob;
    }

    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    public Person placeOfBirth(String placeOfBirth) {
        this.setPlaceOfBirth(placeOfBirth);
        return this;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPersonalPhoneNumber() {
        return this.personalPhoneNumber;
    }

    public Person personalPhoneNumber(String personalPhoneNumber) {
        this.setPersonalPhoneNumber(personalPhoneNumber);
        return this;
    }

    public void setPersonalPhoneNumber(String personalPhoneNumber) {
        this.personalPhoneNumber = personalPhoneNumber;
    }

    public String getPersonalEmail() {
        return this.personalEmail;
    }

    public Person personalEmail(String personalEmail) {
        this.setPersonalEmail(personalEmail);
        return this;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getPermanentAddress() {
        return this.permanentAddress;
    }

    public Person permanentAddress(String permanentAddress) {
        this.setPermanentAddress(permanentAddress);
        return this;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getTemporaryAddress() {
        return this.temporaryAddress;
    }

    public Person temporaryAddress(String temporaryAddress) {
        this.setTemporaryAddress(temporaryAddress);
        return this;
    }

    public void setTemporaryAddress(String temporaryAddress) {
        this.temporaryAddress = temporaryAddress;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public Person idNumber(String idNumber) {
        this.setIdNumber(idNumber);
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public ZonedDateTime getIdIssuedDate() {
        return this.idIssuedDate;
    }

    public Person idIssuedDate(ZonedDateTime idIssuedDate) {
        this.setIdIssuedDate(idIssuedDate);
        return this;
    }

    public void setIdIssuedDate(ZonedDateTime idIssuedDate) {
        this.idIssuedDate = idIssuedDate;
    }

    public String getIdIssuedLocation() {
        return this.idIssuedLocation;
    }

    public Person idIssuedLocation(String idIssuedLocation) {
        this.setIdIssuedLocation(idIssuedLocation);
        return this;
    }

    public void setIdIssuedLocation(String idIssuedLocation) {
        this.idIssuedLocation = idIssuedLocation;
    }

    public String getSocialInsuranceNumber() {
        return this.socialInsuranceNumber;
    }

    public Person socialInsuranceNumber(String socialInsuranceNumber) {
        this.setSocialInsuranceNumber(socialInsuranceNumber);
        return this;
    }

    public void setSocialInsuranceNumber(String socialInsuranceNumber) {
        this.socialInsuranceNumber = socialInsuranceNumber;
    }

    public String getTaxIdentificationNumber() {
        return this.taxIdentificationNumber;
    }

    public Person taxIdentificationNumber(String taxIdentificationNumber) {
        this.setTaxIdentificationNumber(taxIdentificationNumber);
        return this;
    }

    public void setTaxIdentificationNumber(String taxIdentificationNumber) {
        this.taxIdentificationNumber = taxIdentificationNumber;
    }

    public String getQualification() {
        return this.qualification;
    }

    public Person qualification(String qualification) {
        this.setQualification(qualification);
        return this;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Person employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", sex='" + getSex() + "'" +
            ", dob='" + getDob() + "'" +
            ", placeOfBirth='" + getPlaceOfBirth() + "'" +
            ", personalPhoneNumber='" + getPersonalPhoneNumber() + "'" +
            ", personalEmail='" + getPersonalEmail() + "'" +
            ", permanentAddress='" + getPermanentAddress() + "'" +
            ", temporaryAddress='" + getTemporaryAddress() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", idIssuedDate='" + getIdIssuedDate() + "'" +
            ", idIssuedLocation='" + getIdIssuedLocation() + "'" +
            ", socialInsuranceNumber='" + getSocialInsuranceNumber() + "'" +
            ", taxIdentificationNumber='" + getTaxIdentificationNumber() + "'" +
            ", qualification='" + getQualification() + "'" +
            "}";
    }
}
