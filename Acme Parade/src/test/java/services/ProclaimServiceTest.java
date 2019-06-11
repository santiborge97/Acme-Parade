
package services;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Chapter;
import domain.Proclaim;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ProclaimServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private ChapterService	chapterService;

	@Autowired
	private ProclaimService	proclaimService;


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
	 * a)(Level A) Requirement 14.2: An actor who is not authenticated must be able to: Browse the proclaims of the chapters
	 * 
	 * b)Negative cases:
	 * 2. The expected result is incorrect
	 * 
	 * c) Sentence coverage
	 * -findAll(): 1 passed cases / 2 total cases = 50%
	 * -save(): 1 passed cases / 6 total cases = 16.66667%
	 * 
	 * d) Data coverage
	 * -Proclaim: 0 passed cases / 3 total cases = 0%
	 */

	@Test
	public void driverListProclaims() {
		final Object testingData[][] = {

			{
				5, null
			},//1. All fine
			{
				28, IllegalArgumentException.class
			},//2. The expected result is incorrect

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListProclaims((Integer) testingData[i][0], (Class<?>) testingData[i][1]);

	}
	protected void templateListProclaims(final Integer expectedNumOfProclaims, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			super.authenticate("chapter1");

			final Proclaim proclaim = this.proclaimService.create();

			final Chapter chapter = this.chapterService.findOne(super.getEntityId("chapter1"));

			proclaim.setChapter(chapter);
			proclaim.setDescription("Hola Mundo");

			this.startTransaction();

			this.proclaimService.save(proclaim);
			this.proclaimService.flush();

			final Integer result = this.proclaimService.findAll().size();
			Assert.isTrue(expectedNumOfProclaims + 1 == result);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();

	}

	/*
	 * ACME-PARADE
	 * a)(Level A) Requirement 17.1: An actor who is authenticated as a chapter must be able to: Publish a proclaim
	 * 
	 * b)Negative cases:
	 * 2. A brotherhood is registered on the system
	 * 3. Description = null
	 * 4. Description = more than 250 characters (251)
	 * 
	 * c) Sentence coverage
	 * -create(): 2 passed cases / 3 total cases = 66.66667%
	 * -save(): 2 passed cases / 6 total cases = 33.33333%
	 * 
	 * d) Data coverage
	 * -Proclaim: 1 passed cases / 3 total cases = 33.33333%
	 */

	@Test
	public void driverCreateProclaim() {
		final Object testingData[][] = {

			{
				"chapter1", "Hola Mundo", null
			},//1. All fine
			{
				"brotherhood1", "Hola Mundo", IllegalArgumentException.class
			},//2. A brotherhood is registered on the system
			{
				"chapter1", null, ConstraintViolationException.class
			},//3. Description = null
			{
				"chapter1",
				"DPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDP" + "DPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPD" + "PDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPD" + "PDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPD"
					+ "PDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPD", ConstraintViolationException.class
			},//4. Description = more than 250 characters (251)
			{
				"chapter1",
				"DPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDP" + "DPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPD" + "PDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDP"
					+ "DPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDDPDPDPDPDP", null
			},//5. Description = 249 characters
			{
				"chapter1",
				"DPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDP" + "DPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPD" + "PDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDP"
					+ "DPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDPDDPDPDPDPDPD", null
			},//6. Description = 250 characters
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateProclaim((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateCreateProclaim(final String chapterRegistered, final String description, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();

			super.authenticate(chapterRegistered);

			final Proclaim proclaim = this.proclaimService.create();

			final Chapter chapter = this.chapterService.findOne(super.getEntityId("chapter1"));

			proclaim.setChapter(chapter);
			proclaim.setDescription(description);

			final Proclaim saved = this.proclaimService.save(proclaim);
			this.proclaimService.flush();

			final Collection<Proclaim> proclaims = this.proclaimService.findAll();
			Assert.isTrue(proclaims.contains(saved));

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * -------Coverage ProclaimService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * findAll() = 50%
	 * create() = 66.66667%
	 * save() = 33.33333%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Proclaim = 33.33333%
	 */

}
