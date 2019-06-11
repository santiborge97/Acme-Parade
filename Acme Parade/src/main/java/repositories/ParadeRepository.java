
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Parade;

@Repository
public interface ParadeRepository extends JpaRepository<Parade, Integer> {
	
	
	//@Query("")
	//Collection<Parade> findParadeCopy(String title, String description, Date organisationMoment, int maxColumn, int maxRow);

	@Query("select p from Parade p join p.floats f where f.id = ?1")
	Collection<Parade> findParadesByFloatId(int floatId);

	@Query("select count(p) from Parade p where p.ticker = ?1")
	int countParadeWithTicker(String ticker);

	@Query("select p from Parade p where p.brotherhood.id = ?1 order by p.status desc")
	Collection<Parade> findParadeByBrotherhoodId(int brotherhoodId);

	@Query("select p from Parade p where p.finalMode = true")
	Collection<Parade> findParadeCanBeSeen();

	@Query("select p from Parade p where p.finalMode = false and p.brotherhood.id = ?1")
	Collection<Parade> findParadeCannotBeSeenOfBrotherhoodId(int brotherhoodId);

	@Query("select p from Parade p where p.finalMode = true and p.brotherhood.id = ?1 and p.status = 'ACCEPTED'")
	Collection<Parade> findParadeCanBeSeenOfBrotherhoodId(int brotherhoodId);

	@Query("select p from Parade p where p.finalMode = true and p.brotherhood.id = ?1 order by p.status desc")
	Collection<Parade> findParadeCanBeSeenOfBrotherhoodIdForChapter(int brotherhoodId);

	@Query("select p from Parade p join p.brotherhood.members m where m.id = ?1")
	Collection<Parade> findMemberParades(int memberId);
	
	@Query("select distinct(p) from Parade p join p.brotherhood.members m where m.id = ? and p.status = 'ACCEPTED' and p.finalMode = true")
	Collection<Parade> findParadesInWhichThisMemberCanApplyWithoutAnyProblem(int memberId);

	@Query("select (select count(p1) from Parade p1 where p1.finalMode = false )/count(p2)*1.0 from Parade p2 where p2.finalMode = true")
	Double ratioDraftFinalModeParade();

	@Query("select (select count(p1) from Parade p1 where (p1.finalMode = true) and (p1.status = 'SUBMITTED') )/count(p2)*1.0 from Parade p2 where p2.finalMode = true")
	Double ratioSubmitted();

	@Query("select (select count(p1) from Parade p1 where (p1.finalMode = true) and (p1.status = 'REJECTED') )/count(p2)*1.0 from Parade p2 where p2.finalMode = true")
	Double ratioRejected();

	@Query("select (select count(p1) from Parade p1 where (p1.finalMode = true) and (p1.status = 'ACCEPTED') )/count(p2)*1.0 from Parade p2 where p2.finalMode = true")
	Double ratioAccepted();

	@Query("select (select count(p) from Parade p where (select c from Chapter c where c.area.id=p.brotherhood.area.id)!=null)/count(pt)*1.0 from Parade pt")
	Double avgParadesCoordinatedByChapters();

	@Query("select count(p) from Parade p where p.brotherhood.area.id = ?1")
	Integer countParadesByChapterId(int chapterAreaId);

}
