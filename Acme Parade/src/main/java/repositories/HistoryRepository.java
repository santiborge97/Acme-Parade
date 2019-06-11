
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

	@Query("select avg(h.legalRecords.size + h.periodRecords.size + h.linkRecords.size + h.miscellaneousRecords.size) + 1 from History h")
	Double avgRecordPerHistory();

	@Query("select max(h.legalRecords.size + h.periodRecords.size + h.linkRecords.size + h.miscellaneousRecords.size) + 1 from History h")
	Double maxRecordPerHistory();

	@Query("select min(h.legalRecords.size + h.periodRecords.size + h.linkRecords.size + h.miscellaneousRecords.size) + 1 from History h")
	Double minRecordPerHistory();

	@Query("select stddev(h.legalRecords.size + h.periodRecords.size + h.linkRecords.size + h.miscellaneousRecords.size + 1) from History h")
	Double stddevRecordPerHistory();

	@Query("select h.brotherhood from History h where ((h.legalRecords.size + h.periodRecords.size + h.linkRecords.size + h.miscellaneousRecords.size) + 1) > (select avg(h1.legalRecords.size + h1.periodRecords.size + h1.linkRecords.size + h1.miscellaneousRecords.size) + 1 from History h1)")
	Collection<Brotherhood> brotherhoodsMoreThanAverage();

	@Query("select h.brotherhood from History h where ((h.legalRecords.size + h.periodRecords.size + h.linkRecords.size + h.miscellaneousRecords.size) + 1) = (select max(h1.legalRecords.size + h1.periodRecords.size + h1.linkRecords.size + h1.miscellaneousRecords.size) + 1 from History h1)")
	Collection<Brotherhood> largestBrotherhood();

	@Query("select h from History h where h.brotherhood.id = ?1")
	History findByBrotherhoodId(int brotherhoodId);

	@Query("select ((h.legalRecords.size + h.periodRecords.size + h.linkRecords.size + h.miscellaneousRecords.size) + 1) from History h where h.brotherhood.id = ?1")
	Double recordsPerBrotherhoodId(int id);

	@Query("select h from History h join h.periodRecords pr where pr.id=?1")
	History historyPerPeriodRecordId(int periodRecordId);

	@Query("select h from History h join h.linkRecords lr where lr.id=?1")
	History historyPerLinkRecordId(int linkRecordId);

	@Query("select h from History h join h.legalRecords lr where lr.id=?1")
	History historyPerLegalRecordId(int legalRecordId);

	@Query("select h from History h join h.miscellaneousRecords mr where mr.id=?1")
	History historyPerMiscellaneousRecordId(int miscellaneousRecordId);

	@Query("select h from History h where h.inceptionRecord.id=?1")
	History historyPerInceptionRecordId(int inceptionRecordId);

}
