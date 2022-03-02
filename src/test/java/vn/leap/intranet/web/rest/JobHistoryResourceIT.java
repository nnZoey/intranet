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
import vn.leap.intranet.IntegrationTest;
import vn.leap.intranet.domain.Employee;
import vn.leap.intranet.domain.JobHistory;
import vn.leap.intranet.repository.JobHistoryRepository;

/**
 * Integration tests for the {@link JobHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JobHistoryResourceIT {

    private static final String DEFAULT_EMPLOYEE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EFFECTIVE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EFFECTIVE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_SALARY = 1000000L;
    private static final Long UPDATED_SALARY = 1000001L;

    private static final Long DEFAULT_COMMISSION_PCT = 1L;
    private static final Long UPDATED_COMMISSION_PCT = 2L;

    private static final String DEFAULT_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CONTRACT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTRACT_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CONTRACT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTRACT_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CONTRACT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/job-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobHistoryMockMvc;

    private JobHistory jobHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobHistory createEntity(EntityManager em) {
        JobHistory jobHistory = new JobHistory()
            .employeeCode(DEFAULT_EMPLOYEE_CODE)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .salary(DEFAULT_SALARY)
            .commissionPct(DEFAULT_COMMISSION_PCT)
            .contractNumber(DEFAULT_CONTRACT_NUMBER)
            .contractStartDate(DEFAULT_CONTRACT_START_DATE)
            .contractEndDate(DEFAULT_CONTRACT_END_DATE)
            .contractType(DEFAULT_CONTRACT_TYPE);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        jobHistory.setEmployee(employee);
        return jobHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobHistory createUpdatedEntity(EntityManager em) {
        JobHistory jobHistory = new JobHistory()
            .employeeCode(UPDATED_EMPLOYEE_CODE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .salary(UPDATED_SALARY)
            .commissionPct(UPDATED_COMMISSION_PCT)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .contractStartDate(UPDATED_CONTRACT_START_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .contractType(UPDATED_CONTRACT_TYPE);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        jobHistory.setEmployee(employee);
        return jobHistory;
    }

    @BeforeEach
    public void initTest() {
        jobHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createJobHistory() throws Exception {
        int databaseSizeBeforeCreate = jobHistoryRepository.findAll().size();
        // Create the JobHistory
        restJobHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isCreated());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        JobHistory testJobHistory = jobHistoryList.get(jobHistoryList.size() - 1);
        assertThat(testJobHistory.getEmployeeCode()).isEqualTo(DEFAULT_EMPLOYEE_CODE);
        assertThat(testJobHistory.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testJobHistory.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testJobHistory.getCommissionPct()).isEqualTo(DEFAULT_COMMISSION_PCT);
        assertThat(testJobHistory.getContractNumber()).isEqualTo(DEFAULT_CONTRACT_NUMBER);
        assertThat(testJobHistory.getContractStartDate()).isEqualTo(DEFAULT_CONTRACT_START_DATE);
        assertThat(testJobHistory.getContractEndDate()).isEqualTo(DEFAULT_CONTRACT_END_DATE);
        assertThat(testJobHistory.getContractType()).isEqualTo(DEFAULT_CONTRACT_TYPE);
    }

    @Test
    @Transactional
    void createJobHistoryWithExistingId() throws Exception {
        // Create the JobHistory with an existing ID
        jobHistory.setId(1L);

        int databaseSizeBeforeCreate = jobHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmployeeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobHistoryRepository.findAll().size();
        // set the field null
        jobHistory.setEmployeeCode(null);

        // Create the JobHistory, which fails.

        restJobHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isBadRequest());

        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEffectiveDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobHistoryRepository.findAll().size();
        // set the field null
        jobHistory.setEffectiveDate(null);

        // Create the JobHistory, which fails.

        restJobHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isBadRequest());

        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSalaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobHistoryRepository.findAll().size();
        // set the field null
        jobHistory.setSalary(null);

        // Create the JobHistory, which fails.

        restJobHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isBadRequest());

        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommissionPctIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobHistoryRepository.findAll().size();
        // set the field null
        jobHistory.setCommissionPct(null);

        // Create the JobHistory, which fails.

        restJobHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isBadRequest());

        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobHistoryRepository.findAll().size();
        // set the field null
        jobHistory.setContractNumber(null);

        // Create the JobHistory, which fails.

        restJobHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isBadRequest());

        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobHistoryRepository.findAll().size();
        // set the field null
        jobHistory.setContractStartDate(null);

        // Create the JobHistory, which fails.

        restJobHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isBadRequest());

        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobHistoryRepository.findAll().size();
        // set the field null
        jobHistory.setContractEndDate(null);

        // Create the JobHistory, which fails.

        restJobHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isBadRequest());

        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJobHistories() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList
        restJobHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeCode").value(hasItem(DEFAULT_EMPLOYEE_CODE)))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(sameInstant(DEFAULT_EFFECTIVE_DATE))))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].commissionPct").value(hasItem(DEFAULT_COMMISSION_PCT.intValue())))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].contractStartDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_START_DATE))))
            .andExpect(jsonPath("$.[*].contractEndDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_END_DATE))))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE)));
    }

    @Test
    @Transactional
    void getJobHistory() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        // Get the jobHistory
        restJobHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, jobHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobHistory.getId().intValue()))
            .andExpect(jsonPath("$.employeeCode").value(DEFAULT_EMPLOYEE_CODE))
            .andExpect(jsonPath("$.effectiveDate").value(sameInstant(DEFAULT_EFFECTIVE_DATE)))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.intValue()))
            .andExpect(jsonPath("$.commissionPct").value(DEFAULT_COMMISSION_PCT.intValue()))
            .andExpect(jsonPath("$.contractNumber").value(DEFAULT_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.contractStartDate").value(sameInstant(DEFAULT_CONTRACT_START_DATE)))
            .andExpect(jsonPath("$.contractEndDate").value(sameInstant(DEFAULT_CONTRACT_END_DATE)))
            .andExpect(jsonPath("$.contractType").value(DEFAULT_CONTRACT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingJobHistory() throws Exception {
        // Get the jobHistory
        restJobHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJobHistory() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();

        // Update the jobHistory
        JobHistory updatedJobHistory = jobHistoryRepository.findById(jobHistory.getId()).get();
        // Disconnect from session so that the updates on updatedJobHistory are not directly saved in db
        em.detach(updatedJobHistory);
        updatedJobHistory
            .employeeCode(UPDATED_EMPLOYEE_CODE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .salary(UPDATED_SALARY)
            .commissionPct(UPDATED_COMMISSION_PCT)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .contractStartDate(UPDATED_CONTRACT_START_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .contractType(UPDATED_CONTRACT_TYPE);

        restJobHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJobHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJobHistory))
            )
            .andExpect(status().isOk());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
        JobHistory testJobHistory = jobHistoryList.get(jobHistoryList.size() - 1);
        assertThat(testJobHistory.getEmployeeCode()).isEqualTo(UPDATED_EMPLOYEE_CODE);
        assertThat(testJobHistory.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testJobHistory.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testJobHistory.getCommissionPct()).isEqualTo(UPDATED_COMMISSION_PCT);
        assertThat(testJobHistory.getContractNumber()).isEqualTo(UPDATED_CONTRACT_NUMBER);
        assertThat(testJobHistory.getContractStartDate()).isEqualTo(UPDATED_CONTRACT_START_DATE);
        assertThat(testJobHistory.getContractEndDate()).isEqualTo(UPDATED_CONTRACT_END_DATE);
        assertThat(testJobHistory.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingJobHistory() throws Exception {
        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
        jobHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobHistory() throws Exception {
        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
        jobHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobHistory() throws Exception {
        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
        jobHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobHistoryWithPatch() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();

        // Update the jobHistory using partial update
        JobHistory partialUpdatedJobHistory = new JobHistory();
        partialUpdatedJobHistory.setId(jobHistory.getId());

        partialUpdatedJobHistory
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .contractStartDate(UPDATED_CONTRACT_START_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .contractType(UPDATED_CONTRACT_TYPE);

        restJobHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobHistory))
            )
            .andExpect(status().isOk());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
        JobHistory testJobHistory = jobHistoryList.get(jobHistoryList.size() - 1);
        assertThat(testJobHistory.getEmployeeCode()).isEqualTo(DEFAULT_EMPLOYEE_CODE);
        assertThat(testJobHistory.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testJobHistory.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testJobHistory.getCommissionPct()).isEqualTo(DEFAULT_COMMISSION_PCT);
        assertThat(testJobHistory.getContractNumber()).isEqualTo(UPDATED_CONTRACT_NUMBER);
        assertThat(testJobHistory.getContractStartDate()).isEqualTo(UPDATED_CONTRACT_START_DATE);
        assertThat(testJobHistory.getContractEndDate()).isEqualTo(UPDATED_CONTRACT_END_DATE);
        assertThat(testJobHistory.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateJobHistoryWithPatch() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();

        // Update the jobHistory using partial update
        JobHistory partialUpdatedJobHistory = new JobHistory();
        partialUpdatedJobHistory.setId(jobHistory.getId());

        partialUpdatedJobHistory
            .employeeCode(UPDATED_EMPLOYEE_CODE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .salary(UPDATED_SALARY)
            .commissionPct(UPDATED_COMMISSION_PCT)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .contractStartDate(UPDATED_CONTRACT_START_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .contractType(UPDATED_CONTRACT_TYPE);

        restJobHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobHistory))
            )
            .andExpect(status().isOk());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
        JobHistory testJobHistory = jobHistoryList.get(jobHistoryList.size() - 1);
        assertThat(testJobHistory.getEmployeeCode()).isEqualTo(UPDATED_EMPLOYEE_CODE);
        assertThat(testJobHistory.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testJobHistory.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testJobHistory.getCommissionPct()).isEqualTo(UPDATED_COMMISSION_PCT);
        assertThat(testJobHistory.getContractNumber()).isEqualTo(UPDATED_CONTRACT_NUMBER);
        assertThat(testJobHistory.getContractStartDate()).isEqualTo(UPDATED_CONTRACT_START_DATE);
        assertThat(testJobHistory.getContractEndDate()).isEqualTo(UPDATED_CONTRACT_END_DATE);
        assertThat(testJobHistory.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingJobHistory() throws Exception {
        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
        jobHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobHistory() throws Exception {
        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
        jobHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobHistory() throws Exception {
        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
        jobHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jobHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJobHistory() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        int databaseSizeBeforeDelete = jobHistoryRepository.findAll().size();

        // Delete the jobHistory
        restJobHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
