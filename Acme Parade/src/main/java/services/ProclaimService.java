
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProclaimRepository;
import security.Authority;
import domain.Actor;
import domain.Chapter;
import domain.Proclaim;

@Service
@Transactional
public class ProclaimService {

	//managed repository
	@Autowired
	private ProclaimRepository	proclaimRepository;

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;


	//Create
	public Proclaim create() {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.CHAPTER);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		final Date currentMoment = new Date(System.currentTimeMillis() - 1000);

		Proclaim result;

		result = new Proclaim();

		result.setMoment(currentMoment);
		result.setChapter(this.chapterService.findByPrincipal());

		return result;

	}

	//FindAll
	public Collection<Proclaim> findAll() {

		final Collection<Proclaim> proclaim = this.proclaimRepository.findAll();

		Assert.notNull(proclaim);

		return proclaim;

	}

	//Find One
	public Proclaim findOne(final int proclaimID) {

		final Proclaim proclaim = this.proclaimRepository.findOne(proclaimID);

		Assert.notNull(proclaim);

		return proclaim;

	}

	//Save solo para crear
	public Proclaim save(final Proclaim proclaim) {

		Assert.notNull(proclaim);

		Proclaim result = proclaim;
		final Chapter chapter = this.chapterService.findByPrincipal();
		Assert.notNull(chapter);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.CHAPTER);

		Assert.isTrue(chapter.getUserAccount().getAuthorities().contains(authority));
		Assert.isTrue(chapter.getId() == proclaim.getChapter().getId());

		if (proclaim.getId() == 0)
			result = this.proclaimRepository.save(proclaim);

		return result;
	}

	public void deleteAll(final int actorId) {

		final Collection<Proclaim> proclaims = this.proclaimsPerChapter(actorId);

		if (!proclaims.isEmpty())
			for (final Proclaim p : proclaims)
				this.proclaimRepository.delete(p);
	}

	//Other methods
	public Proclaim reconstruct(final Proclaim proclaim, final BindingResult binding) {

		Proclaim result = proclaim;
		final Proclaim proclaimNew = this.create();

		proclaim.setMoment(proclaimNew.getMoment());
		proclaim.setChapter(proclaimNew.getChapter());
		this.validator.validate(proclaim, binding);
		result = proclaim;

		return result;
	}

	public Collection<Proclaim> proclaimsPerChapter(final int actorId) {

		final Collection<Proclaim> result = this.proclaimRepository.proclaimsPerChapter(actorId);

		return result;
	}

	public void flush() {
		this.proclaimRepository.flush();
	}

}
