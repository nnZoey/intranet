package vn.leap.intranet.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import vn.leap.intranet.domain.*; // for static metamodels
import vn.leap.intranet.domain.Employee;
import vn.leap.intranet.repository.EmployeeRepository;
import vn.leap.intranet.service.criteria.EmployeeCriteria;

/**
 * Service for executing complex queries for {@link Employee} entities in the database.
 * The main input is a {@link EmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Employee} or a {@link Page} of {@link Employee} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeQueryService extends QueryService<Employee> {

    private final Logger log = LoggerFactory.getLogger(EmployeeQueryService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeQueryService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Return a {@link List} of {@link Employee} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Employee> findByCriteria(EmployeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Employee} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Employee> findByCriteria(EmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Employee> createSpecification(EmployeeCriteria criteria) {
        Specification<Employee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Employee_.id));
            }
            if (criteria.getEmployeeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmployeeCode(), Employee_.employeeCode));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Employee_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Employee_.lastName));
            }
            if (criteria.getEffectiveDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEffectiveDate(), Employee_.effectiveDate));
            }
            if (criteria.getSlogan() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlogan(), Employee_.slogan));
            }
            if (criteria.getProfessionalEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfessionalEmail(), Employee_.professionalEmail));
            }
            if (criteria.getProfessionalPhoneNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getProfessionalPhoneNumber(), Employee_.professionalPhoneNumber));
            }
            if (criteria.getCommissionPct() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommissionPct(), Employee_.commissionPct));
            }
            if (criteria.getHiredDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHiredDate(), Employee_.hiredDate));
            }
            if (criteria.getContractNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContractNumber(), Employee_.contractNumber));
            }
            if (criteria.getContractStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContractStartDate(), Employee_.contractStartDate));
            }
            if (criteria.getContractEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContractEndDate(), Employee_.contractEndDate));
            }
            if (criteria.getContractType() != null) {
                specification = specification.and(buildSpecification(criteria.getContractType(), Employee_.contractType));
            }
            if (criteria.getSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalary(), Employee_.salary));
            }
            if (criteria.getSex() != null) {
                specification = specification.and(buildSpecification(criteria.getSex(), Employee_.sex));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), Employee_.dob));
            }
            if (criteria.getPlaceOfBirth() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlaceOfBirth(), Employee_.placeOfBirth));
            }
            if (criteria.getPersonalPhoneNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPersonalPhoneNumber(), Employee_.personalPhoneNumber));
            }
            if (criteria.getPermanentAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPermanentAddress(), Employee_.permanentAddress));
            }
            if (criteria.getTemporaryAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTemporaryAddress(), Employee_.temporaryAddress));
            }
            if (criteria.getIdNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdNumber(), Employee_.idNumber));
            }
            if (criteria.getIdIssuedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdIssuedDate(), Employee_.idIssuedDate));
            }
            if (criteria.getIdIssuedLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdIssuedLocation(), Employee_.idIssuedLocation));
            }
            if (criteria.getSocialInsuranceNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSocialInsuranceNumber(), Employee_.socialInsuranceNumber));
            }
            if (criteria.getTaxIdentificationNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTaxIdentificationNumber(), Employee_.taxIdentificationNumber));
            }
            if (criteria.getQualification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQualification(), Employee_.qualification));
            }
            if (criteria.getBankAccount() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankAccount(), Employee_.bankAccount));
            }
            if (criteria.getBankCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankCode(), Employee_.bankCode));
            }
            if (criteria.getUserIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserIdId(), root -> root.join(Employee_.userId, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getAssignmentEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssignmentEmployeeId(),
                            root -> root.join(Employee_.assignmentEmployees, JoinType.LEFT).get(Assignment_.id)
                        )
                    );
            }
            if (criteria.getSupervisorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSupervisorId(),
                            root -> root.join(Employee_.supervisor, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
            if (criteria.getJobId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getJobId(), root -> root.join(Employee_.job, JoinType.LEFT).get(Job_.id))
                    );
            }
            if (criteria.getTeamMembersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTeamMembersId(),
                            root -> root.join(Employee_.teamMembers, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
