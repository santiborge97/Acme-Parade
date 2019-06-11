
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.LegalRecordRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Service
@Transactional
public class LegalRecordService {

	// Managed Repository ------------------------
	@Autowired
	private LegalRecordRepository	legalRecordRepository;

	// Suporting services ------------------------

	@Autowired
	private HistoryService			historyService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods -----------------------

	public LegalRecord create() {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		LegalRecord result;

		result = new LegalRecord();

		return result;

	}

	public Collection<LegalRecord> findAll() {

		Collection<LegalRecord> result;

		result = this.legalRecordRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public LegalRecord findOne(final int legalRecordId) {

		LegalRecord result;

		result = this.legalRecordRepository.findOne(legalRecordId);

		return result;
	}

	public LegalRecord save(final LegalRecord legalRecord) {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		Assert.notNull(legalRecord);

		if (legalRecord.getId() != 0) {

			final History h = this.historyService.historyPerLegalRecordId(legalRecord.getId());
			final Brotherhood owner = h.getBrotherhood();

			Assert.isTrue(actor.getId() == owner.getId());

		}

		LegalRecord result;

		result = this.legalRecordRepository.save(legalRecord);

		if (legalRecord.getId() == 0) {

			final Brotherhood brotherhood = (Brotherhood) actor;

			final History history = this.historyService.findByBrotherhoodId(brotherhood.getId());
			Assert.notNull(history);
			final Collection<LegalRecord> lr = history.getLegalRecords();
			lr.add(result);
			history.setLegalRecords(lr);
			this.historyService.save(history);
		}

		return result;
	}

	public void delete(final LegalRecord legalRecord) {

		Assert.notNull(legalRecord);
		Assert.isTrue(legalRecord.getId() != 0);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		final Authority br = new Authority();
		br.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(br));

		final History history = this.historyService.findByBrotherhoodId(actor.getId());
		Assert.notNull(history);

		Assert.isTrue(history.getBrotherhood().getId() == actor.getId());

		history.getLegalRecords().remove(legalRecord);
		this.legalRecordRepository.delete(legalRecord);
	}

	// Other business methods -----------------------

	public Boolean securityLegal(final int legalId) {

		Boolean res = false;
		Collection<LegalRecord> loginLegal = null;

		final LegalRecord owner = this.findOne(legalId);

		final Brotherhood login = this.brotherhoodService.findByPrincipal();
		final History loginHistory = this.historyService.findByBrotherhoodId(login.getId());

		if (loginHistory != null)
			loginLegal = loginHistory.getLegalRecords();

		if (loginLegal != null && owner != null)
			if (loginLegal.contains(owner))
				res = true;

		return res;
	}

	public void flush() {
		this.legalRecordRepository.flush();
	}

}
