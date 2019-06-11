
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorRepository;
import repositories.SponsorshipRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Box;
import domain.Sponsor;
import forms.RegisterSponsorForm;

@Service
@Transactional
public class SponsorService {

	// Managed Repository ------------------------
	@Autowired
	private SponsorRepository		sponsorRepository;

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private Validator				validator;


	// Simple CRUD methods 

	public Sponsor create() {
		final Sponsor sponsor = new Sponsor();

		final UserAccount userAccount = this.userAccountService.createSponsor();
		sponsor.setUserAccount(userAccount);

		sponsor.setScore(null);
		sponsor.setSpammer(null);

		return sponsor;

	}

	public Sponsor findOne(final int sponsorId) {

		Sponsor sponsor;
		sponsor = this.sponsorRepository.findOne(sponsorId);
		Assert.notNull(sponsor);
		return sponsor;

	}

	public Collection<Sponsor> findAll() {

		Collection<Sponsor> result;
		result = this.sponsorRepository.findAll();
		Assert.notNull(result);
		return result;

	}

	public Sponsor save(final Sponsor sponsor) {

		Assert.notNull(sponsor);
		Sponsor result;

		if (sponsor.getId() != 0) {
			final Authority admin = new Authority();
			admin.setAuthority(Authority.SPONSOR);

			final Actor actor = this.actorService.findByPrincipal();
			Assert.notNull(actor);

			Assert.isTrue(actor.getId() == sponsor.getId());

			this.actorService.checkEmail(sponsor.getEmail(), false);
			this.actorService.checkPhone(sponsor.getPhone());

			result = this.sponsorRepository.save(sponsor);

		} else {

			this.actorService.checkEmail(sponsor.getEmail(), true);
			this.actorService.checkPhone(sponsor.getPhone());

			String pass = sponsor.getUserAccount().getPassword();

			final Md5PasswordEncoder code = new Md5PasswordEncoder();

			pass = code.encodePassword(pass, null);

			final UserAccount userAccount = sponsor.getUserAccount();
			userAccount.setPassword(pass);

			sponsor.setUserAccount(userAccount);

			result = this.sponsorRepository.save(sponsor);

			Box inBox, outBox, trashBox, spamBox, notificationBox;
			inBox = this.boxService.create();
			outBox = this.boxService.create();
			trashBox = this.boxService.create();
			spamBox = this.boxService.create();
			notificationBox = this.boxService.create();

			inBox.setName("in box");
			outBox.setName("out box");
			trashBox.setName("trash box");
			spamBox.setName("spam box");
			notificationBox.setName("notification box");

			inBox.setByDefault(true);
			outBox.setByDefault(true);
			trashBox.setByDefault(true);
			spamBox.setByDefault(true);
			notificationBox.setByDefault(true);

			inBox.setActor(result);
			outBox.setActor(result);
			trashBox.setActor(result);
			spamBox.setActor(result);
			notificationBox.setActor(result);

			final Collection<Box> boxes = new ArrayList<>();
			boxes.add(spamBox);
			boxes.add(trashBox);
			boxes.add(inBox);
			boxes.add(outBox);
			boxes.add(notificationBox);

			inBox = this.boxService.saveNewActor(inBox);
			outBox = this.boxService.saveNewActor(outBox);
			trashBox = this.boxService.saveNewActor(trashBox);
			spamBox = this.boxService.saveNewActor(spamBox);
			notificationBox = this.boxService.saveNewActor(notificationBox);

		}
		return result;
	}

	// Other business methods 

	public Sponsor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		final Sponsor sponsor = this.sponsorRepository.findByUserAccountId(userAccount.getId());

		return sponsor;
	}

	public Sponsor findByPrincipal() {
		Sponsor sponsor;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		sponsor = this.findByUserAccount(userAccount);
		Assert.notNull(sponsor);

		return sponsor;
	}

	public void flush() {
		this.sponsorRepository.flush();
	}

	public Sponsor reconstruct(final RegisterSponsorForm form, final BindingResult binding) {

		this.validator.validate(form, binding);

		final Sponsor sponsor = this.create();

		sponsor.setName(form.getName());
		sponsor.setMiddleName(form.getMiddleName());
		sponsor.setSurname(form.getSurname());
		sponsor.setPhoto(form.getPhoto());
		sponsor.setEmail(form.getEmail());
		sponsor.setPhone(form.getPhone());
		sponsor.setAddress(form.getAddress());
		sponsor.getUserAccount().setUsername(form.getUsername());
		sponsor.getUserAccount().setPassword(form.getPassword());

		return sponsor;

	}

	public Sponsor reconstruct(final Sponsor sponsor, final BindingResult binding) {

		final Sponsor result;

		final Sponsor sponsorBBDD = this.findOne(sponsor.getId());

		sponsor.setUserAccount(sponsorBBDD.getUserAccount());
		sponsor.setScore(sponsorBBDD.getScore());
		sponsor.setSpammer(sponsorBBDD.getSpammer());

		this.validator.validate(sponsor, binding);

		result = sponsor;

		return result;

	}

	public Collection<Sponsor> sponsorsWithActiveSponsorships() {

		int c = 0;
		final Collection<Sponsor> result = new HashSet<Sponsor>();
		final Collection<Sponsor> total = this.sponsorRepository.findAll();

		for (final Sponsor s : total) {
			c = this.sponsorshipRepository.activeSponsorshipsPerSponsorId(s.getId()).size();
			if (c > 0)
				result.add(s);

		}
		return result;
	}

}
