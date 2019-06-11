
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
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PositionServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private PositionService	positionService;


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
	 * a)(Level C) Requirement 12.2: Administrator manage the catalogue of positions: List
	 * 
	 * b) Negative cases:
	 * 2. Wrong return
	 * 
	 * c) Sentence coverage
	 * -findAll(): 1 passed cases / 3 total cases = 33.3%
	 * 
	 * d) Data coverage
	 * -Position: 0 passed cases / 2 total cases = 0%
	 */

	@Test
	public void driverListPosition() {

		final Object testingData[][] = {

			{
				"7", null
			}, //1. All fine
			{
				"8", IllegalArgumentException.class
			}, //2. Wrong return

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListPosition((int) Integer.valueOf((String) testingData[i][0]), (Class<?>) testingData[i][1]);
	}

	protected void templateListPosition(final int size, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.startTransaction();

			final Collection<Position> positions = this.positionService.findAll();
			this.positionService.flush();

			Assert.isTrue(positions.size() == size);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 12.2: Administrator manage the catalogue of positions: Display
	 * 
	 * b) Negative cases:
	 * 2. Not position
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Position: 0 passed cases / 2 total cases = 0%
	 */

	@Test
	public void driverDisplayPosition() {

		final Object testingData[][] = {

			{
				"position1", null
			}, //1. All fine
			{
				"message1", IllegalArgumentException.class
			}, //2. Not position

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDisplayPosition((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateDisplayPosition(final String positionBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.startTransaction();

			final Position position = this.positionService.findOne(super.getEntityId(positionBean));
			this.positionService.flush();

			Assert.isTrue(position != null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 12.2: Administrator manage the catalogue of positions: Create
	 * 
	 * b) Negative cases:
	 * 2. English name = null
	 * 3. English name = blank
	 * 
	 * c) Sentence coverage
	 * -create(): 1 passed cases / 1 total cases = 100%
	 * -save(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Position: 1 passed cases / 2 total cases = 50%
	 */

	@Test
	public void driverCreatePosition() {

		final Object testingData[][] = {

			{
				"Example", "Example", null
			}, //1. All fine
			{
				null, "Example", ConstraintViolationException.class
			}, //2. English name = null
			{
				"		", "Example", ConstraintViolationException.class
			}, //3. English name = blank

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreatePosition((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateCreatePosition(final String englishName, final String spanishName, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Position position = this.positionService.create();

			position.setEnglishName(englishName);
			position.setSpanishName(spanishName);

			this.startTransaction();

			this.positionService.save(position);
			this.positionService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 12.2: Administrator manage the catalogue of positions: Edit
	 * 
	 * b) Negative cases:
	 * 2. Spanish name = null
	 * 3. Spanish name = blank
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 2 total cases = 50%
	 * -save(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Position: 1 passed cases / 2 total cases = 50%
	 */

	@Test
	public void driverEditPosition() {

		final Object testingData[][] = {

			{
				"position6", "Fundraiser", "Example", null
			}, //1. All fine
			{
				"position6", "Fundraiser", null, ConstraintViolationException.class
			}, //2. Spanish name = null
			{
				"position6", "Fundraiser", "		", ConstraintViolationException.class
			}, //3. Spanish name = blank

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditPosition((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateEditPosition(final String positionBean, final String englishName, final String spanishName, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Position position = this.positionService.findOne(super.getEntityId(positionBean));

			position.setEnglishName(englishName);
			position.setSpanishName(spanishName);

			this.startTransaction();

			this.positionService.save(position);
			this.positionService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 12.2: Administrator manage the catalogue of positions: Delete
	 * 
	 * b) Negative cases:
	 * 2. Used position
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 2 total cases = 50%
	 * -delete(): 2 passed cases / 4 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Position: 0 passed cases / 2 total cases = 0%
	 */

	@Test
	public void driverDeletePosition() {

		final Object testingData[][] = {

			{
				"position4", null
			}, //1. Not used
			{
				"position2", IllegalArgumentException.class
			}, //2. Used

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeletePosition((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateDeletePosition(final String positionBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Position position = this.positionService.findOne(super.getEntityId(positionBean));

			this.startTransaction();

			this.positionService.delete(position);
			this.positionService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * -------Coverage PositionService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * save() = 50%
	 * findOne() = 50%
	 * findAll() = 33.33333%
	 * create() = 100%
	 * delete() = 50%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Position = 100%
	 */

}
