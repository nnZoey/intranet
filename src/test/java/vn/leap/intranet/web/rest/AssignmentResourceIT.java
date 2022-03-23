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
import vn.leap.intranet.domain.Assignment;
import vn.leap.intranet.domain.Employee;
import vn.leap.intranet.domain.Project;
import vn.leap.intranet.domain.enumeration.ProjectRole;
import vn.leap.intranet.repository.AssignmentRepository;
import vn.leap.intranet.service.criteria.AssignmentCriteria;

/**
 * Integration tests for the {@link AssignmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssignmentResourceIT {

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ProjectRole DEFAULT_PROJECT_ROLE = ProjectRole.ENGINEER;
    private static final ProjectRole UPDATED_PROJECT_ROLE = ProjectRole.ADMIN;

    private static final String ENTITY_API_URL = "/api/assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssignmentMockMvc;

    private Assignment assignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assignment createEntity(EntityManager em) {
        Assignment assignment = new Assignment().startDate(DEFAULT_START_DATE).projectRole(DEFAULT_PROJECT_ROLE);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        assignment.setProject(project);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        assignment.setEmployee(employee);
        return assignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assignment createUpdatedEntity(EntityManager em) {
        Assignment assignment = new Assignment().startDate(UPDATED_START_DATE).projectRole(UPDATED_PROJECT_ROLE);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        assignment.setProject(project);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        assignment.setEmployee(employee);
        return assignment;
    }

    @BeforeEach
    public void initTest() {
        assignment = createEntity(em);
    }

    @Test
    @Transactional
    void createAssignment() throws Exception {
        int databaseSizeBeforeCreate = assignmentRepository.findAll().size();
        // Create the Assignment
        restAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignment)))
            .andExpect(status().isCreated());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeCreate + 1);
        Assignment testAssignment = assignmentList.get(assignmentList.size() - 1);
        assertThat(testAssignment.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAssignment.getProjectRole()).isEqualTo(DEFAULT_PROJECT_ROLE);
    }

    @Test
    @Transactional
    void createAssignmentWithExistingId() throws Exception {
        // Create the Assignment with an existing ID
        assignment.setId(1L);

        int databaseSizeBeforeCreate = assignmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignment)))
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().size();
        // set the field null
        assignment.setStartDate(null);

        // Create the Assignment, which fails.

        restAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignment)))
            .andExpect(status().isBadRequest());

        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProjectRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().size();
        // set the field null
        assignment.setProjectRole(null);

        // Create the Assignment, which fails.

        restAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignment)))
            .andExpect(status().isBadRequest());

        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssignments() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].projectRole").value(hasItem(DEFAULT_PROJECT_ROLE.toString())));
    }

    @Test
    @Transactional
    void getAssignment() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get the assignment
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, assignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assignment.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.projectRole").value(DEFAULT_PROJECT_ROLE.toString()));
    }

    @Test
    @Transactional
    void getAssignmentsByIdFiltering() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        Long id = assignment.getId();

        defaultAssignmentShouldBeFound("id.equals=" + id);
        defaultAssignmentShouldNotBeFound("id.notEquals=" + id);

        defaultAssignmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssignmentShouldNotBeFound("id.greaterThan=" + id);

        defaultAssignmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssignmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssignmentsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where startDate equals to DEFAULT_START_DATE
        defaultAssignmentShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the assignmentList where startDate equals to UPDATED_START_DATE
        defaultAssignmentShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where startDate not equals to DEFAULT_START_DATE
        defaultAssignmentShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the assignmentList where startDate not equals to UPDATED_START_DATE
        defaultAssignmentShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultAssignmentShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the assignmentList where startDate equals to UPDATED_START_DATE
        defaultAssignmentShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where startDate is not null
        defaultAssignmentShouldBeFound("startDate.specified=true");

        // Get all the assignmentList where startDate is null
        defaultAssignmentShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultAssignmentShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the assignmentList where startDate is greater than or equal to UPDATED_START_DATE
        defaultAssignmentShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where startDate is less than or equal to DEFAULT_START_DATE
        defaultAssignmentShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the assignmentList where startDate is less than or equal to SMALLER_START_DATE
        defaultAssignmentShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where startDate is less than DEFAULT_START_DATE
        defaultAssignmentShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the assignmentList where startDate is less than UPDATED_START_DATE
        defaultAssignmentShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where startDate is greater than DEFAULT_START_DATE
        defaultAssignmentShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the assignmentList where startDate is greater than SMALLER_START_DATE
        defaultAssignmentShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByProjectRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where projectRole equals to DEFAULT_PROJECT_ROLE
        defaultAssignmentShouldBeFound("projectRole.equals=" + DEFAULT_PROJECT_ROLE);

        // Get all the assignmentList where projectRole equals to UPDATED_PROJECT_ROLE
        defaultAssignmentShouldNotBeFound("projectRole.equals=" + UPDATED_PROJECT_ROLE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByProjectRoleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where projectRole not equals to DEFAULT_PROJECT_ROLE
        defaultAssignmentShouldNotBeFound("projectRole.notEquals=" + DEFAULT_PROJECT_ROLE);

        // Get all the assignmentList where projectRole not equals to UPDATED_PROJECT_ROLE
        defaultAssignmentShouldBeFound("projectRole.notEquals=" + UPDATED_PROJECT_ROLE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByProjectRoleIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where projectRole in DEFAULT_PROJECT_ROLE or UPDATED_PROJECT_ROLE
        defaultAssignmentShouldBeFound("projectRole.in=" + DEFAULT_PROJECT_ROLE + "," + UPDATED_PROJECT_ROLE);

        // Get all the assignmentList where projectRole equals to UPDATED_PROJECT_ROLE
        defaultAssignmentShouldNotBeFound("projectRole.in=" + UPDATED_PROJECT_ROLE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByProjectRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where projectRole is not null
        defaultAssignmentShouldBeFound("projectRole.specified=true");

        // Get all the assignmentList where projectRole is null
        defaultAssignmentShouldNotBeFound("projectRole.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(project);
        em.flush();
        assignment.setProject(project);
        assignmentRepository.saveAndFlush(assignment);
        Long projectId = project.getId();

        // Get all the assignmentList where project equals to projectId
        defaultAssignmentShouldBeFound("projectId.equals=" + projectId);

        // Get all the assignmentList where project equals to (projectId + 1)
        defaultAssignmentShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllAssignmentsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        assignment.setEmployee(employee);
        assignmentRepository.saveAndFlush(assignment);
        Long employeeId = employee.getId();

        // Get all the assignmentList where employee equals to employeeId
        defaultAssignmentShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the assignmentList where employee equals to (employeeId + 1)
        defaultAssignmentShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssignmentShouldBeFound(String filter) throws Exception {
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].projectRole").value(hasItem(DEFAULT_PROJECT_ROLE.toString())));

        // Check, that the count call also returns 1
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssignmentShouldNotBeFound(String filter) throws Exception {
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssignment() throws Exception {
        // Get the assignment
        restAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssignment() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();

        // Update the assignment
        Assignment updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        // Disconnect from session so that the updates on updatedAssignment are not directly saved in db
        em.detach(updatedAssignment);
        updatedAssignment.startDate(UPDATED_START_DATE).projectRole(UPDATED_PROJECT_ROLE);

        restAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAssignment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAssignment))
            )
            .andExpect(status().isOk());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
        Assignment testAssignment = assignmentList.get(assignmentList.size() - 1);
        assertThat(testAssignment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAssignment.getProjectRole()).isEqualTo(UPDATED_PROJECT_ROLE);
    }

    @Test
    @Transactional
    void putNonExistingAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();
        assignment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assignment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assignment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();
        assignment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assignment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();
        assignment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssignmentWithPatch() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();

        // Update the assignment using partial update
        Assignment partialUpdatedAssignment = new Assignment();
        partialUpdatedAssignment.setId(assignment.getId());

        partialUpdatedAssignment.startDate(UPDATED_START_DATE).projectRole(UPDATED_PROJECT_ROLE);

        restAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssignment))
            )
            .andExpect(status().isOk());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
        Assignment testAssignment = assignmentList.get(assignmentList.size() - 1);
        assertThat(testAssignment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAssignment.getProjectRole()).isEqualTo(UPDATED_PROJECT_ROLE);
    }

    @Test
    @Transactional
    void fullUpdateAssignmentWithPatch() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();

        // Update the assignment using partial update
        Assignment partialUpdatedAssignment = new Assignment();
        partialUpdatedAssignment.setId(assignment.getId());

        partialUpdatedAssignment.startDate(UPDATED_START_DATE).projectRole(UPDATED_PROJECT_ROLE);

        restAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssignment))
            )
            .andExpect(status().isOk());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
        Assignment testAssignment = assignmentList.get(assignmentList.size() - 1);
        assertThat(testAssignment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAssignment.getProjectRole()).isEqualTo(UPDATED_PROJECT_ROLE);
    }

    @Test
    @Transactional
    void patchNonExistingAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();
        assignment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assignment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();
        assignment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assignment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();
        assignment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assignment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssignment() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        int databaseSizeBeforeDelete = assignmentRepository.findAll().size();

        // Delete the assignment
        restAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, assignment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
