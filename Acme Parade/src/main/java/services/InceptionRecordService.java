
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.InceptionRecordRepository;
import security.Authority;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	//Managed Repository

	@Autowired
	private InceptionRecordRepository	inceptionRecordRepository;

	//Supporting services

	@Autowired
	private BrotherhoodService			brotherhoodService;

	@Autowired
	private HistoryService				historyService;


	//Simple CRUD methods

	public InceptionRecord create() {

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.notNull(brotherhood);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(brotherhood.getUserAccount().getAuthorities().contains(authority));

		InceptionRecord result;

		result = new InceptionRecord();

		return result;
	}

	public Collection<InceptionRecord> findAll() {

		final Collection<InceptionRecord> inceptionRecords = this.inceptionRecordRepository.findAll();

		Assert.notNull(inceptionRecords);

		return inceptionRecords;
	}

	public InceptionRecord findOne(final int inceptionRecordId) {

		final InceptionRecord inceptionRecord = this.inceptionRecordRepository.findOne(inceptionRecordId);

		return inceptionRecord;
	}

	public InceptionRecord save(final InceptionRecord inceptionRecord) {

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.notNull(brotherhood);
		Assert.notNull(inceptionRecord);

		if (inceptionRecord.getId() != 0) {
			final History historyPerInceptionRecord = this.historyService.historyPerInceptionRecordId(inceptionRecord.getId());
			if (historyPerInceptionRecord != null)
				Assert.isTrue(historyPerInceptionRecord.getBrotherhood().getId() == brotherhood.getId());
		}

		InceptionRecord result;

		this.checkPictures(inceptionRecord.getPhotos());

		result = this.inceptionRecordRepository.save(inceptionRecord);

		return result;
	}
	//Other business methods----

	public Boolean securityInception(final int inceptionId) {

		Boolean res = false;
		InceptionRecord loginInception = null;

		final InceptionRecord owner = this.findOne(inceptionId);

		final Brotherhood login = this.brotherhoodService.findByPrincipal();
		final History loginHistory = this.historyService.findByBrotherhoodId(login.getId());

		if (loginHistory != null)
			loginInception = loginHistory.getInceptionRecord();

		if (loginInception != null && owner != null)
			if (loginInception.equals(owner))
				res = true;

		return res;
	}

	public void flush() {
		this.inceptionRecordRepository.flush();
	}

	public void checkPictures(final Collection<String> attachments) {

		for (final String url : attachments) {
			final boolean checkUrl = url.matches("^http(s*)://(?:[a-zA-Z0-9-]+[\\.\\:])+[a-zA-Z0-9/]+$");
			Assert.isTrue(checkUrl);

		}
	}
}
