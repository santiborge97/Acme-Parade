
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ConfigurationServiceTest extends AbstractTest {

	//The SUT--------------------------------------------------
	@Autowired
	private ConfigurationService	configurationService;


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
	 * a)(Level A) Requirement 28.2: Administrator manage the lists of positive and negative words
	 * 
	 * b) Negative cases:
	 * 2. Not administrator
	 * 
	 * c) Sentence coverage
	 * -save(): 1 passed cases / 4 total cases = 25%
	 * -findConfiguration(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Configuration: 0 passed cases / 12 total cases = 0%
	 */

	@Test
	public void PositiveNegativeWordsTest() {
		final Object testingData[][] = {
			{
				"admin", "good", "excellent", "bad", null
			},//1. All fine
			{
				"member1", "good", "excellent", "bad", IllegalArgumentException.class
			},//2. Not administrator
		};

		for (int i = 0; i < testingData.length; i++)
			this.AuthorityPositiveNegativeWordsTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void AuthorityPositiveNegativeWordsTemplate(final String username, final String positive1, final String positive2, final String negative1, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			final Collection<String> positives = new HashSet<>();
			positives.add(positive1);
			positives.add(positive2);
			final Collection<String> negatives = new HashSet<>();
			negatives.add(negative1);

			final Configuration config = this.configurationService.findConfiguration();

			config.setPositiveWords(positives);
			config.setNegativeWords(negatives);

			this.startTransaction();

			this.configurationService.save(config);
			this.configurationService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		this.checkExceptions(expected, caught);
	}

	/*
	 * -------Coverage ConfigurationService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * save() = 25%
	 * findConfiguration() = 50%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Configuration = 0%
	 */
}
