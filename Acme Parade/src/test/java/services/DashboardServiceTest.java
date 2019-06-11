/*
 * SampleTest.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Sponsor;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DashboardServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private HistoryService		historyService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private AreaService			areaService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private SponsorService		sponsorService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	/*
	 * ----CALCULATE SENTENCE COVERAGE----
	 * To calculate the sentence coverage, we have to look at each "service's method"
	 * we are testing and we have to analyse its composition (if, for, ...) and Asserts.
	 * Then, we calculate the number of total cases which our code can execute. The equation will be:
	 * 
	 * (nº passed cases / nº total cases)*100 = coverage(%)
	 * 
	 * In the end of the class, we conclude with the total coverage of the service's methods
	 * which means the service's coverage.
	 * 
	 * 
	 * ----CALCULATE DATA COVERAGE----
	 * To calculate the data coverage, we have look at
	 * each object's attributes, we analyse in each one of them
	 * the domain's restrictions and the business rules
	 * about the attribute. If we have tested all types of cases
	 * in a attribute, that is called "proven attribute".
	 * 
	 * (nº proven attributes/ nº total attributes)*100 = coverage(%)
	 * 
	 * ----Note:
	 * It's clear that if we have tested all cases about a method in a test
	 * and now It have already had a 100% of coverage, we don't have to
	 * mention its coverage in other test.
	 */

	/*
	 * ACME.MADRUGÁ
	 * a)(Level C and B) Requirement 12.3 and 22.2: Administrator display a dashboard
	 * ACME-PARADE
	 * a)(Level C, B and A) Requirement 4.1, 8.1 and 18.2: Administrator display a dashboard
	 * 
	 * b)Negative cases:
	 * 2. Invalid authority
	 * 3. Not authenticated
	 * 4. Wrong return
	 * 
	 * c) Sentence coverage
	 * -avgRecordPerHistory() = 1 passed cases / 3 total cases = 33.33333%
	 * -maxRecordPerHistory() = 1 passed cases / 3 total cases = 33.33333%
	 * -minRecordPerHistory() = 1 passed cases / 3 total cases = 33.33333%
	 * -stddevRecordPerHistory() = 1 passed cases / 3 total cases = 33.33333%
	 * -brotherhoodsMoreThanAverage() = 1 passed cases / 3 total cases = 33.33333%
	 * -avgParadesCoordinatedByChapters() = 1 passed cases / 1 total cases = 100%
	 * -minParadesCoordinatedByChapters() = 2 passed cases / 6 total cases = 33.33333%
	 * -maxParadesCoordinatedByChapters() = 2 passed cases / 6 total cases = 33.33333%
	 * -stddevParadesCoordinatedByChapters() = 2 passed cases / 5 total cases = 40%
	 * -chaptersCoordinatesMoreThan10Percent() = 2 passed cases / 6 total cases = 33.33333%
	 * -ratioAreasNotCoordinatedAnyChapters() = 1 passed cases / 1 total cases = 100%
	 * -ratioDraftFinalModeParade() = 1 passed cases / 1 total cases = 100%
	 * -ratioAccepted() = 1 passed cases / 1 total cases = 100%
	 * -ratioRejected() = 1 passed cases / 1 total cases = 100%
	 * -ratioSubmitted() = 1 passed cases / 1 total cases = 100%
	 * -ratioOfActiveSponsorships() = 1 passed cases / 1 total cases = 100%
	 * -averageActiveSponsorshipsPerSponsor() = 1 passed cases / 1 total cases = 100%
	 * -minActiveSponsorshipsPerSponsor() = 2 passed cases / 5 total cases = 40%
	 * -maxActiveSponsorshipsPerSponsor() = 2 passed cases / 5 total cases = 40%
	 * -standartDeviationOfActiveSponsorshipsPerSponsor() = 2 passed cases / 5 total cases = 40%
	 * -avgMemberPerBrotherhood() = 4 passed cases / 7 total cases = 57.14%
	 * -minMemberPerBrotherhood() = 4 passed cases / 7 total cases = 57.14%
	 * -maxMemberPerBrotherhood() = 4 passed cases / 7 total cases = 57.14%
	 * -stddevMemberPerBrotherhood() = 4 passed cases / 7 total cases = 57.14%
	 * -membersTenPerCent() = 5 passed cases / 10 total cases = 50%
	 * -ratioBrotherhoodPerArea() = 3 passed cases / 5 total cases = 60%
	 * -countBrotherhoodPerArea() = 2 passed cases / 2 total cases = 100%
	 * -minBrotherhoodPerArea() = 4 passed cases / 7 total cases = 57.14%
	 * -maxBrotherhoodPerArea() = 4 passed cases / 7 total cases = 57.14%
	 * -avgBrotherhoodPerArea() = 4 passed cases / 7 total cases = 57.14%
	 * -stddevBrotherhoodPerArea() = 4 passed cases / 7 total cases = 57.14%
	 * -minResultPerFinder() = 4 passed cases / 7 total cases = 57.14%
	 * -maxResultPerFinder() = 4 passed cases / 7 total cases = 57.14%
	 * -avgResultPerFinder() = 4 passed cases / 7 total cases = 57.14%
	 * -stddevResultPerFinder() = 4 passed cases / 7 total cases = 57.14%
	 * -ratioEmptyFinders() = 1 passed cases / 1 total cases = 100%
	 * -theSmallestBrotherhoods() = 1 passed / 1 total cases = 100%
	 * -thelargestBrotherhood() = 1 passed cases / 1 total cases = 100%
	 * -largestBrotherhood() = 1 passed cases / 3 total cases = 33.33333%
	 * -top5SporsorsActivedSponsorships() = 2 passed cases / 2 total cases = 100%
	 */

	@Test
	public void authorityTest() {
		final Object authorityTest[][] = {

			{
				"admin", null
			},//1.All fine
			{
				"member1", IllegalArgumentException.class
			},//2. Invalid authority
			{
				"brotherhood1", IllegalArgumentException.class
			},//2. Invalid authority
			{
				null, IllegalArgumentException.class
			},//3. Not authenticated
		};

		for (int i = 0; i < authorityTest.length; i++)
			this.AuthorityTemplate((String) authorityTest[i][0], (Class<?>) authorityTest[i][1]);
	}

	protected void AuthorityTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			final Double avg = this.historyService.avgRecordPerHistory();

			Assert.isTrue(avg != null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void valueTest() {
		final Object valueTest[][] = {

			{
				"avgRecordPerHistory", 4.5, null
			},//1. All fine 
			{
				"avgRecordPerHistory", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"maxRecordPerHistory", 8.0, null
			},//1. All fine 
			{
				"maxRecordPerHistory", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"minRecordPerHistory", 1.0, null
			},//1. All fine 
			{
				"minRecordPerHistory", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"stddevRecordPerHistory", 3.5, null
			},//1. All fine 
			{
				"stddevRecordPerHistory", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"brotherhoodsMoreThanAverage", 1.0, null
			},//1. All fine 
			{
				"brotherhoodsMoreThanAverage", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"avgParadesCoordinatedByChapters", 0.9375, null
			},//1. All fine 
			{
				"avgParadesCoordinatedByChapters", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"minParadesCoordinatedByChapters", 1.0, null
			},//1. All fine
			{
				"minParadesCoordinatedByChapters", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"maxParadesCoordinatedByChapters", 14.0, null
			},//1. All fine
			{
				"maxParadesCoordinatedByChapters", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"stddevParadesCoordinatedByChapters", 7.54172421709165, null
			},//1. All fine
			{
				"stddevParadesCoordinatedByChapters", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"chaptersCoordinatesMoreThan10Percent", 1.0, null
			},//1. All fine
			{
				"chaptersCoordinatesMoreThan10Percent", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"ratioAreasNotCoordinatedAnyChapters", 0.5, null
			},//1. All fine
			{
				"ratioAreasNotCoordinatedAnyChapters", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"ratioDraftFinalModeParade", 0.06667, null
			},//1. All fine
			{
				"ratioDraftFinalModeParade", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"ratioAccepted", 0.4, null
			},//1. All fine
			{
				"ratioAccepted", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"ratioRejected", 0.2, null
			},//1. All fine
			{
				"ratioRejected", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"ratioSubmitted", 0.4, null
			},//1. All fine
			{
				"ratioSubmitted", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"ratioOfActiveSponsorships", 0.76923, null
			},//1. All fine
			{
				"ratioOfActiveSponsorships", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"averageActiveSponsorshipsPerSponsor", 1.66667, null
			},//1. All fine
			{
				"averageActiveSponsorshipsPerSponsor", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"minActiveSponsorshipsPerSponsor", 0.0, null
			},//1. All fine
			{
				"minActiveSponsorshipsPerSponsor", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"maxActiveSponsorshipsPerSponsor", 4.0, null
			},//1. All fine
			{
				"maxActiveSponsorshipsPerSponsor", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"standartDeviationOfActiveSponsorshipsPerSponsor", 1.2472146745582064, null
			},//1. All fine
			{
				"standartDeviationOfActiveSponsorshipsPerSponsor", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"avgMemberPerBrotherhood", 2.125, null
			},//1. All fine
			{
				"avgMemberPerBrotherhood", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"minMemberPerBrotherhood", 0.0, null
			},//1. All fine
			{
				"minMemberPerBrotherhood", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"maxMemberPerBrotherhood", 3.0, null
			},//1. All fine
			{
				"maxMemberPerBrotherhood", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"stddevMemberPerBrotherhood", 1.0532687216470449, null
			},//1. All fine
			{
				"stddevMemberPerBrotherhood", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"membersTenPerCent", 1.0, null
			},//1. All fine
			{
				"membersTenPerCent", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"ratioBrotherhoodPerArea", 2.0, null
			},//1. All fine
			{
				"ratioBrotherhoodPerArea", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"countBrotherhoodPerArea", 4.0, null
			},//1. All fine
			{
				"countBrotherhoodPerArea", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"minBrotherhoodPerArea", 0.0, null
			},//1. All fine
			{
				"minBrotherhoodPerArea", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"maxBrotherhoodPerArea", 3.0, null
			},//1. All fine
			{
				"maxBrotherhoodPerArea", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"avgBrotherhoodPerArea", 1.75, null
			},//1. All fine
			{
				"avgBrotherhoodPerArea", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"stddevBrotherhoodPerArea", 1.299038105676658, null
			},//1. All fine
			{
				"stddevBrotherhoodPerArea", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"minResultPerFinder", 0.0, null
			},//1. All fine
			{
				"minResultPerFinder", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"maxResultPerFinder", 0.0, null
			},//1. All fine
			{
				"maxResultPerFinder", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"avgResultPerFinder", 0.0, null
			},//1. All fine
			{
				"avgResultPerFinder", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"stddevResultPerFinder", 0.0, null
			},//1. All fine
			{
				"stddevResultPerFinder", 111.111, IllegalArgumentException.class
			},//4. Wrong return
			{
				"ratioEmptyFinders", 1.0, null
			},//1. All fine
			{
				"ratioEmptyFinders", 111.111, IllegalArgumentException.class
			},//4. Wrong return
		};

		for (int i = 0; i < valueTest.length; i++)
			this.ValueTemplate((String) valueTest[i][0], (Double) valueTest[i][1], (Class<?>) valueTest[i][2]);
	}

	protected void ValueTemplate(final String method, final Double value, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate("admin");

			Double test = 0.0;

			if (method == "avgRecordPerHistory")
				test = this.historyService.avgRecordPerHistory();
			else if (method == "maxRecordPerHistory")
				test = this.historyService.maxRecordPerHistory();
			else if (method == "minRecordPerHistory")
				test = this.historyService.minRecordPerHistory();
			else if (method == "stddevRecordPerHistory")
				test = this.historyService.stddevRecordPerHistory();
			else if (method == "brotherhoodsMoreThanAverage")
				test = this.historyService.brotherhoodsMoreThanAverage().size() * 1.0;
			else if (method == "avgParadesCoordinatedByChapters")
				test = this.paradeService.avgParadesCoordinatedByChapters();
			else if (method == "minParadesCoordinatedByChapters")
				test = this.paradeService.minParadesCoordinatedByChapters() * 1.0;
			else if (method == "maxParadesCoordinatedByChapters")
				test = this.paradeService.maxParadesCoordinatedByChapters() * 1.0;
			else if (method == "stddevParadesCoordinatedByChapters")
				test = this.paradeService.stddevParadesCoordinatedByChapters();
			else if (method == "chaptersCoordinatesMoreThan10Percent")
				test = this.paradeService.chaptersCoordinatesMoreThan10Percent().size() * 1.0;
			else if (method == "ratioAreasNotCoordinatedAnyChapters")
				test = this.areaService.ratioAreasNotCoordinatedAnyChapters();
			else if (method == "ratioDraftFinalModeParade")
				test = this.paradeService.ratioDraftFinalModeParade();
			else if (method == "ratioAccepted")
				test = this.paradeService.ratioAccepted();
			else if (method == "ratioRejected")
				test = this.paradeService.ratioRejected();
			else if (method == "ratioSubmitted")
				test = this.paradeService.ratioSubmitted();
			else if (method == "ratioOfActiveSponsorships")
				test = this.sponsorshipService.ratioOfActiveSponsorships();
			else if (method == "averageActiveSponsorshipsPerSponsor")
				test = this.sponsorshipService.averageActiveSponsorshipsPerSponsor();
			else if (method == "minActiveSponsorshipsPerSponsor")
				test = this.sponsorshipService.minActiveSponsorshipsPerSponsor() * 1.0;
			else if (method == "maxActiveSponsorshipsPerSponsor")
				test = this.sponsorshipService.maxActiveSponsorshipsPerSponsor() * 1.0;
			else if (method == "standartDeviationOfActiveSponsorshipsPerSponsor")
				test = this.sponsorshipService.standartDeviationOfActiveSponsorshipsPerSponsor();
			else if (method == "avgMemberPerBrotherhood")
				test = Double.valueOf(this.brotherhoodService.avgMemberPerBrotherhood());
			else if (method == "minMemberPerBrotherhood")
				test = Double.valueOf(this.brotherhoodService.minMemberPerBrotherhood());
			else if (method == "maxMemberPerBrotherhood")
				test = Double.valueOf(this.brotherhoodService.maxMemberPerBrotherhood());
			else if (method == "stddevMemberPerBrotherhood")
				test = Double.valueOf(this.brotherhoodService.stddevMemberPerBrotherhood());
			else if (method == "membersTenPerCent")
				test = this.memberService.membersTenPerCent().size() * 1.0;

			else if (method == "ratioBrotherhoodPerArea")
				test = Double.valueOf(this.areaService.ratioBrotherhoodPerArea());
			else if (method == "countBrotherhoodPerArea")
				test = Double.valueOf(this.areaService.countBrotherhoodPerArea());
			else if (method == "minBrotherhoodPerArea")
				test = Double.valueOf(this.areaService.minBrotherhoodPerArea());
			else if (method == "maxBrotherhoodPerArea")
				test = Double.valueOf(this.areaService.maxBrotherhoodPerArea());
			else if (method == "avgBrotherhoodPerArea")
				test = Double.valueOf(this.areaService.avgBrotherhoodPerArea());
			else if (method == "stddevBrotherhoodPerArea")
				test = Double.valueOf(this.areaService.stddevBrotherhoodPerArea());

			else if (method == "minResultPerFinder")
				test = Double.valueOf(this.finderService.minResultPerFinder());
			else if (method == "maxResultPerFinder")
				test = Double.valueOf(this.finderService.maxResultPerFinder());
			else if (method == "avgResultPerFinder")
				test = Double.valueOf(this.finderService.avgResultPerFinder());
			else if (method == "stddevResultPerFinder")
				test = Double.valueOf(this.finderService.stddevResultPerFinder());

			else if (method == "ratioEmptyFinders")
				test = this.finderService.ratioEmptyFinders();

			Assert.isTrue(test.equals(value));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void theSmallestBrotherhoodsTest() {
		final Object theSmallestBrotherhoodsTest[][] = {
			{
				"brotherhood8", null
			},//1. All fine
			{
				"brotherhood6", IllegalArgumentException.class
			},//4. Wrong return
		};

		for (int i = 0; i < theSmallestBrotherhoodsTest.length; i++)
			this.theSmallestBrotherhoodsTemplate(super.getEntityId((String) theSmallestBrotherhoodsTest[i][0]), (Class<?>) theSmallestBrotherhoodsTest[i][1]);
	}

	protected void theSmallestBrotherhoodsTemplate(final int brotherhoodId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			final Brotherhood b = this.brotherhoodService.findOne(brotherhoodId);

			final Collection<Brotherhood> bs = this.brotherhoodService.theSmallestBrotherhoods();

			Assert.isTrue(bs.contains(b.getTitle()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void theLargestBrotherhoodsTest() {
		final Object theLargestBrotherhoodsTest[][] = {
			{

				"brotherhood6", null
			},//1. All fine
			{
				"brotherhood8", IllegalArgumentException.class
			},//4. Wrong return
		};

		for (int i = 0; i < theLargestBrotherhoodsTest.length; i++)
			this.theLargestBrotherhoodsTemplate(super.getEntityId((String) theLargestBrotherhoodsTest[i][0]), (Class<?>) theLargestBrotherhoodsTest[i][1]);
	}

	protected void theLargestBrotherhoodsTemplate(final int brotherhoodId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			final Brotherhood b = this.brotherhoodService.findOne(brotherhoodId);

			final Collection<Brotherhood> bs = this.brotherhoodService.theLargestBrotherhoods();

			Assert.isTrue(bs.contains(b.getTitle()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void largestBrotherhoodsTest() {
		final Object largestBrotherhoodsTest[][] = {
			{
				"brotherhood1", null
			},//1. All fine
			{
				"brotherhood5", IllegalArgumentException.class
			},//4. Wrong return
		};

		for (int i = 0; i < largestBrotherhoodsTest.length; i++)
			this.largestBrotherhoodsTemplate(super.getEntityId((String) largestBrotherhoodsTest[i][0]), (Class<?>) largestBrotherhoodsTest[i][1]);
	}

	protected void largestBrotherhoodsTemplate(final int brotherhoodId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			super.authenticate("admin");

			final Brotherhood b = this.brotherhoodService.findOne(brotherhoodId);

			final Collection<Brotherhood> bs = this.historyService.largestBrotherhood();

			Assert.isTrue(bs.contains(b));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	@Test
	public void top5Test() {
		final Object top5Test[][] = {
			{
				"sponsor1", null
			},//1. All fine
			{
				"sponsor6", IllegalArgumentException.class
			},//4. Wrong return
		};

		for (int i = 0; i < top5Test.length; i++)
			this.top5Template(super.getEntityId((String) top5Test[i][0]), (Class<?>) top5Test[i][1]);
	}

	protected void top5Template(final int sponsorId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			final Sponsor s = this.sponsorService.findOne(sponsorId);

			final Collection<String> cs = this.sponsorshipService.top5SporsorsActivedSponsorships();

			Assert.isTrue(cs.contains(s.getName()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/*
	 * -------Coverage Dashboard-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * 
	 * avgRecordPerHistory() = 33.33333%
	 * maxRecordPerHistory() = 33.33333%
	 * minRecordPerHistory() = 33.33333%
	 * stddevRecordPerHistory() = 33.33333%
	 * brotherhoodsMoreThanAverage() = 33.33333%
	 * avgParadesCoordinatedByChapters() = 100%
	 * minParadesCoordinatedByChapters() = 33.33333%
	 * maxParadesCoordinatedByChapters() = 33.33333%
	 * stddevParadesCoordinatedByChapters() = 40%
	 * chaptersCoordinatesMoreThan10Percent() = 33.33333%
	 * ratioAreasNotCoordinatedAnyChapters() = 100%
	 * ratioDraftFinalModeParade() = 100%
	 * ratioAccepted() = 100%
	 * ratioRejected() = 100%
	 * ratioSubmitted() = 100%
	 * ratioOfActiveSponsorships() = 100%
	 * averageActiveSponsorshipsPerSponsor() = 100%
	 * minActiveSponsorshipsPerSponsor() = 40%
	 * maxActiveSponsorshipsPerSponsor() = 40%
	 * standartDeviationOfActiveSponsorshipsPerSponsor() = 40%
	 * avgMemberPerBrotherhood() = 57.14%
	 * minMemberPerBrotherhood() = 57.14%
	 * maxMemberPerBrotherhood() = 57.14%
	 * stddevMemberPerBrotherhood() = 57.14%
	 * membersTenPerCent() = 50%
	 * ratioBrotherhoodPerArea() = 60%
	 * countBrotherhoodPerArea() = 100%
	 * minBrotherhoodPerArea() = 57.14%
	 * maxBrotherhoodPerArea() = 57.14%
	 * avgBrotherhoodPerArea() = 57.14%
	 * stddevBrotherhoodPerArea() = 57.14%
	 * minResultPerFinder() = 57.14%
	 * maxResultPerFinder() = 57.14%
	 * avgResultPerFinder() = 57.14%
	 * stddevResultPerFinder() = 57.14%
	 * ratioEmptyFinders() = 100%
	 * theSmallestBrotherhoods() = 100%
	 * thelargestBrotherhood() = 100%
	 * largestBrotherhood() = 33.33333%
	 * top5SporsorsActivedSponsorships() = 100%
	 */
}
