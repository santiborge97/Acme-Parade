
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Finder;
import domain.Parade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FinderServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private FinderService	finderService;

	@Autowired
	private ActorService	actorService;


	/*
	 * ----CALCULATE SENTENCE COVERAGE----
	 * To calculate the sentence coverage, we have to look at each "service's method"
	 * we are testing and we have to analyse its composition (if, for, Assert...) and Asserts.
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
	 * ACME-MADRUGÁ
	 * a)(Level B) Requirement 21.2: An actor who is authenticated as a member must be able to: Manage his her finder
	 * 
	 * b)Negative cases:
	 * 2. Invalid authority
	 * 
	 * c) Sentence coverage
	 * -findFinderByMember(): 2 passed cases / 3 total cases = 66,66667%
	 * -save(): 2 passed cases / 7 total cases = 28,5%
	 * 
	 * d) Data coverage
	 * -Finder: 0 passed cases / 7 total cases = 0%
	 */

	@Test
	public void driverFinder() {
		final Object testingData[][] = {

			{
				"member1", "triana", null
			},//1. All fine
			{
				"chapter1", "triana", IllegalArgumentException.class
			},//2. Invalid authority

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateFinder((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateFinder(final String actor, final String area, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			super.authenticate(actor);

			final Finder finder = this.finderService.findFinderByMember(this.actorService.findOne(super.getEntityId(actor)));
			finder.setArea(area);
			final Finder saved = this.finderService.save(finder);
			super.unauthenticate();

			for (final Parade p : saved.getParades())
				Assert.isTrue(p.getBrotherhood().getArea().getName() == area);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

	}

	/*
	 * -------Coverage FinderService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * findFinderByMember() = 66,66667%
	 * save() = 28,5%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Finder = 0%
	 */
}
