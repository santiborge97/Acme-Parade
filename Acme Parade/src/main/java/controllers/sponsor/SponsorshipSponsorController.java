
package controllers.sponsor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.ParadeService;
import services.SponsorService;
import services.SponsorshipService;
import domain.Parade;
import domain.Sponsor;
import domain.Sponsorship;
import forms.SponsorshipForm;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorshipSponsorController {

	//Services -----------------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private ParadeService			paradeService;


	//List----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Collection<Sponsorship> sponsorships;

		final Sponsor sponsor = this.sponsorService.findByPrincipal();

		sponsorships = this.sponsorshipService.findAllBySponsorId(sponsor.getId());

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("sponsorship/list");
		result.addObject("sponsorships", sponsorships);
		result.addObject("requestURI", "sponsorships/sponsor/list.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);

		return result;

	}

	//Display------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int sponsorshipId) {
		final ModelAndView result;
		final Sponsorship sponsorship;
		final Sponsor login;

		final String banner = this.configurationService.findConfiguration().getBanner();
		final Sponsorship sponsorshipNotFound = this.sponsorshipService.findOne(sponsorshipId);

		if (sponsorshipNotFound == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			login = this.sponsorService.findByPrincipal();

			sponsorship = this.sponsorshipService.findOne(sponsorshipId);

			if (login == sponsorship.getSponsor()) {

				final Collection<String> makes = this.configurationService.findConfiguration().getMakes();

				final Map<String, String> makesMap = new HashMap<>();

				for (final String string : makes)
					makesMap.put(string, string);

				result = new ModelAndView("sponsorship/display");
				result.addObject("makes", makesMap);
				result.addObject("sponsorship", sponsorship);
				result.addObject("banner", banner);

			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	//Create------------------------------------------------------------------
	@RequestMapping(value = "/sponsor", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		final Parade parade = this.paradeService.findOne(paradeId);

		if (parade == null) {

			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);

		} else {

			final Integer sponsorshipId = this.sponsorshipService.findSponsorshipByParadeAndSponsorId(paradeId, this.sponsorService.findByPrincipal().getId());

			if (sponsorshipId != null) {

				final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);

				final SponsorshipForm sponsorshipForm = this.sponsorshipService.editForm(sponsorship);
				result = this.createEditModelAndView(sponsorshipForm, null);

			} else {

				final Collection<String> makes = this.configurationService.findConfiguration().getMakes();

				final Map<String, String> makesMap = new HashMap<>();

				for (final String string : makes)
					makesMap.put(string, string);

				final SponsorshipForm sponsorship = this.sponsorshipService.create(paradeId);

				result = new ModelAndView("sponsorship/edit");
				result.addObject("sponsorship", sponsorship);
				result.addObject("banner", banner);
				result.addObject("makes", makesMap);

			}

		}

		return result;

	}

	//Edit--------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;
		SponsorshipForm sponsorshipForm;
		Boolean security;

		final String banner = this.configurationService.findConfiguration().getBanner();

		sponsorship = this.sponsorshipService.findOne(sponsorshipId);

		if (sponsorship == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			security = this.sponsorshipService.sponsorshipSponsorSecurity(sponsorshipId);

			if (security) {

				sponsorshipForm = this.sponsorshipService.editForm(sponsorship);
				result = this.createEditModelAndView(sponsorshipForm, null);

			} else
				result = new ModelAndView("redirect:/welcome/index.do");

			result.addObject("banner", banner);
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "sponsorship") final SponsorshipForm sponsorshipform, final BindingResult binding) {
		ModelAndView result;

		final Sponsorship sponsorship = this.sponsorshipService.reconstruct(sponsorshipform, binding);
		Boolean security;

		if (sponsorship.getId() == 0)
			security = true;
		else
			security = this.sponsorshipService.sponsorshipSponsorSecurity(sponsorship.getId());

		if (security) {

			if (binding.hasErrors())
				result = this.createEditModelAndView(sponsorshipform, null);
			else
				try {
					this.sponsorshipService.save(sponsorship);
					result = new ModelAndView("redirect:/sponsorship/sponsor/list.do");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(sponsorshipform, "sponsorship.commit.error");

				}

		} else
			result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}

	//deactivate--------------------------------------------------------------------
	@RequestMapping(value = "/deactivate", method = RequestMethod.GET)
	public ModelAndView deActivate(@RequestParam final int sponsorshipId) {

		ModelAndView result;
		Sponsorship sponsorship;
		Boolean security;

		final String banner = this.configurationService.findConfiguration().getBanner();

		sponsorship = this.sponsorshipService.findOne(sponsorshipId);

		if (sponsorship == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			security = this.sponsorshipService.sponsorshipSponsorSecurity(sponsorshipId);

			if (security) {

				this.sponsorshipService.deactivate(sponsorshipId);
				result = new ModelAndView("redirect:/sponsorship/sponsor/list.do");

			} else
				result = new ModelAndView("redirect:/welcome/index.do");

			result.addObject("banner", banner);
		}
		return result;

	}

	//reactivate--------------------------------------------------------------------
	@RequestMapping(value = "/reactivate", method = RequestMethod.GET)
	public ModelAndView reActivate(@RequestParam final int sponsorshipId) {

		ModelAndView result;
		Sponsorship sponsorship;
		Boolean security;

		final String banner = this.configurationService.findConfiguration().getBanner();

		sponsorship = this.sponsorshipService.findOne(sponsorshipId);

		if (sponsorship == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			security = this.sponsorshipService.sponsorshipSponsorSecurity(sponsorshipId);

			if (security) {

				this.sponsorshipService.reactivate(sponsorshipId);
				result = new ModelAndView("redirect:/sponsorship/sponsor/list.do");

			} else
				result = new ModelAndView("redirect:/welcome/index.do");

			result.addObject("banner", banner);
		}
		return result;

	}

	//Other business methods---------------------------------------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorship, final String messageCode) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Collection<String> makes = this.configurationService.findConfiguration().getMakes();

		final Map<String, String> makesMap = new HashMap<>();

		for (final String string : makes)
			makesMap.put(string, string);

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorship);
		result.addObject("messageError", messageCode);
		result.addObject("banner", banner);
		result.addObject("makes", makesMap);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;

	}

}
