
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BrotherhoodServiceTest extends AbstractTest {

	//The SUT--------------------------------------------------
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
	 * a)(Level A) Requirement 14.1: An actor who is not authenticated must be able to: Navigate to the brotherhood that have settle in an area
	 * 
	 * b)Negative cases:
	 * 2. Area not contain this brotherhood
	 * 
	 * c) Sentence coverage
	 * -findBrotherhoodsByAreaId(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Brotherhood: 0 passed cases / 15 total cases = 0%
	 */

	@Test
	public void driverListBrotherhoodsByArea() {
		final Object testingData[][] = {

			{
				"area1", "brotherhood1", null
			},//1. All fine 
			{
				"area1", "brotherhood3", IllegalArgumentException.class
			},//2. Area not contain this brotherhood

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListBrotherhoodsByArea((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateListBrotherhoodsByArea(final String areaId, final String brotherhoodId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			final Integer areaIdInteger = super.getEntityId(areaId);
			final Integer brotherhoodIdInteger = super.getEntityId(brotherhoodId);
			final Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodIdInteger);

			final Collection<Brotherhood> brotherhoods = this.brotherhoodService.findBrotherhoodsByAreaId(areaIdInteger);

			Assert.isTrue(brotherhoods.contains(brotherhood));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 8.2 and 9.1 :An actor who is not authenticated must be able to: List the brotherhoods in the system.
	 * An actor who is authenticated must be able to: Do the same as an actor who is not authenticated, but register to the system.
	 * 
	 * b)Negative cases:
	 * 2. Wrong return
	 * 
	 * c) Sentence coverage:
	 * -findAll(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage:
	 * -Brotherhood: 0 passed cases / 15 total cases = 0%
	 */

	@Test
	public void driverListBrotherhoods() {
		final Object testingData[][] = {

			{
				8, null, null
			},//1. All fine
			{
				28, null, IllegalArgumentException.class
			},//2. Wrong return
			{
				8, "brotherhood1", null
			},//3. All fine(authenticated brotherhood)
			{
				8, "sponsor1", null
			},//4. All fine(authenticated sponsor) 
			{
				8, "chapter1", null
			},//5. All fine(authenticated chapter)
			{
				8, "member1", null
			},//6. All fine(authenticated member)
			{
				8, "admin", null
			},//7. All fine(authenticated administrator)

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListBrotherhoods((Integer) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateListBrotherhoods(final Integer expectedInt, final String user, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			if (user != null)
				super.authenticate(user);

			final Brotherhood brotherhood = this.brotherhoodService.create();

			brotherhood.setTitle("title1");
			final Collection<String> pictures = new ArrayList<>();
			pictures.add("https://docs.google.com/document/d/1mAOEp0duzbBYUXV0/edit");
			brotherhood.setPictures(pictures);
			final DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			final Date date = format.parse("1997/03/05");
			brotherhood.setEstablishment(date);
			brotherhood.setName("name1");
			brotherhood.setMiddleName("middleName1");
			brotherhood.setSurname("surname1");
			brotherhood.setPhoto("https://google.com");
			brotherhood.setEmail("email1@gmail.com");
			brotherhood.setPhone("672195205");
			brotherhood.setAddress("address1");

			brotherhood.getUserAccount().setUsername("brotherhood56");
			brotherhood.getUserAccount().setPassword("brotherhood56");

			this.startTransaction();

			this.brotherhoodService.save(brotherhood);
			this.brotherhoodService.flush();

			final Integer result = this.brotherhoodService.findAll().size();
			super.unauthenticate();
			Assert.isTrue(expectedInt + 1 == result);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 8.1: An actor who is not authenticated must be able to: Register to the system as a Brotherhood
	 * 
	 * b)Negative cases:
	 * 2. Title = blank
	 * 3. Title = javaScript
	 * 
	 * c) Sentence coverage:
	 * -save(): 1 passed cases / 5 total cases = 20%
	 * -create(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage:
	 * -Brotherhood: 1 passed cases / 15 total cases = 6,66667%
	 */

	@Test
	public void driverRegisterBrotherhood() {
		final Object testingData[][] = {
			{
				"title1", "https://docs.google.com/document/d/1mAOEp0duzbBYUXV0/edit", "1997/03/05", "name1", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "chapter56", "chapter56", null
			},//1. All fine
			{
				"", "https://docs.google.com/document/d/1mAOEp0duzbBYUXV0/edit", "1997/03/05", "name1", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "chapter56", "chapter56",
				ConstraintViolationException.class
			},//2. Title = blank
			{
				"<script>alert('hola')</script>", "https://docs.google.com/document/d/1mAOEp0duzbBYUXV0/edit", "1997/03/05", "name1", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "chapter56", "chapter56",
				ConstraintViolationException.class
			},//3. Title = javaScript

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterBrotherhood((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (Class<?>) testingData[i][12]);
	}
	protected void templateRegisterBrotherhood(final String title, final String picture, final String date, final String name, final String middleName, final String surname, final String photo, final String email, final String phone, final String address,
		final String username, final String password, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			final Brotherhood brotherhood = this.brotherhoodService.create();

			brotherhood.setTitle(title);
			final Collection<String> pictures = new ArrayList<>();
			pictures.add(picture);
			brotherhood.setPictures(pictures);
			final DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			final Date datee = format.parse(date);
			brotherhood.setEstablishment(datee);
			brotherhood.setName(name);
			brotherhood.setMiddleName(middleName);
			brotherhood.setSurname(surname);
			brotherhood.setPhoto(photo);
			brotherhood.setEmail(email);
			brotherhood.setPhone(phone);
			brotherhood.setAddress(address);

			brotherhood.getUserAccount().setUsername(username);
			brotherhood.getUserAccount().setPassword(password);

			this.startTransaction();

			this.brotherhoodService.save(brotherhood);
			this.brotherhoodService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 9.2: Actor who is authenticated: Edit personal data
	 * 
	 * b) Negative cases:
	 * 2. The user who is logged, It's not the same as the user who is being edited
	 * 
	 * c) Sentence coverage
	 * -save(): 2 passed cases / 5 total cases = 40%
	 * -findOne(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Brotherhood: 0 passed cases / 15 total cases = 0%
	 */

	@Test
	public void driverEditBrotherhood() {
		final Object testingData[][] = {

			{
				"brotherhood1", "brotherhood1", "title1", null
			}, //1.All fine
			{
				"chapter1", "brotherhood1", "title1", IllegalArgumentException.class
			}, //2. The user who is logged, It's not the same as the user who is being edited
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditBrotherhood((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	protected void templateEditBrotherhood(final String username, final int brotherhoodId, final String title, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);
			final Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodId);

			brotherhood.setTitle(title);

			this.startTransaction();

			this.brotherhoodService.save(brotherhood);
			this.brotherhoodService.flush();

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();
	}

	/*
	 * -------Coverage BrotherhoodService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * save() = 40%
	 * findOne() = 50%
	 * create() = 100%
	 * findAll() = 50%
	 * findBrotherhoodsByAreaId() = 50%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Brotherhood = 6,66667%
	 */

}
