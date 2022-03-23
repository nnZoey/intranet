package vn.leap.intranet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vn.leap.intranet.web.rest.TestUtil.sameInstant;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import vn.leap.intranet.IntegrationTest;
import vn.leap.intranet.domain.Assignment;
import vn.leap.intranet.domain.Employee;
import vn.leap.intranet.domain.Employee;
import vn.leap.intranet.domain.Job;
import vn.leap.intranet.domain.User;
import vn.leap.intranet.domain.enumeration.ContractType;
import vn.leap.intranet.domain.enumeration.Sex;
import vn.leap.intranet.repository.EmployeeRepository;
import vn.leap.intranet.service.criteria.EmployeeCriteria;

/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_EMPLOYEE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_EFFECTIVE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EFFECTIVE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_EFFECTIVE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_SLOGAN = "AAAAAAAAAA";
    private static final String UPDATED_SLOGAN = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSIONAL_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSIONAL_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSIONAL_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSIONAL_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Long DEFAULT_COMMISSION_PCT = 1L;
    private static final Long UPDATED_COMMISSION_PCT = 2L;
    private static final Long SMALLER_COMMISSION_PCT = 1L - 1L;

    private static final ZonedDateTime DEFAULT_HIRED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HIRED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_HIRED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CONTRACT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTRACT_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CONTRACT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CONTRACT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTRACT_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CONTRACT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ContractType DEFAULT_CONTRACT_TYPE = ContractType.DEFINITIVE;
    private static final ContractType UPDATED_CONTRACT_TYPE = ContractType.INFINITIVE;

    private static final byte[] DEFAULT_CONTRACT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTRACT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTRACT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTRACT_FILE_CONTENT_TYPE = "image/png";

    private static final Long DEFAULT_SALARY = 1000000L;
    private static final Long UPDATED_SALARY = 1000001L;
    private static final Long SMALLER_SALARY = 1000000L - 1L;

    private static final Sex DEFAULT_SEX = Sex.FEMALE;
    private static final Sex UPDATED_SEX = Sex.MALE;

    private static final ZonedDateTime DEFAULT_DOB = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DOB = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DOB = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_PLACE_OF_BIRTH = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_OF_BIRTH = "BBBBBBBBBB";

    private static final String DEFAULT_PERSONAL_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PERSONAL_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PERMANENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PERMANENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPORARY_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_TEMPORARY_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ID_ISSUED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ID_ISSUED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ID_ISSUED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_ID_ISSUED_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_ID_ISSUED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_SOCIAL_INSURANCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SOCIAL_INSURANCE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_IDENTIFICATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TAX_IDENTIFICATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_QUALIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BANK_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeCode(DEFAULT_EMPLOYEE_CODE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .slogan(DEFAULT_SLOGAN)
            .professionalEmail(DEFAULT_PROFESSIONAL_EMAIL)
            .professionalPhoneNumber(DEFAULT_PROFESSIONAL_PHONE_NUMBER)
            .commissionPct(DEFAULT_COMMISSION_PCT)
            .hiredDate(DEFAULT_HIRED_DATE)
            .contractNumber(DEFAULT_CONTRACT_NUMBER)
            .contractStartDate(DEFAULT_CONTRACT_START_DATE)
            .contractEndDate(DEFAULT_CONTRACT_END_DATE)
            .contractType(DEFAULT_CONTRACT_TYPE)
            .contractFile(DEFAULT_CONTRACT_FILE)
            .contractFileContentType(DEFAULT_CONTRACT_FILE_CONTENT_TYPE)
            .salary(DEFAULT_SALARY)
            .sex(DEFAULT_SEX)
            .dob(DEFAULT_DOB)
            .placeOfBirth(DEFAULT_PLACE_OF_BIRTH)
            .personalPhoneNumber(DEFAULT_PERSONAL_PHONE_NUMBER)
            .permanentAddress(DEFAULT_PERMANENT_ADDRESS)
            .temporaryAddress(DEFAULT_TEMPORARY_ADDRESS)
            .idNumber(DEFAULT_ID_NUMBER)
            .idIssuedDate(DEFAULT_ID_ISSUED_DATE)
            .idIssuedLocation(DEFAULT_ID_ISSUED_LOCATION)
            .socialInsuranceNumber(DEFAULT_SOCIAL_INSURANCE_NUMBER)
            .taxIdentificationNumber(DEFAULT_TAX_IDENTIFICATION_NUMBER)
            .qualification(DEFAULT_QUALIFICATION)
            .bankAccount(DEFAULT_BANK_ACCOUNT)
            .bankCode(DEFAULT_BANK_CODE);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeCode(UPDATED_EMPLOYEE_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .slogan(UPDATED_SLOGAN)
            .professionalEmail(UPDATED_PROFESSIONAL_EMAIL)
            .professionalPhoneNumber(UPDATED_PROFESSIONAL_PHONE_NUMBER)
            .commissionPct(UPDATED_COMMISSION_PCT)
            .hiredDate(UPDATED_HIRED_DATE)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .contractStartDate(UPDATED_CONTRACT_START_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .contractType(UPDATED_CONTRACT_TYPE)
            .contractFile(UPDATED_CONTRACT_FILE)
            .contractFileContentType(UPDATED_CONTRACT_FILE_CONTENT_TYPE)
            .salary(UPDATED_SALARY)
            .sex(UPDATED_SEX)
            .dob(UPDATED_DOB)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .personalPhoneNumber(UPDATED_PERSONAL_PHONE_NUMBER)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .temporaryAddress(UPDATED_TEMPORARY_ADDRESS)
            .idNumber(UPDATED_ID_NUMBER)
            .idIssuedDate(UPDATED_ID_ISSUED_DATE)
            .idIssuedLocation(UPDATED_ID_ISSUED_LOCATION)
            .socialInsuranceNumber(UPDATED_SOCIAL_INSURANCE_NUMBER)
            .taxIdentificationNumber(UPDATED_TAX_IDENTIFICATION_NUMBER)
            .qualification(UPDATED_QUALIFICATION)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .bankCode(UPDATED_BANK_CODE);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        // Create the Employee
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeCode()).isEqualTo(DEFAULT_EMPLOYEE_CODE);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testEmployee.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testEmployee.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testEmployee.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
        assertThat(testEmployee.getProfessionalEmail()).isEqualTo(DEFAULT_PROFESSIONAL_EMAIL);
        assertThat(testEmployee.getProfessionalPhoneNumber()).isEqualTo(DEFAULT_PROFESSIONAL_PHONE_NUMBER);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(DEFAULT_COMMISSION_PCT);
        assertThat(testEmployee.getHiredDate()).isEqualTo(DEFAULT_HIRED_DATE);
        assertThat(testEmployee.getContractNumber()).isEqualTo(DEFAULT_CONTRACT_NUMBER);
        assertThat(testEmployee.getContractStartDate()).isEqualTo(DEFAULT_CONTRACT_START_DATE);
        assertThat(testEmployee.getContractEndDate()).isEqualTo(DEFAULT_CONTRACT_END_DATE);
        assertThat(testEmployee.getContractType()).isEqualTo(DEFAULT_CONTRACT_TYPE);
        assertThat(testEmployee.getContractFile()).isEqualTo(DEFAULT_CONTRACT_FILE);
        assertThat(testEmployee.getContractFileContentType()).isEqualTo(DEFAULT_CONTRACT_FILE_CONTENT_TYPE);
        assertThat(testEmployee.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testEmployee.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testEmployee.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testEmployee.getPlaceOfBirth()).isEqualTo(DEFAULT_PLACE_OF_BIRTH);
        assertThat(testEmployee.getPersonalPhoneNumber()).isEqualTo(DEFAULT_PERSONAL_PHONE_NUMBER);
        assertThat(testEmployee.getPermanentAddress()).isEqualTo(DEFAULT_PERMANENT_ADDRESS);
        assertThat(testEmployee.getTemporaryAddress()).isEqualTo(DEFAULT_TEMPORARY_ADDRESS);
        assertThat(testEmployee.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testEmployee.getIdIssuedDate()).isEqualTo(DEFAULT_ID_ISSUED_DATE);
        assertThat(testEmployee.getIdIssuedLocation()).isEqualTo(DEFAULT_ID_ISSUED_LOCATION);
        assertThat(testEmployee.getSocialInsuranceNumber()).isEqualTo(DEFAULT_SOCIAL_INSURANCE_NUMBER);
        assertThat(testEmployee.getTaxIdentificationNumber()).isEqualTo(DEFAULT_TAX_IDENTIFICATION_NUMBER);
        assertThat(testEmployee.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
        assertThat(testEmployee.getBankAccount()).isEqualTo(DEFAULT_BANK_ACCOUNT);
        assertThat(testEmployee.getBankCode()).isEqualTo(DEFAULT_BANK_CODE);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmployeeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setEmployeeCode(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setFirstName(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setLastName(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEffectiveDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setEffectiveDate(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfessionalEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setProfessionalEmail(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfessionalPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setProfessionalPhoneNumber(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommissionPctIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setCommissionPct(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHiredDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setHiredDate(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setContractNumber(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setContractStartDate(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setContractType(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSalaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setSalary(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setSex(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDobIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setDob(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPersonalPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setPersonalPhoneNumber(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPermanentAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setPermanentAddress(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTemporaryAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setTemporaryAddress(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setIdNumber(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdIssuedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setIdIssuedDate(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdIssuedLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setIdIssuedLocation(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBankAccountIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setBankAccount(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBankCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setBankCode(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeCode").value(hasItem(DEFAULT_EMPLOYEE_CODE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(sameInstant(DEFAULT_EFFECTIVE_DATE))))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].professionalEmail").value(hasItem(DEFAULT_PROFESSIONAL_EMAIL)))
            .andExpect(jsonPath("$.[*].professionalPhoneNumber").value(hasItem(DEFAULT_PROFESSIONAL_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].commissionPct").value(hasItem(DEFAULT_COMMISSION_PCT.intValue())))
            .andExpect(jsonPath("$.[*].hiredDate").value(hasItem(sameInstant(DEFAULT_HIRED_DATE))))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].contractStartDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_START_DATE))))
            .andExpect(jsonPath("$.[*].contractEndDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_END_DATE))))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contractFileContentType").value(hasItem(DEFAULT_CONTRACT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contractFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTRACT_FILE))))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(sameInstant(DEFAULT_DOB))))
            .andExpect(jsonPath("$.[*].placeOfBirth").value(hasItem(DEFAULT_PLACE_OF_BIRTH)))
            .andExpect(jsonPath("$.[*].personalPhoneNumber").value(hasItem(DEFAULT_PERSONAL_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].permanentAddress").value(hasItem(DEFAULT_PERMANENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].temporaryAddress").value(hasItem(DEFAULT_TEMPORARY_ADDRESS)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].idIssuedDate").value(hasItem(sameInstant(DEFAULT_ID_ISSUED_DATE))))
            .andExpect(jsonPath("$.[*].idIssuedLocation").value(hasItem(DEFAULT_ID_ISSUED_LOCATION)))
            .andExpect(jsonPath("$.[*].socialInsuranceNumber").value(hasItem(DEFAULT_SOCIAL_INSURANCE_NUMBER)))
            .andExpect(jsonPath("$.[*].taxIdentificationNumber").value(hasItem(DEFAULT_TAX_IDENTIFICATION_NUMBER)))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION)))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT)))
            .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE)));
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.employeeCode").value(DEFAULT_EMPLOYEE_CODE))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.effectiveDate").value(sameInstant(DEFAULT_EFFECTIVE_DATE)))
            .andExpect(jsonPath("$.slogan").value(DEFAULT_SLOGAN))
            .andExpect(jsonPath("$.professionalEmail").value(DEFAULT_PROFESSIONAL_EMAIL))
            .andExpect(jsonPath("$.professionalPhoneNumber").value(DEFAULT_PROFESSIONAL_PHONE_NUMBER))
            .andExpect(jsonPath("$.commissionPct").value(DEFAULT_COMMISSION_PCT.intValue()))
            .andExpect(jsonPath("$.hiredDate").value(sameInstant(DEFAULT_HIRED_DATE)))
            .andExpect(jsonPath("$.contractNumber").value(DEFAULT_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.contractStartDate").value(sameInstant(DEFAULT_CONTRACT_START_DATE)))
            .andExpect(jsonPath("$.contractEndDate").value(sameInstant(DEFAULT_CONTRACT_END_DATE)))
            .andExpect(jsonPath("$.contractType").value(DEFAULT_CONTRACT_TYPE.toString()))
            .andExpect(jsonPath("$.contractFileContentType").value(DEFAULT_CONTRACT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.contractFile").value(Base64Utils.encodeToString(DEFAULT_CONTRACT_FILE)))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.intValue()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.dob").value(sameInstant(DEFAULT_DOB)))
            .andExpect(jsonPath("$.placeOfBirth").value(DEFAULT_PLACE_OF_BIRTH))
            .andExpect(jsonPath("$.personalPhoneNumber").value(DEFAULT_PERSONAL_PHONE_NUMBER))
            .andExpect(jsonPath("$.permanentAddress").value(DEFAULT_PERMANENT_ADDRESS))
            .andExpect(jsonPath("$.temporaryAddress").value(DEFAULT_TEMPORARY_ADDRESS))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.idIssuedDate").value(sameInstant(DEFAULT_ID_ISSUED_DATE)))
            .andExpect(jsonPath("$.idIssuedLocation").value(DEFAULT_ID_ISSUED_LOCATION))
            .andExpect(jsonPath("$.socialInsuranceNumber").value(DEFAULT_SOCIAL_INSURANCE_NUMBER))
            .andExpect(jsonPath("$.taxIdentificationNumber").value(DEFAULT_TAX_IDENTIFICATION_NUMBER))
            .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION))
            .andExpect(jsonPath("$.bankAccount").value(DEFAULT_BANK_ACCOUNT))
            .andExpect(jsonPath("$.bankCode").value(DEFAULT_BANK_CODE));
    }

    @Test
    @Transactional
    void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        Long id = employee.getId();

        defaultEmployeeShouldBeFound("id.equals=" + id);
        defaultEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeCode equals to DEFAULT_EMPLOYEE_CODE
        defaultEmployeeShouldBeFound("employeeCode.equals=" + DEFAULT_EMPLOYEE_CODE);

        // Get all the employeeList where employeeCode equals to UPDATED_EMPLOYEE_CODE
        defaultEmployeeShouldNotBeFound("employeeCode.equals=" + UPDATED_EMPLOYEE_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeCode not equals to DEFAULT_EMPLOYEE_CODE
        defaultEmployeeShouldNotBeFound("employeeCode.notEquals=" + DEFAULT_EMPLOYEE_CODE);

        // Get all the employeeList where employeeCode not equals to UPDATED_EMPLOYEE_CODE
        defaultEmployeeShouldBeFound("employeeCode.notEquals=" + UPDATED_EMPLOYEE_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeCode in DEFAULT_EMPLOYEE_CODE or UPDATED_EMPLOYEE_CODE
        defaultEmployeeShouldBeFound("employeeCode.in=" + DEFAULT_EMPLOYEE_CODE + "," + UPDATED_EMPLOYEE_CODE);

        // Get all the employeeList where employeeCode equals to UPDATED_EMPLOYEE_CODE
        defaultEmployeeShouldNotBeFound("employeeCode.in=" + UPDATED_EMPLOYEE_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeCode is not null
        defaultEmployeeShouldBeFound("employeeCode.specified=true");

        // Get all the employeeList where employeeCode is null
        defaultEmployeeShouldNotBeFound("employeeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeCodeContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeCode contains DEFAULT_EMPLOYEE_CODE
        defaultEmployeeShouldBeFound("employeeCode.contains=" + DEFAULT_EMPLOYEE_CODE);

        // Get all the employeeList where employeeCode contains UPDATED_EMPLOYEE_CODE
        defaultEmployeeShouldNotBeFound("employeeCode.contains=" + UPDATED_EMPLOYEE_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeCode does not contain DEFAULT_EMPLOYEE_CODE
        defaultEmployeeShouldNotBeFound("employeeCode.doesNotContain=" + DEFAULT_EMPLOYEE_CODE);

        // Get all the employeeList where employeeCode does not contain UPDATED_EMPLOYEE_CODE
        defaultEmployeeShouldBeFound("employeeCode.doesNotContain=" + UPDATED_EMPLOYEE_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName not equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName not equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName is not null
        defaultEmployeeShouldBeFound("firstName.specified=true");

        // Get all the employeeList where firstName is null
        defaultEmployeeShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName contains DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName contains UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName does not contain DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName does not contain UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName not equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName not equals to UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName is not null
        defaultEmployeeShouldBeFound("lastName.specified=true");

        // Get all the employeeList where lastName is null
        defaultEmployeeShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName contains DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName contains UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName does not contain DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName does not contain UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEffectiveDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where effectiveDate equals to DEFAULT_EFFECTIVE_DATE
        defaultEmployeeShouldBeFound("effectiveDate.equals=" + DEFAULT_EFFECTIVE_DATE);

        // Get all the employeeList where effectiveDate equals to UPDATED_EFFECTIVE_DATE
        defaultEmployeeShouldNotBeFound("effectiveDate.equals=" + UPDATED_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEffectiveDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where effectiveDate not equals to DEFAULT_EFFECTIVE_DATE
        defaultEmployeeShouldNotBeFound("effectiveDate.notEquals=" + DEFAULT_EFFECTIVE_DATE);

        // Get all the employeeList where effectiveDate not equals to UPDATED_EFFECTIVE_DATE
        defaultEmployeeShouldBeFound("effectiveDate.notEquals=" + UPDATED_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEffectiveDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where effectiveDate in DEFAULT_EFFECTIVE_DATE or UPDATED_EFFECTIVE_DATE
        defaultEmployeeShouldBeFound("effectiveDate.in=" + DEFAULT_EFFECTIVE_DATE + "," + UPDATED_EFFECTIVE_DATE);

        // Get all the employeeList where effectiveDate equals to UPDATED_EFFECTIVE_DATE
        defaultEmployeeShouldNotBeFound("effectiveDate.in=" + UPDATED_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEffectiveDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where effectiveDate is not null
        defaultEmployeeShouldBeFound("effectiveDate.specified=true");

        // Get all the employeeList where effectiveDate is null
        defaultEmployeeShouldNotBeFound("effectiveDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEffectiveDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where effectiveDate is greater than or equal to DEFAULT_EFFECTIVE_DATE
        defaultEmployeeShouldBeFound("effectiveDate.greaterThanOrEqual=" + DEFAULT_EFFECTIVE_DATE);

        // Get all the employeeList where effectiveDate is greater than or equal to UPDATED_EFFECTIVE_DATE
        defaultEmployeeShouldNotBeFound("effectiveDate.greaterThanOrEqual=" + UPDATED_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEffectiveDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where effectiveDate is less than or equal to DEFAULT_EFFECTIVE_DATE
        defaultEmployeeShouldBeFound("effectiveDate.lessThanOrEqual=" + DEFAULT_EFFECTIVE_DATE);

        // Get all the employeeList where effectiveDate is less than or equal to SMALLER_EFFECTIVE_DATE
        defaultEmployeeShouldNotBeFound("effectiveDate.lessThanOrEqual=" + SMALLER_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEffectiveDateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where effectiveDate is less than DEFAULT_EFFECTIVE_DATE
        defaultEmployeeShouldNotBeFound("effectiveDate.lessThan=" + DEFAULT_EFFECTIVE_DATE);

        // Get all the employeeList where effectiveDate is less than UPDATED_EFFECTIVE_DATE
        defaultEmployeeShouldBeFound("effectiveDate.lessThan=" + UPDATED_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEffectiveDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where effectiveDate is greater than DEFAULT_EFFECTIVE_DATE
        defaultEmployeeShouldNotBeFound("effectiveDate.greaterThan=" + DEFAULT_EFFECTIVE_DATE);

        // Get all the employeeList where effectiveDate is greater than SMALLER_EFFECTIVE_DATE
        defaultEmployeeShouldBeFound("effectiveDate.greaterThan=" + SMALLER_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesBySloganIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where slogan equals to DEFAULT_SLOGAN
        defaultEmployeeShouldBeFound("slogan.equals=" + DEFAULT_SLOGAN);

        // Get all the employeeList where slogan equals to UPDATED_SLOGAN
        defaultEmployeeShouldNotBeFound("slogan.equals=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    void getAllEmployeesBySloganIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where slogan not equals to DEFAULT_SLOGAN
        defaultEmployeeShouldNotBeFound("slogan.notEquals=" + DEFAULT_SLOGAN);

        // Get all the employeeList where slogan not equals to UPDATED_SLOGAN
        defaultEmployeeShouldBeFound("slogan.notEquals=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    void getAllEmployeesBySloganIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where slogan in DEFAULT_SLOGAN or UPDATED_SLOGAN
        defaultEmployeeShouldBeFound("slogan.in=" + DEFAULT_SLOGAN + "," + UPDATED_SLOGAN);

        // Get all the employeeList where slogan equals to UPDATED_SLOGAN
        defaultEmployeeShouldNotBeFound("slogan.in=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    void getAllEmployeesBySloganIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where slogan is not null
        defaultEmployeeShouldBeFound("slogan.specified=true");

        // Get all the employeeList where slogan is null
        defaultEmployeeShouldNotBeFound("slogan.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesBySloganContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where slogan contains DEFAULT_SLOGAN
        defaultEmployeeShouldBeFound("slogan.contains=" + DEFAULT_SLOGAN);

        // Get all the employeeList where slogan contains UPDATED_SLOGAN
        defaultEmployeeShouldNotBeFound("slogan.contains=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    void getAllEmployeesBySloganNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where slogan does not contain DEFAULT_SLOGAN
        defaultEmployeeShouldNotBeFound("slogan.doesNotContain=" + DEFAULT_SLOGAN);

        // Get all the employeeList where slogan does not contain UPDATED_SLOGAN
        defaultEmployeeShouldBeFound("slogan.doesNotContain=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalEmail equals to DEFAULT_PROFESSIONAL_EMAIL
        defaultEmployeeShouldBeFound("professionalEmail.equals=" + DEFAULT_PROFESSIONAL_EMAIL);

        // Get all the employeeList where professionalEmail equals to UPDATED_PROFESSIONAL_EMAIL
        defaultEmployeeShouldNotBeFound("professionalEmail.equals=" + UPDATED_PROFESSIONAL_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalEmail not equals to DEFAULT_PROFESSIONAL_EMAIL
        defaultEmployeeShouldNotBeFound("professionalEmail.notEquals=" + DEFAULT_PROFESSIONAL_EMAIL);

        // Get all the employeeList where professionalEmail not equals to UPDATED_PROFESSIONAL_EMAIL
        defaultEmployeeShouldBeFound("professionalEmail.notEquals=" + UPDATED_PROFESSIONAL_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalEmailIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalEmail in DEFAULT_PROFESSIONAL_EMAIL or UPDATED_PROFESSIONAL_EMAIL
        defaultEmployeeShouldBeFound("professionalEmail.in=" + DEFAULT_PROFESSIONAL_EMAIL + "," + UPDATED_PROFESSIONAL_EMAIL);

        // Get all the employeeList where professionalEmail equals to UPDATED_PROFESSIONAL_EMAIL
        defaultEmployeeShouldNotBeFound("professionalEmail.in=" + UPDATED_PROFESSIONAL_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalEmail is not null
        defaultEmployeeShouldBeFound("professionalEmail.specified=true");

        // Get all the employeeList where professionalEmail is null
        defaultEmployeeShouldNotBeFound("professionalEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalEmailContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalEmail contains DEFAULT_PROFESSIONAL_EMAIL
        defaultEmployeeShouldBeFound("professionalEmail.contains=" + DEFAULT_PROFESSIONAL_EMAIL);

        // Get all the employeeList where professionalEmail contains UPDATED_PROFESSIONAL_EMAIL
        defaultEmployeeShouldNotBeFound("professionalEmail.contains=" + UPDATED_PROFESSIONAL_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalEmailNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalEmail does not contain DEFAULT_PROFESSIONAL_EMAIL
        defaultEmployeeShouldNotBeFound("professionalEmail.doesNotContain=" + DEFAULT_PROFESSIONAL_EMAIL);

        // Get all the employeeList where professionalEmail does not contain UPDATED_PROFESSIONAL_EMAIL
        defaultEmployeeShouldBeFound("professionalEmail.doesNotContain=" + UPDATED_PROFESSIONAL_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalPhoneNumber equals to DEFAULT_PROFESSIONAL_PHONE_NUMBER
        defaultEmployeeShouldBeFound("professionalPhoneNumber.equals=" + DEFAULT_PROFESSIONAL_PHONE_NUMBER);

        // Get all the employeeList where professionalPhoneNumber equals to UPDATED_PROFESSIONAL_PHONE_NUMBER
        defaultEmployeeShouldNotBeFound("professionalPhoneNumber.equals=" + UPDATED_PROFESSIONAL_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalPhoneNumber not equals to DEFAULT_PROFESSIONAL_PHONE_NUMBER
        defaultEmployeeShouldNotBeFound("professionalPhoneNumber.notEquals=" + DEFAULT_PROFESSIONAL_PHONE_NUMBER);

        // Get all the employeeList where professionalPhoneNumber not equals to UPDATED_PROFESSIONAL_PHONE_NUMBER
        defaultEmployeeShouldBeFound("professionalPhoneNumber.notEquals=" + UPDATED_PROFESSIONAL_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalPhoneNumber in DEFAULT_PROFESSIONAL_PHONE_NUMBER or UPDATED_PROFESSIONAL_PHONE_NUMBER
        defaultEmployeeShouldBeFound(
            "professionalPhoneNumber.in=" + DEFAULT_PROFESSIONAL_PHONE_NUMBER + "," + UPDATED_PROFESSIONAL_PHONE_NUMBER
        );

        // Get all the employeeList where professionalPhoneNumber equals to UPDATED_PROFESSIONAL_PHONE_NUMBER
        defaultEmployeeShouldNotBeFound("professionalPhoneNumber.in=" + UPDATED_PROFESSIONAL_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalPhoneNumber is not null
        defaultEmployeeShouldBeFound("professionalPhoneNumber.specified=true");

        // Get all the employeeList where professionalPhoneNumber is null
        defaultEmployeeShouldNotBeFound("professionalPhoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalPhoneNumber contains DEFAULT_PROFESSIONAL_PHONE_NUMBER
        defaultEmployeeShouldBeFound("professionalPhoneNumber.contains=" + DEFAULT_PROFESSIONAL_PHONE_NUMBER);

        // Get all the employeeList where professionalPhoneNumber contains UPDATED_PROFESSIONAL_PHONE_NUMBER
        defaultEmployeeShouldNotBeFound("professionalPhoneNumber.contains=" + UPDATED_PROFESSIONAL_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByProfessionalPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where professionalPhoneNumber does not contain DEFAULT_PROFESSIONAL_PHONE_NUMBER
        defaultEmployeeShouldNotBeFound("professionalPhoneNumber.doesNotContain=" + DEFAULT_PROFESSIONAL_PHONE_NUMBER);

        // Get all the employeeList where professionalPhoneNumber does not contain UPDATED_PROFESSIONAL_PHONE_NUMBER
        defaultEmployeeShouldBeFound("professionalPhoneNumber.doesNotContain=" + UPDATED_PROFESSIONAL_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommissionPctIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where commissionPct equals to DEFAULT_COMMISSION_PCT
        defaultEmployeeShouldBeFound("commissionPct.equals=" + DEFAULT_COMMISSION_PCT);

        // Get all the employeeList where commissionPct equals to UPDATED_COMMISSION_PCT
        defaultEmployeeShouldNotBeFound("commissionPct.equals=" + UPDATED_COMMISSION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommissionPctIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where commissionPct not equals to DEFAULT_COMMISSION_PCT
        defaultEmployeeShouldNotBeFound("commissionPct.notEquals=" + DEFAULT_COMMISSION_PCT);

        // Get all the employeeList where commissionPct not equals to UPDATED_COMMISSION_PCT
        defaultEmployeeShouldBeFound("commissionPct.notEquals=" + UPDATED_COMMISSION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommissionPctIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where commissionPct in DEFAULT_COMMISSION_PCT or UPDATED_COMMISSION_PCT
        defaultEmployeeShouldBeFound("commissionPct.in=" + DEFAULT_COMMISSION_PCT + "," + UPDATED_COMMISSION_PCT);

        // Get all the employeeList where commissionPct equals to UPDATED_COMMISSION_PCT
        defaultEmployeeShouldNotBeFound("commissionPct.in=" + UPDATED_COMMISSION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommissionPctIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where commissionPct is not null
        defaultEmployeeShouldBeFound("commissionPct.specified=true");

        // Get all the employeeList where commissionPct is null
        defaultEmployeeShouldNotBeFound("commissionPct.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByCommissionPctIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where commissionPct is greater than or equal to DEFAULT_COMMISSION_PCT
        defaultEmployeeShouldBeFound("commissionPct.greaterThanOrEqual=" + DEFAULT_COMMISSION_PCT);

        // Get all the employeeList where commissionPct is greater than or equal to (DEFAULT_COMMISSION_PCT + 1)
        defaultEmployeeShouldNotBeFound("commissionPct.greaterThanOrEqual=" + (DEFAULT_COMMISSION_PCT + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByCommissionPctIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where commissionPct is less than or equal to DEFAULT_COMMISSION_PCT
        defaultEmployeeShouldBeFound("commissionPct.lessThanOrEqual=" + DEFAULT_COMMISSION_PCT);

        // Get all the employeeList where commissionPct is less than or equal to SMALLER_COMMISSION_PCT
        defaultEmployeeShouldNotBeFound("commissionPct.lessThanOrEqual=" + SMALLER_COMMISSION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommissionPctIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where commissionPct is less than DEFAULT_COMMISSION_PCT
        defaultEmployeeShouldNotBeFound("commissionPct.lessThan=" + DEFAULT_COMMISSION_PCT);

        // Get all the employeeList where commissionPct is less than (DEFAULT_COMMISSION_PCT + 1)
        defaultEmployeeShouldBeFound("commissionPct.lessThan=" + (DEFAULT_COMMISSION_PCT + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByCommissionPctIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where commissionPct is greater than DEFAULT_COMMISSION_PCT
        defaultEmployeeShouldNotBeFound("commissionPct.greaterThan=" + DEFAULT_COMMISSION_PCT);

        // Get all the employeeList where commissionPct is greater than SMALLER_COMMISSION_PCT
        defaultEmployeeShouldBeFound("commissionPct.greaterThan=" + SMALLER_COMMISSION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByHiredDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hiredDate equals to DEFAULT_HIRED_DATE
        defaultEmployeeShouldBeFound("hiredDate.equals=" + DEFAULT_HIRED_DATE);

        // Get all the employeeList where hiredDate equals to UPDATED_HIRED_DATE
        defaultEmployeeShouldNotBeFound("hiredDate.equals=" + UPDATED_HIRED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHiredDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hiredDate not equals to DEFAULT_HIRED_DATE
        defaultEmployeeShouldNotBeFound("hiredDate.notEquals=" + DEFAULT_HIRED_DATE);

        // Get all the employeeList where hiredDate not equals to UPDATED_HIRED_DATE
        defaultEmployeeShouldBeFound("hiredDate.notEquals=" + UPDATED_HIRED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHiredDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hiredDate in DEFAULT_HIRED_DATE or UPDATED_HIRED_DATE
        defaultEmployeeShouldBeFound("hiredDate.in=" + DEFAULT_HIRED_DATE + "," + UPDATED_HIRED_DATE);

        // Get all the employeeList where hiredDate equals to UPDATED_HIRED_DATE
        defaultEmployeeShouldNotBeFound("hiredDate.in=" + UPDATED_HIRED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHiredDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hiredDate is not null
        defaultEmployeeShouldBeFound("hiredDate.specified=true");

        // Get all the employeeList where hiredDate is null
        defaultEmployeeShouldNotBeFound("hiredDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByHiredDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hiredDate is greater than or equal to DEFAULT_HIRED_DATE
        defaultEmployeeShouldBeFound("hiredDate.greaterThanOrEqual=" + DEFAULT_HIRED_DATE);

        // Get all the employeeList where hiredDate is greater than or equal to UPDATED_HIRED_DATE
        defaultEmployeeShouldNotBeFound("hiredDate.greaterThanOrEqual=" + UPDATED_HIRED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHiredDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hiredDate is less than or equal to DEFAULT_HIRED_DATE
        defaultEmployeeShouldBeFound("hiredDate.lessThanOrEqual=" + DEFAULT_HIRED_DATE);

        // Get all the employeeList where hiredDate is less than or equal to SMALLER_HIRED_DATE
        defaultEmployeeShouldNotBeFound("hiredDate.lessThanOrEqual=" + SMALLER_HIRED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHiredDateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hiredDate is less than DEFAULT_HIRED_DATE
        defaultEmployeeShouldNotBeFound("hiredDate.lessThan=" + DEFAULT_HIRED_DATE);

        // Get all the employeeList where hiredDate is less than UPDATED_HIRED_DATE
        defaultEmployeeShouldBeFound("hiredDate.lessThan=" + UPDATED_HIRED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHiredDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hiredDate is greater than DEFAULT_HIRED_DATE
        defaultEmployeeShouldNotBeFound("hiredDate.greaterThan=" + DEFAULT_HIRED_DATE);

        // Get all the employeeList where hiredDate is greater than SMALLER_HIRED_DATE
        defaultEmployeeShouldBeFound("hiredDate.greaterThan=" + SMALLER_HIRED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractNumber equals to DEFAULT_CONTRACT_NUMBER
        defaultEmployeeShouldBeFound("contractNumber.equals=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the employeeList where contractNumber equals to UPDATED_CONTRACT_NUMBER
        defaultEmployeeShouldNotBeFound("contractNumber.equals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractNumber not equals to DEFAULT_CONTRACT_NUMBER
        defaultEmployeeShouldNotBeFound("contractNumber.notEquals=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the employeeList where contractNumber not equals to UPDATED_CONTRACT_NUMBER
        defaultEmployeeShouldBeFound("contractNumber.notEquals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractNumber in DEFAULT_CONTRACT_NUMBER or UPDATED_CONTRACT_NUMBER
        defaultEmployeeShouldBeFound("contractNumber.in=" + DEFAULT_CONTRACT_NUMBER + "," + UPDATED_CONTRACT_NUMBER);

        // Get all the employeeList where contractNumber equals to UPDATED_CONTRACT_NUMBER
        defaultEmployeeShouldNotBeFound("contractNumber.in=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractNumber is not null
        defaultEmployeeShouldBeFound("contractNumber.specified=true");

        // Get all the employeeList where contractNumber is null
        defaultEmployeeShouldNotBeFound("contractNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByContractNumberContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractNumber contains DEFAULT_CONTRACT_NUMBER
        defaultEmployeeShouldBeFound("contractNumber.contains=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the employeeList where contractNumber contains UPDATED_CONTRACT_NUMBER
        defaultEmployeeShouldNotBeFound("contractNumber.contains=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractNumber does not contain DEFAULT_CONTRACT_NUMBER
        defaultEmployeeShouldNotBeFound("contractNumber.doesNotContain=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the employeeList where contractNumber does not contain UPDATED_CONTRACT_NUMBER
        defaultEmployeeShouldBeFound("contractNumber.doesNotContain=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractStartDate equals to DEFAULT_CONTRACT_START_DATE
        defaultEmployeeShouldBeFound("contractStartDate.equals=" + DEFAULT_CONTRACT_START_DATE);

        // Get all the employeeList where contractStartDate equals to UPDATED_CONTRACT_START_DATE
        defaultEmployeeShouldNotBeFound("contractStartDate.equals=" + UPDATED_CONTRACT_START_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractStartDate not equals to DEFAULT_CONTRACT_START_DATE
        defaultEmployeeShouldNotBeFound("contractStartDate.notEquals=" + DEFAULT_CONTRACT_START_DATE);

        // Get all the employeeList where contractStartDate not equals to UPDATED_CONTRACT_START_DATE
        defaultEmployeeShouldBeFound("contractStartDate.notEquals=" + UPDATED_CONTRACT_START_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractStartDate in DEFAULT_CONTRACT_START_DATE or UPDATED_CONTRACT_START_DATE
        defaultEmployeeShouldBeFound("contractStartDate.in=" + DEFAULT_CONTRACT_START_DATE + "," + UPDATED_CONTRACT_START_DATE);

        // Get all the employeeList where contractStartDate equals to UPDATED_CONTRACT_START_DATE
        defaultEmployeeShouldNotBeFound("contractStartDate.in=" + UPDATED_CONTRACT_START_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractStartDate is not null
        defaultEmployeeShouldBeFound("contractStartDate.specified=true");

        // Get all the employeeList where contractStartDate is null
        defaultEmployeeShouldNotBeFound("contractStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByContractStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractStartDate is greater than or equal to DEFAULT_CONTRACT_START_DATE
        defaultEmployeeShouldBeFound("contractStartDate.greaterThanOrEqual=" + DEFAULT_CONTRACT_START_DATE);

        // Get all the employeeList where contractStartDate is greater than or equal to UPDATED_CONTRACT_START_DATE
        defaultEmployeeShouldNotBeFound("contractStartDate.greaterThanOrEqual=" + UPDATED_CONTRACT_START_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractStartDate is less than or equal to DEFAULT_CONTRACT_START_DATE
        defaultEmployeeShouldBeFound("contractStartDate.lessThanOrEqual=" + DEFAULT_CONTRACT_START_DATE);

        // Get all the employeeList where contractStartDate is less than or equal to SMALLER_CONTRACT_START_DATE
        defaultEmployeeShouldNotBeFound("contractStartDate.lessThanOrEqual=" + SMALLER_CONTRACT_START_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractStartDate is less than DEFAULT_CONTRACT_START_DATE
        defaultEmployeeShouldNotBeFound("contractStartDate.lessThan=" + DEFAULT_CONTRACT_START_DATE);

        // Get all the employeeList where contractStartDate is less than UPDATED_CONTRACT_START_DATE
        defaultEmployeeShouldBeFound("contractStartDate.lessThan=" + UPDATED_CONTRACT_START_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractStartDate is greater than DEFAULT_CONTRACT_START_DATE
        defaultEmployeeShouldNotBeFound("contractStartDate.greaterThan=" + DEFAULT_CONTRACT_START_DATE);

        // Get all the employeeList where contractStartDate is greater than SMALLER_CONTRACT_START_DATE
        defaultEmployeeShouldBeFound("contractStartDate.greaterThan=" + SMALLER_CONTRACT_START_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractEndDate equals to DEFAULT_CONTRACT_END_DATE
        defaultEmployeeShouldBeFound("contractEndDate.equals=" + DEFAULT_CONTRACT_END_DATE);

        // Get all the employeeList where contractEndDate equals to UPDATED_CONTRACT_END_DATE
        defaultEmployeeShouldNotBeFound("contractEndDate.equals=" + UPDATED_CONTRACT_END_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractEndDate not equals to DEFAULT_CONTRACT_END_DATE
        defaultEmployeeShouldNotBeFound("contractEndDate.notEquals=" + DEFAULT_CONTRACT_END_DATE);

        // Get all the employeeList where contractEndDate not equals to UPDATED_CONTRACT_END_DATE
        defaultEmployeeShouldBeFound("contractEndDate.notEquals=" + UPDATED_CONTRACT_END_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractEndDate in DEFAULT_CONTRACT_END_DATE or UPDATED_CONTRACT_END_DATE
        defaultEmployeeShouldBeFound("contractEndDate.in=" + DEFAULT_CONTRACT_END_DATE + "," + UPDATED_CONTRACT_END_DATE);

        // Get all the employeeList where contractEndDate equals to UPDATED_CONTRACT_END_DATE
        defaultEmployeeShouldNotBeFound("contractEndDate.in=" + UPDATED_CONTRACT_END_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractEndDate is not null
        defaultEmployeeShouldBeFound("contractEndDate.specified=true");

        // Get all the employeeList where contractEndDate is null
        defaultEmployeeShouldNotBeFound("contractEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByContractEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractEndDate is greater than or equal to DEFAULT_CONTRACT_END_DATE
        defaultEmployeeShouldBeFound("contractEndDate.greaterThanOrEqual=" + DEFAULT_CONTRACT_END_DATE);

        // Get all the employeeList where contractEndDate is greater than or equal to UPDATED_CONTRACT_END_DATE
        defaultEmployeeShouldNotBeFound("contractEndDate.greaterThanOrEqual=" + UPDATED_CONTRACT_END_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractEndDate is less than or equal to DEFAULT_CONTRACT_END_DATE
        defaultEmployeeShouldBeFound("contractEndDate.lessThanOrEqual=" + DEFAULT_CONTRACT_END_DATE);

        // Get all the employeeList where contractEndDate is less than or equal to SMALLER_CONTRACT_END_DATE
        defaultEmployeeShouldNotBeFound("contractEndDate.lessThanOrEqual=" + SMALLER_CONTRACT_END_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractEndDate is less than DEFAULT_CONTRACT_END_DATE
        defaultEmployeeShouldNotBeFound("contractEndDate.lessThan=" + DEFAULT_CONTRACT_END_DATE);

        // Get all the employeeList where contractEndDate is less than UPDATED_CONTRACT_END_DATE
        defaultEmployeeShouldBeFound("contractEndDate.lessThan=" + UPDATED_CONTRACT_END_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractEndDate is greater than DEFAULT_CONTRACT_END_DATE
        defaultEmployeeShouldNotBeFound("contractEndDate.greaterThan=" + DEFAULT_CONTRACT_END_DATE);

        // Get all the employeeList where contractEndDate is greater than SMALLER_CONTRACT_END_DATE
        defaultEmployeeShouldBeFound("contractEndDate.greaterThan=" + SMALLER_CONTRACT_END_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractType equals to DEFAULT_CONTRACT_TYPE
        defaultEmployeeShouldBeFound("contractType.equals=" + DEFAULT_CONTRACT_TYPE);

        // Get all the employeeList where contractType equals to UPDATED_CONTRACT_TYPE
        defaultEmployeeShouldNotBeFound("contractType.equals=" + UPDATED_CONTRACT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractType not equals to DEFAULT_CONTRACT_TYPE
        defaultEmployeeShouldNotBeFound("contractType.notEquals=" + DEFAULT_CONTRACT_TYPE);

        // Get all the employeeList where contractType not equals to UPDATED_CONTRACT_TYPE
        defaultEmployeeShouldBeFound("contractType.notEquals=" + UPDATED_CONTRACT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractTypeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractType in DEFAULT_CONTRACT_TYPE or UPDATED_CONTRACT_TYPE
        defaultEmployeeShouldBeFound("contractType.in=" + DEFAULT_CONTRACT_TYPE + "," + UPDATED_CONTRACT_TYPE);

        // Get all the employeeList where contractType equals to UPDATED_CONTRACT_TYPE
        defaultEmployeeShouldNotBeFound("contractType.in=" + UPDATED_CONTRACT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeesByContractTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where contractType is not null
        defaultEmployeeShouldBeFound("contractType.specified=true");

        // Get all the employeeList where contractType is null
        defaultEmployeeShouldNotBeFound("contractType.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary equals to DEFAULT_SALARY
        defaultEmployeeShouldBeFound("salary.equals=" + DEFAULT_SALARY);

        // Get all the employeeList where salary equals to UPDATED_SALARY
        defaultEmployeeShouldNotBeFound("salary.equals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary not equals to DEFAULT_SALARY
        defaultEmployeeShouldNotBeFound("salary.notEquals=" + DEFAULT_SALARY);

        // Get all the employeeList where salary not equals to UPDATED_SALARY
        defaultEmployeeShouldBeFound("salary.notEquals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary in DEFAULT_SALARY or UPDATED_SALARY
        defaultEmployeeShouldBeFound("salary.in=" + DEFAULT_SALARY + "," + UPDATED_SALARY);

        // Get all the employeeList where salary equals to UPDATED_SALARY
        defaultEmployeeShouldNotBeFound("salary.in=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary is not null
        defaultEmployeeShouldBeFound("salary.specified=true");

        // Get all the employeeList where salary is null
        defaultEmployeeShouldNotBeFound("salary.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary is greater than or equal to DEFAULT_SALARY
        defaultEmployeeShouldBeFound("salary.greaterThanOrEqual=" + DEFAULT_SALARY);

        // Get all the employeeList where salary is greater than or equal to UPDATED_SALARY
        defaultEmployeeShouldNotBeFound("salary.greaterThanOrEqual=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary is less than or equal to DEFAULT_SALARY
        defaultEmployeeShouldBeFound("salary.lessThanOrEqual=" + DEFAULT_SALARY);

        // Get all the employeeList where salary is less than or equal to SMALLER_SALARY
        defaultEmployeeShouldNotBeFound("salary.lessThanOrEqual=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary is less than DEFAULT_SALARY
        defaultEmployeeShouldNotBeFound("salary.lessThan=" + DEFAULT_SALARY);

        // Get all the employeeList where salary is less than UPDATED_SALARY
        defaultEmployeeShouldBeFound("salary.lessThan=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary is greater than DEFAULT_SALARY
        defaultEmployeeShouldNotBeFound("salary.greaterThan=" + DEFAULT_SALARY);

        // Get all the employeeList where salary is greater than SMALLER_SALARY
        defaultEmployeeShouldBeFound("salary.greaterThan=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySexIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sex equals to DEFAULT_SEX
        defaultEmployeeShouldBeFound("sex.equals=" + DEFAULT_SEX);

        // Get all the employeeList where sex equals to UPDATED_SEX
        defaultEmployeeShouldNotBeFound("sex.equals=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllEmployeesBySexIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sex not equals to DEFAULT_SEX
        defaultEmployeeShouldNotBeFound("sex.notEquals=" + DEFAULT_SEX);

        // Get all the employeeList where sex not equals to UPDATED_SEX
        defaultEmployeeShouldBeFound("sex.notEquals=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllEmployeesBySexIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sex in DEFAULT_SEX or UPDATED_SEX
        defaultEmployeeShouldBeFound("sex.in=" + DEFAULT_SEX + "," + UPDATED_SEX);

        // Get all the employeeList where sex equals to UPDATED_SEX
        defaultEmployeeShouldNotBeFound("sex.in=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllEmployeesBySexIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sex is not null
        defaultEmployeeShouldBeFound("sex.specified=true");

        // Get all the employeeList where sex is null
        defaultEmployeeShouldNotBeFound("sex.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dob equals to DEFAULT_DOB
        defaultEmployeeShouldBeFound("dob.equals=" + DEFAULT_DOB);

        // Get all the employeeList where dob equals to UPDATED_DOB
        defaultEmployeeShouldNotBeFound("dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEmployeesByDobIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dob not equals to DEFAULT_DOB
        defaultEmployeeShouldNotBeFound("dob.notEquals=" + DEFAULT_DOB);

        // Get all the employeeList where dob not equals to UPDATED_DOB
        defaultEmployeeShouldBeFound("dob.notEquals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEmployeesByDobIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dob in DEFAULT_DOB or UPDATED_DOB
        defaultEmployeeShouldBeFound("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB);

        // Get all the employeeList where dob equals to UPDATED_DOB
        defaultEmployeeShouldNotBeFound("dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEmployeesByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dob is not null
        defaultEmployeeShouldBeFound("dob.specified=true");

        // Get all the employeeList where dob is null
        defaultEmployeeShouldNotBeFound("dob.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dob is greater than or equal to DEFAULT_DOB
        defaultEmployeeShouldBeFound("dob.greaterThanOrEqual=" + DEFAULT_DOB);

        // Get all the employeeList where dob is greater than or equal to UPDATED_DOB
        defaultEmployeeShouldNotBeFound("dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEmployeesByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dob is less than or equal to DEFAULT_DOB
        defaultEmployeeShouldBeFound("dob.lessThanOrEqual=" + DEFAULT_DOB);

        // Get all the employeeList where dob is less than or equal to SMALLER_DOB
        defaultEmployeeShouldNotBeFound("dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllEmployeesByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dob is less than DEFAULT_DOB
        defaultEmployeeShouldNotBeFound("dob.lessThan=" + DEFAULT_DOB);

        // Get all the employeeList where dob is less than UPDATED_DOB
        defaultEmployeeShouldBeFound("dob.lessThan=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEmployeesByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dob is greater than DEFAULT_DOB
        defaultEmployeeShouldNotBeFound("dob.greaterThan=" + DEFAULT_DOB);

        // Get all the employeeList where dob is greater than SMALLER_DOB
        defaultEmployeeShouldBeFound("dob.greaterThan=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllEmployeesByPlaceOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where placeOfBirth equals to DEFAULT_PLACE_OF_BIRTH
        defaultEmployeeShouldBeFound("placeOfBirth.equals=" + DEFAULT_PLACE_OF_BIRTH);

        // Get all the employeeList where placeOfBirth equals to UPDATED_PLACE_OF_BIRTH
        defaultEmployeeShouldNotBeFound("placeOfBirth.equals=" + UPDATED_PLACE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllEmployeesByPlaceOfBirthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where placeOfBirth not equals to DEFAULT_PLACE_OF_BIRTH
        defaultEmployeeShouldNotBeFound("placeOfBirth.notEquals=" + DEFAULT_PLACE_OF_BIRTH);

        // Get all the employeeList where placeOfBirth not equals to UPDATED_PLACE_OF_BIRTH
        defaultEmployeeShouldBeFound("placeOfBirth.notEquals=" + UPDATED_PLACE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllEmployeesByPlaceOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where placeOfBirth in DEFAULT_PLACE_OF_BIRTH or UPDATED_PLACE_OF_BIRTH
        defaultEmployeeShouldBeFound("placeOfBirth.in=" + DEFAULT_PLACE_OF_BIRTH + "," + UPDATED_PLACE_OF_BIRTH);

        // Get all the employeeList where placeOfBirth equals to UPDATED_PLACE_OF_BIRTH
        defaultEmployeeShouldNotBeFound("placeOfBirth.in=" + UPDATED_PLACE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllEmployeesByPlaceOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where placeOfBirth is not null
        defaultEmployeeShouldBeFound("placeOfBirth.specified=true");

        // Get all the employeeList where placeOfBirth is null
        defaultEmployeeShouldNotBeFound("placeOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPlaceOfBirthContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where placeOfBirth contains DEFAULT_PLACE_OF_BIRTH
        defaultEmployeeShouldBeFound("placeOfBirth.contains=" + DEFAULT_PLACE_OF_BIRTH);

        // Get all the employeeList where placeOfBirth contains UPDATED_PLACE_OF_BIRTH
        defaultEmployeeShouldNotBeFound("placeOfBirth.contains=" + UPDATED_PLACE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllEmployeesByPlaceOfBirthNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where placeOfBirth does not contain DEFAULT_PLACE_OF_BIRTH
        defaultEmployeeShouldNotBeFound("placeOfBirth.doesNotContain=" + DEFAULT_PLACE_OF_BIRTH);

        // Get all the employeeList where placeOfBirth does not contain UPDATED_PLACE_OF_BIRTH
        defaultEmployeeShouldBeFound("placeOfBirth.doesNotContain=" + UPDATED_PLACE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllEmployeesByPersonalPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where personalPhoneNumber equals to DEFAULT_PERSONAL_PHONE_NUMBER
        defaultEmployeeShouldBeFound("personalPhoneNumber.equals=" + DEFAULT_PERSONAL_PHONE_NUMBER);

        // Get all the employeeList where personalPhoneNumber equals to UPDATED_PERSONAL_PHONE_NUMBER
        defaultEmployeeShouldNotBeFound("personalPhoneNumber.equals=" + UPDATED_PERSONAL_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByPersonalPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where personalPhoneNumber not equals to DEFAULT_PERSONAL_PHONE_NUMBER
        defaultEmployeeShouldNotBeFound("personalPhoneNumber.notEquals=" + DEFAULT_PERSONAL_PHONE_NUMBER);

        // Get all the employeeList where personalPhoneNumber not equals to UPDATED_PERSONAL_PHONE_NUMBER
        defaultEmployeeShouldBeFound("personalPhoneNumber.notEquals=" + UPDATED_PERSONAL_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByPersonalPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where personalPhoneNumber in DEFAULT_PERSONAL_PHONE_NUMBER or UPDATED_PERSONAL_PHONE_NUMBER
        defaultEmployeeShouldBeFound("personalPhoneNumber.in=" + DEFAULT_PERSONAL_PHONE_NUMBER + "," + UPDATED_PERSONAL_PHONE_NUMBER);

        // Get all the employeeList where personalPhoneNumber equals to UPDATED_PERSONAL_PHONE_NUMBER
        defaultEmployeeShouldNotBeFound("personalPhoneNumber.in=" + UPDATED_PERSONAL_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByPersonalPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where personalPhoneNumber is not null
        defaultEmployeeShouldBeFound("personalPhoneNumber.specified=true");

        // Get all the employeeList where personalPhoneNumber is null
        defaultEmployeeShouldNotBeFound("personalPhoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPersonalPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where personalPhoneNumber contains DEFAULT_PERSONAL_PHONE_NUMBER
        defaultEmployeeShouldBeFound("personalPhoneNumber.contains=" + DEFAULT_PERSONAL_PHONE_NUMBER);

        // Get all the employeeList where personalPhoneNumber contains UPDATED_PERSONAL_PHONE_NUMBER
        defaultEmployeeShouldNotBeFound("personalPhoneNumber.contains=" + UPDATED_PERSONAL_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByPersonalPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where personalPhoneNumber does not contain DEFAULT_PERSONAL_PHONE_NUMBER
        defaultEmployeeShouldNotBeFound("personalPhoneNumber.doesNotContain=" + DEFAULT_PERSONAL_PHONE_NUMBER);

        // Get all the employeeList where personalPhoneNumber does not contain UPDATED_PERSONAL_PHONE_NUMBER
        defaultEmployeeShouldBeFound("personalPhoneNumber.doesNotContain=" + UPDATED_PERSONAL_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByPermanentAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where permanentAddress equals to DEFAULT_PERMANENT_ADDRESS
        defaultEmployeeShouldBeFound("permanentAddress.equals=" + DEFAULT_PERMANENT_ADDRESS);

        // Get all the employeeList where permanentAddress equals to UPDATED_PERMANENT_ADDRESS
        defaultEmployeeShouldNotBeFound("permanentAddress.equals=" + UPDATED_PERMANENT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeesByPermanentAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where permanentAddress not equals to DEFAULT_PERMANENT_ADDRESS
        defaultEmployeeShouldNotBeFound("permanentAddress.notEquals=" + DEFAULT_PERMANENT_ADDRESS);

        // Get all the employeeList where permanentAddress not equals to UPDATED_PERMANENT_ADDRESS
        defaultEmployeeShouldBeFound("permanentAddress.notEquals=" + UPDATED_PERMANENT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeesByPermanentAddressIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where permanentAddress in DEFAULT_PERMANENT_ADDRESS or UPDATED_PERMANENT_ADDRESS
        defaultEmployeeShouldBeFound("permanentAddress.in=" + DEFAULT_PERMANENT_ADDRESS + "," + UPDATED_PERMANENT_ADDRESS);

        // Get all the employeeList where permanentAddress equals to UPDATED_PERMANENT_ADDRESS
        defaultEmployeeShouldNotBeFound("permanentAddress.in=" + UPDATED_PERMANENT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeesByPermanentAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where permanentAddress is not null
        defaultEmployeeShouldBeFound("permanentAddress.specified=true");

        // Get all the employeeList where permanentAddress is null
        defaultEmployeeShouldNotBeFound("permanentAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPermanentAddressContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where permanentAddress contains DEFAULT_PERMANENT_ADDRESS
        defaultEmployeeShouldBeFound("permanentAddress.contains=" + DEFAULT_PERMANENT_ADDRESS);

        // Get all the employeeList where permanentAddress contains UPDATED_PERMANENT_ADDRESS
        defaultEmployeeShouldNotBeFound("permanentAddress.contains=" + UPDATED_PERMANENT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeesByPermanentAddressNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where permanentAddress does not contain DEFAULT_PERMANENT_ADDRESS
        defaultEmployeeShouldNotBeFound("permanentAddress.doesNotContain=" + DEFAULT_PERMANENT_ADDRESS);

        // Get all the employeeList where permanentAddress does not contain UPDATED_PERMANENT_ADDRESS
        defaultEmployeeShouldBeFound("permanentAddress.doesNotContain=" + UPDATED_PERMANENT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeesByTemporaryAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where temporaryAddress equals to DEFAULT_TEMPORARY_ADDRESS
        defaultEmployeeShouldBeFound("temporaryAddress.equals=" + DEFAULT_TEMPORARY_ADDRESS);

        // Get all the employeeList where temporaryAddress equals to UPDATED_TEMPORARY_ADDRESS
        defaultEmployeeShouldNotBeFound("temporaryAddress.equals=" + UPDATED_TEMPORARY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeesByTemporaryAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where temporaryAddress not equals to DEFAULT_TEMPORARY_ADDRESS
        defaultEmployeeShouldNotBeFound("temporaryAddress.notEquals=" + DEFAULT_TEMPORARY_ADDRESS);

        // Get all the employeeList where temporaryAddress not equals to UPDATED_TEMPORARY_ADDRESS
        defaultEmployeeShouldBeFound("temporaryAddress.notEquals=" + UPDATED_TEMPORARY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeesByTemporaryAddressIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where temporaryAddress in DEFAULT_TEMPORARY_ADDRESS or UPDATED_TEMPORARY_ADDRESS
        defaultEmployeeShouldBeFound("temporaryAddress.in=" + DEFAULT_TEMPORARY_ADDRESS + "," + UPDATED_TEMPORARY_ADDRESS);

        // Get all the employeeList where temporaryAddress equals to UPDATED_TEMPORARY_ADDRESS
        defaultEmployeeShouldNotBeFound("temporaryAddress.in=" + UPDATED_TEMPORARY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeesByTemporaryAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where temporaryAddress is not null
        defaultEmployeeShouldBeFound("temporaryAddress.specified=true");

        // Get all the employeeList where temporaryAddress is null
        defaultEmployeeShouldNotBeFound("temporaryAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByTemporaryAddressContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where temporaryAddress contains DEFAULT_TEMPORARY_ADDRESS
        defaultEmployeeShouldBeFound("temporaryAddress.contains=" + DEFAULT_TEMPORARY_ADDRESS);

        // Get all the employeeList where temporaryAddress contains UPDATED_TEMPORARY_ADDRESS
        defaultEmployeeShouldNotBeFound("temporaryAddress.contains=" + UPDATED_TEMPORARY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeesByTemporaryAddressNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where temporaryAddress does not contain DEFAULT_TEMPORARY_ADDRESS
        defaultEmployeeShouldNotBeFound("temporaryAddress.doesNotContain=" + DEFAULT_TEMPORARY_ADDRESS);

        // Get all the employeeList where temporaryAddress does not contain UPDATED_TEMPORARY_ADDRESS
        defaultEmployeeShouldBeFound("temporaryAddress.doesNotContain=" + UPDATED_TEMPORARY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idNumber equals to DEFAULT_ID_NUMBER
        defaultEmployeeShouldBeFound("idNumber.equals=" + DEFAULT_ID_NUMBER);

        // Get all the employeeList where idNumber equals to UPDATED_ID_NUMBER
        defaultEmployeeShouldNotBeFound("idNumber.equals=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idNumber not equals to DEFAULT_ID_NUMBER
        defaultEmployeeShouldNotBeFound("idNumber.notEquals=" + DEFAULT_ID_NUMBER);

        // Get all the employeeList where idNumber not equals to UPDATED_ID_NUMBER
        defaultEmployeeShouldBeFound("idNumber.notEquals=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idNumber in DEFAULT_ID_NUMBER or UPDATED_ID_NUMBER
        defaultEmployeeShouldBeFound("idNumber.in=" + DEFAULT_ID_NUMBER + "," + UPDATED_ID_NUMBER);

        // Get all the employeeList where idNumber equals to UPDATED_ID_NUMBER
        defaultEmployeeShouldNotBeFound("idNumber.in=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idNumber is not null
        defaultEmployeeShouldBeFound("idNumber.specified=true");

        // Get all the employeeList where idNumber is null
        defaultEmployeeShouldNotBeFound("idNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByIdNumberContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idNumber contains DEFAULT_ID_NUMBER
        defaultEmployeeShouldBeFound("idNumber.contains=" + DEFAULT_ID_NUMBER);

        // Get all the employeeList where idNumber contains UPDATED_ID_NUMBER
        defaultEmployeeShouldNotBeFound("idNumber.contains=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdNumberNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idNumber does not contain DEFAULT_ID_NUMBER
        defaultEmployeeShouldNotBeFound("idNumber.doesNotContain=" + DEFAULT_ID_NUMBER);

        // Get all the employeeList where idNumber does not contain UPDATED_ID_NUMBER
        defaultEmployeeShouldBeFound("idNumber.doesNotContain=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedDate equals to DEFAULT_ID_ISSUED_DATE
        defaultEmployeeShouldBeFound("idIssuedDate.equals=" + DEFAULT_ID_ISSUED_DATE);

        // Get all the employeeList where idIssuedDate equals to UPDATED_ID_ISSUED_DATE
        defaultEmployeeShouldNotBeFound("idIssuedDate.equals=" + UPDATED_ID_ISSUED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedDate not equals to DEFAULT_ID_ISSUED_DATE
        defaultEmployeeShouldNotBeFound("idIssuedDate.notEquals=" + DEFAULT_ID_ISSUED_DATE);

        // Get all the employeeList where idIssuedDate not equals to UPDATED_ID_ISSUED_DATE
        defaultEmployeeShouldBeFound("idIssuedDate.notEquals=" + UPDATED_ID_ISSUED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedDate in DEFAULT_ID_ISSUED_DATE or UPDATED_ID_ISSUED_DATE
        defaultEmployeeShouldBeFound("idIssuedDate.in=" + DEFAULT_ID_ISSUED_DATE + "," + UPDATED_ID_ISSUED_DATE);

        // Get all the employeeList where idIssuedDate equals to UPDATED_ID_ISSUED_DATE
        defaultEmployeeShouldNotBeFound("idIssuedDate.in=" + UPDATED_ID_ISSUED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedDate is not null
        defaultEmployeeShouldBeFound("idIssuedDate.specified=true");

        // Get all the employeeList where idIssuedDate is null
        defaultEmployeeShouldNotBeFound("idIssuedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedDate is greater than or equal to DEFAULT_ID_ISSUED_DATE
        defaultEmployeeShouldBeFound("idIssuedDate.greaterThanOrEqual=" + DEFAULT_ID_ISSUED_DATE);

        // Get all the employeeList where idIssuedDate is greater than or equal to UPDATED_ID_ISSUED_DATE
        defaultEmployeeShouldNotBeFound("idIssuedDate.greaterThanOrEqual=" + UPDATED_ID_ISSUED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedDate is less than or equal to DEFAULT_ID_ISSUED_DATE
        defaultEmployeeShouldBeFound("idIssuedDate.lessThanOrEqual=" + DEFAULT_ID_ISSUED_DATE);

        // Get all the employeeList where idIssuedDate is less than or equal to SMALLER_ID_ISSUED_DATE
        defaultEmployeeShouldNotBeFound("idIssuedDate.lessThanOrEqual=" + SMALLER_ID_ISSUED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedDate is less than DEFAULT_ID_ISSUED_DATE
        defaultEmployeeShouldNotBeFound("idIssuedDate.lessThan=" + DEFAULT_ID_ISSUED_DATE);

        // Get all the employeeList where idIssuedDate is less than UPDATED_ID_ISSUED_DATE
        defaultEmployeeShouldBeFound("idIssuedDate.lessThan=" + UPDATED_ID_ISSUED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedDate is greater than DEFAULT_ID_ISSUED_DATE
        defaultEmployeeShouldNotBeFound("idIssuedDate.greaterThan=" + DEFAULT_ID_ISSUED_DATE);

        // Get all the employeeList where idIssuedDate is greater than SMALLER_ID_ISSUED_DATE
        defaultEmployeeShouldBeFound("idIssuedDate.greaterThan=" + SMALLER_ID_ISSUED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedLocation equals to DEFAULT_ID_ISSUED_LOCATION
        defaultEmployeeShouldBeFound("idIssuedLocation.equals=" + DEFAULT_ID_ISSUED_LOCATION);

        // Get all the employeeList where idIssuedLocation equals to UPDATED_ID_ISSUED_LOCATION
        defaultEmployeeShouldNotBeFound("idIssuedLocation.equals=" + UPDATED_ID_ISSUED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedLocation not equals to DEFAULT_ID_ISSUED_LOCATION
        defaultEmployeeShouldNotBeFound("idIssuedLocation.notEquals=" + DEFAULT_ID_ISSUED_LOCATION);

        // Get all the employeeList where idIssuedLocation not equals to UPDATED_ID_ISSUED_LOCATION
        defaultEmployeeShouldBeFound("idIssuedLocation.notEquals=" + UPDATED_ID_ISSUED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedLocationIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedLocation in DEFAULT_ID_ISSUED_LOCATION or UPDATED_ID_ISSUED_LOCATION
        defaultEmployeeShouldBeFound("idIssuedLocation.in=" + DEFAULT_ID_ISSUED_LOCATION + "," + UPDATED_ID_ISSUED_LOCATION);

        // Get all the employeeList where idIssuedLocation equals to UPDATED_ID_ISSUED_LOCATION
        defaultEmployeeShouldNotBeFound("idIssuedLocation.in=" + UPDATED_ID_ISSUED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedLocation is not null
        defaultEmployeeShouldBeFound("idIssuedLocation.specified=true");

        // Get all the employeeList where idIssuedLocation is null
        defaultEmployeeShouldNotBeFound("idIssuedLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedLocationContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedLocation contains DEFAULT_ID_ISSUED_LOCATION
        defaultEmployeeShouldBeFound("idIssuedLocation.contains=" + DEFAULT_ID_ISSUED_LOCATION);

        // Get all the employeeList where idIssuedLocation contains UPDATED_ID_ISSUED_LOCATION
        defaultEmployeeShouldNotBeFound("idIssuedLocation.contains=" + UPDATED_ID_ISSUED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByIdIssuedLocationNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where idIssuedLocation does not contain DEFAULT_ID_ISSUED_LOCATION
        defaultEmployeeShouldNotBeFound("idIssuedLocation.doesNotContain=" + DEFAULT_ID_ISSUED_LOCATION);

        // Get all the employeeList where idIssuedLocation does not contain UPDATED_ID_ISSUED_LOCATION
        defaultEmployeeShouldBeFound("idIssuedLocation.doesNotContain=" + UPDATED_ID_ISSUED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEmployeesBySocialInsuranceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialInsuranceNumber equals to DEFAULT_SOCIAL_INSURANCE_NUMBER
        defaultEmployeeShouldBeFound("socialInsuranceNumber.equals=" + DEFAULT_SOCIAL_INSURANCE_NUMBER);

        // Get all the employeeList where socialInsuranceNumber equals to UPDATED_SOCIAL_INSURANCE_NUMBER
        defaultEmployeeShouldNotBeFound("socialInsuranceNumber.equals=" + UPDATED_SOCIAL_INSURANCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesBySocialInsuranceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialInsuranceNumber not equals to DEFAULT_SOCIAL_INSURANCE_NUMBER
        defaultEmployeeShouldNotBeFound("socialInsuranceNumber.notEquals=" + DEFAULT_SOCIAL_INSURANCE_NUMBER);

        // Get all the employeeList where socialInsuranceNumber not equals to UPDATED_SOCIAL_INSURANCE_NUMBER
        defaultEmployeeShouldBeFound("socialInsuranceNumber.notEquals=" + UPDATED_SOCIAL_INSURANCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesBySocialInsuranceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialInsuranceNumber in DEFAULT_SOCIAL_INSURANCE_NUMBER or UPDATED_SOCIAL_INSURANCE_NUMBER
        defaultEmployeeShouldBeFound("socialInsuranceNumber.in=" + DEFAULT_SOCIAL_INSURANCE_NUMBER + "," + UPDATED_SOCIAL_INSURANCE_NUMBER);

        // Get all the employeeList where socialInsuranceNumber equals to UPDATED_SOCIAL_INSURANCE_NUMBER
        defaultEmployeeShouldNotBeFound("socialInsuranceNumber.in=" + UPDATED_SOCIAL_INSURANCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesBySocialInsuranceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialInsuranceNumber is not null
        defaultEmployeeShouldBeFound("socialInsuranceNumber.specified=true");

        // Get all the employeeList where socialInsuranceNumber is null
        defaultEmployeeShouldNotBeFound("socialInsuranceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesBySocialInsuranceNumberContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialInsuranceNumber contains DEFAULT_SOCIAL_INSURANCE_NUMBER
        defaultEmployeeShouldBeFound("socialInsuranceNumber.contains=" + DEFAULT_SOCIAL_INSURANCE_NUMBER);

        // Get all the employeeList where socialInsuranceNumber contains UPDATED_SOCIAL_INSURANCE_NUMBER
        defaultEmployeeShouldNotBeFound("socialInsuranceNumber.contains=" + UPDATED_SOCIAL_INSURANCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesBySocialInsuranceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialInsuranceNumber does not contain DEFAULT_SOCIAL_INSURANCE_NUMBER
        defaultEmployeeShouldNotBeFound("socialInsuranceNumber.doesNotContain=" + DEFAULT_SOCIAL_INSURANCE_NUMBER);

        // Get all the employeeList where socialInsuranceNumber does not contain UPDATED_SOCIAL_INSURANCE_NUMBER
        defaultEmployeeShouldBeFound("socialInsuranceNumber.doesNotContain=" + UPDATED_SOCIAL_INSURANCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByTaxIdentificationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where taxIdentificationNumber equals to DEFAULT_TAX_IDENTIFICATION_NUMBER
        defaultEmployeeShouldBeFound("taxIdentificationNumber.equals=" + DEFAULT_TAX_IDENTIFICATION_NUMBER);

        // Get all the employeeList where taxIdentificationNumber equals to UPDATED_TAX_IDENTIFICATION_NUMBER
        defaultEmployeeShouldNotBeFound("taxIdentificationNumber.equals=" + UPDATED_TAX_IDENTIFICATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByTaxIdentificationNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where taxIdentificationNumber not equals to DEFAULT_TAX_IDENTIFICATION_NUMBER
        defaultEmployeeShouldNotBeFound("taxIdentificationNumber.notEquals=" + DEFAULT_TAX_IDENTIFICATION_NUMBER);

        // Get all the employeeList where taxIdentificationNumber not equals to UPDATED_TAX_IDENTIFICATION_NUMBER
        defaultEmployeeShouldBeFound("taxIdentificationNumber.notEquals=" + UPDATED_TAX_IDENTIFICATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByTaxIdentificationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where taxIdentificationNumber in DEFAULT_TAX_IDENTIFICATION_NUMBER or UPDATED_TAX_IDENTIFICATION_NUMBER
        defaultEmployeeShouldBeFound(
            "taxIdentificationNumber.in=" + DEFAULT_TAX_IDENTIFICATION_NUMBER + "," + UPDATED_TAX_IDENTIFICATION_NUMBER
        );

        // Get all the employeeList where taxIdentificationNumber equals to UPDATED_TAX_IDENTIFICATION_NUMBER
        defaultEmployeeShouldNotBeFound("taxIdentificationNumber.in=" + UPDATED_TAX_IDENTIFICATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByTaxIdentificationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where taxIdentificationNumber is not null
        defaultEmployeeShouldBeFound("taxIdentificationNumber.specified=true");

        // Get all the employeeList where taxIdentificationNumber is null
        defaultEmployeeShouldNotBeFound("taxIdentificationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByTaxIdentificationNumberContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where taxIdentificationNumber contains DEFAULT_TAX_IDENTIFICATION_NUMBER
        defaultEmployeeShouldBeFound("taxIdentificationNumber.contains=" + DEFAULT_TAX_IDENTIFICATION_NUMBER);

        // Get all the employeeList where taxIdentificationNumber contains UPDATED_TAX_IDENTIFICATION_NUMBER
        defaultEmployeeShouldNotBeFound("taxIdentificationNumber.contains=" + UPDATED_TAX_IDENTIFICATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByTaxIdentificationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where taxIdentificationNumber does not contain DEFAULT_TAX_IDENTIFICATION_NUMBER
        defaultEmployeeShouldNotBeFound("taxIdentificationNumber.doesNotContain=" + DEFAULT_TAX_IDENTIFICATION_NUMBER);

        // Get all the employeeList where taxIdentificationNumber does not contain UPDATED_TAX_IDENTIFICATION_NUMBER
        defaultEmployeeShouldBeFound("taxIdentificationNumber.doesNotContain=" + UPDATED_TAX_IDENTIFICATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByQualificationIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where qualification equals to DEFAULT_QUALIFICATION
        defaultEmployeeShouldBeFound("qualification.equals=" + DEFAULT_QUALIFICATION);

        // Get all the employeeList where qualification equals to UPDATED_QUALIFICATION
        defaultEmployeeShouldNotBeFound("qualification.equals=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByQualificationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where qualification not equals to DEFAULT_QUALIFICATION
        defaultEmployeeShouldNotBeFound("qualification.notEquals=" + DEFAULT_QUALIFICATION);

        // Get all the employeeList where qualification not equals to UPDATED_QUALIFICATION
        defaultEmployeeShouldBeFound("qualification.notEquals=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByQualificationIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where qualification in DEFAULT_QUALIFICATION or UPDATED_QUALIFICATION
        defaultEmployeeShouldBeFound("qualification.in=" + DEFAULT_QUALIFICATION + "," + UPDATED_QUALIFICATION);

        // Get all the employeeList where qualification equals to UPDATED_QUALIFICATION
        defaultEmployeeShouldNotBeFound("qualification.in=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByQualificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where qualification is not null
        defaultEmployeeShouldBeFound("qualification.specified=true");

        // Get all the employeeList where qualification is null
        defaultEmployeeShouldNotBeFound("qualification.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByQualificationContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where qualification contains DEFAULT_QUALIFICATION
        defaultEmployeeShouldBeFound("qualification.contains=" + DEFAULT_QUALIFICATION);

        // Get all the employeeList where qualification contains UPDATED_QUALIFICATION
        defaultEmployeeShouldNotBeFound("qualification.contains=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByQualificationNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where qualification does not contain DEFAULT_QUALIFICATION
        defaultEmployeeShouldNotBeFound("qualification.doesNotContain=" + DEFAULT_QUALIFICATION);

        // Get all the employeeList where qualification does not contain UPDATED_QUALIFICATION
        defaultEmployeeShouldBeFound("qualification.doesNotContain=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByBankAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankAccount equals to DEFAULT_BANK_ACCOUNT
        defaultEmployeeShouldBeFound("bankAccount.equals=" + DEFAULT_BANK_ACCOUNT);

        // Get all the employeeList where bankAccount equals to UPDATED_BANK_ACCOUNT
        defaultEmployeeShouldNotBeFound("bankAccount.equals=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllEmployeesByBankAccountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankAccount not equals to DEFAULT_BANK_ACCOUNT
        defaultEmployeeShouldNotBeFound("bankAccount.notEquals=" + DEFAULT_BANK_ACCOUNT);

        // Get all the employeeList where bankAccount not equals to UPDATED_BANK_ACCOUNT
        defaultEmployeeShouldBeFound("bankAccount.notEquals=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllEmployeesByBankAccountIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankAccount in DEFAULT_BANK_ACCOUNT or UPDATED_BANK_ACCOUNT
        defaultEmployeeShouldBeFound("bankAccount.in=" + DEFAULT_BANK_ACCOUNT + "," + UPDATED_BANK_ACCOUNT);

        // Get all the employeeList where bankAccount equals to UPDATED_BANK_ACCOUNT
        defaultEmployeeShouldNotBeFound("bankAccount.in=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllEmployeesByBankAccountIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankAccount is not null
        defaultEmployeeShouldBeFound("bankAccount.specified=true");

        // Get all the employeeList where bankAccount is null
        defaultEmployeeShouldNotBeFound("bankAccount.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByBankAccountContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankAccount contains DEFAULT_BANK_ACCOUNT
        defaultEmployeeShouldBeFound("bankAccount.contains=" + DEFAULT_BANK_ACCOUNT);

        // Get all the employeeList where bankAccount contains UPDATED_BANK_ACCOUNT
        defaultEmployeeShouldNotBeFound("bankAccount.contains=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllEmployeesByBankAccountNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankAccount does not contain DEFAULT_BANK_ACCOUNT
        defaultEmployeeShouldNotBeFound("bankAccount.doesNotContain=" + DEFAULT_BANK_ACCOUNT);

        // Get all the employeeList where bankAccount does not contain UPDATED_BANK_ACCOUNT
        defaultEmployeeShouldBeFound("bankAccount.doesNotContain=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllEmployeesByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankCode equals to DEFAULT_BANK_CODE
        defaultEmployeeShouldBeFound("bankCode.equals=" + DEFAULT_BANK_CODE);

        // Get all the employeeList where bankCode equals to UPDATED_BANK_CODE
        defaultEmployeeShouldNotBeFound("bankCode.equals=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBankCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankCode not equals to DEFAULT_BANK_CODE
        defaultEmployeeShouldNotBeFound("bankCode.notEquals=" + DEFAULT_BANK_CODE);

        // Get all the employeeList where bankCode not equals to UPDATED_BANK_CODE
        defaultEmployeeShouldBeFound("bankCode.notEquals=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBankCodeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankCode in DEFAULT_BANK_CODE or UPDATED_BANK_CODE
        defaultEmployeeShouldBeFound("bankCode.in=" + DEFAULT_BANK_CODE + "," + UPDATED_BANK_CODE);

        // Get all the employeeList where bankCode equals to UPDATED_BANK_CODE
        defaultEmployeeShouldNotBeFound("bankCode.in=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBankCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankCode is not null
        defaultEmployeeShouldBeFound("bankCode.specified=true");

        // Get all the employeeList where bankCode is null
        defaultEmployeeShouldNotBeFound("bankCode.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByBankCodeContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankCode contains DEFAULT_BANK_CODE
        defaultEmployeeShouldBeFound("bankCode.contains=" + DEFAULT_BANK_CODE);

        // Get all the employeeList where bankCode contains UPDATED_BANK_CODE
        defaultEmployeeShouldNotBeFound("bankCode.contains=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBankCodeNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where bankCode does not contain DEFAULT_BANK_CODE
        defaultEmployeeShouldNotBeFound("bankCode.doesNotContain=" + DEFAULT_BANK_CODE);

        // Get all the employeeList where bankCode does not contain UPDATED_BANK_CODE
        defaultEmployeeShouldBeFound("bankCode.doesNotContain=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        User userId;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            userId = UserResourceIT.createEntity(em);
            em.persist(userId);
            em.flush();
        } else {
            userId = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(userId);
        em.flush();
        employee.setUserId(userId);
        employeeRepository.saveAndFlush(employee);
        Long userIdId = userId.getId();

        // Get all the employeeList where userId equals to userIdId
        defaultEmployeeShouldBeFound("userIdId.equals=" + userIdId);

        // Get all the employeeList where userId equals to (userIdId + 1)
        defaultEmployeeShouldNotBeFound("userIdId.equals=" + (userIdId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByAssignmentIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Assignment assignment;
        if (TestUtil.findAll(em, Assignment.class).isEmpty()) {
            assignment = AssignmentResourceIT.createEntity(em);
            em.persist(assignment);
            em.flush();
        } else {
            assignment = TestUtil.findAll(em, Assignment.class).get(0);
        }
        em.persist(assignment);
        em.flush();
        employee.addAssignment(assignment);
        employeeRepository.saveAndFlush(employee);
        Long assignmentId = assignment.getId();

        // Get all the employeeList where assignment equals to assignmentId
        defaultEmployeeShouldBeFound("assignmentId.equals=" + assignmentId);

        // Get all the employeeList where assignment equals to (assignmentId + 1)
        defaultEmployeeShouldNotBeFound("assignmentId.equals=" + (assignmentId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesBySupervisorIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Employee supervisor;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            supervisor = EmployeeResourceIT.createEntity(em);
            em.persist(supervisor);
            em.flush();
        } else {
            supervisor = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(supervisor);
        em.flush();
        employee.setSupervisor(supervisor);
        employeeRepository.saveAndFlush(employee);
        Long supervisorId = supervisor.getId();

        // Get all the employeeList where supervisor equals to supervisorId
        defaultEmployeeShouldBeFound("supervisorId.equals=" + supervisorId);

        // Get all the employeeList where supervisor equals to (supervisorId + 1)
        defaultEmployeeShouldNotBeFound("supervisorId.equals=" + (supervisorId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByJobIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Job job;
        if (TestUtil.findAll(em, Job.class).isEmpty()) {
            job = JobResourceIT.createEntity(em);
            em.persist(job);
            em.flush();
        } else {
            job = TestUtil.findAll(em, Job.class).get(0);
        }
        em.persist(job);
        em.flush();
        employee.setJob(job);
        employeeRepository.saveAndFlush(employee);
        Long jobId = job.getId();

        // Get all the employeeList where job equals to jobId
        defaultEmployeeShouldBeFound("jobId.equals=" + jobId);

        // Get all the employeeList where job equals to (jobId + 1)
        defaultEmployeeShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByTeamMembersIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Employee teamMembers;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            teamMembers = EmployeeResourceIT.createEntity(em);
            em.persist(teamMembers);
            em.flush();
        } else {
            teamMembers = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(teamMembers);
        em.flush();
        employee.addTeamMembers(teamMembers);
        employeeRepository.saveAndFlush(employee);
        Long teamMembersId = teamMembers.getId();

        // Get all the employeeList where teamMembers equals to teamMembersId
        defaultEmployeeShouldBeFound("teamMembersId.equals=" + teamMembersId);

        // Get all the employeeList where teamMembers equals to (teamMembersId + 1)
        defaultEmployeeShouldNotBeFound("teamMembersId.equals=" + (teamMembersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeCode").value(hasItem(DEFAULT_EMPLOYEE_CODE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(sameInstant(DEFAULT_EFFECTIVE_DATE))))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].professionalEmail").value(hasItem(DEFAULT_PROFESSIONAL_EMAIL)))
            .andExpect(jsonPath("$.[*].professionalPhoneNumber").value(hasItem(DEFAULT_PROFESSIONAL_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].commissionPct").value(hasItem(DEFAULT_COMMISSION_PCT.intValue())))
            .andExpect(jsonPath("$.[*].hiredDate").value(hasItem(sameInstant(DEFAULT_HIRED_DATE))))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].contractStartDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_START_DATE))))
            .andExpect(jsonPath("$.[*].contractEndDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_END_DATE))))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contractFileContentType").value(hasItem(DEFAULT_CONTRACT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contractFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTRACT_FILE))))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(sameInstant(DEFAULT_DOB))))
            .andExpect(jsonPath("$.[*].placeOfBirth").value(hasItem(DEFAULT_PLACE_OF_BIRTH)))
            .andExpect(jsonPath("$.[*].personalPhoneNumber").value(hasItem(DEFAULT_PERSONAL_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].permanentAddress").value(hasItem(DEFAULT_PERMANENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].temporaryAddress").value(hasItem(DEFAULT_TEMPORARY_ADDRESS)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].idIssuedDate").value(hasItem(sameInstant(DEFAULT_ID_ISSUED_DATE))))
            .andExpect(jsonPath("$.[*].idIssuedLocation").value(hasItem(DEFAULT_ID_ISSUED_LOCATION)))
            .andExpect(jsonPath("$.[*].socialInsuranceNumber").value(hasItem(DEFAULT_SOCIAL_INSURANCE_NUMBER)))
            .andExpect(jsonPath("$.[*].taxIdentificationNumber").value(hasItem(DEFAULT_TAX_IDENTIFICATION_NUMBER)))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION)))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT)))
            .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE)));

        // Check, that the count call also returns 1
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .employeeCode(UPDATED_EMPLOYEE_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .slogan(UPDATED_SLOGAN)
            .professionalEmail(UPDATED_PROFESSIONAL_EMAIL)
            .professionalPhoneNumber(UPDATED_PROFESSIONAL_PHONE_NUMBER)
            .commissionPct(UPDATED_COMMISSION_PCT)
            .hiredDate(UPDATED_HIRED_DATE)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .contractStartDate(UPDATED_CONTRACT_START_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .contractType(UPDATED_CONTRACT_TYPE)
            .contractFile(UPDATED_CONTRACT_FILE)
            .contractFileContentType(UPDATED_CONTRACT_FILE_CONTENT_TYPE)
            .salary(UPDATED_SALARY)
            .sex(UPDATED_SEX)
            .dob(UPDATED_DOB)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .personalPhoneNumber(UPDATED_PERSONAL_PHONE_NUMBER)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .temporaryAddress(UPDATED_TEMPORARY_ADDRESS)
            .idNumber(UPDATED_ID_NUMBER)
            .idIssuedDate(UPDATED_ID_ISSUED_DATE)
            .idIssuedLocation(UPDATED_ID_ISSUED_LOCATION)
            .socialInsuranceNumber(UPDATED_SOCIAL_INSURANCE_NUMBER)
            .taxIdentificationNumber(UPDATED_TAX_IDENTIFICATION_NUMBER)
            .qualification(UPDATED_QUALIFICATION)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .bankCode(UPDATED_BANK_CODE);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeCode()).isEqualTo(UPDATED_EMPLOYEE_CODE);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testEmployee.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testEmployee.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testEmployee.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testEmployee.getProfessionalEmail()).isEqualTo(UPDATED_PROFESSIONAL_EMAIL);
        assertThat(testEmployee.getProfessionalPhoneNumber()).isEqualTo(UPDATED_PROFESSIONAL_PHONE_NUMBER);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(UPDATED_COMMISSION_PCT);
        assertThat(testEmployee.getHiredDate()).isEqualTo(UPDATED_HIRED_DATE);
        assertThat(testEmployee.getContractNumber()).isEqualTo(UPDATED_CONTRACT_NUMBER);
        assertThat(testEmployee.getContractStartDate()).isEqualTo(UPDATED_CONTRACT_START_DATE);
        assertThat(testEmployee.getContractEndDate()).isEqualTo(UPDATED_CONTRACT_END_DATE);
        assertThat(testEmployee.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
        assertThat(testEmployee.getContractFile()).isEqualTo(UPDATED_CONTRACT_FILE);
        assertThat(testEmployee.getContractFileContentType()).isEqualTo(UPDATED_CONTRACT_FILE_CONTENT_TYPE);
        assertThat(testEmployee.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testEmployee.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testEmployee.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testEmployee.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BIRTH);
        assertThat(testEmployee.getPersonalPhoneNumber()).isEqualTo(UPDATED_PERSONAL_PHONE_NUMBER);
        assertThat(testEmployee.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testEmployee.getTemporaryAddress()).isEqualTo(UPDATED_TEMPORARY_ADDRESS);
        assertThat(testEmployee.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testEmployee.getIdIssuedDate()).isEqualTo(UPDATED_ID_ISSUED_DATE);
        assertThat(testEmployee.getIdIssuedLocation()).isEqualTo(UPDATED_ID_ISSUED_LOCATION);
        assertThat(testEmployee.getSocialInsuranceNumber()).isEqualTo(UPDATED_SOCIAL_INSURANCE_NUMBER);
        assertThat(testEmployee.getTaxIdentificationNumber()).isEqualTo(UPDATED_TAX_IDENTIFICATION_NUMBER);
        assertThat(testEmployee.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
        assertThat(testEmployee.getBankAccount()).isEqualTo(UPDATED_BANK_ACCOUNT);
        assertThat(testEmployee.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .professionalEmail(UPDATED_PROFESSIONAL_EMAIL)
            .professionalPhoneNumber(UPDATED_PROFESSIONAL_PHONE_NUMBER)
            .commissionPct(UPDATED_COMMISSION_PCT)
            .hiredDate(UPDATED_HIRED_DATE)
            .contractType(UPDATED_CONTRACT_TYPE)
            .socialInsuranceNumber(UPDATED_SOCIAL_INSURANCE_NUMBER)
            .bankCode(UPDATED_BANK_CODE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeCode()).isEqualTo(DEFAULT_EMPLOYEE_CODE);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testEmployee.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testEmployee.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testEmployee.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
        assertThat(testEmployee.getProfessionalEmail()).isEqualTo(UPDATED_PROFESSIONAL_EMAIL);
        assertThat(testEmployee.getProfessionalPhoneNumber()).isEqualTo(UPDATED_PROFESSIONAL_PHONE_NUMBER);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(UPDATED_COMMISSION_PCT);
        assertThat(testEmployee.getHiredDate()).isEqualTo(UPDATED_HIRED_DATE);
        assertThat(testEmployee.getContractNumber()).isEqualTo(DEFAULT_CONTRACT_NUMBER);
        assertThat(testEmployee.getContractStartDate()).isEqualTo(DEFAULT_CONTRACT_START_DATE);
        assertThat(testEmployee.getContractEndDate()).isEqualTo(DEFAULT_CONTRACT_END_DATE);
        assertThat(testEmployee.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
        assertThat(testEmployee.getContractFile()).isEqualTo(DEFAULT_CONTRACT_FILE);
        assertThat(testEmployee.getContractFileContentType()).isEqualTo(DEFAULT_CONTRACT_FILE_CONTENT_TYPE);
        assertThat(testEmployee.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testEmployee.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testEmployee.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testEmployee.getPlaceOfBirth()).isEqualTo(DEFAULT_PLACE_OF_BIRTH);
        assertThat(testEmployee.getPersonalPhoneNumber()).isEqualTo(DEFAULT_PERSONAL_PHONE_NUMBER);
        assertThat(testEmployee.getPermanentAddress()).isEqualTo(DEFAULT_PERMANENT_ADDRESS);
        assertThat(testEmployee.getTemporaryAddress()).isEqualTo(DEFAULT_TEMPORARY_ADDRESS);
        assertThat(testEmployee.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testEmployee.getIdIssuedDate()).isEqualTo(DEFAULT_ID_ISSUED_DATE);
        assertThat(testEmployee.getIdIssuedLocation()).isEqualTo(DEFAULT_ID_ISSUED_LOCATION);
        assertThat(testEmployee.getSocialInsuranceNumber()).isEqualTo(UPDATED_SOCIAL_INSURANCE_NUMBER);
        assertThat(testEmployee.getTaxIdentificationNumber()).isEqualTo(DEFAULT_TAX_IDENTIFICATION_NUMBER);
        assertThat(testEmployee.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
        assertThat(testEmployee.getBankAccount()).isEqualTo(DEFAULT_BANK_ACCOUNT);
        assertThat(testEmployee.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .employeeCode(UPDATED_EMPLOYEE_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .slogan(UPDATED_SLOGAN)
            .professionalEmail(UPDATED_PROFESSIONAL_EMAIL)
            .professionalPhoneNumber(UPDATED_PROFESSIONAL_PHONE_NUMBER)
            .commissionPct(UPDATED_COMMISSION_PCT)
            .hiredDate(UPDATED_HIRED_DATE)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .contractStartDate(UPDATED_CONTRACT_START_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .contractType(UPDATED_CONTRACT_TYPE)
            .contractFile(UPDATED_CONTRACT_FILE)
            .contractFileContentType(UPDATED_CONTRACT_FILE_CONTENT_TYPE)
            .salary(UPDATED_SALARY)
            .sex(UPDATED_SEX)
            .dob(UPDATED_DOB)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .personalPhoneNumber(UPDATED_PERSONAL_PHONE_NUMBER)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .temporaryAddress(UPDATED_TEMPORARY_ADDRESS)
            .idNumber(UPDATED_ID_NUMBER)
            .idIssuedDate(UPDATED_ID_ISSUED_DATE)
            .idIssuedLocation(UPDATED_ID_ISSUED_LOCATION)
            .socialInsuranceNumber(UPDATED_SOCIAL_INSURANCE_NUMBER)
            .taxIdentificationNumber(UPDATED_TAX_IDENTIFICATION_NUMBER)
            .qualification(UPDATED_QUALIFICATION)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .bankCode(UPDATED_BANK_CODE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeCode()).isEqualTo(UPDATED_EMPLOYEE_CODE);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testEmployee.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testEmployee.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testEmployee.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testEmployee.getProfessionalEmail()).isEqualTo(UPDATED_PROFESSIONAL_EMAIL);
        assertThat(testEmployee.getProfessionalPhoneNumber()).isEqualTo(UPDATED_PROFESSIONAL_PHONE_NUMBER);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(UPDATED_COMMISSION_PCT);
        assertThat(testEmployee.getHiredDate()).isEqualTo(UPDATED_HIRED_DATE);
        assertThat(testEmployee.getContractNumber()).isEqualTo(UPDATED_CONTRACT_NUMBER);
        assertThat(testEmployee.getContractStartDate()).isEqualTo(UPDATED_CONTRACT_START_DATE);
        assertThat(testEmployee.getContractEndDate()).isEqualTo(UPDATED_CONTRACT_END_DATE);
        assertThat(testEmployee.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
        assertThat(testEmployee.getContractFile()).isEqualTo(UPDATED_CONTRACT_FILE);
        assertThat(testEmployee.getContractFileContentType()).isEqualTo(UPDATED_CONTRACT_FILE_CONTENT_TYPE);
        assertThat(testEmployee.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testEmployee.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testEmployee.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testEmployee.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BIRTH);
        assertThat(testEmployee.getPersonalPhoneNumber()).isEqualTo(UPDATED_PERSONAL_PHONE_NUMBER);
        assertThat(testEmployee.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testEmployee.getTemporaryAddress()).isEqualTo(UPDATED_TEMPORARY_ADDRESS);
        assertThat(testEmployee.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testEmployee.getIdIssuedDate()).isEqualTo(UPDATED_ID_ISSUED_DATE);
        assertThat(testEmployee.getIdIssuedLocation()).isEqualTo(UPDATED_ID_ISSUED_LOCATION);
        assertThat(testEmployee.getSocialInsuranceNumber()).isEqualTo(UPDATED_SOCIAL_INSURANCE_NUMBER);
        assertThat(testEmployee.getTaxIdentificationNumber()).isEqualTo(UPDATED_TAX_IDENTIFICATION_NUMBER);
        assertThat(testEmployee.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
        assertThat(testEmployee.getBankAccount()).isEqualTo(UPDATED_BANK_ACCOUNT);
        assertThat(testEmployee.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
