
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Sponsor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private SponsorService	sponsorService;


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
	 * ACME-PARADE
	 * a)(Level A) Requirement 15.1: Register as a sponsor
	 * 
	 * b) Negative cases:
	 * 2. Surname = null
	 * 
	 * c)Sentence coverage:
	 * -create(); 1 passed cases / 1 total cases = 100%
	 * -save(): 1 passed cases / 5 total cases = 20%
	 * 
	 * d)Data coverage:
	 * -Sponsor: 1 passed cases / 10 total cases = 10%
	 */

	@Test
	public void driverRegisterSponsor() {
		final Object testingData[][] = {
			{
				"name1", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "chapter56", "chapter56", null
			},//1. All fine
			{
				"name1", "middleName1", null, "https://google.com", "email1@gmail.com", "672195205", "address1", "chapter57", "chapter57", ConstraintViolationException.class
			},//2. Surname =  null
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterSponsor((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	protected void templateRegisterSponsor(final String name, final String middleName, final String surname, final String photo, final String email, final String phone, final String address, final String username, final String password,
		final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			final Sponsor sponsor = this.sponsorService.create();

			sponsor.setName(name);
			sponsor.setMiddleName(middleName);
			sponsor.setSurname(surname);
			sponsor.setPhoto(photo);
			sponsor.setEmail(email);
			sponsor.setPhone(phone);
			sponsor.setAddress(address);

			sponsor.getUserAccount().setUsername(username);
			sponsor.getUserAccount().setPassword(password);

			this.startTransaction();

			this.sponsorService.save(sponsor);
			this.sponsorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 9.2: Actor manage his/her profile
	 * 
	 * b) Negative cases:
	 * 2. Invalid authenticated
	 * 
	 * c)Sentence coverage:
	 * -findOne(): 1 passed cases / 2 total cases = 50%
	 * -save() 2 passed cases / 5 total cases = 40%
	 * 
	 * d)Data coverage:
	 * -Sponsor: 0 passed cases / 10 total cases = 0%
	 */

	@Test
	public void driverEditSponsor() {
		final Object testingData[][] = {

			{
				"sponsor1", "sponsor1", "calle 13", "a@a.com", "3333", "middleName", "surname", "name", "http://www.photo.com", "title", null
			},//1. All fine
			{
				"brotherhood1", "sponsor1", "calle 13", "a@a.com", "+34 333 3333", "middleName", "surname", "name", "http://www.photo.com", "title", IllegalArgumentException.class
			},//2. Invalid authenticated 

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditSponsor((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);

	}

	protected void templateEditSponsor(final String username, final int sponsorId, final String address, final String email, final String phone, final String middleName, final String surname, final String name, final String photo, final String title,
		final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			final Sponsor sponsor = this.sponsorService.findOne(sponsorId);

			sponsor.setAddress(address);
			sponsor.setEmail(email);
			sponsor.setMiddleName(middleName);
			sponsor.setName(surname);
			sponsor.setName(name);
			sponsor.setPhoto(photo);

			this.startTransaction();

			this.sponsorService.save(sponsor);
			this.sponsorService.flush();

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();
	}

	/*
	 * -------Coverage SponsorService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * create() = 100%
	 * save() = 40%
	 * findOne() = 50%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Sponsor = 10%
	 */
}
