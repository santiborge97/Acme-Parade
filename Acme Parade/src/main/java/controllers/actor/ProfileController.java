/*
 * ProfileController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.Credentials;
import services.ActorService;
import services.AdministratorService;
import services.BrotherhoodService;
import services.ChapterService;
import services.ConfigurationService;
import services.MemberService;
import services.SponsorService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Sponsor;

@Controller
@RequestMapping("/profile")
public class ProfileController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private SponsorService			sponsorService;


	@RequestMapping(value = "/displayPrincipal", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findOne(this.actorService.findByPrincipal().getId());
		Assert.notNull(actor);

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("actor/display");
		result.addObject("actor", actor);
		result.addObject("banner", banner);
		result.addObject("admin", false);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		final Actor actor;

		final Actor principal = this.actorService.findByPrincipal();
		actor = this.actorService.findOne(principal.getId());
		Assert.isTrue(actor.equals(principal));

		final Authority authority1 = new Authority();
		authority1.setAuthority(Authority.BROTHERHOOD);

		final Authority authority2 = new Authority();
		authority2.setAuthority(Authority.MEMBER);

		final Authority authority3 = new Authority();
		authority3.setAuthority(Authority.ADMIN);

		final Authority authority4 = new Authority();
		authority4.setAuthority(Authority.CHAPTER);

		final Authority authority5 = new Authority();
		authority5.setAuthority(Authority.SPONSOR);

		String auth = null;
		String action = null;
		if (actor.getUserAccount().getAuthorities().contains(authority1)) {
			auth = "brotherhood";
			action = "editBrotherhood.do";

		} else if (actor.getUserAccount().getAuthorities().contains(authority2)) {
			auth = "member";
			action = "editMember.do";
		} else if (actor.getUserAccount().getAuthorities().contains(authority3)) {
			auth = "administrator";
			action = "editAdministrator.do";
		} else if (actor.getUserAccount().getAuthorities().contains(authority4)) {
			auth = "chapter";
			action = "editChapter.do";
		} else if (actor.getUserAccount().getAuthorities().contains(authority5)) {
			auth = "sponsor";
			action = "editSponsor.do";
		}

		final String banner = this.configurationService.findConfiguration().getBanner();
		final String defaultCountry = this.configurationService.findConfiguration().getCountryCode();
		result = new ModelAndView("actor/edit");
		result.addObject("actionURI", action);
		result.addObject(auth, actor);
		result.addObject("authority", auth);
		result.addObject("banner", banner);
		result.addObject("defaultCountry", defaultCountry);

		return result;
	}
	@RequestMapping(value = "/editMember", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMember(@ModelAttribute("member") final Member member, final BindingResult binding) {
		ModelAndView result;

		final Member memberReconstruct = this.memberService.reconstruct(member, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewMember(memberReconstruct);
		else
			try {
				this.memberService.save(memberReconstruct);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(memberReconstruct.getUserAccount().getUsername());
				credentials.setPassword(memberReconstruct.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/profile/displayPrincipal.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewMember(memberReconstruct, "actor.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndViewMember(final Member member) {
		ModelAndView result;

		result = this.createEditModelAndViewMember(member, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewMember(final Member member, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("actor/edit");

		result.addObject("member", member);
		result.addObject("authority", "member");
		result.addObject("actionURI", "editMember.do");
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

	@RequestMapping(value = "/editBrotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBrotherhood(@ModelAttribute("brotherhood") final Brotherhood brotherhood, final BindingResult binding) {
		ModelAndView result;

		final Brotherhood brotherhoodReconstruct = this.brotherhoodService.reconstruct(brotherhood, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewBrotherhood(brotherhoodReconstruct);
		else
			try {
				this.brotherhoodService.save(brotherhoodReconstruct);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(brotherhoodReconstruct.getUserAccount().getUsername());
				credentials.setPassword(brotherhoodReconstruct.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/profile/displayPrincipal.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewBrotherhood(brotherhoodReconstruct, "actor.commit.error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndViewBrotherhood(final Brotherhood brotherhood) {
		ModelAndView result;

		result = this.createEditModelAndViewBrotherhood(brotherhood, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewBrotherhood(final Brotherhood brotherhood, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("actor/edit");

		result.addObject("brotherhood", brotherhood);
		result.addObject("authority", "brotherhood");
		result.addObject("actionURI", "editBrotherhood.do");
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

	@RequestMapping(value = "/editAdministrator", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAdministrator(@ModelAttribute("administrator") final Administrator admin, final BindingResult binding) {
		ModelAndView result;

		final Administrator adminReconstruct = this.administratorService.reconstruct(admin, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewAdmin(adminReconstruct);
		else
			try {
				this.administratorService.save(adminReconstruct);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(adminReconstruct.getUserAccount().getUsername());
				credentials.setPassword(adminReconstruct.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/profile/displayPrincipal.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewAdmin(adminReconstruct, "actor.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndViewAdmin(final Administrator admin) {
		ModelAndView result;

		result = this.createEditModelAndViewAdmin(admin, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewAdmin(final Administrator admin, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("actor/edit");

		result.addObject("administrator", admin);
		result.addObject("authority", "administrator");
		result.addObject("actionURI", "editAdministrator.do");
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

	@RequestMapping(value = "/editSponsor", method = RequestMethod.POST, params = "save")
	public ModelAndView saveSponsor(@ModelAttribute("sponsor") final Sponsor sponsor, final BindingResult binding) {
		ModelAndView result;

		final Sponsor sponsorReconstruct = this.sponsorService.reconstruct(sponsor, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewSponsor(sponsorReconstruct);
		else
			try {
				this.sponsorService.save(sponsorReconstruct);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(sponsorReconstruct.getUserAccount().getUsername());
				credentials.setPassword(sponsorReconstruct.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/profile/displayPrincipal.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewSponsor(sponsorReconstruct, "sponsor.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndViewSponsor(final Sponsor sponsor) {
		ModelAndView result;

		result = this.createEditModelAndViewSponsor(sponsor, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewSponsor(final Sponsor sponsor, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("actor/edit");

		result.addObject("sponsor", sponsor);
		result.addObject("authority", "sponsor");
		result.addObject("actionURI", "editSponsor.do");
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

	@RequestMapping(value = "/editChapter", method = RequestMethod.POST, params = "save")
	public ModelAndView saveChapter(@ModelAttribute("chapter") final Chapter chapter, final BindingResult binding) {
		ModelAndView result;

		final Chapter chapterReconstruct = this.chapterService.reconstruct(chapter, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewChapter(chapterReconstruct);
		else
			try {
				this.chapterService.save(chapterReconstruct);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(chapterReconstruct.getUserAccount().getUsername());
				credentials.setPassword(chapterReconstruct.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/profile/displayPrincipal.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewChapter(chapterReconstruct, "chapter.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndViewChapter(final Chapter chapter) {
		ModelAndView result;

		result = this.createEditModelAndViewChapter(chapter, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewChapter(final Chapter chapter, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("actor/edit");

		result.addObject("chapter", chapter);
		result.addObject("authority", "chapter");
		result.addObject("actionURI", "editChapter.do");
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}
}
