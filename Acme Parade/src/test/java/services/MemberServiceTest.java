
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MemberServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private MemberService	memberService;


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
	 * a)(Level C) Requirement 8.1: An actor who is not authenticated must be able to: Register to the system as a member
	 * 
	 * b)Negative cases:
	 * 2. Name = javaScript
	 * 3. Name = blank
	 * 4. Middle name = blank
	 * 5. Photo = no URL
	 * 6. Email = no pattern
	 * 
	 * c) Sentence coverage:
	 * -save(): 1 passed cases / 5 total cases = 20%
	 * -create(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage:
	 * -Member: 4 passed cases / 11 total cases = 36,36364%
	 */

	@Test
	public void driverRegisterMember() {
		final Object testingData[][] = {
			{
				"name1", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "chapter56", "chapter56", null
			},//1. All fine
			{
				"<script>alert('hola')</script>", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "chapter57", "chapter57", ConstraintViolationException.class
			},//2. Name = javaScript
			{
				"", "middleName1", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "chapter58", "chapter58", ConstraintViolationException.class
			},//3. Name = blank
			{
				"name1", "", "surname1", "https://google.com", "email1@gmail.com", "672195205", "address1", "chapter59", "chapter59", null
			},//4. Middle name = blank
			{
				"name1", "middleName1", "surname1", "hola", "email1@gmail.com", "672195205", "address1", "chapter61", "chapter55", ConstraintViolationException.class
			},//5. Photo = no URL
			{
				"name1", "middleName1", "surname1", "https://google.com", "123455666", "672195205", "address1", "chapter64", "chapter64", IllegalArgumentException.class
			},//6. Email = no pattern

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterMember((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	protected void templateRegisterMember(final String name, final String middleName, final String surname, final String photo, final String email, final String phone, final String address, final String username, final String password,
		final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			final Member member = this.memberService.create();

			member.setName(name);
			member.setMiddleName(middleName);
			member.setSurname(surname);
			member.setPhoto(photo);
			member.setEmail(email);
			member.setPhone(phone);
			member.setAddress(address);

			member.getUserAccount().setUsername(username);
			member.getUserAccount().setPassword(password);

			this.startTransaction();

			this.memberService.save(member);
			this.memberService.flush();

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
	 * -save(): 2 passed cases / 5 total cases = 20%
	 * -findOne(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage
	 * -Member: 0 passed cases / 11 total cases = 0%
	 */

	@Test
	public void driverEditBrotherhood() {
		final Object testingData[][] = {

			{
				"member1", "member1", "name1", null
			}, //1.All fine
			{
				"chapter1", "member1", "name1", IllegalArgumentException.class
			}, //2. The user who is logged, It's not the same as the user who is being edited
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditBrotherhood((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	protected void templateEditBrotherhood(final String username, final int memberId, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);
			final Member member = this.memberService.findOne(memberId);

			member.setName(name);

			this.startTransaction();

			this.memberService.save(member);
			this.memberService.flush();

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();
	}

	/*
	 * -------Coverage MemberService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * -save() = 20%
	 * -findOne() = 100%
	 * -create() = 100%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Member = 36,36364%
	 */

}
