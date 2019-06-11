
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	// Managed Repository ------------------------
	@Autowired
	private PeriodRecordRepository	periodRecordRepository;

	// Suporting services ------------------------

	@Autowired
	private HistoryService			historyService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods -----------------------

	public PeriodRecord create() {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		PeriodRecord result;

		result = new PeriodRecord();

		return result;

	}

	public Collection<PeriodRecord> findAll() {

		Collection<PeriodRecord> result;

		result = this.periodRecordRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public PeriodRecord findOne(final int periodRecordId) {

		PeriodRecord result;

		result = this.periodRecordRepository.findOne(periodRecordId);

		return result;
	}

	public PeriodRecord save(final PeriodRecord periodRecord) {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		Assert.notNull(periodRecord);
		PeriodRecord result;

		final Date startDate = periodRecord.getStartYear();
		final Date endDate = periodRecord.getEndYear();
		Assert.isTrue(startDate.before(endDate));

		if (periodRecord.getId() != 0) {

			final History h = this.historyService.historyPerPeriodRecordId(periodRecord.getId());
			final Brotherhood owner = h.getBrotherhood();

			Assert.isTrue(actor.getId() == owner.getId());

		}

		this.checkPictures(periodRecord.getPhotos());

		result = this.periodRecordRepository.save(periodRecord);

		if (periodRecord.getId() == 0) {

			final Brotherhood brotherhood = (Brotherhood) actor;

			final History history = this.historyService.findByBrotherhoodId(brotherhood.getId());
			Assert.notNull(history);
			final Collection<PeriodRecord> pr = history.getPeriodRecords();
			pr.add(result);
			history.setPeriodRecords(pr);
			this.historyService.save(history);
		}

		return result;
	}

	public void delete(final PeriodRecord periodRecord) {

		Assert.notNull(periodRecord);
		Assert.isTrue(periodRecord.getId() != 0);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		final Authority br = new Authority();
		br.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(br));

		final History history = this.historyService.findByBrotherhoodId(actor.getId());
		Assert.notNull(history);

		history.getPeriodRecords().remove(periodRecord);
		this.periodRecordRepository.delete(periodRecord);
	}

	// Other business methods -----------------------

	public Boolean securityPeriod(final int periodId) {

		Boolean res = false;
		Collection<PeriodRecord> loginPeriod = null;

		final PeriodRecord owner = this.findOne(periodId);

		final Brotherhood login = this.brotherhoodService.findByPrincipal();
		final History loginHistory = this.historyService.findByBrotherhoodId(login.getId());

		if (loginHistory != null)
			loginPeriod = loginHistory.getPeriodRecords();

		if (loginPeriod != null && owner != null)
			if (loginPeriod.contains(owner))
				res = true;

		return res;
	}

	public void flush() {
		this.periodRecordRepository.flush();
	}

	public void checkPictures(final Collection<String> attachments) {

		for (final String url : attachments) {
			final boolean checkUrl = url.matches("^http(s*)://(?:[a-zA-Z0-9-]+[\\.\\:])+[a-zA-Z0-9/]+$");
			Assert.isTrue(checkUrl);

		}
	}

}
