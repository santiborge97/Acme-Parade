
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChapterRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Box;
import domain.Chapter;
import forms.RegisterChapterForm;

@Service
@Transactional
public class ChapterService {

	// Managed repository

	@Autowired
	private ChapterRepository	chapterRepository;

	// Suporting services

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private BoxService			boxService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods 

	public Chapter create() {
		final Chapter chapter = new Chapter();

		final UserAccount userAccount = this.userAccountService.createChapter();
		chapter.setUserAccount(userAccount);

		chapter.setScore(null);
		chapter.setSpammer(null);

		return chapter;

	}

	public Chapter findOne(final int chapterId) {

		Chapter chapter;
		chapter = this.chapterRepository.findOne(chapterId);
		return chapter;

	}

	public Collection<Chapter> findAll() {

		Collection<Chapter> result;
		result = this.chapterRepository.findAll();
		Assert.notNull(result);
		return result;

	}

	public Chapter save(final Chapter chapter) {

		Chapter result = null;
		Assert.notNull(chapter);

		if (chapter.getId() != 0) {

			final Actor actor = this.actorService.findByPrincipal();
			Assert.notNull(actor);

			Assert.isTrue(actor.getId() == chapter.getId());

			final Chapter chapterBBDD = this.findOne(chapter.getId());

			if (chapterBBDD.getArea() != null)
				Assert.isTrue(chapterBBDD.getArea().equals(chapter.getArea()));
			else if (chapterBBDD.getArea() == null && chapter.getArea() != null) {
				final Chapter chapterByArea = this.findChapterByAreaId(chapter.getArea().getId());
				Assert.isNull(chapterByArea);
			}

			this.actorService.checkEmail(chapter.getEmail(), false);
			this.actorService.checkPhone(chapter.getPhone());

			final String phone = this.actorService.checkPhone(chapter.getPhone());
			chapter.setPhone(phone);

			result = this.chapterRepository.save(chapter);
		} else {
			String pass = chapter.getUserAccount().getPassword();

			final Md5PasswordEncoder code = new Md5PasswordEncoder();

			pass = code.encodePassword(pass, null);

			final UserAccount userAccount = chapter.getUserAccount();
			userAccount.setPassword(pass);

			chapter.setUserAccount(userAccount);

			this.actorService.checkEmail(chapter.getEmail(), false);
			this.actorService.checkPhone(chapter.getPhone());

			final String phone = this.actorService.checkPhone(chapter.getPhone());
			chapter.setPhone(phone);

			result = this.chapterRepository.save(chapter);

			Box inBox, outBox, trashBox, spamBox, notificationBox;
			inBox = this.boxService.create();
			outBox = this.boxService.create();
			trashBox = this.boxService.create();
			spamBox = this.boxService.create();
			notificationBox = this.boxService.create();

			inBox.setName("in box");
			outBox.setName("out box");
			trashBox.setName("trash box");
			spamBox.setName("spam box");
			notificationBox.setName("notification box");

			inBox.setByDefault(true);
			outBox.setByDefault(true);
			trashBox.setByDefault(true);
			spamBox.setByDefault(true);
			notificationBox.setByDefault(true);

			inBox.setActor(result);
			outBox.setActor(result);
			trashBox.setActor(result);
			spamBox.setActor(result);
			notificationBox.setActor(result);

			final Collection<Box> boxes = new ArrayList<>();
			boxes.add(spamBox);
			boxes.add(trashBox);
			boxes.add(inBox);
			boxes.add(outBox);
			boxes.add(notificationBox);

			inBox = this.boxService.saveNewActor(inBox);
			outBox = this.boxService.saveNewActor(outBox);
			trashBox = this.boxService.saveNewActor(trashBox);
			spamBox = this.boxService.saveNewActor(spamBox);
			notificationBox = this.boxService.saveNewActor(notificationBox);

		}

		return result;

	}
	// Other business methods 

	public Chapter findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		final Chapter chapter = this.chapterRepository.findByUserAccountId(userAccount.getId());

		return chapter;
	}

	public Chapter findByPrincipal() {
		Chapter chapter;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		chapter = this.findByUserAccount(userAccount);
		Assert.notNull(chapter);

		return chapter;
	}

	public Chapter reconstruct(final RegisterChapterForm form, final BindingResult binding) {

		this.validator.validate(form, binding);

		final Chapter chapter = this.create();

		chapter.setTitle(form.getTitle());
		chapter.setName(form.getName());
		chapter.setMiddleName(form.getMiddleName());
		chapter.setSurname(form.getSurname());
		chapter.setPhoto(form.getPhoto());
		chapter.setEmail(form.getEmail());
		chapter.setPhone(form.getPhone());
		chapter.setAddress(form.getAddress());
		chapter.getUserAccount().setUsername(form.getUsername());
		chapter.getUserAccount().setPassword(form.getPassword());

		return chapter;

	}

	public Chapter reconstruct(final Chapter chapter, final BindingResult binding) {

		final Chapter result;

		final Chapter chapterBBDD = this.findOne(chapter.getId());

		chapter.setUserAccount(chapterBBDD.getUserAccount());
		chapter.setScore(chapterBBDD.getScore());
		chapter.setSpammer(chapterBBDD.getSpammer());
		chapter.setArea(chapterBBDD.getArea());

		this.validator.validate(chapter, binding);

		result = chapter;

		return result;

	}

	public void flush() {
		this.chapterRepository.flush();
	}

	public void saveAndFlush(final Chapter chapter) {
		this.chapterRepository.saveAndFlush(chapter);
	}

	public Chapter findChapterByAreaId(final int areaId) {
		final Chapter chapter = this.chapterRepository.findChapterByAreaId(areaId);

		return chapter;
	}

}
