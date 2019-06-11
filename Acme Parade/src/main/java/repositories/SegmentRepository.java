
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Segment;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Integer> {

	@Query("select s1 from Segment s1 where s1.parade.id = ?1 and s1.timeOrigin = (select max(s2.timeOrigin) from Segment s2 where s2.parade.id = ?1)")
	Segment lastSegment(int paradeId);

	@Query("select s from Segment s where s.parade.id =?1")
	Collection<Segment> segmentsPerParade(int paradeId);

	@Query("select s from Segment s where s.contiguous.id = ?1")
	Segment segmentContiguous(int segmentId);
}
