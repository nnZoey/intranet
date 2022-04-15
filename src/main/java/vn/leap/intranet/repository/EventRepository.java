package vn.leap.intranet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.leap.intranet.domain.Event;

/**
 * Spring Data SQL repository for the Event entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Query("Select e from Event e order by e.eventDate ASC")
    List<Event> findAllByEventDateSort();
}
