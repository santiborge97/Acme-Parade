
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class HistoryServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private HistoryService			historyService;

	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


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
	 * a)(Level C) Requirement 3.1 :An actor who is authenticated as a brotherhood must be able to: Manage their history: Create
	 * 
	 * b)Negative cases:
	 * 2. Not a Brotherhood
	 * 
	 * c) Sentence coverage
	 * -create(): 2 passed cases / 3 total cases = 66.66667%
	 * -save(): 1 passed cases / 5 total cases = 20%
	 * 
	 * d) Data coverage
	 * -History: not applicable
	 */

	@Test
	public void driverCreateHistory() {
		final Object testingData[][] = {
			{
				"brotherhood3", "title1", "descrption1", "http://photo1.com", "http://photo2.com", null
			},//1. All fine
			{
				"member1", "title1", "descrption1", "http://photo1.com", "http://photo2.com", IllegalArgumentException.class
			},//2. Not a Brotherhood
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateHistory((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	protected void templateCreateHistory(final String username, final String title, final String description, final String photo1, final String photo2, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			super.authenticate(username);

			final int brotherhoodId = super.getEntityId(username);

			final Brotherhood b = this.brotherhoodService.findOne(brotherhoodId);

			final InceptionRecord inceptionRecord = this.inceptionRecordService.create();

			final Collection<String> photos = new HashSet<String>();

			photos.add(photo1);
			photos.add(photo2);

			inceptionRecord.setTitle(title);
			inceptionRecord.setDescription(description);
			inceptionRecord.setPhotos(photos);

			final History history = this.historyService.create(inceptionRecord);

			history.setBrotherhood(b);
			history.setInceptionRecord(inceptionRecord);

			history.setLegalRecords(new ArrayList<LegalRecord>());
			history.setLinkRecords(new ArrayList<LinkRecord>());
			history.setPeriodRecords(new ArrayList<PeriodRecord>());
			history.setMiscellaneousRecords(new ArrayList<MiscellaneousRecord>());

			this.historyService.save(history);
			this.historyService.flush();

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
	 * -History: not applicable
	 */

	@Test
	public void driverListHistory() {
		final Object testingData[][] = {

			{
				2, null
			},//1. All fine
			{
				1651, IllegalArgumentException.class
			},//2. Incorrect result

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListHistory((Integer) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void templateListHistory(final Integer expectedInt, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			final Integer result = this.historyService.findAll().size();
			Assert.isTrue(expectedInt == result);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * -------Coverage HistoryService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * findAll() = 50%
	 * create() = 66.66667%
	 * save() = 20%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * History: not applicable
	 */

}
