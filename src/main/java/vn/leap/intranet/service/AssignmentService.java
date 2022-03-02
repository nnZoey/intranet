package vn.leap.intranet.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.leap.intranet.domain.Assignment;
import vn.leap.intranet.repository.AssignmentRepository;

/**
 * Service Implementation for managing {@link Assignment}.
 */
@Service
@Transactional
public class AssignmentService {

    private final Logger log = LoggerFactory.getLogger(AssignmentService.class);

    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * Save a assignment.
     *
     * @param assignment the entity to save.
     * @return the persisted entity.
     */
    public Assignment save(Assignment assignment) {
        log.debug("Request to save Assignment : {}", assignment);
        return assignmentRepository.save(assignment);
    }

    /**
     * Partially update a assignment.
     *
     * @param assignment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Assignment> partialUpdate(Assignment assignment) {
        log.debug("Request to partially update Assignment : {}", assignment);

        return assignmentRepository
            .findById(assignment.getId())
            .map(existingAssignment -> {
                if (assignment.getStartDate() != null) {
                    existingAssignment.setStartDate(assignment.getStartDate());
                }
                if (assignment.getProjectRole() != null) {
                    existingAssignment.setProjectRole(assignment.getProjectRole());
                }

                return existingAssignment;
            })
            .map(assignmentRepository::save);
    }

    /**
     * Get all the assignments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Assignment> findAll() {
        log.debug("Request to get all Assignments");
        return assignmentRepository.findAll();
    }

    /**
     * Get one assignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Assignment> findOne(Long id) {
        log.debug("Request to get Assignment : {}", id);
        return assignmentRepository.findById(id);
    }

    /**
     * Delete the assignment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Assignment : {}", id);
        assignmentRepository.deleteById(id);
    }
}
