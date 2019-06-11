
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Parade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ParadeServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------

	@Autowired
	private ParadeService	paradeService;


	/*
	 * ----CALCULATE SENTENCE COVERAGE----
	 * To calculate the sentence coverage, we have to look at each "service's method"
	 * we are testing and we have to analyse its composition (if, for, Assert...) and Asserts.
	 * Then, we calculate the number of total cases which our code can execute. The equation will be:
	 * 
	 * (nï¿½ passed cases / nï¿½ total cases)*100 = coverage(%)
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
	 * (nï¿½ proven attributes/ nï¿½ total attributes)*100 = coverage(%)
	 * 
	 * ----Note:
	 * It's clear that if we have tested all cases about a method in a test
	 * and now It have already had a 100% of coverage, we don't have to
	 * mention its coverage in other test.
	 */

	/*
	 * ACME-PARADE
	 * a)(Level B)Requirement 2.2: Chapter manage the parades that are published by the brotherhoods in the area that they co-ordinate.
	 * 
	 * b)Negative cases:
	 * 2. A chapter that no coordinate a parade decides about it
	 * 
	 * c) Sentence coverage:
	 * -save(): 4 passed cases /13 total cases = 30,77%
	 * 
	 * d) Data coverage: 0%
	 * -Parade: 0 passed cases / 11 total cases = 0%
	 */

	@Test
	public void driverDecideParade() {
		final Object testingData[][] = {

			{
				"chapter1", "parade1", "ACCEPTED", null
			},//1. All fine
			{
				"chapter2", "parade1", "REJECTED", IllegalArgumentException.class
			},//2. A chapter that no coordinate a parade decides about it
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDecideParade((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	protected void templateDecideParade(final String username, final int paradeId, final String decision, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			final Parade parade = this.paradeService.findOne(paradeId);

			parade.setStatus(decision);

			this.startTransaction();
			this.paradeService.save(parade);
			this.paradeService.flush();

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();
	}

	/*
	 * ACME-PARADE
	 * a)(Level A) Requirement 14.1: An actor who is not authenticated must be able to: Navigate to the parades that a brotherhood organise
	 * 
	 * b)Negative cases:
	 * 2. The parade not belongs to brotherhood
	 * 3. Not accepted parade
	 * 
	 * c) Sentence coverage
	 * findParadeCanBeSeenOfBrotherhoodId(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Parade: 0 passed cases / 11 total cases = 0%
	 */

	@Test
	public void driverListParadeByBrotherhood() {
		final Object testingData[][] = {

			{
				"parade5", "brotherhood1", null
			},//1. All fine
			{
				"parade2", "brotherhood1", IllegalArgumentException.class
			},//2. The parade not belongs to brotherhood
			{
				"parade1", "brotherhood1", IllegalArgumentException.class
			},//3. Not accepted parade

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListParadeByBrotherhood((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateListParadeByBrotherhood(final String paradeId, final String brotherhoodId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			final Integer paradeIdInteger = super.getEntityId(paradeId);
			final Integer brotherhoodIdInteger = super.getEntityId(brotherhoodId);

			final Parade parade = this.paradeService.findOne(paradeIdInteger);

			final Collection<Parade> parades = this.paradeService.findParadeCanBeSeenOfBrotherhoodId(brotherhoodIdInteger);

			Assert.isTrue(parades.contains(parade));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-PARADE
	 * a)(Level B) Requirement 2.2: An actor who is not authenticated must be able to: Listing parades that are published by the brotherhoods in the area that they co-ordinate
	 * 
	 * b)Negative cases:
	 * 2. The parade not belongs
	 * 
	 * c) Sentence coverage
	 * findParadeCanBeSeenOfBrotherhoodIdForChapter(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Parade: 0 passed cases / 11 total cases = 0%
	 */

	@Test
	public void driverListParadeByBrotherhoodForChapter() {
		final Object testingData[][] = {

			{
				"parade5", "brotherhood1", null
			},//1. All fine
			{
				"parade2", "brotherhood1", IllegalArgumentException.class
			},//2. The parade not belongs to brotherhood

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListParadeByBrotherhoodForChapter((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateListParadeByBrotherhoodForChapter(final String paradeId, final String brotherhoodId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			final Integer paradeIdInteger = super.getEntityId(paradeId);
			final Integer brotherhoodIdInteger = super.getEntityId(brotherhoodId);

			final Parade parade = this.paradeService.findOne(paradeIdInteger);

			final Collection<Parade> parades = this.paradeService.findParadeCanBeSeenOfBrotherhoodId(brotherhoodIdInteger);

			Assert.isTrue(parades.contains(parade));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-PARADE
	 * a)(Level B) Requirement 3.2: An actor who is authenticated as a brotherhood must be able to: Make a copy of one of their parades
	 * 
	 * b)Negative test case:
	 * 2. Brothehood doesn't own the parade
	 * 
	 * c) Sentence coverage:
	 * -copy() = 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage:
	 * -Parade: 0 passed cases / 11 total cases = 0%
	 */
	@Test
	public void driverCopyParade() {
		final Object testingData[][] = {

			{
				"parade5", "brotherhood1", null

			},//1. All fine
			{
				"parade2", "brotherhood1", IllegalArgumentException.class
			},//2. Brothehood doesn't own the parade
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCopyParade((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateCopyParade(final String paradeId, final String brotherhoodId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.authenticate(brotherhoodId);

			final Integer paradeIdInteger = super.getEntityId(paradeId);

			final Parade parade = this.paradeService.findOne(paradeIdInteger);

			final Parade paradeCopy = this.paradeService.copy(paradeIdInteger);

			this.paradeService.flush();

			super.unauthenticate();
			Assert.isTrue(parade.getTitle().equals(paradeCopy.getTitle()));
			Assert.isTrue(parade.getDescription().equals(paradeCopy.getDescription()));
			Assert.isTrue(parade.getOrganisationMoment().equals(paradeCopy.getOrganisationMoment()));
			Assert.isTrue(paradeCopy.getFinalMode().equals(false));
			Assert.isNull(paradeCopy.getRejectedComment());
			Assert.isNull(paradeCopy.getStatus());
			Assert.isTrue(!parade.getTicker().equals(paradeCopy.getTicker()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 10.2: An actor who is authenticated as a brotherhood must be able to: List their parades
	 * 
	 * b)Negative cases:
	 * 2. The parade not belongs to brotherhood
	 * 
	 * c) Sentence coverage
	 * -findParadeByBrotherhoodId(): 1 passed cases / 1 total cases = 100%
	 * -findOne(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Parade: 0 passed cases / 11 total cases = 0%
	 */

	@Test
	public void driverListParadesOfABrotherhood() {
		final Object testingData[][] = {

			{
				"parade1", "brotherhood1", null
			},//1. All fine 
			{
				"parade2", "brotherhood1", IllegalArgumentException.class
			},//2. The parade not belongs to brotherhood

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListParadesOfABrotherhood((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateListParadesOfABrotherhood(final String paradeId, final String brotherhoodUsername, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			super.authenticate(brotherhoodUsername);
			final Integer paradeIdInteger = super.getEntityId(paradeId);
			final Integer brotherhoodIdInteger = super.getEntityId(brotherhoodUsername);

			final Parade parade = this.paradeService.findOne(paradeIdInteger);

			final Collection<Parade> parades = this.paradeService.findParadeByBrotherhoodId(brotherhoodIdInteger);
			super.unauthenticate();
			Assert.isTrue(parades.contains(parade));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 10.2: An actor who is authenticated as a brotherhood must be able to: Create a Parade
	 * 
	 * b)Negative cases:
	 * 2. Invalid authority
	 * 
	 * c) Sentence coverage
	 * -create(): 2 passed cases / 3 total cases = 66.66667%
	 * -save(): 1 passed cases / 14 total cases = 7,14%
	 * -findAll(): 1 passed cases / 2 total cases = 50%
	 * -findOne(): 1 passed cases/1 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Parade: 0 passed cases / 11 total cases = 0%
	 */

	@Test
	public void driverCreateParade() {
		final Object testingData[][] = {

			{
				"brotherhood1", "description1", "title1", "2019/09/25 11:00", true, 5, 5, "190925-KPPRM4", null
			},//1. All fine
			{
				"chapter1", "description1", "title1", "2019/09/25 11:00", true, 5, 5, "190925-PSPRM4", IllegalArgumentException.class
			},//2. Invalid authority

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateParade((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Boolean) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);

	}
	protected void templateCreateParade(final String actor, final String description, final String title, final String organisationMoment, final Boolean finalMode, final int maxColumn, final int maxRow, final String ticker, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();
			super.authenticate(actor);

			final Parade parade = this.paradeService.create();

			parade.setDescription(description);
			parade.setTitle(title);

			final DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			final Date date = format.parse(organisationMoment);

			parade.setOrganisationMoment(date);
			parade.setFinalMode(finalMode);
			parade.setMaxColumn(maxColumn);
			parade.setMaxRow(maxRow);
			parade.setTicker(ticker);

			final Parade saved = this.paradeService.save(parade);
			this.paradeService.flush();

			final Collection<Parade> parades = this.paradeService.findAll();
			super.unauthenticate();
			Assert.isTrue(parades.contains(saved));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}
		super.checkExceptions(expected, caught);

	}
	/*
	 * ACME-MADRUGÁ
	 * a)(Level C)Requirement 10.2: An actor who is authenticated as a brotherhood must be able to: Update a Float
	 * 
	 * b)Negative cases:
	 * 2. Invalid authority
	 * 3. The parade not belongs to brotherhood
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 1 total cases = 100%
	 * -save(): 2 passed cases / 14 total cases = 14,28%
	 * -findAll(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Parade: 0 passed cases / 11 total cases = 0%
	 */

	@Test
	public void driverUpdateParade() {
		final Object testingData[][] = {

			{
				"brotherhood1", "parade16", "description1", null
			},//1. All fine
			{
				"member1", "parade16", "description1", IllegalArgumentException.class
			},//2. Invalid authority 
			{
				"brotherhood1", "parade2", "description1", IllegalArgumentException.class
			},//3. The parade not belongs to brotherhood

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateParade((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	protected void templateUpdateParade(final String actor, final String paradeId, final String description, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			super.authenticate(actor);

			final Parade parade = this.paradeService.findOne(super.getEntityId(paradeId));
			parade.setDescription(description);

			this.startTransaction();
			final Parade saved = this.paradeService.save(parade);
			this.paradeService.flush();

			final Collection<Parade> parades = this.paradeService.findAll();
			super.unauthenticate();
			Assert.isTrue(parades.contains(saved));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 10.2: An actor who is authenticated as a brotherhood must be able to: Delete a Parade
	 * 
	 * b)Negative cases:
	 * 2. Invalid authority
	 * 3. The parade not belongs to brotherhood
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 1 total cases = 100%
	 * -findAll(): 1 passed cases / 2 total cases = 50%
	 * -delete(): 4 passed cases / 12 total cases = 33.33333%
	 * 
	 * d) Data coverage
	 * -Parade: 0 passed cases / 11 total cases = 0%
	 */

	@Test
	public void driverDeleteParade() {
		final Object testingData[][] = {

			{
				"brotherhood1", "parade16", null
			},//1. All fine
			{
				"chapter1", "parade16", IllegalArgumentException.class
			},//2. Invalid authority
			{
				"brotherhood1", "parade2", IllegalArgumentException.class
			},//3. The parade not belongs to brotherhood

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteParade((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateDeleteParade(final String actor, final String paradeId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			super.authenticate(actor);

			final Parade parade = this.paradeService.findOne(super.getEntityId(paradeId));

			this.startTransaction();
			this.paradeService.delete(parade);
			this.paradeService.flush();

			final Collection<Parade> parades = this.paradeService.findAll();
			super.unauthenticate();
			Assert.isTrue(!parades.contains(parade));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();

	}

	/*
	 * -------Coverage ParadeService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * save() = 30,77%
	 * findParadeCanBeSeenOfBrotherhoodId() = 100%
	 * findParadeCanBeSeenOfBrotherhoodIdForChapter() = 100%
	 * copy() = 50%
	 * findParadeByBrotherhoodId() =100%
	 * findOne() =100%
	 * create() =66.66667%
	 * findAll() =50%
	 * delete()= 33.33333%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * -Parade: 0 passed cases / 11 total cases = 0%
	 */

}
