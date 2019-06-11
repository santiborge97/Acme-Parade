
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
import domain.Brotherhood;
import domain.Member;
import domain.Parade;
import domain.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private RequestService		requestService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private MemberService		memberService;


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
	 * a)(Level C) Requirement 10.6: Brotherhood manage the request to march on a procession: List
	 * 
	 * b) Negative cases:
	 * 2. Wrong return on pending list
	 * 3. Wrong return on approved and rejected list
	 * 
	 * c) Sentence coverage
	 * -findPendingRequestsByBrotherhoodId(): 1 passed cases / 2 total cases = 50%
	 * -findFinalRequestsByBrotherhoodId(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Request: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void driverBrotherhoodListRequest() {

		final Object testingData[][] = {

			{
				"brotherhood1", "4", "2", null
			}, //1. All fine
			{
				"brotherhood1", "8", "2", IllegalArgumentException.class
			}, //2. Wrong return on pending list
			{
				"brotherhood1", "4", "6", IllegalArgumentException.class
			}, //3. Wrong return on approved and rejected list

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateBrotherhoodListRequest((String) testingData[i][0], (int) Integer.valueOf((String) testingData[i][1]), (int) Integer.valueOf((String) testingData[i][2]), (Class<?>) testingData[i][3]);
	}

	protected void templateBrotherhoodListRequest(final String brotherhoodBean, final int size1, final int size2, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Brotherhood brotherhood = this.brotherhoodService.findOne(super.getEntityId(brotherhoodBean));

			this.startTransaction();

			final Collection<Request> pendingRequest = this.requestService.findPendingRequestsByBrotherhoodId(brotherhood.getId());

			final Collection<Request> totalRequest = this.requestService.findFinalRequestsByBrotherhoodId(brotherhood.getId());
			this.requestService.flush();

			Assert.isTrue(pendingRequest.size() == size1);
			Assert.isTrue(totalRequest.size() == size2);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 10.6: Brotherhood manage the request to march on a procession: Showing
	 * 
	 * b) Negative cases:
	 * 2. Not request
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Request: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void driverBrotherhoodDisplayRequest() {

		final Object testingData[][] = {

			{
				"request2", null
			}, //1. All fine
			{
				"segment1", IllegalArgumentException.class
			}, //2. Not request

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateBrotherhoodDisplayRequest((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateBrotherhoodDisplayRequest(final String requestBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.startTransaction();

			final Request request = this.requestService.findOne(super.getEntityId(requestBean));
			this.requestService.flush();

			Assert.isTrue(request != null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 10.6: Brotherhood manage the request to march on a procession: Reject
	 * 
	 * b) Negative cases:
	 * 2. Comment = JavaScript
	 * 
	 * c) Sentence coverage
	 * -save(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Request: 1 passed cases / 6 total cases = 16,66667%
	 */

	@Test
	public void driverRejectedRequest() {

		final Object testingData[][] = {

			{
				"request2", "Test", null
			}, //1. All fine
			{
				"request2", "<script>alert('hola')</script>", ConstraintViolationException.class
			}, //2. Comment = JavaScript 

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRejected((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateRejected(final String requestBean, final String comment, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Request request = this.requestService.findOne(super.getEntityId(requestBean));

			request.setComment(comment);
			request.setStatus("REJECTED");

			this.startTransaction();

			this.requestService.save(request);
			this.requestService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 10.6: Brotherhood manage the request to march on a procession: Accept
	 * 
	 * b) Negative cases:
	 * 2. Row number = negative number
	 * 3. Row number = too big number
	 * 
	 * c) Sentence coverage
	 * -save(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Request: 1 passed cases / 6 total cases = 16,66667%
	 */

	@Test
	public void driverAcceptedRequest() {

		final Object testingData[][] = {

			{
				"request2", 2, 1, null
			}, //1. All fine
			{
				"request2", -2, 1, ConstraintViolationException.class
			}, //2. Row number = negative number
			{
				"request2", 2000000000, 1, ConstraintViolationException.class
			}, //3. Row number = too big number

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAccepted((String) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateAccepted(final String requestBean, final Integer row, final Integer column, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Request request = this.requestService.findOne(super.getEntityId(requestBean));

			request.setRowNumber(row);
			request.setColumnNumber(column);
			request.setStatus("APPROVED");

			this.startTransaction();

			this.requestService.save(request);
			this.requestService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 11.1: Member manage his or her requests to march on a procession: List
	 * 
	 * b) Negative cases:
	 * 2. Wrong return
	 * 
	 * c) Sentence coverage
	 * -findRequestsByMemberId(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Request: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void driverMemberListRequest() {

		final Object testingData[][] = {

			{
				"member1", "8", null
			}, //1. All fine
			{
				"member1", "82", IllegalArgumentException.class
			}, //2. Wrong return 

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateMemberListRequest((String) testingData[i][0], (int) Integer.valueOf((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	protected void templateMemberListRequest(final String memberBean, final int size, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			final Member member = this.memberService.findOne(super.getEntityId(memberBean));

			this.startTransaction();

			final Collection<Request> requests = this.requestService.findRequestsByMemberId(member.getId());
			this.requestService.flush();

			Assert.isTrue(requests.size() == size);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 11.1: Member manage his or her requests to march on a procession: Display
	 * 
	 * b) Negative cases:
	 * 2. Wrong return
	 * 
	 * c) Sentence coverage
	 * -findOne(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Request: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void driverMemberDisplayRequest() {

		final Object testingData[][] = {

			{
				"request2", null
			}, //1. All fine
			{
				"box3", IllegalArgumentException.class
			}, //2. Not request

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateMemberDisplayRequest((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateMemberDisplayRequest(final String requestBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.startTransaction();

			final Request request = this.requestService.findOne(super.getEntityId(requestBean));
			this.requestService.flush();

			Assert.isTrue(request != null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 11.1: Member manage his or her requests to march on a procession: Create
	 * 
	 * b) Negative cases:
	 * 2. Invalid authority
	 * 
	 * c) Sentence coverage
	 * -create(): 1 passed cases / 3 total cases = 33.33333%
	 * -save(): 1 passed cases / 2 total cases = 50%
	 * 
	 * d) Data coverage
	 * -Request: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void driverMemberCreateRequest() {

		final Object testingData[][] = {

			{
				"member1", "parade1", null
			},//1. All fine
			{
				"brotherhood1", "parade1", IllegalArgumentException.class
			},//2. Invalid authority

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateMemberCreateRequest((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateMemberCreateRequest(final String username, final String paradeBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.authenticate(username);

			final Parade parade = this.paradeService.findOne(super.getEntityId(paradeBean));

			this.startTransaction();

			final Request request = this.requestService.create();
			request.setParade(parade);

			this.requestService.save(request);
			this.requestService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level C) Requirement 11.1: Member manage his or her requests to march on a procession: Delete
	 * 
	 * b) Negative cases:
	 * 2. Accepted request
	 * 
	 * c) Sentence coverage
	 * -delete(): 2 passed cases / 5 total cases = 40%
	 * 
	 * d) Data coverage
	 * -Request: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void driverMemberDeleteRequest() {

		final Object testingData[][] = {

			{
				"member1", "request2", null
			}, //1. All fine
			{
				"member1", "request1", IllegalArgumentException.class
			}, //2. Accepted request

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateMemberDeleteRequest((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateMemberDeleteRequest(final String username, final String requestBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.authenticate(username);

			this.startTransaction();

			final Request request = this.requestService.findOne(super.getEntityId(requestBean));

			this.requestService.delete(request);
			this.requestService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * -------Coverage RequestService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * findPendingRequestsByBrotherhoodId() = 50%
	 * findFinalRequestsByBrotherhoodId() = 50%
	 * findOne() = 50%
	 * save() = 50%
	 * findRequestsByMemberId() = 50%
	 * create() = 33.33333%
	 * delete() = 40%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Request = 33,33333%
	 */
}
