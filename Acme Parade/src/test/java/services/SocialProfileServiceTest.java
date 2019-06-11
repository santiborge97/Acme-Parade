
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
import domain.SocialProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SocialProfileServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private SocialProfileService	socialProfileService;


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
	 * a)(Level A) Requirement 27.1: An actor who is authenticated must be able to: List his or her social profiles
	 * 
	 * b)Negative cases:
	 * 2. The social profile not belongs to actor
	 * 
	 * c) Sentence coverage
	 * -findAllByActor()= 1 passed cases / 2 total cases = 50%
	 * -findOne()= 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage
	 * -SocialProfile: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverListSocialProfiles() {
		final Object testingData[][] = {

			{
				"socialProfile1", "brotherhood1", null
			},//1. All fine
			{
				"socialProfile5", "brotherhood1", IllegalArgumentException.class
			},//2. The social profile not belongs to actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListSocialProfiles((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateListSocialProfiles(final String socialProfileId, final String brotherhoodUsername, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			super.authenticate(brotherhoodUsername);
			final Integer socialProfileIdInteger = super.getEntityId(socialProfileId);
			final Integer brotherhoodIdInteger = super.getEntityId(brotherhoodUsername);

			final SocialProfile socialProfile = this.socialProfileService.findOne(socialProfileIdInteger);

			final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllByActor(brotherhoodIdInteger);
			super.unauthenticate();
			Assert.isTrue(socialProfiles.contains(socialProfile));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A)Requirement 27.1: An actor who is authenticated must be able to: Create his or her social profiles
	 * 
	 * b)Negative cases:
	 * 2. Not authenticated
	 * 3. Nick = blank
	 * 4. Nick = not safe html
	 * 5. SocialName = blank
	 * 6. SocialName = not safe html
	 * 
	 * c) Sentence coverage
	 * -create() = 1 passed cases / 1 total cases =100%
	 * -save() = 2 passed cases / 4 total cases = 50%
	 * -findAll() = 1 passed cases / 2 total cases=50%
	 * 
	 * d) Data coverage
	 * -SocialProfile: 2 passed cases / 4 total cases = 50%
	 */

	@Test
	public void driverCreateSocialProfile() {
		final Object testingData[][] = {

			{
				"brotherhood1", "nick1", "socialName1", "https://www.youtube.com", null
			},//1. All fine
			{
				null, "nick1", "socialName1", "https://www.youtube.com", IllegalArgumentException.class
			},//2. Not authenticated
			{
				"brotherhood1", "", "socialName1", "https://www.youtube.com", ConstraintViolationException.class
			},//3. Nick = blank
			{
				"brotherhood1", "<script>alert('hola')</script>", "socialName1", "https://www.youtube.com", ConstraintViolationException.class
			},//4. Nick = not safe html
			{
				"brotherhood1", "nick1", "", "https://www.youtube.com", ConstraintViolationException.class
			},//5. SocialName = blank
			{
				"brotherhood1", "nick1", "<script>alert('hola')</script>", "https://www.youtube.com", ConstraintViolationException.class
			},//6. SocialName = not safe html

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSocialProfile((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);

	}
	protected void templateCreateSocialProfile(final String actor, final String nick, final String socialName, final String link, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			if (actor != null)
				super.authenticate(actor);

			this.startTransaction();

			final SocialProfile socialProfile = this.socialProfileService.create();
			socialProfile.setNick(nick);
			socialProfile.setLink(link);
			socialProfile.setSocialName(socialName);

			final SocialProfile saved = this.socialProfileService.save(socialProfile);
			this.socialProfileService.flush();

			final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAll();
			super.unauthenticate();
			Assert.isTrue(socialProfiles.contains(saved));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A)Requirement 27.1: An actor who is authenticated must be able to: Update his or her social profiles
	 * 
	 * b)Negative cases:
	 * 2. The social profile not belongs to actor
	 * 
	 * c) Sentence coverage
	 * -findOne() = 1 passed cases / 1 total cases = 100%
	 * -save() = 2 passed cases / 4 total cases = 50%
	 * -findAll()= 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -SocialProfile: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverUpdateSocialProfile() {
		final Object testingData[][] = {

			{
				"brotherhood1", "socialProfile1", "nick1", null
			},//1. All fine 
			{
				"brotherhood1", "socialProfile5", "nick1", IllegalArgumentException.class
			},//2. The social profile not belongs to actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateSocialProfile((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	protected void templateUpdateSocialProfile(final String actor, final String socialProfileId, final String nick, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			super.authenticate(actor);

			final SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId(socialProfileId));
			socialProfile.setNick(nick);

			this.startTransaction();
			final SocialProfile saved = this.socialProfileService.save(socialProfile);
			this.socialProfileService.flush();

			final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAll();
			super.unauthenticate();
			Assert.isTrue(socialProfiles.contains(saved));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();

	}
	/*
	 * ACME-MADRUGÁ
	 * a)(Level A)Requirement 27.1 :An actor who is authenticated must be able to: Delete his or her social profiles
	 * 
	 * b)Negative cases:
	 * 2. The social profile not belongs to brotherhood
	 * 
	 * c) Sentence coverage
	 * findOne() = 1 passed cases / 1 total cases = 100%
	 * delete() = 2 passed cases / 4 total cases = 50%
	 * findAll() = 1 passed cases / 2 total cases=50%
	 * 
	 * d) Data coverage
	 * -SocialProfile: 0 passed cases / 4 total cases = 0%
	 */

	@Test
	public void driverDeleteSocialProfile() {
		final Object testingData[][] = {

			{
				"brotherhood1", "socialProfile1", null
			},//1. All fine
			{
				"brotherhood1", "socialProfile5", IllegalArgumentException.class
			},//2. The social profile not belongs to brotherhood

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteSocialProfile((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateDeleteSocialProfile(final String actor, final String socialProfileId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			super.authenticate(actor);

			final SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId(socialProfileId));

			this.startTransaction();
			this.socialProfileService.delete(socialProfile);
			this.socialProfileService.flush();

			final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAll();
			super.unauthenticate();
			Assert.isTrue(!socialProfiles.contains(socialProfile));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();

	}

	/*
	 * -------Coverage SocialProfileService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * -findAllByActor() = 50%
	 * -findOne() = 100%
	 * -create() =100%
	 * -save() = 75%
	 * -findAll() =50%
	 * delete() = 50%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * SocialProfile = 50%
	 */

}
