
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
import domain.Actor;
import domain.Box;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BoxServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private BoxService		boxService;

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
	 * a)(Level A) Requirement 27.2: Actors manage their boxes : List
	 * 
	 * b) Negative cases:
	 * 2. Wrong return
	 * 
	 * c) Sentence coverage
	 * -findAllBoxByActor(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Box: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverListBox() {

		final Object testingData[][] = {

			{
				"brotherhood1", "5", null
			}, //1. All fine
			{
				"brotherhood1", "17", IllegalArgumentException.class
			}, //2. Wrong return

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListBoxes((String) testingData[i][0], (int) Integer.valueOf((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	protected void templateListBoxes(final String actorBean, final int size, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Actor actor = this.actorService.findOne(super.getEntityId(actorBean));

			this.startTransaction();

			final Collection<Box> boxes = this.boxService.findAllBoxByActor(actor.getId());
			this.boxService.flush();

			Assert.isTrue(boxes.size() == size);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 27.2: Actors manage their boxes : Display
	 * 
	 * b) Negative cases:
	 * 2. Not box
	 * 
	 * c) Sentence coverage
	 * -findOne(): 2 passed cases / 2 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Box: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverDisplayBox() {

		final Object testingData[][] = {

			{
				"box1", null
			}, //1. All fine
			{
				"message1", IllegalArgumentException.class
			}, //2. Not box

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDisplayBox((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateDisplayBox(final String boxBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.startTransaction();

			final Box box = this.boxService.findOne(super.getEntityId(boxBean));
			this.boxService.flush();

			Assert.isTrue(box != null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 27.2: Actors manage their boxes : Create
	 * 
	 * b) Negative cases:
	 * 2. Name = null
	 * 3. Name = blank
	 * 4. Name = default name
	 * 
	 * c) Sentence coverage
	 * -create(): 1 passed cases / 1 total cases = 100%
	 * -save(): 2 passed cases / 11 total cases = 27,27273%
	 * 
	 * d) Data coverage
	 * -Box: 1 passed cases / 4 total cases = 25%
	 */

	@Test
	public void driverCreateBox() {

		final Object testingData[][] = {

			{
				"Test", false, null, "member1", null
			}, //1. All fine
			{
				null, false, null, "member1", ConstraintViolationException.class
			}, //2. Name = null
			{
				"		", false, null, "member1", ConstraintViolationException.class
			}, //3. Name = blank
			{
				"in box", false, null, "member1", IllegalArgumentException.class
			}, //4. Name = default name

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateBox((String) testingData[i][0], (Boolean) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateCreateBox(final String name, final Boolean byDefault, final String parentBean, final String username, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		this.authenticate(username);

		try {

			final Box box = this.boxService.create();

			Box parent = null;

			if (parentBean != null)
				parent = this.boxService.findOne(super.getEntityId(parentBean));

			box.setName(name);
			box.setByDefault(byDefault);
			box.setParent(parent);

			this.startTransaction();

			this.boxService.save(box);
			this.boxService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 27.2: Actors manage their boxes : Edit
	 * 
	 * b) Negative cases:
	 * 2. Edit by default
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 2 total cases = 50%
	 * -save(): 2 passes cases / 11 total cases = 27,27273%
	 * 
	 * d) Data coverage
	 * -Box: 1 passed cases / 4 total cases = 25%
	 */

	@Test
	public void driverEditBox() {

		final Object testingData[][] = {

			{
				"box111", "Test", "admin", null
			}, //1. All fine
			{
				"box4", "Test", "admin", IllegalArgumentException.class
			}, //2. Edit by default

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditBox((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateEditBox(final String boxBean, final String name, final String username, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		this.authenticate(username);

		try {

			final Box box = this.boxService.findOne(super.getEntityId(boxBean));

			box.setName(name);

			this.startTransaction();

			this.boxService.save(box);
			this.boxService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 27.2: Actors manage their boxes : Delete
	 * 
	 * b) Negative cases:
	 * 2. Delete by default
	 * 
	 * c) Sentence coverage
	 * -delete(): 3 passed cases / 9 total cases = 33.33333%
	 * 
	 * d) Data coverage
	 * -Box: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverDeleteBox() {

		final Object testingData[][] = {

			{
				"box111", "admin", null
			}, //1. All fine
			{
				"box4", "admin", IllegalArgumentException.class
			}, //2. Delete by default

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteBox((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteBox(final String boxBean, final String username, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		this.authenticate(username);

		try {

			final Box box = this.boxService.findOne(super.getEntityId(boxBean));

			this.startTransaction();

			this.boxService.delete(box);
			this.boxService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * -------Coverage BoxService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * save() = 27,27273%
	 * findOne() = 100%
	 * create() = 100%
	 * delete() = 33.33333%
	 * findAllBoxByActor = 100%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Box = 25%
	 */
}
