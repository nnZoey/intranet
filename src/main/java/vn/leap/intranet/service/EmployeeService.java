package vn.leap.intranet.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.leap.intranet.domain.Employee;
import vn.leap.intranet.repository.EmployeeRepository;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Save a employee.
     *
     * @param employee the entity to save.
     * @return the persisted entity.
     */
    public Employee save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    /**
     * Partially update a employee.
     *
     * @param employee the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Employee> partialUpdate(Employee employee) {
        log.debug("Request to partially update Employee : {}", employee);

        return employeeRepository
            .findById(employee.getId())
            .map(existingEmployee -> {
                if (employee.getEmployeeCode() != null) {
                    existingEmployee.setEmployeeCode(employee.getEmployeeCode());
                }
                if (employee.getFirstName() != null) {
                    existingEmployee.setFirstName(employee.getFirstName());
                }
                if (employee.getLastName() != null) {
                    existingEmployee.setLastName(employee.getLastName());
                }
                if (employee.getImage() != null) {
                    existingEmployee.setImage(employee.getImage());
                }
                if (employee.getImageContentType() != null) {
                    existingEmployee.setImageContentType(employee.getImageContentType());
                }
                if (employee.getEffectiveDate() != null) {
                    existingEmployee.setEffectiveDate(employee.getEffectiveDate());
                }
                if (employee.getSlogan() != null) {
                    existingEmployee.setSlogan(employee.getSlogan());
                }
                if (employee.getProfessionalEmail() != null) {
                    existingEmployee.setProfessionalEmail(employee.getProfessionalEmail());
                }
                if (employee.getProfessionalPhoneNumber() != null) {
                    existingEmployee.setProfessionalPhoneNumber(employee.getProfessionalPhoneNumber());
                }
                if (employee.getCommissionPct() != null) {
                    existingEmployee.setCommissionPct(employee.getCommissionPct());
                }
                if (employee.getHiredDate() != null) {
                    existingEmployee.setHiredDate(employee.getHiredDate());
                }
                if (employee.getContractNumber() != null) {
                    existingEmployee.setContractNumber(employee.getContractNumber());
                }
                if (employee.getContractStartDate() != null) {
                    existingEmployee.setContractStartDate(employee.getContractStartDate());
                }
                if (employee.getContractEndDate() != null) {
                    existingEmployee.setContractEndDate(employee.getContractEndDate());
                }
                if (employee.getContractType() != null) {
                    existingEmployee.setContractType(employee.getContractType());
                }
                if (employee.getContractFile() != null) {
                    existingEmployee.setContractFile(employee.getContractFile());
                }
                if (employee.getContractFileContentType() != null) {
                    existingEmployee.setContractFileContentType(employee.getContractFileContentType());
                }
                if (employee.getSalary() != null) {
                    existingEmployee.setSalary(employee.getSalary());
                }
                if (employee.getSex() != null) {
                    existingEmployee.setSex(employee.getSex());
                }
                if (employee.getDob() != null) {
                    existingEmployee.setDob(employee.getDob());
                }
                if (employee.getPlaceOfBirth() != null) {
                    existingEmployee.setPlaceOfBirth(employee.getPlaceOfBirth());
                }
                if (employee.getPersonalPhoneNumber() != null) {
                    existingEmployee.setPersonalPhoneNumber(employee.getPersonalPhoneNumber());
                }
                if (employee.getPermanentAddress() != null) {
                    existingEmployee.setPermanentAddress(employee.getPermanentAddress());
                }
                if (employee.getTemporaryAddress() != null) {
                    existingEmployee.setTemporaryAddress(employee.getTemporaryAddress());
                }
                if (employee.getIdNumber() != null) {
                    existingEmployee.setIdNumber(employee.getIdNumber());
                }
                if (employee.getIdIssuedDate() != null) {
                    existingEmployee.setIdIssuedDate(employee.getIdIssuedDate());
                }
                if (employee.getIdIssuedLocation() != null) {
                    existingEmployee.setIdIssuedLocation(employee.getIdIssuedLocation());
                }
                if (employee.getSocialInsuranceNumber() != null) {
                    existingEmployee.setSocialInsuranceNumber(employee.getSocialInsuranceNumber());
                }
                if (employee.getTaxIdentificationNumber() != null) {
                    existingEmployee.setTaxIdentificationNumber(employee.getTaxIdentificationNumber());
                }
                if (employee.getQualification() != null) {
                    existingEmployee.setQualification(employee.getQualification());
                }
                if (employee.getBankAccount() != null) {
                    existingEmployee.setBankAccount(employee.getBankAccount());
                }
                if (employee.getBankCode() != null) {
                    existingEmployee.setBankCode(employee.getBankCode());
                }

                return existingEmployee;
            })
            .map(employeeRepository::save);
    }

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(pageable);
    }

    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Employee> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id);
    }

    /**
     * Delete the employee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }
}
