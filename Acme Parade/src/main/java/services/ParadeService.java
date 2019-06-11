
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ParadeRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Brotherhood;
import domain.Chapter;
import domain.Finder;
import domain.Float;
import domain.Parade;
import domain.Request;

@Service
@Transactional
public class ParadeService {

	// Managed repository

	@Autowired
	private ParadeRepository	paradeRepository;

	// Suporting services

	@Autowired
	private ActorService		actorService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private Validator			validator;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private SegmentService		segmentService;


	// Simple CRUD methods

	public Parade create() {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		final Parade result = new Parade();

		final Collection<Float> floatt = new HashSet<>();
		result.setFloats(floatt);
		result.setBrotherhood(this.brotherhoodService.findByPrincipal());

		result.setFinalMode(false);
		result.setStatus(null);

		return result;

	}
	private String generateTicker(final Date moment) {

		final DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		final String dateString = dateFormat.format(moment);

		final String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		final StringBuilder salt = new StringBuilder();
		final Random rnd = new Random();
		while (salt.length() < 6) { // length of the random string.
			final int index = (int) (rnd.nextFloat() * alphaNumeric.length());
			salt.append(alphaNumeric.charAt(index));
		}
		final String randomAlphaNumeric = salt.toString();

		final String ticker = dateString + "-" + randomAlphaNumeric;

		final int paradeSameTicker = this.paradeRepository.countParadeWithTicker(ticker);

		//nos aseguramos que que sea �nico
		while (paradeSameTicker > 0)
			this.generateTicker(moment);

		return ticker;

	}

	public Collection<Parade> findAll() {

		final Collection<Parade> parades = this.paradeRepository.findAll();

		Assert.notNull(parades);

		return parades;
	}

	public Parade findOne(final int paradeId) {

		final Parade parade = this.paradeRepository.findOne(paradeId);

		return parade;

	}

	public Parade save(final Parade parade) {
		//hasta que no tenga el brotherhood area no pueden organizarse parades
		Assert.notNull(parade.getBrotherhood().getArea());

		Assert.notNull(parade);

		Parade result = parade;

		Brotherhood brotherhood = null;
		Chapter chapter = null;

		final Authority authorityBrotherhood = new Authority();
		authorityBrotherhood.setAuthority(Authority.BROTHERHOOD);
		final Authority authorityChapter = new Authority();
		authorityChapter.setAuthority(Authority.CHAPTER);

		if (LoginService.getPrincipal().getAuthorities().contains(authorityBrotherhood))
			brotherhood = this.brotherhoodService.findByPrincipal();
		else if (LoginService.getPrincipal().getAuthorities().contains(authorityChapter)) {
			chapter = this.chapterService.findByPrincipal();
			final Chapter chapterCoordinatedParades = this.chapterService.findChapterByAreaId(parade.getBrotherhood().getArea().getId());
			Assert.isTrue(chapter.getId() == chapterCoordinatedParades.getId());
		}
		Assert.isTrue(brotherhood != null || chapter != null);

		/*
		 * Aqu� pasamos el status a SUBMITTED si se le pusiera en el create o el edit
		 * el FINAL MODE a TRUE
		 */
		if (parade.getId() != 0) {
			final Parade paradeBBDD = this.findOne(parade.getId());
			//si estaba a true ya no se puede modificar
			if (!LoginService.getPrincipal().getAuthorities().contains(authorityChapter))
				Assert.isTrue(paradeBBDD.getFinalMode() == false);

			//si estaba a false el de BBDD y ahora se ha puesto a true
			if (parade.getStatus() == null && parade.getFinalMode() == true)
				parade.setStatus("SUBMITTED");

			if (parade.getStatus() == "REJECTED")
				Assert.isTrue(parade.getRejectedComment() != null && parade.getRejectedComment() != "");

		} else if (parade.getId() == 0 && parade.getFinalMode() == true)
			parade.setStatus("SUBMITTED");
		final Date currentMoment = new Date(System.currentTimeMillis() - 1000);
		Assert.isTrue(parade.getOrganisationMoment().after(currentMoment));

		result = this.paradeRepository.save(parade);

		return result;

	}
	//Esto es una soluci�n debido a que si editamos una brotherhood, hay que editar
	//las parades. Si hay parades que ya pasaron dar�a fallo en el de arriba
	//por el Assert de fechas. Aqui no lo tenemos. Solo es empleado el metodo cuando se edita Brotherhood
	public Parade saveByEditBrotherhood(final Parade parade) {
		//hasta que no tenga el brotherhood area no pueden organizarse parades
		Assert.notNull(parade.getBrotherhood().getArea());

		Assert.notNull(parade);

		Parade result = parade;
		this.paradeBrotherhoodSecurity(parade.getId());

		result = this.paradeRepository.save(parade);

		return result;

	}

	public void delete(final Parade parade) {

		Assert.notNull(parade);
		Assert.isTrue(parade.getId() != 0);
		Assert.isTrue(parade.getFinalMode() != true);

		final Actor brotherhood = this.actorService.findByPrincipal();
		Assert.notNull(brotherhood);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(brotherhood.getUserAccount().getAuthorities().contains(authority));
		final Collection<Parade> parades = this.paradeRepository.findParadeByBrotherhoodId(brotherhood.getId());
		Assert.isTrue(parades.contains(parade));

		//Finder
		Collection<Finder> findersByParade = this.finderService.findFindersByParadeId(parade.getId());
		if (!findersByParade.isEmpty())
			for (final Finder f : findersByParade) {

				f.getParades().remove(parade);
				this.finderService.save(f);
			}
		findersByParade = this.finderService.findFindersByParadeId(parade.getId());
		Assert.isTrue(findersByParade.isEmpty());

		this.paradeRepository.delete(parade);
	}

	public void deleteAll(final int actorId) {

		final Collection<Parade> parades = this.paradeRepository.findParadeByBrotherhoodId(actorId);

		if (!parades.isEmpty())
			for (final Parade p : parades) {

				this.segmentService.deleteAll(p.getId());

				final Collection<Finder> findersByParade = this.finderService.findFindersByParadeId(p.getId());
				if (!findersByParade.isEmpty())
					for (final Finder f : findersByParade) {

						f.getParades().remove(p);
						this.finderService.save(f);
					}

				final Collection<Request> requests = this.requestService.requestPerParadeId(p.getId());

				if (!requests.isEmpty())
					for (final Request r : requests)
						this.requestService.delete(r);

				this.paradeRepository.delete(p);

			}

	}

	public Parade copy(final int paradeId) {

		Parade result;

		final Parade original = this.paradeRepository.findOne(paradeId);
		final Brotherhood owner = this.brotherhoodService.findByPrincipal();
		
		if (owner.getId() == original.getBrotherhood().getId()) {

			final String ticker = this.generateTicker(original.getOrganisationMoment());
	
			final Parade copy = this.create();
	
			copy.setDescription(original.getDescription());
			copy.setBrotherhood(original.getBrotherhood());
			copy.setMaxColumn(original.getMaxColumn());
			copy.setMaxRow(original.getMaxRow());
			copy.setOrganisationMoment(original.getOrganisationMoment());
			copy.setRejectedComment(null);
			copy.setStatus(null);
			copy.setTicker(ticker);
			copy.setTitle(original.getTitle());
	
			result = this.paradeRepository.save(copy);
		} else {
			throw new IllegalArgumentException();
		}

		return result;

	}

	//Other business methods-----------------------------------

	public Collection<Parade> findParadeByBrotherhoodId(final int brotherhoodId) {

		final Collection<Parade> parades = this.paradeRepository.findParadeByBrotherhoodId(brotherhoodId);

		return parades;
	}

	public Collection<Parade> findMemberParades(final int memberId) {

		final Collection<Parade> parades = this.paradeRepository.findMemberParades(memberId);

		return parades;
	}

	public Collection<Parade> findParadesByFloatId(final int floatId) {

		final Collection<Parade> parades = this.paradeRepository.findParadesByFloatId(floatId);

		return parades;
	}

	public Collection<Parade> findParadeCanBeSeen() {

		return this.paradeRepository.findParadeCanBeSeen();
	}

	public Collection<Parade> findParadeCannotBeSeenOfBrotherhoodId(final int brotherhoodId) {

		return this.paradeRepository.findParadeCannotBeSeenOfBrotherhoodId(brotherhoodId);
	}

	public Collection<Parade> findParadeCanBeSeenOfBrotherhoodId(final int brotherhoodId) {

		final Collection<Parade> result = this.paradeRepository.findParadeCanBeSeenOfBrotherhoodId(brotherhoodId);

		return result;
	}

	public Collection<Parade> findParadeCanBeSeenOfBrotherhoodIdForChapter(final int brotherhoodId) {

		final Collection<Parade> result = this.paradeRepository.findParadeCanBeSeenOfBrotherhoodIdForChapter(brotherhoodId);

		return result;
	}

	public Collection<String> findParadesLessThirtyDays() {

		final Collection<String> result = new HashSet<>();

		final Collection<Parade> parades = this.paradeRepository.findAll();

		if (!parades.isEmpty())
			for (final Parade p : parades) {
				final Date moment = p.getOrganisationMoment();

				final Date now = new Date(System.currentTimeMillis() - 1000);

				final Interval interval = new Interval(now.getTime(), moment.getTime());
				if (interval.toDuration().getStandardDays() <= 30)
					result.add(p.getTitle());
			}

		return result;
	}

	public Double ratioDraftFinalModeParade() {

		final Double result = this.paradeRepository.ratioDraftFinalModeParade();

		return result;
	}

	public Double ratioSubmitted() {

		final Double result = this.paradeRepository.ratioSubmitted();

		return result;
	}

	public Double ratioRejected() {

		final Double result = this.paradeRepository.ratioRejected();

		return result;
	}

	public Double ratioAccepted() {

		final Double result = this.paradeRepository.ratioAccepted();

		return result;
	}
	public Parade reconstruct(final Parade parade, final BindingResult binding) {

		Parade result = parade;
		final Parade paradeNew = this.create();

		if (parade.getId() == 0 || parade == null) {

			parade.setBrotherhood(paradeNew.getBrotherhood());
			parade.setFloats(paradeNew.getFloats());

			if (parade.getOrganisationMoment() != null) {

				final String ticker = this.generateTicker(parade.getOrganisationMoment());
				parade.setTicker(ticker);

			} else
				parade.setTicker(null);

			this.validator.validate(parade, binding);

			result = parade;
		} else {

			final Parade paradeBBDD = this.findOne(parade.getId());

			parade.setBrotherhood(paradeBBDD.getBrotherhood());
			parade.setFloats(paradeBBDD.getFloats());

			if (parade.getOrganisationMoment() != null) {

				final String ticker = this.generateTicker(parade.getOrganisationMoment());
				parade.setTicker(ticker);

			} else
				parade.setTicker(null);

			this.validator.validate(parade, binding);

		}

		return result;

	}

	public Parade reconstructReject(final Parade parade, final BindingResult binding) {

		final Parade paradeBBDD = this.findOne(parade.getId());

		parade.setBrotherhood(paradeBBDD.getBrotherhood());
		parade.setFloats(paradeBBDD.getFloats());
		parade.setTitle(paradeBBDD.getTitle());
		parade.setDescription(paradeBBDD.getDescription());
		parade.setOrganisationMoment(paradeBBDD.getOrganisationMoment());
		parade.setTicker(paradeBBDD.getTicker());
		parade.setFinalMode(paradeBBDD.getFinalMode());
		parade.setMaxColumn(paradeBBDD.getMaxColumn());
		parade.setMaxRow(paradeBBDD.getMaxRow());
		parade.setStatus(paradeBBDD.getStatus());

		this.validator.validate(parade, binding);

		return parade;
	}

	public Boolean paradeBrotherhoodSecurity(final int paradeId) {
		Boolean res = false;
		final Parade parade = this.findOne(paradeId);

		final Brotherhood owner = parade.getBrotherhood();

		final Brotherhood login = this.brotherhoodService.findByPrincipal();

		if (login.equals(owner))
			res = true;

		return res;
	}
	public void flush() {
		this.paradeRepository.flush();

	}

	public Boolean exist(final int paradeId) {
		Boolean res = false;

		final Parade parade = this.paradeRepository.findOne(paradeId);

		if (parade != null)
			res = true;

		return res;
	}

	public Double avgParadesCoordinatedByChapters() {

		final Double res = this.paradeRepository.avgParadesCoordinatedByChapters();

		return res;

	}

	public Integer minParadesCoordinatedByChapters() {
		Integer min = 0;

		final Collection<Chapter> chapters = this.chapterService.findAll();

		for (final Chapter c : chapters)
			if (c.getArea() != null) {
				final Integer res = this.paradeRepository.countParadesByChapterId(c.getArea().getId());
				if (min == 0 || res < min)
					min = res;
			}

		return min;
	}

	public Integer maxParadesCoordinatedByChapters() {
		Integer max = 0;
		final Collection<Chapter> chapters = this.chapterService.findAll();

		for (final Chapter c : chapters)
			if (c.getArea() != null) {
				final Integer res = this.paradeRepository.countParadesByChapterId(c.getArea().getId());
				if (max == 0 || res > max)
					max = res;
			}

		return max;
	}

	public Double stddevParadesCoordinatedByChapters() {
		Double res = 0.0;
		final Double avg = this.avgParadesCoordinatedByChapters();

		final Collection<Chapter> chapters = this.chapterService.findAll();

		for (final Chapter c : chapters)
			if (c.getArea() != null) {
				final Integer xi = this.paradeRepository.countParadesByChapterId(c.getArea().getId());
				res = res + Math.pow(xi - avg, 2);
			}

		res = Math.sqrt(res / chapters.size());

		return res;

	}

	public Collection<Chapter> chaptersCoordinatesMoreThan10Percent() {
		final Collection<Chapter> res = new HashSet<Chapter>();

		final Collection<Chapter> chapters = this.chapterService.findAll();

		for (final Chapter c : chapters)
			if (c.getArea() != null) {
				final Integer xi = this.paradeRepository.countParadesByChapterId(c.getArea().getId());
				if (xi > this.avgParadesCoordinatedByChapters() * 1.1)
					res.add(c);
			}

		return res;
	}
	
	public Collection<Parade> findParadesInWhichThisMemberCanApplyWithoutAnyProblem(int memberId) {
		return this.paradeRepository.findParadesInWhichThisMemberCanApplyWithoutAnyProblem(memberId);
	}

}
