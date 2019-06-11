
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	@Query("select c from Chapter c where c.userAccount.id = ?1")
	Chapter findByUserAccountId(int userAccountId);

	@Query("select c from Chapter c where c.area.id = ?1")
	Chapter findChapterByAreaId(int areaId);

}
