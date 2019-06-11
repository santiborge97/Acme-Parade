
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Credentials;
import services.BrotherhoodService;
import services.ChapterService;
import services.ConfigurationService;
import services.MemberService;
import services.SponsorService;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Sponsor;
import forms.RegisterBrotherhoodForm;
import forms.RegisterChapterForm;
import forms.RegisterMemberForm;
import forms.RegisterSponsorForm;

@Controller
@RequestMapping("/register")
public class RegisterController extends AbstractController {

	// Services

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private SponsorService			sponsorService;


	//Brotherhood

	@RequestMapping(value = "/createBrotherhood", method = RequestMethod.GET)
	public ModelAndView createBrotherhood() {
		final ModelAndView result;
		final RegisterBrotherhoodForm brotherhood = new RegisterBrotherhoodForm();

		result = this.createEditModelAndViewBrotherhood(brotherhood);

		return result;
	}

	@RequestMapping(value = "/editBrotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBrotherhood(@ModelAttribute("brotherhood") final RegisterBrotherhoodForm form, final BindingResult binding) {
		ModelAndView result;
		final Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewBrotherhood(form);
		else
			try {
				Assert.isTrue(form.getCheckbox());
				Assert.isTrue(form.checkPassword());
				this.brotherhoodService.save(brotherhood);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(brotherhood.getUserAccount().getUsername());
				credentials.setPassword(brotherhood.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/security/login.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewBrotherhood(form, "brotherhood.commit.error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndViewBrotherhood(final RegisterBrotherhoodForm brotherhood) {
		ModelAndView result;

		result = this.createEditModelAndViewBrotherhood(brotherhood, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewBrotherhood(final RegisterBrotherhoodForm brotherhood, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("security/signUpBrotherhood");
		result.addObject("brotherhood", brotherhood);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

	// Member
	@RequestMapping(value = "/createMember", method = RequestMethod.GET)
	public ModelAndView createMember() {
		final ModelAndView result;

		final RegisterMemberForm member = new RegisterMemberForm();

		result = this.createEditModelAndViewMember(member);

		return result;
	}

	@RequestMapping(value = "/editMember", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMember(@ModelAttribute("member") final RegisterMemberForm form, final BindingResult binding) {
		ModelAndView result;
		Member member;

		member = this.memberService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewMember(form);
		else
			try {
				Assert.isTrue(form.getCheckbox());
				Assert.isTrue(form.checkPassword());
				this.memberService.save(member);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(member.getUserAccount().getUsername());
				credentials.setPassword(member.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/security/login.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewMember(form, "member.commit.error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndViewMember(final RegisterMemberForm member) {
		ModelAndView result;

		result = this.createEditModelAndViewMember(member, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewMember(final RegisterMemberForm member, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("security/signUpMember");
		result.addObject("member", member);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

	// Chapter
	@RequestMapping(value = "/createChapter", method = RequestMethod.GET)
	public ModelAndView createChapter() {
		final ModelAndView result;

		final RegisterChapterForm chapter = new RegisterChapterForm();

		result = this.createEditModelAndViewChapter(chapter);

		return result;
	}

	@RequestMapping(value = "/editChapter", method = RequestMethod.POST, params = "save")
	public ModelAndView saveChapter(@ModelAttribute("chapter") final RegisterChapterForm form, final BindingResult binding) {
		ModelAndView result;
		Chapter chapter;

		chapter = this.chapterService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewChapter(form);
		else
			try {
				Assert.isTrue(form.getCheckbox());
				Assert.isTrue(form.checkPassword());
				this.chapterService.save(chapter);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(chapter.getUserAccount().getUsername());
				credentials.setPassword(chapter.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/security/login.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewChapter(form, "chapter.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndViewChapter(final RegisterChapterForm chapter) {
		ModelAndView result;

		result = this.createEditModelAndViewChapter(chapter, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewChapter(final RegisterChapterForm chapter, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("security/signUpChapter");
		result.addObject("chapter", chapter);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

	@RequestMapping(value = "/createSponsor", method = RequestMethod.GET)
	public ModelAndView createSponsor() {
		final ModelAndView result;
		final RegisterSponsorForm sponsor = new RegisterSponsorForm();

		result = this.createEditModelAndViewSponsor(sponsor);

		return result;
	}

	@RequestMapping(value = "/editSponsor", method = RequestMethod.POST, params = "save")
	public ModelAndView saveSponsor(@ModelAttribute("sponsor") final RegisterSponsorForm form, final BindingResult binding) {
		ModelAndView result;
		Sponsor sponsor;

		sponsor = this.sponsorService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewSponsor(form);
		else
			try {
				Assert.isTrue(form.getCheckbox());
				Assert.isTrue(form.checkPassword());
				this.sponsorService.save(sponsor);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(sponsor.getUserAccount().getUsername());
				credentials.setPassword(sponsor.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/security/login.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewSponsor(form, "sponsor.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndViewSponsor(final RegisterSponsorForm sponsor) {
		ModelAndView result;

		result = this.createEditModelAndViewSponsor(sponsor, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewSponsor(final RegisterSponsorForm sponsor, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("security/signUpSponsor");
		result.addObject("sponsor", sponsor);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

}
