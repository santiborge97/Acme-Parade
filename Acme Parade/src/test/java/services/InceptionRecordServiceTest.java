
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
import domain.InceptionRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class InceptionRecordServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private InceptionRecordService	inceptionRecordService;


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
	 * -InceptionRecord: 2 passed cases / 3 total cases = 66.66667%
	 */

	@Test
	public void driverCreateInceptionRecord() {
		final Object testingData[][] = {
			{
				"brotherhood1", "title1", "descrption1", "http://photo1.com", "http://photo2.com", null
			},//1. All fine
			{
				"brotherhood1", null, "descrption1", "http://photo1.com", "http://photo2.com", ConstraintViolationException.class
			},//2. Title = null
			{
				"brotherhood1", "title1", null, "http://photo1.com", "http://photo2.com", ConstraintViolationException.class
			},//3. Description = null
			{
				"brotherhood1", "title1", "", "http://photo1.com", "http://photo2.com", ConstraintViolationException.class
			},//4. Description = ""
			{
				"brotherhood1", "", "descrption1", "http://photo1.com", "http://photo2.com", ConstraintViolationException.class
			},//5. Title = ""
			{
				null, "title1", "descrption1", "http://photo1.com", "http://photo2.com", IllegalArgumentException.class
			},//6. Not authority
			{
				"member1", "title1", "descrption1", "http://photo1.com", "http://photo2.com", IllegalArgumentException.class
			},//7. Not a Brotherhood
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateInceptionRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateCreateInceptionRecord(final String username, final String title, final String description, final String photo1, final String photo2, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			super.authenticate(username);

			final InceptionRecord inceptionRecord = this.inceptionRecordService.create();

			final Collection<String> photos = new HashSet<String>();
			photos.add(photo1);
			photos.add(photo2);

			inceptionRecord.setTitle(title);
			inceptionRecord.setDescription(description);
			inceptionRecord.setPhotos(photos);

			this.inceptionRecordService.save(inceptionRecord);
			this.inceptionRecordService.flush();

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
	 * -save(): 3 passed cases / 6 total cases = 50%
	 * 
	 * d) Data coverage
	 * -InceptionRecord: 0 passed cases / 3 total cases = 0%
	 */

	@Test
	public void driverEditInceptionRecord() {
		final Object testingData[][] = {
			{
				"brotherhood1", "inceptionRecord1", "title1", "descrption1", "http://photo1.com", "http://photo2.com", null
			},//1. All fine
			{
				null, "inceptionRecord1", "title1", "descrption1", "http://photo1.com", "http://photo2.com", IllegalArgumentException.class
			},//2. Not authority
			{
				"member1", "inceptionRecord1", "title1", "descrption1", "http://photo1.com", "http://photo2.com", IllegalArgumentException.class
			},//3. Not a Brotherhood
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditInceptionRecord((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
				(Class<?>) testingData[i][6]);
	}
	protected void templateEditInceptionRecord(final String username, final int inceptionRecordId, final String title, final String description, final String photo1, final String photo2, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			super.authenticate(username);

			final InceptionRecord inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordId);

			final Collection<String> photos = new HashSet<String>();
			photos.add(photo1);
			photos.add(photo2);

			inceptionRecord.setTitle(title);
			inceptionRecord.setDescription(description);
			inceptionRecord.setPhotos(photos);

			this.inceptionRecordService.save(inceptionRecord);
			this.inceptionRecordService.flush();

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
	 * -InceptionRecord: 0 passed cases / 3 total cases = 0%
	 */

	@Test
	public void driverListInceptionRecord() {
		final Object testingData[][] = {

			{
				2, null
			},//1. All fine
			{
				1651, IllegalArgumentException.class
			},//2. Incorrect result

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListInceptionRecord((Integer) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void templateListInceptionRecord(final Integer expectedInt, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			final Integer result = this.inceptionRecordService.findAll().size();
			Assert.isTrue(expectedInt == result);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * -------Coverage InceptionRecordService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * findAll() = 50%
	 * save() = 50%
	 * create() = 100%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * InceptionRecord = 66,66667%
	 */

}
