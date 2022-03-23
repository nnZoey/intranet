package vn.leap.intranet.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.leap.intranet.domain.Assignment;
import vn.leap.intranet.repository.AssignmentRepository;
import vn.leap.intranet.service.AssignmentQueryService;
import vn.leap.intranet.service.AssignmentService;
import vn.leap.intranet.service.criteria.AssignmentCriteria;
import vn.leap.intranet.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.leap.intranet.domain.Assignment}.
 */
@RestController
@RequestMapping("/api")
public class AssignmentResource {

    private final Logger log = LoggerFactory.getLogger(AssignmentResource.class);

    private static final String ENTITY_NAME = "assignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssignmentService assignmentService;

    private final AssignmentRepository assignmentRepository;

    private final AssignmentQueryService assignmentQueryService;

    public AssignmentResource(
        AssignmentService assignmentService,
        AssignmentRepository assignmentRepository,
        AssignmentQueryService assignmentQueryService
    ) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
        this.assignmentQueryService = assignmentQueryService;
    }

    /**
     * {@code POST  /assignments} : Create a new assignment.
     *
     * @param assignment the assignment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assignment, or with status {@code 400 (Bad Request)} if the assignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assignments")
    public ResponseEntity<Assignment> createAssignment(@Valid @RequestBody Assignment assignment) throws URISyntaxException {
        log.debug("REST request to save Assignment : {}", assignment);
        if (assignment.getId() != null) {
            throw new BadRequestAlertException("A new assignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Assignment result = assignmentService.save(assignment);
        return ResponseEntity
            .created(new URI("/api/assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assignments/:id} : Updates an existing assignment.
     *
     * @param id the id of the assignment to save.
     * @param assignment the assignment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignment,
     * or with status {@code 400 (Bad Request)} if the assignment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assignment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assignments/{id}")
    public ResponseEntity<Assignment> updateAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Assignment assignment
    ) throws URISyntaxException {
        log.debug("REST request to update Assignment : {}, {}", id, assignment);
        if (assignment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Assignment result = assignmentService.save(assignment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assignment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assignments/:id} : Partial updates given fields of an existing assignment, field will ignore if it is null
     *
     * @param id the id of the assignment to save.
     * @param assignment the assignment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignment,
     * or with status {@code 400 (Bad Request)} if the assignment is not valid,
     * or with status {@code 404 (Not Found)} if the assignment is not found,
     * or with status {@code 500 (Internal Server Error)} if the assignment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assignments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Assignment> partialUpdateAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Assignment assignment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Assignment partially : {}, {}", id, assignment);
        if (assignment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Assignment> result = assignmentService.partialUpdate(assignment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assignment.getId().toString())
        );
    }

    /**
     * {@code GET  /assignments} : get all the assignments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assignments in body.
     */
    @GetMapping("/assignments")
    public ResponseEntity<List<Assignment>> getAllAssignments(AssignmentCriteria criteria) {
        log.debug("REST request to get Assignments by criteria: {}", criteria);
        List<Assignment> entityList = assignmentQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /assignments/count} : count all the assignments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/assignments/count")
    public ResponseEntity<Long> countAssignments(AssignmentCriteria criteria) {
        log.debug("REST request to count Assignments by criteria: {}", criteria);
        return ResponseEntity.ok().body(assignmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /assignments/:id} : get the "id" assignment.
     *
     * @param id the id of the assignment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assignment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assignments/{id}")
    public ResponseEntity<Assignment> getAssignment(@PathVariable Long id) {
        log.debug("REST request to get Assignment : {}", id);
        Optional<Assignment> assignment = assignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assignment);
    }

    /**
     * {@code DELETE  /assignments/:id} : delete the "id" assignment.
     *
     * @param id the id of the assignment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        log.debug("REST request to delete Assignment : {}", id);
        assignmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
