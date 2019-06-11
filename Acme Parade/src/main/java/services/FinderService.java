
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import security.Authority;
import domain.Actor;
import domain.Finder;
import domain.Member;
import domain.Parade;

@Service
@Transactional
public class FinderService {

	// Managed repository -------------------------------------------

	@Autowired
	private FinderRepository	finderRepository;

	// Supporting services ------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods ------------------------------------------

	public Finder create() {

		Finder result;

		result = new Finder();

		result.setLastUpdate(new Date(System.currentTimeMillis() - 1000));

		return result;

	}

	public Collection<Finder> findAll() {

		final Collection<Finder> result = this.finderRepository.findAll();
		Assert.notNull(result);
		return result;

	}

	public Finder findOne(final int finderId) {

		final Finder result = this.finderRepository.findOne(finderId);
		Assert.notNull(result);
		return result;

	}

	public Finder save(final Finder finder) {
		Assert.notNull(finder);

		if (finder.getId() != 0) {
			final Actor actor = this.actorService.findByPrincipal();
			Assert.notNull(actor);

			final Authority hW = new Authority();
			hW.setAuthority(Authority.MEMBER);
			if (actor.getUserAccount().getAuthorities().contains(hW)) {
				final Member owner = finder.getMember();
				Assert.notNull(owner);
				Assert.isTrue(actor.getId() == owner.getId());
				finder.setLastUpdate(new Date(System.currentTimeMillis() - 1000));
				final Collection<Parade> paradesSearchedList = this.paradeSearchedList(finder);
				finder.setParades(paradesSearchedList);
			}
		} else
			finder.setLastUpdate(new Date(System.currentTimeMillis() - 1000));

		final Finder result = this.finderRepository.save(finder);

		return result;

	}

	public void deleteFinderActor(final int actorId) {

		final Finder finder = this.finderRepository.findFinderByMember(actorId);

		this.finderRepository.delete(finder);

	}
	// Other business methods -----------------------------------------
	public Collection<Finder> findFindersByParadeId(final int paradeId) {
		final Collection<Finder> result = this.finderRepository.findFindersByParadeId(paradeId);
		Assert.notNull(result);
		return result;
	}

	public Finder findFinderByMember(final Actor actor) {
		final Authority member = new Authority();
		member.setAuthority(Authority.MEMBER);

		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(member));
		final Finder finder = this.finderRepository.findFinderByMember(actor.getId());
		return finder;

	}

	public Collection<Parade> paradeSearchedList(final Finder finder) {
		final Collection<Parade> parades = this.paradeService.findAll();
		final Collection<Parade> result = new ArrayList<Parade>();

		for (final Parade p : parades)
			if (this.testParade(finder, p) == true)
				result.add(p);

		return result;

	}

	private Boolean testParade(final Finder finder, final Parade p) {

		//filters
		final String keyWord = finder.getKeyWord();
		final String area = finder.getArea();
		Date maxDateConvert = null;
		Date minDateConvert = null;

		if (!finder.getMaxDate().isEmpty() && finder.getMaxDate() != null)
			maxDateConvert = this.convertStringToDate(finder.getMaxDate());

		if (!finder.getMinDate().isEmpty() && finder.getMinDate() != null)
			minDateConvert = this.convertStringToDate(finder.getMinDate());

		//inicializamos a true porque en cualquier caso lo vamos a mostrar todo

		boolean keyWordFind;
		final boolean areaFind;
		boolean dates;

		//Casos de keyWord
		if (finder.getKeyWord() != null && finder.getKeyWord() != "")
			keyWordFind = (p.getTicker().contains(keyWord) || p.getDescription().contains(keyWord) || p.getBrotherhood().getTitle().contains(keyWord) || p.getTitle().contains(keyWord));
		else
			keyWordFind = true;

		//Casos de Area
		if (finder.getArea() != null && finder.getArea() != "")
			areaFind = p.getBrotherhood().getArea().getName().contains(area);
		else
			areaFind = true;

		//Casos de dates

		if (minDateConvert != null && maxDateConvert != null)
			dates = p.getOrganisationMoment().compareTo(minDateConvert) >= 0 && p.getOrganisationMoment().compareTo(maxDateConvert) <= 0;
		else if (minDateConvert == null && maxDateConvert != null)
			dates = p.getOrganisationMoment().compareTo(maxDateConvert) <= 0;
		else if (minDateConvert != null && maxDateConvert == null)
			dates = p.getOrganisationMoment().compareTo(minDateConvert) >= 0;
		else
			dates = true;

		//---------------comparamos todos los parámetros---------------
		boolean isSearched = false;
		if (keyWordFind && areaFind && dates)
			isSearched = true;

		return isSearched;

	}

	public Date convertStringToDate(final String dateString) {
		Date date = null;
		final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		try {
			date = df.parse(dateString);
		} catch (final Exception ex) {
			System.out.println(ex);
		}
		return date;
	}

	public Collection<Double> statsOfFinderResults() {

		final Collection<Double> statsOfFindersResult = this.finderRepository.statsOfFinderResults();

		return statsOfFindersResult;
	}

	public String minResultPerFinder() {

		String res = "N/A";
		Integer min = 999999999;
		final Collection<Finder> finders = this.finderRepository.findAll();

		if (!finders.isEmpty()) {
			for (final Finder f : finders) {
				final Collection<Parade> p = f.getParades();
				if (!p.isEmpty() && (p.size() < min))
					min = p.size();
				else if (p.isEmpty())
					min = 0;

			}

			res = Integer.toString(min);

		}

		return res;
	}

	public String maxResultPerFinder() {

		String res = "N/A";
		Integer max = 0;
		final Collection<Finder> finders = this.finderRepository.findAll();

		if (!finders.isEmpty()) {
			for (final Finder f : finders) {
				final Collection<Parade> p = f.getParades();
				if (!p.isEmpty() && (p.size() > max))
					max = p.size();
			}

			res = Integer.toString(max);

		}

		return res;
	}

	public String avgResultPerFinder() {

		String res = "N/A";
		Double avg = 0.0;
		final Collection<Finder> finders = this.finderRepository.findAll();

		if (!finders.isEmpty()) {
			final Integer total = finders.size();
			Integer sum = 0;
			for (final Finder f : finders) {
				final Collection<Parade> p = f.getParades();
				if (!p.isEmpty())
					sum = sum + p.size();
			}

			if (sum > 0)
				avg = (sum * 1.0) / total;

			res = Double.toString(avg);

		}

		return res;
	}

	public String stddevResultPerFinder() {

		String res = "N/A";
		Double avg = 0.0;
		Integer count = 0;
		Integer sum2 = 0;
		Double op = 0.0;
		final Collection<Finder> finders = this.finderRepository.findAll();

		if (!finders.isEmpty()) {
			final Integer total = finders.size();
			Integer sum = 0;
			for (final Finder f : finders) {
				final Collection<Parade> p = f.getParades();
				if (!p.isEmpty()) {
					sum = sum + p.size();
					sum2 = sum2 + (p.size() * p.size());
				}

			}

			count = total;

			if (sum > 0)
				avg = (sum * 1.0) / total;

			op = Math.sqrt(((sum2 * 1.0) / count) - (avg * avg));
			res = Double.toString(op);

		}

		return res;
	}

	public Double ratioEmptyFinders() {

		final Double ratioEmptyFinders = this.finderRepository.ratioEmptyFinders();

		return ratioEmptyFinders;
	}

	public Finder reconstruct(final Finder finder, final BindingResult binding) {

		final Finder finderBBDD = this.findOne(finder.getId());

		finder.setMember(finderBBDD.getMember());

		finder.setLastUpdate(new Date(System.currentTimeMillis() - 1000));

		this.validator.validate(finder, binding);

		return finder;

	}
}
