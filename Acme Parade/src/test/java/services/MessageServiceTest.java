
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Box;
import domain.Enrolment;
import domain.Message;
import domain.Parade;
import domain.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MessageServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private MessageService		messageService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private BoxService			boxService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private RequestService		requestService;


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
	 * a)(Level A) Requirement 26: Actors can exchange messages
	 * 
	 * b)Negative cases:
	 * 2. Not authenticated
	 * 
	 * c) Sentence coverage:
	 * -create3(): 1 passed cases / 1 total cases=100%
	 * -save():1 passed cases / 5 total cases = 20%
	 * 
	 * d) Data coverage:
	 * -Message: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void driverExchangeMessage() {
		final Object testingData[][] = {
			{
				"member1", "brotherhood1", null
			},//1. All fine
			{
				null, "brotherhood1", AssertionError.class
			},//2. Not authenticated

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateExchangeMessage((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateExchangeMessage(final String sender, final String recipient, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();
			if (sender != null)
				super.authenticate(sender);

			final Message message = this.messageService.create3();
			message.setBody("Cuerpo1TEST");
			message.setRecipient(this.actorService.findOne(super.getEntityId(recipient)));
			message.setSubject("Subject1TEST");
			message.setTags("Tag1TEST");

			final Collection<Box> boxes = new ArrayList<Box>();
			boxes.add(this.boxService.findOutBoxByActorId(super.getEntityId(sender)));
			boxes.add(this.boxService.findInBoxByActorId(super.getEntityId(recipient)));
			message.setBoxes(boxes);

			final Message saved = this.messageService.save(message);
			this.messageService.flush();

			final Box recipientBox = this.boxService.findInBoxByActorId(super.getEntityId(recipient));

			final Collection<Message> messages = this.messageService.findMessagesByBoxId(recipientBox.getId());
			Assert.isTrue(messages.contains(saved));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
		this.rollbackTransaction();

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 28.1: An actor who is authenticated as an administrator must be able to: Broadcast a notification to the actors of the system
	 * 
	 * b)Negative cases:
	 * 2. Not authenticated actor
	 * 
	 * c) Sentence coverage:
	 * -broadcastSystem(): 2 passed cases / 6 total cases = 33.33333%
	 * 
	 * d) Data coverage:
	 * -Message: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void driverBroadcastMessage() {
		final Object testingData[][] = {
			{
				"admin", "chapter1", null
			},//1. All fine
			{
				null, "brotherhood1", IllegalArgumentException.class
			},//2. Not authenticated actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateBroadcastMessage((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateBroadcastMessage(final String adminUsername, final String recipient, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();
			if (adminUsername != null)
				super.authenticate(adminUsername);

			final Message message = this.messageService.create3();
			message.setBody("Cuerpo1TEST");
			message.setRecipient(this.actorService.findByPrincipal());
			message.setSender(this.actorService.findByPrincipal());
			message.setSubject("Subject1TEST");
			message.setTags("Tag1TEST");

			this.messageService.broadcastSystem(message);

			final Message saved = this.messageService.save(message);
			this.messageService.flush();

			final Box recipientBox = this.boxService.findNotificationBoxByActorId(super.getEntityId(recipient));

			final Collection<Message> messages = this.messageService.findMessagesByBoxId(recipientBox.getId());

			Assert.isTrue(messages.contains(saved));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
		super.unauthenticate();
		this.rollbackTransaction();

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 32: System notifications (new parade)
	 * 
	 * b)Negative cases:
	 * 2. Wrong destination
	 * 
	 * c) Sentence coverage:
	 * -NotificationNewParade(): 1 passed cases / 3 total cases = 33.33333%
	 * 
	 * d) Data coverage:
	 * -Message: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void driverNotificationNewParade() {
		final Object testingData[][] = {
			{
				"parade1", "box20", null
			},//1.All fine
			{
				"parade1", "box22", IllegalArgumentException.class
			},//2. Wrong destination

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNotificationNewParadeMessage((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateNotificationNewParadeMessage(final String parade, final String box, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();

			super.authenticate("admin");

			Collection<Message> messages = this.messageService.findMessagesByBoxId(super.getEntityId(box));

			final Integer oldSize = messages.size();

			final Parade paradeFind = this.paradeService.findOne(super.getEntityId(parade));
			this.messageService.NotificationNewParade(paradeFind, paradeFind.getBrotherhood());

			messages = this.messageService.findMessagesByBoxId(super.getEntityId(box));

			Assert.isTrue(messages.size() == oldSize + 1);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

		super.unauthenticate();

		this.rollbackTransaction();

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 32: System notifications (new enrolment)
	 * 
	 * b)Negative cases:
	 * 2. Wrong destination
	 * 
	 * c) Sentence coverage:
	 * -NotificationNewEnrolment(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage:
	 * -Message: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void notificationNewEnrolment() {
		final Object testingData[][] = {
			{
				"enrolment2", "box20", null
			},//1. All fine
			{
				"enrolment2", "box22", IllegalArgumentException.class
			},//2. Wrong destination

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNotificationNewEnrolment((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateNotificationNewEnrolment(final String enrolment, final String box, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();

			super.authenticate("admin");

			Collection<Message> messages = this.messageService.findMessagesByBoxId(super.getEntityId(box));

			final Integer oldSize = messages.size();

			final Enrolment enrolmentFind = this.enrolmentService.findOne(super.getEntityId(enrolment));

			this.messageService.NotificationNewEnrolment(enrolmentFind);

			messages = this.messageService.findMessagesByBoxId(super.getEntityId(box));

			Assert.isTrue(messages.size() == oldSize + 1);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

		super.unauthenticate();

		this.rollbackTransaction();

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 32: System notifications (dropOut member)
	 * 
	 * b)Negative cases:
	 * 2. Wrong destination
	 * 
	 * c)Sentence coverage:
	 * -NotificationDropOutMember(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage:
	 * -Message: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void notificationDropOutMember() {
		final Object testingData[][] = {
			{
				"enrolment2", "box10", null
			},//1. All fine
			{
				"enrolment2", "box22", IllegalArgumentException.class
			},//2. Wrong destination

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNotificationDropOutMember((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateNotificationDropOutMember(final String enrolment, final String box, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();

			super.authenticate("admin");

			Collection<Message> messages = this.messageService.findMessagesByBoxId(super.getEntityId(box));

			final Integer oldSize = messages.size();

			final Enrolment enrolmentFind = this.enrolmentService.findOne(super.getEntityId(enrolment));

			this.messageService.NotificationDropOutMember(enrolmentFind);

			messages = this.messageService.findMessagesByBoxId(super.getEntityId(box));

			Assert.isTrue(messages.size() == oldSize + 1);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

		super.unauthenticate();

		this.rollbackTransaction();

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 32: System notifications (dropOut brotherhood)
	 * 
	 * b)Negative cases:
	 * 2. Wrong destination
	 * 
	 * c) Sentence coverage:
	 * -NotificationDropOutBrotherhood(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage:
	 * -Message: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void notificationDropOutBrotherhood() {
		final Object testingData[][] = {
			{
				"enrolment2", "box20", null
			},//1.All fine
			{
				"enrolment2", "box22", IllegalArgumentException.class
			},//2. Wrong destination

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNotificationDropOutBrotherhood((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateNotificationDropOutBrotherhood(final String enrolment, final String box, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();

			super.authenticate("admin");

			Collection<Message> messages = this.messageService.findMessagesByBoxId(super.getEntityId(box));

			final Integer oldSize = messages.size();

			final Enrolment enrolmentFind = this.enrolmentService.findOne(super.getEntityId(enrolment));

			this.messageService.NotificationDropOutBrotherhood(enrolmentFind);

			messages = this.messageService.findMessagesByBoxId(super.getEntityId(box));

			Assert.isTrue(messages.size() == oldSize + 1);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

		super.unauthenticate();

		this.rollbackTransaction();

	}

	/*
	 * ACME-MADRUGÁ
	 * a)(Level A) Requirement 32: System notifications (request status)
	 * 
	 * b)Negative cases:
	 * 2. Wrong destination
	 * 
	 * c) Sentence coverage:
	 * -NotificationRequestStatus(): 1 passed cases / 1 total cases = 100%
	 * 
	 * d) Data coverage:
	 * -Message: 0 passed cases / 6 total cases = 0%
	 */

	@Test
	public void notificationRequestStatus() {
		final Object testingData[][] = {
			{
				"request2", "box20", null
			},//1. All fine
			{
				"request2", "box22", IllegalArgumentException.class
			},//2. Wrong destination

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNotificationRequestStatus((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateNotificationRequestStatus(final String request, final String box, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();

			super.authenticate("admin");

			Collection<Message> messages = this.messageService.findMessagesByBoxId(super.getEntityId(box));

			final Integer oldSize = messages.size();

			final Request requestFind = this.requestService.findOne(super.getEntityId(request));

			this.messageService.NotificationRequestStatus(requestFind);

			messages = this.messageService.findMessagesByBoxId(super.getEntityId(box));

			Assert.isTrue(messages.size() == oldSize + 1);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

		super.unauthenticate();

		this.rollbackTransaction();

	}

	/*
	 * -------Coverage MessageService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * -create3() =100%
	 * -save() = 20%
	 * -broadcastSystem() = 33.33333%
	 * -NotificationNewParade() = 33.33333%
	 * -NotificationNewEnrolment() = 100%
	 * -NotificationDropOutMember() = 100%
	 * -NotificationDropOutBrotherhood() = 100%
	 * -NotificationRequestStatus() = 100%
	 * 
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Message = 0%
	 */
}
