
package services;

import java.util.ArrayList;
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
import domain.Float;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FloatServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private FloatService	floatService;


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
	 * a)(Level C) Requirement 8.2 and 9.2 :An actor who is not authenticated must be able to: List the floats that a brotherhood owns.
	 * An actor who is authenticated must be able to: Do the same as an actor who is not authenticated, but register to the system.
	 * 
	 * b)Negative cases:
	 * 2. The float not contains the brotherhood
	 * 
	 * c) Sentence coverage
	 * -findFloatsByBrotherhoodId(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Float: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverListFloatByBrotherhood() {
		final Object testingData[][] = {

			{
				null, "float4", "brotherhood1", null
			},//1. All fine(not authenticated)
			{
				null, "float2", "brotherhood1", IllegalArgumentException.class

			},//2. The float not contains the brotherhood
			{
				"brotherhood1", "float4", "brotherhood1", null
			},//3. All fine (authenticated brotherhood)
			{
				"chapter1", "float4", "brotherhood1", null
			},//4. All fine (authenticated chapter)
			{
				"admin", "float4", "brotherhood1", null
			},//5. All fine (authenticated administrator)
			{
				"member1", "float4", "brotherhood1", null
			},//6. All fine (authenticated member)
			{
				"sponsor1", "float4", "brotherhood1", null
			}, //7. All fine (authenticated sponsor)

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListFloatByBrotherhood((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	protected void templateListFloatByBrotherhood(final String user, final String floatId, final String brotherhoodId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			if (user != null)
				super.authenticate(user);
			final Integer floatIdInteger = super.getEntityId(floatId);
			final Integer brotherhoodIdInteger = super.getEntityId(brotherhoodId);

			final Float floatt = this.floatService.findOne(floatIdInteger);

			final Collection<Float> floats = this.floatService.findFloatsByBrotherhoodId(brotherhoodIdInteger);
			super.unauthenticate();
			Assert.isTrue(floats.contains(floatt));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 10.1: An actor who is authenticated as a brotherhood must be able to: List their floats
	 * 
	 * b)Negative cases:
	 * 2. The float not contains the brotherhood
	 * 
	 * c) Sentence coverage
	 * -findFloatsByBrotherhoodId(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Float: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverListFloatsOfABrotherhood() {
		final Object testingData[][] = {

			{
				"float4", "brotherhood1", null
			},//1. All fine
			{
				"float2", "brotherhood1", IllegalArgumentException.class
			},//2. The float not contains the brotherhood

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListFloatsOfABrotherhood((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateListFloatsOfABrotherhood(final String floatId, final String brotherhoodUsername, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			super.authenticate(brotherhoodUsername);
			final Integer floatIdInteger = super.getEntityId(floatId);
			final Integer brotherhoodIdInteger = super.getEntityId(brotherhoodUsername);

			final Float floatt = this.floatService.findOne(floatIdInteger);

			final Collection<Float> floats = this.floatService.findFloatsByBrotherhoodId(brotherhoodIdInteger);
			super.unauthenticate();
			Assert.isTrue(floats.contains(floatt));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C)Requirement 10.1: An actor who is authenticated as a brotherhood must be able to: Create a Float
	 * 
	 * b)Negative cases:
	 * 2. Invalid authority
	 * 3. Description = Not Safe Html
	 * 4. Title = blank
	 * 5. Pictures = no URL
	 * 
	 * c) Sentence coverage
	 * -create(): 2 passed cases / 3 total cases = 66.67%
	 * -save(): 1 passed cases / 5 total cases = 20%
	 * -findAll(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Float: 3 passed cases / 4 total cases = 75%
	 */

	@Test
	public void driverCreateFloat() {
		final Object testingData[][] = {

			{
				"brotherhood1", "description1", "title1", "https://www.youtube.com", null
			},//1. All fine
			{
				"chapter1", "description1", "title1", "https://www.youtube.com", IllegalArgumentException.class
			},//2. Invalid authority 
			{
				"brotherhood1", "<script>alert('hola')</script>", "title1", "https://www.youtube.com", ConstraintViolationException.class
			},//3. Description = Not Safe Html
			{
				"brotherhood1", "description1", "", "https://www.youtube.com", ConstraintViolationException.class
			},//4. Title = blank
			{
				"brotherhood1", "description1", "title1", "hola", IllegalArgumentException.class
			},//5. Pictures = no URL

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateFloat((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);

	}
	protected void templateCreateFloat(final String actor, final String description, final String title, final String picture, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			super.authenticate(actor);

			this.startTransaction();

			final Float floatt = this.floatService.create();
			floatt.setDescription(description);
			floatt.setTitle(title);
			final Collection<String> pictures = new ArrayList<>();
			pictures.add(picture);
			floatt.setPictures(pictures);

			final Float saved = this.floatService.save(floatt);
			this.floatService.flush();

			final Collection<Float> floats = this.floatService.findAll();
			Assert.isTrue(floats.contains(saved));

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C)Requirement 10.1 :An actor who is authenticated as a brotherhood must be able to: Update a Float
	 * 
	 * b)Negative cases:
	 * 2. Invalid authority
	 * 3. The float not belongs to brotherhood
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 1 total cases = 100%
	 * -save(): 1 passed cases / 5 total cases = 20%
	 * -findAll(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Float: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverUpdateFloat() {
		final Object testingData[][] = {

			{
				"brotherhood1", "float4", "description1", null
			},//1. All fine
			{
				"chapter1", "float4", "description1", IllegalArgumentException.class
			},//2. Invalid authority
			{
				"brotherhood1", "float3", "description1", IllegalArgumentException.class
			},//3. The float not belongs to brotherhood

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateFloat((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	protected void templateUpdateFloat(final String actor, final String floatId, final String description, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			super.authenticate(actor);

			final Float floatt = this.floatService.findOne(super.getEntityId(floatId));
			floatt.setDescription(description);

			this.startTransaction();
			final Float saved = this.floatService.save(floatt);
			this.floatService.flush();

			final Collection<Float> floats = this.floatService.findAll();
			super.unauthenticate();
			Assert.isTrue(floats.contains(saved));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}
	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 10.1 :An actor who is authenticated as a brotherhood must be able to: Delete a Float
	 * 
	 * b)Negative cases:
	 * 2. Invalid authority
	 * 3. The float not belongs to brotherhood
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 1 total cases = 100%
	 * -findAll(): 1 passed cases / 2 total cases = 50%
	 * -delete(): 3 passed cases / 9 total cases = 33.33333%
	 * 
	 * d) Data coverage
	 * -Float: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverDeleteFloat() {
		final Object testingData[][] = {

			{
				"brotherhood1", "float4", null
			},//1. All fine
			{
				"chapter1", "float4", IllegalArgumentException.class
			},//2. Invalid authority
			{
				"brotherhood1", "float3", IllegalArgumentException.class
			},//3. The float not belongs to brotherhood

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteFloat((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateDeleteFloat(final String actor, final String floatId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			super.authenticate(actor);

			final Float floatt = this.floatService.findOne(super.getEntityId(floatId));

			this.startTransaction();

			this.floatService.delete(floatt);
			this.floatService.flush();

			final Collection<Float> floats = this.floatService.findAll();
			super.unauthenticate();
			Assert.isTrue(!floats.contains(floatt));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	/*
	 * -------Coverage FloatService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * findOne() = 100%
	 * findAll() = 50%
	 * delete() = 33.33333%
	 * create() = 66.66667%
	 * save() = 20%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Float = 75%
	 */
}
