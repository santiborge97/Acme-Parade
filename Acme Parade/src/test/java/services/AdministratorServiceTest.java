
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	//The SUT--------------------------------------------------
	@Autowired
	private AdministratorService	adminService;


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
	 * -Administrator: 0 passed cases / 10 total cases = 0%
	 */

	@Test
	public void driverEditAdmin() {
		final Object testingData[][] = {

			{
				"admin", "administrator1", "name1", null
			}, //1. All fine
			{
				"chapter1", "administrator1", "name1", IllegalArgumentException.class
			}, //2. The user who is logged, It's not the same as the user who is being edited
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditAdmin((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	protected void templateEditAdmin(final String username, final int adminId, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);
			final Administrator admin = this.adminService.findOne(adminId);

			admin.setName(name);

			this.startTransaction();

			this.adminService.save(admin);
			this.adminService.flush();

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 12.1: An actor who is authenticated as an administrator must be able to: Create user accounts for new administrators
	 * 
	 * b)Negative cases:
	 * 2. Name = blank
	 * 3. Name = html
	 * 4. The actor who is authenticated is not an Administrator
	 * 
	 * c) Sentence coverage:
	 * -save(): 1 passed cases / 5 total cases = 20%
	 * -create(): 2 passed cases / 3 total cases = 66.66667%
	 * 
	 * d) Data coverage:
	 * -Administrator: 1 passed cases / 10 total cases = 10%
	 */

	@Test
	public void driverRegisterAdmin() {
		final Object testingData[][] = {
			{
				"admin", "name1", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "admin55", "admin55", null
			},//1. All fine
			{
				"admin", "", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "admin55", "admin55", ConstraintViolationException.class
			},//2. Name = blank
			{
				"admin", "<script>alert('hola')</script>", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "admin55", "admin55", ConstraintViolationException.class
			},//3. Name = html
			{
				"brotherhood1", "name1", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "admin55", "admin55", IllegalArgumentException.class
			},//4. The actor who is authenticated is not an Administrator

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterAdmin((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
	}
	protected void templateRegisterAdmin(final String usernameLogin, final String name, final String middleName, final String surname, final String photo, final String email, final String phone, final String address, final String username,
		final String password, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();
			super.authenticate(usernameLogin);

			final Administrator admin = this.adminService.create();

			admin.setName(name);
			admin.setMiddleName(middleName);
			admin.setSurname(surname);
			admin.setPhoto(photo);
			admin.setEmail(email);
			admin.setPhone(phone);
			admin.setAddress(address);

			admin.getUserAccount().setUsername(username);
			admin.getUserAccount().setPassword(password);

			this.adminService.save(admin);
			this.adminService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}
		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 28.2: Administrator launch a process to set spammers
	 * 
	 * b) Negative cases:
	 * 2. Invalid authority
	 * 
	 * c) Sentence coverage
	 * -spammer(): 9 passed cases / 16 total cases = 56,25%
	 * 
	 * d) Data coverage
	 * -Administrator: 0 passed cases / 10 total cases = 0%
	 */

	@Test
	public void SpammerTest() {
		final Object testingData[][] = {
			{
				"admin", null
			},//1. All fine
			{
				"member1", IllegalArgumentException.class
			},//2. Invalid authority
		};

		for (int i = 0; i < testingData.length; i++)
			this.AuthoritySpammerTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void AuthoritySpammerTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			this.adminService.spammer();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 28.2: Administrator launch a process to set scores
	 * 
	 * b) Negative cases:
	 * 2. Invalid authority
	 * 
	 * c) Sentence coverage
	 * -calculateScore(): 7 passed cases / 9 total cases = 77,8%
	 * 
	 * d) Data coverage
	 * -Administrator: 0 passed cases / 10 total cases = 0%
	 */

	@Test
	public void ScoreTest() {
		final Object testingData[][] = {
			{
				"admin", null
			},//1. All fine
			{
				"member1", IllegalArgumentException.class
			},//2. Invalid authority
		};

		for (int i = 0; i < testingData.length; i++)
			this.AuthorityScoreTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void AuthorityScoreTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			this.adminService.calculateScore();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/*
	 * -------Coverage AdministratorService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * save() = 40%
	 * findOne() = 50%
	 * create() = 66.66667%
	 * spammer() = 56,25%
	 * calculateScore() = 77,8%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Administrator = 10%
	 */

}
