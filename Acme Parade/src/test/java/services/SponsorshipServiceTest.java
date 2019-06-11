
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Parade;
import domain.Sponsorship;
import forms.SponsorshipForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private SponsorService		sponsorService;

	@Autowired
	private ParadeService		paradeService;


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
	 * ACME-PARADE
	 * a)(Level A) Requirement 16.1: Sponsor manage his/her sponsorships: Edit
	 * 
	 * b) Negative cases:
	 * 2. Wrong user, same authority
	 * 3. Wrong authority
	 * 4. No authority
	 * 5. No register user
	 * 6. Credit Card expired year
	 * 7. Credit Card expired month
	 * 8. Credit Card expired both month and year
	 * 9. Incorrect banner URL
	 * 10. Incorrect target URL
	 * 11. Null banner URL
	 * 12. Null target URL
	 * 13. Null holder name
	 * 14. Null number
	 * 15. Incorrect number
	 * 
	 * c) Sentence coverage
	 * -save(): 6 passed cases / 6 total cases = 100%
	 * -findOne(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Sponsorship: 2 passed cases / 6 total cases = 33,3%
	 * -CreditCard: 4 passed cases / 6 total cases = 66,6%
	 */

	@Test
	public void driverEditSponsorsip() {
		final Object testingData[][] = {

			{
				"sponsor1", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1111222233334444", 12, 2020, 123, true, 0.0, null
			},//1. All fine
			{
				"sponsor2", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1111222233334444", 12, 2020, 123, true, 0.0,
				IllegalArgumentException.class
			},//2. Wrong user, same authority
			{
				"brotherhood1", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1111222233334444", 12, 2020, 123, true, 0.0,
				IllegalArgumentException.class
			},//3. Wrong authority
			{
				"", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1111222233334444", 12, 2020, 123, true, 0.0,
				IllegalArgumentException.class
			},//4. No authority 
			{
				"sponsorNoExistTest", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1111222233334444", 12, 2020, 123, true, 0.0,
				IllegalArgumentException.class
			},//5. No register user
			{
				"sponsor1", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1111222233334444", 12, 2018, 123, true, 0.0,
				IllegalArgumentException.class
			},//6. Credit Card expired year
			{
				"sponsor1", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1111222233334444", 01, 2019, 123, true, 0.0,
				IllegalArgumentException.class
			},//7. Credit Card expired month
			{
				"sponsor1", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1111222233334444", 01, 2018, 123, true, 0.0,
				IllegalArgumentException.class
			},//8. Credit Card expired both month and year 
			{
				"sponsor1", "sponsorship1", "test", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1111222233334444", 12,
				2020, 123, true, 0.0, ConstraintViolationException.class
			},//9. Incorrect banner URL
			{
				"sponsor1", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "test", "José", "VISA", "1111222233334444", 12,
				2020, 123, true, 0.0, ConstraintViolationException.class
			},//10. Incorrect target URL
			{
				"sponsor1", "sponsorship1", null, "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1111222233334444", 12,
				2020, 123, true, 0.0, ConstraintViolationException.class
			},//11. Null banner URL
			{
				"sponsor1", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", null, "José", "VISA", "1111222233334444", 12,
				2020, 123, true, 0.0, ConstraintViolationException.class
			},//12. Null target URL
			{
				"sponsor1", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", null, "VISA", "1111222233334444", 12, 2020, 123, true, 0.0,
				ConstraintViolationException.class
			},//13. Null holder name
			{
				"sponsor1", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", null, 12, 2020, 123, true, 0.0,
				ConstraintViolationException.class
			},//14. Null number
			{
				"sponsor1", "sponsorship1", "http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png",
				"http://www.foxandfiddlecalifornia.com/wp-content/uploads/2018/12/logo-clip-art-vector-online-royalty-free-public-domain-latest-copyright-logos-valuable-3.png", "José", "VISA", "1234", 12, 2020, 123, true, 0.0,
				ConstraintViolationException.class
			},//15. Incorrect number

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditSponsorship((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Integer) testingData[i][7], (Integer) testingData[i][8], (Integer) testingData[i][9], (Boolean) testingData[i][10], (Double) testingData[i][11], (Class<?>) testingData[i][12]);

	}
	protected void templateEditSponsorship(final String username, final int sponsorshipId, final String banner, final String targetUrl, final String holderName, final String make, final String number, final Integer expMonth, final Integer expYear,
		final Integer cvv, final Boolean activated, final Double cost, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			this.authenticate(username);

			final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);

			final CreditCard creditCard = new CreditCard();

			creditCard.setHolderName(holderName);
			creditCard.setMake(make);
			creditCard.setNumber(number);
			creditCard.setExpMonth(expMonth);
			creditCard.setExpYear(expYear);
			creditCard.setCvv(cvv);

			sponsorship.setBanner(banner);
			sponsorship.setTargetUrl(targetUrl);
			sponsorship.setCreditCard(creditCard);
			sponsorship.setActivated(activated);
			sponsorship.setCost(cost);

			this.sponsorshipService.save(sponsorship);
			this.sponsorshipService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-PARADE
	 * a)(Level A) Requirement 16.1: Sponsor manage his/her sponsorships : Create
	 * 
	 * b) Negative cases:
	 * 2. Wrong authority
	 * 3. No authority
	 * 4. No register user
	 * 5. Parade not accepted
	 * 6. Parade not exists
	 * 
	 * c) Sentence coverage
	 * -create(): 6 passed cases / 6 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Sponsorship: 0 passed cases / 4 total cases = 0%
	 */
	@Test
	public void driverCreateSponsorship() {
		final Object testingData[][] = {

			{
				"sponsor1", "parade6", null
			},//1. All fine
			{
				"brotherhood", "parade5", IllegalArgumentException.class
			},//2. Wrong authority
			{
				null, "parade5", IllegalArgumentException.class
			},//3. No authority
			{
				"sponsorNoTest", "parade5", IllegalArgumentException.class
			},//4. No register user
			{
				"sponsor1", "parade1", IllegalArgumentException.class
			},//5. Parade not accepted
			{
				"sponsor1", "paradeNoTest", AssertionError.class
			},//6. Parade not exists

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSponsorship((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateCreateSponsorship(final String username, final String parade, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			this.authenticate(username);

			final int paradeId = super.getEntityId(parade);

			final SponsorshipForm sponsorshipForm = this.sponsorshipService.create(paradeId);

			Assert.isTrue(sponsorshipForm != null);

		} catch (final Throwable oops) {

			caught = oops.getClass();

		}

		this.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-PARADE
	 * a)(Level A) Requirement 16.1: Sponsor manage his/her sponsorships: List
	 * 
	 * b) Negative cases:
	 * 2. Incorrect results
	 * 
	 * c) Sentence coverage
	 * -findAllBySponsorId(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Sponsorship: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverListSponsorship() {
		final Object testingData[][] = {

			{
				"sponsor1", 2, null
			},//1. All fine 
			{
				"sponsor1", 28, IllegalArgumentException.class
			},//2. Incorrect results

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListSponsorship((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateListSponsorship(final String username, final Integer expectedInt, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			super.authenticate(username);

			final int id = this.sponsorService.findByPrincipal().getId();

			final Integer result = this.sponsorshipService.findAllBySponsorId(id).size();
			Assert.isTrue(expectedInt == result);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-PARADE
	 * a)(Level A) Requirement 20: Every time a parade with sponsorships is displayed, a random sponsorship must be selected and its banner must be shown as little intrusively as possible.
	 * 
	 * b) Negative cases:
	 * 2. Wrong return
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 1 total cases = 100%
	 * -ramdomSponsorship(): 5 passed cases / 6 total cases = 83.33333%
	 * 
	 * d) Data coverage
	 * -Sponsorship: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverRandomSponsorship() {
		final Object testingData[][] = {

			{
				"parade5", "sponsorship1", "sponsorship2", "sponsorship6", "sponsorship10", null
			},//1. All fine 
			{
				"parade5", "sponsorship3", "sponsorship4", "sponsorship7", "sponsorship8", IllegalArgumentException.class
			},//2. Wrong return

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRandomSponsorship((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	protected void templateRandomSponsorship(final String pardeBean, final String sponsorshipBean1, final String sponsorshipBean2, final String sponsorshipBean3, final String sponsorshipBean4, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			final Parade parade = this.paradeService.findOne(super.getEntityId(pardeBean));

			final Collection<Sponsorship> sponsorships = new HashSet<>();
			final Sponsorship s1 = this.sponsorshipService.findOne(super.getEntityId(sponsorshipBean1));
			sponsorships.add(s1);
			final Sponsorship s2 = this.sponsorshipService.findOne(super.getEntityId(sponsorshipBean2));
			sponsorships.add(s2);
			final Sponsorship s3 = this.sponsorshipService.findOne(super.getEntityId(sponsorshipBean3));
			sponsorships.add(s3);
			final Sponsorship s4 = this.sponsorshipService.findOne(super.getEntityId(sponsorshipBean4));
			sponsorships.add(s4);

			this.startTransaction();

			final Sponsorship s = this.sponsorshipService.ramdomSponsorship(parade.getId());

			Assert.isTrue(sponsorships.contains(s));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-PARADE
	 * a)(Level A) Requirement 18.1: Administrator must be able to: Launch a process that automatically de-activates the sponsorships whose credit cards have expired.
	 * 
	 * b) Negative cases:
	 * 2. Invalid authority
	 * 
	 * c) Sentence coverage
	 * -deactivateExpiredCardSponsorships(): 2 passed cases / 5 total cases = 40%
	 * 
	 * d) Data coverage
	 * -Sponsorship: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverDeactivateExpiredCardSponsorships() {

		final Object testingData[][] = {

			{
				"admin", null
			}, //1. All fine
			{
				"member1", IllegalArgumentException.class
			},//2. Invalid authority

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeactivateExpiredCardSponsorships((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateDeactivateExpiredCardSponsorships(final String username, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			super.authenticate(username);

			this.startTransaction();

			this.sponsorshipService.deactivateExpiredCardSponsorships();
			this.sponsorshipService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}
	/*
	 * -------Coverage SponsorshipService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * save() = 100%
	 * create() = 100%
	 * findAll() = 100%
	 * list() = 100%
	 * findOne() = 100%
	 * ramdomSponsorship() = 83.33333%
	 * deactivateExpiredCardSponsorships = 40 %
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * -Sponsorship: 33,3%
	 * -CreditCard: 66,6%
	 */

}
