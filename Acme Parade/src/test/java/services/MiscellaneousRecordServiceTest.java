
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.MiscellaneousRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MiscellaneousRecordServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;


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
	 * about the attribute. If we have passed all types of cases
	 * in a attribute, that is called "proven attribute".
	 * 
	 * (nº proven attributes/ nº total attributes)*100 = coverage(%)
	 * 
	 * ----Note:
	 * It's clear that if we have passed all cases about a method in a test
	 * and now It have already had a 100% of coverage, we don't have to
	 * mention its coverage in other test.
	 */

	/*
	 * ACME-PARADE
	 * a)(Level C) Requirement 3.1: An actor who is authenticated as a brotherhood must be able to: Manage their history: Create
	 * 
	 * b)Negative cases:
	 * 2. Title = null
	 * 3. Description = null
	 * 4. Description = ""
	 * 5. Title = ""
	 * 6. Not authority
	 * 7. Not a Brotherhood
	 * 
	 * c) Sentence coverage
	 * -create(): 3 passed cases / 3 total cases = 100%
	 * 
	 * d) Data coverage
	 * -MiscellaneousRecord: 2 passed cases / 2 total cases = 100%
	 */

	@Test
	public void driverCreateMiscellaneousRecord() {
		final Object testingData[][] = {
			{
				"brotherhood1", "title1", "descrption1", null
			},//1. All fine 
			{
				"brotherhood1", null, "descrption1", ConstraintViolationException.class
			},//2. Title = null
			{
				"brotherhood1", "title1", null, ConstraintViolationException.class
			},//3. Description = null
			{
				"brotherhood1", "title1", "", ConstraintViolationException.class
			},//4. Description = ""
			{
				"brotherhood1", "", "descrption1", ConstraintViolationException.class
			},//5. Title = ""
			{
				"null", "title1", "descrption1", IllegalArgumentException.class
			},//6. Not authority
			{
				"member1", "title1", "descrption1", IllegalArgumentException.class
			},//7. Not a Brotherhood
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateMiscellaneousRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	protected void templateCreateMiscellaneousRecord(final String username, final String title, final String description, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			super.authenticate(username);

			final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.create();

			miscellaneousRecord.setTitle(title);
			miscellaneousRecord.setDescription(description);

			this.miscellaneousRecordService.save(miscellaneousRecord);
			this.miscellaneousRecordService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		super.checkExceptions(expected, caught);

		this.rollbackTransaction();

	}

	/*
	 * ACME-PARADE
	 * a)(Level C) Requirement 3.1: An actor who is authenticated as a brotherhood must be able to: Manage their history: Edit
	 * 
	 * b)Negative cases:
	 * 2. Not authority
	 * 3. Not a Brotherhood
	 * 
	 * c) Sentence coverage
	 * -save(): 3 passed cases / 8 total cases = 37.5%
	 * 
	 * d) Data coverage
	 * -MiscellaneousRecord: 0 passed cases / 2 total cases = 0%
	 */

	@Test
	public void driverEditMiscellaneousRecord() {
		final Object testingData[][] = {
			{
				"brotherhood1", "miscellaneousRecord1", "title1", "descrption1", null
			},//1. All fine
			{
				null, "miscellaneousRecord1", "title1", "descrption1", IllegalArgumentException.class
			},//2. Not authority
			{
				"member1", "miscellaneousRecord1", "title1", "descrption1", IllegalArgumentException.class
			},//3. Not a Brotherhood
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditMiscellaneousRecord((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	protected void templateEditMiscellaneousRecord(final String username, final int miscellaneousRecordId, final String title, final String description, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			super.authenticate(username);

			final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);

			miscellaneousRecord.setTitle(title);
			miscellaneousRecord.setDescription(description);

			this.miscellaneousRecordService.save(miscellaneousRecord);
			this.miscellaneousRecordService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		super.checkExceptions(expected, caught);

		this.rollbackTransaction();

	}

	/*
	 * ACME-PARADE
	 * a)(Level C) Requirement 3.1: An actor who is authenticated as a brotherhood must be able to: Manage their history: Delete
	 * 
	 * b)Negative cases:
	 * 2. Not Authority
	 * 3. Invalid authority
	 * 
	 * c) Sentence coverage
	 * -delete(): 3 passed cases / 6 total cases = 50%
	 * 
	 * d) Data coverage
	 * -MiscellaneousRecord: 0 passed cases / 2 total cases = 0%
	 */

	@Test
	public void driverDeleteMiscellaneousRecord() {
		final Object testingData[][] = {

			{
				"brotherhood1", "miscellaneousRecord1", null
			},//1. All fine
			{
				null, "miscellaneousRecord1", IllegalArgumentException.class
			},//2. Not Authority
			{
				"member2", "miscellaneousRecord1", IllegalArgumentException.class
			},//3. Invalid authority
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMiscellaneousRecord((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);

	}

	protected void templateDeleteMiscellaneousRecord(final String username, final int miscellaneousRecordId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			super.authenticate(username);

			final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);

			this.miscellaneousRecordService.delete(miscellaneousRecord);
			this.miscellaneousRecordService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		super.checkExceptions(expected, caught);

		this.rollbackTransaction();

	}

	/*
	 * ACME-PARADE
	 * a)(Level C) Requirement 3.1: An actor who is authenticated as a brotherhood must be able to: Manage their history: List
	 * 
	 * b)Negative cases:
	 * 2. Incorrect result
	 * 
	 * c) Sentence coverage
	 * -findAll(): 1 passed case / 2 total case = 50%
	 * 
	 * d) Data coverage
	 * -MiscellaneousRecord: 0 passed cases / 2 total cases = 0%
	 */

	@Test
	public void driverListMiscellaneousRecord() {
		final Object testingData[][] = {

			{
				3, null
			},//1. All fine
			{
				2867, IllegalArgumentException.class
			},//2. Incorrect result

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListMiscellaneousRecord((Integer) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void templateListMiscellaneousRecord(final Integer expectedInt, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			final Integer result = this.miscellaneousRecordService.findAll().size();
			Assert.isTrue(expectedInt == result);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * -------Coverage MiscellaneousRecordService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * -create() = 100%
	 * -save() = 37.5%
	 * -delete() = 50%
	 * -findAll() = 50%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * MiscellaneousRecord = 100%
	 */

}
