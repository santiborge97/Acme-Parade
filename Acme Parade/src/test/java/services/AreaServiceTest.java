
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
import domain.Area;
import domain.Brotherhood;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AreaServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private AreaService			areaService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


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
	 * a)(Level B) Requirement 22.1: Administrator manage areas : List
	 * 
	 * b) Negative cases:
	 * 2. Wrong return
	 * 
	 * c) Sentence coverage
	 * -findAll(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Area: 0 passed cases / 3 total cases = 0%
	 */

	@Test
	public void driverListArea() {

		final Object testingData[][] = {

			{
				"4", null
			}, //1. All fine
			{
				"17", IllegalArgumentException.class
			}, //2. Wrong return

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListAreas((int) Integer.valueOf((String) testingData[i][0]), (Class<?>) testingData[i][1]);
	}

	protected void templateListAreas(final int size, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.startTransaction();

			final Collection<Area> areas = this.areaService.findAll();
			this.areaService.flush();

			Assert.isTrue(areas.size() == size);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level B) Requirement 22.1: Administrator manage areas : Display
	 * 
	 * b) Negative cases:
	 * 2. Not area
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Area: 0 passed cases / 3 total cases = 0%
	 */

	@Test
	public void driverDisplayArea() {

		final Object testingData[][] = {

			{
				"area1", null
			}, //1. All fine
			{
				"message1", IllegalArgumentException.class
			}, //2. Not area

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDisplayArea((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateDisplayArea(final String areaBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.startTransaction();

			final Area area = this.areaService.findOne(super.getEntityId(areaBean));
			this.areaService.flush();

			Assert.isTrue(area != null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level B) Requirement 22.1: Administrator manage areas : Create
	 * 
	 * b) Negative cases:
	 * 2. Name = null
	 * 3. Name = blank
	 * 
	 * c) Sentence coverage
	 * -create(): 1 passed cases / 1 total cases = 100%
	 * -save(): 1 passed / 5 total cases = 20%
	 * 
	 * d) Data coverage
	 * -Area: 1 passed cases / 3 total cases = 33,33333%
	 */

	@Test
	public void driverCreateArea() {

		final Object testingData[][] = {

			{
				"Test", "http://test.com", null
			}, //1. All fine
			{
				null, "http://test.com", ConstraintViolationException.class
			}, //2. Name = null
			{
				"		", "http://test.com", ConstraintViolationException.class
			}, //3. Name = blank

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateArea((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateCreateArea(final String name, final String picture, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Collection<String> pictures = new HashSet<>();
			pictures.add(picture);

			final Area area = this.areaService.create();

			area.setName(name);
			area.setPictures(pictures);

			this.startTransaction();

			this.areaService.save(area);
			this.areaService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level B) Requirement 22.1: Administrator manage areas : List
	 * 
	 * b) Negative cases:
	 * 2. Name = null
	 * 3. Name = blank
	 * 
	 * c) Sentence coverage
	 * -save(): 1 passed cases / 5 total cases = 20%
	 * 
	 * d) Data coverage
	 * -Area: 1 passed cases / 3 total cases = 33,33333%
	 */

	@Test
	public void driverEditArea() {

		final Object testingData[][] = {

			{
				"area1", "Test", "http://test.com", null
			}, //1. All fine
			{
				"area1", null, "http://test.com", ConstraintViolationException.class
			}, //2. Name = null
			{
				"area1", "		", "http://test.com", ConstraintViolationException.class
			}, //3. Name = blank

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditArea((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateEditArea(final String areaBean, final String name, final String picture, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Collection<String> pictures = new HashSet<>();
			pictures.add(picture);

			final Area area = this.areaService.findOne(super.getEntityId(areaBean));

			area.setName(name);
			area.setPictures(pictures);

			this.startTransaction();

			this.areaService.save(area);
			this.areaService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level B) Requirement 22.1: Administrator manage areas : Delete
	 * 
	 * b) Negative cases:
	 * 2. Assigned area
	 * 
	 * c) Sentence coverage
	 * -delete(): 2 passed cases / 3 total cases = 66.6%
	 * 
	 * d) Data coverage
	 * -Area: 0 passed cases / 3 total cases = 0%
	 */

	@Test
	public void driverDeleteArea() {

		final Object testingData[][] = {

			{
				"area4", null
			}, //1. All fine
			{
				"area1", IllegalArgumentException.class
			}, //2. Assigned area 

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteArea((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateDeleteArea(final String areaBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Area area = this.areaService.findOne(super.getEntityId(areaBean));

			this.startTransaction();

			this.areaService.delete(area);
			this.areaService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level B) Requirement 20.1: Brotherhood select the area in which it was settled
	 * 
	 * b) Negative cases:
	 * 2. Not area
	 * 
	 * c) Sentence coverage
	 * -save(): 1 passed cases / 5 total cases = 20%
	 * 
	 * d) Data coverage
	 * -Area: 0 passed cases / 3 total cases = 0%
	 */

	@Test
	public void driverAssignArea() {
		final Object testingData[][] = {

			{
				"brotherhood8", "area3", null
			},//1. All fine
			{
				"brotherhood1", "brotherhood1", IllegalArgumentException.class
			},//2. Not area

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAssignArea((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateAssignArea(final String username, final String areaBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Area area = this.areaService.findOne(super.getEntityId(areaBean));

			final Brotherhood brotherhood = this.brotherhoodService.findOne(super.getEntityId(username));

			brotherhood.setArea(area);

			if (area != null) {
				final Collection<Brotherhood> brotherhoods = area.getBrotherhoods();

				brotherhoods.add(brotherhood);

				area.setBrotherhoods(brotherhoods);
			}

			this.startTransaction();

			this.areaService.save(area);
			this.brotherhoodService.save(brotherhood);
			this.areaService.flush();
			this.brotherhoodService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();

	}

	/*
	 * -------Coverage AreaService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * save() = 20%
	 * findOne() = 100%
	 * create() = 100%
	 * delete() = 66.66%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Area = 33,33%
	 */
}
