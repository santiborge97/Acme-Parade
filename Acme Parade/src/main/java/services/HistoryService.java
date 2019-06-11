
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.HistoryRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Service
@Transactional
public class HistoryService {

	//Managed repository---------------------------------
	@Autowired
	private HistoryRepository		historyRepository;

	//Suporting services---------------------------------
	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	//Simple CRUD methods--------------------------------
	public History create(final InceptionRecord inceptionRecord) {

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.notNull(brotherhood);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(brotherhood.getUserAccount().getAuthorities().contains(authority));

		final History h = new History();

		h.setBrotherhood(brotherhood);
		h.setInceptionRecord(inceptionRecord);

		h.setLegalRecords(new ArrayList<LegalRecord>());
		h.setLinkRecords(new ArrayList<LinkRecord>());
		h.setPeriodRecords(new ArrayList<PeriodRecord>());
		h.setMiscellaneousRecords(new ArrayList<MiscellaneousRecord>());

		return h;
	}

	public Collection<History> findAll() {
		Collection<History> result;
		result = this.historyRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public History findOne(final int historyId) {
		History h;
		h = this.historyRepository.findOne(historyId);
		Assert.notNull(h);
		return h;
	}

	public History save(final History history) {
		Assert.notNull(history);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Actor owner = history.getBrotherhood();
		Assert.isTrue(actor.getId() == owner.getId());
		if (history.getId() == 0)
			this.inceptionRecordService.save(history.getInceptionRecord());

		final History h = this.historyRepository.save(history);

		return h;
	}

	public Double avgRecordPerHistory() {

		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.avgRecordPerHistory();
	}

	public Double maxRecordPerHistory() {

		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.maxRecordPerHistory();
	}

	public Double minRecordPerHistory() {

		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.minRecordPerHistory();
	}

	public Double stddevRecordPerHistory() {

		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.stddevRecordPerHistory();
	}

	public Collection<Brotherhood> largestBrotherhood() {

		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.largestBrotherhood();
	}

	public Collection<Brotherhood> brotherhoodsMoreThanAverage() {

		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.brotherhoodsMoreThanAverage();
	}

	public void deleteAll(final int actorId) {

		final History history = this.findByBrotherhoodId(actorId);

		if (history != null)
			this.historyRepository.delete(history);
	}

	//Other business methods----------------------------

	public History findByBrotherhoodId(final int brotherhoodId) {

		final History result = this.historyRepository.findByBrotherhoodId(brotherhoodId);

		return result;
	}

	public Boolean securityHistory() {

		Boolean res = false;

		final Brotherhood login = this.brotherhoodService.findByPrincipal();

		final History owner = this.historyRepository.findByBrotherhoodId(login.getId());

		if (owner != null)
			res = true;

		return res;

	}

	public History historyPerPeriodRecordId(final int periodRecordId) {

		final History result = this.historyRepository.historyPerPeriodRecordId(periodRecordId);

		return result;
	}

	public History historyPerLegalRecordId(final int legalRecordId) {

		final History result = this.historyRepository.historyPerLegalRecordId(legalRecordId);

		return result;
	}

	public History historyPerLinkRecordId(final int linkRecordId) {

		final History result = this.historyRepository.historyPerLinkRecordId(linkRecordId);

		return result;
	}

	public History historyPerMiscellaneousRecordId(final int miscellaneousRecordId) {

		final History result = this.historyRepository.historyPerMiscellaneousRecordId(miscellaneousRecordId);

		return result;
	}

	public History historyPerInceptionRecordId(final int inceptionRecordId) {

		final History result = this.historyRepository.historyPerInceptionRecordId(inceptionRecordId);

		return result;
	}

	public void flush() {
		this.historyRepository.flush();
	}
}
