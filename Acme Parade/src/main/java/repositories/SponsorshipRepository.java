
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select s from Sponsorship s where s.sponsor.id = ?1")
	Collection<Sponsorship> findAllBySponsorId(int id);

	@Query("select s from Sponsorship s where s.activated = true and s.parade.id = ?1")
	Collection<Sponsorship> findAllByParadeId(int paradeId);

	@Query("select (select count(s1) from Sponsorship s1 where s1.activated = true)/count(s2)*1.0 from Sponsorship s2")
	Double ratioOfActiveSponsorships();

	@Query("select count(s1)/(select count(s2) from Sponsor s2)*1.0 from Sponsorship s1 where s1.activated=true")
	Double averageActiveSponsorshipsPerSponsor();

	@Query("select s from Sponsorship s where s.activated=true and s.sponsor.id =?1")
	Collection<Sponsorship> activeSponsorshipsPerSponsorId(final int sponsorId);

	@Query("select s from Sponsorship s where (s.creditCard.expYear < ?2) or (s.creditCard.expYear = ?2 or s.creditCard.expMonth < ?1)")
	Collection<Sponsorship> findCreditCardExpired(int actualMonth, int actualYear);

	@Query("select s.sponsor.name from Sponsorship s where s.activated = true group by s.sponsor order by count(s) desc")
	Collection<String> rankingSporsorsActivedSponsorships();

	@Query("select s.id from Sponsorship s where s.parade.id = ?1 and s.sponsor.id = ?2")
	Integer findSponsorshipByParadeAndSponsorId(int paradeId, int id);

}
