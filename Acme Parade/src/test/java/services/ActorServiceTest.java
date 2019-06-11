
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private ActorService	actorService;


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
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 28.5: Administrator can ban an actor
	 * 
	 * b) Negative cases:
	 * 2. Ban yourself
	 * 
	 * c) Sentence coverage
	 * -banOrUnBanActor(): 2 passed cases / 7 total cases = 28,57%
	 * 
	 * d) Data coverage
	 * -Actor: 0 passed cases / 10 total cases = 0%
	 */

	@Test
	public void driverBanActor() {

		final Object testingData[][] = {

			{
				"member1", null
			}, //1. All fine
			{
				"administrator1", IllegalArgumentException.class
			}, //2. Ban yourself

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateBanActor((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateBanActor(final String actorBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			super.authenticate("admin");

			final Actor actor = this.actorService.findOne(super.getEntityId(actorBean));

			this.startTransaction();

			this.actorService.banOrUnBanActor(actor);
			this.actorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 28.6: Administrator can unban an actor
	 * 
	 * b) Negative cases:
	 * 2. Unban yourself
	 * 
	 * c) Sentence coverage
	 * -banOrUnBanActor(): 2 passed cases / 6 total cases = 33,33333%
	 * 
	 * d) Data coverage
	 * -Segment: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverUnbanActor() {

		final Object testingData[][] = {

			{
				"sponsor1", null
			}, //1. All fine
			{
				"administrator1", IllegalArgumentException.class
			}, //2. Unban yourself

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUnbanActor((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateUnbanActor(final String actorBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			super.authenticate("admin");

			final Actor actor = this.actorService.findOne(super.getEntityId(actorBean));

			this.startTransaction();

			this.actorService.banOrUnBanActor(actor);
			this.actorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * -------Coverage ActorService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * banOrUnBanActor = 50%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Actor = 0%
	 */
}
