
package controllers.anonymous;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationService;
import services.ParadeService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Parade;
import domain.Sponsorship;

@Controller
@RequestMapping("/parade")
public class ParadeController extends AbstractController {

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private SponsorshipService		sponsorshipService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		final Brotherhood notFound = this.brotherhoodService.findOne(brotherhoodId);

		if (notFound == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			final Collection<Parade> parades;

			parades = this.paradeService.findParadeCanBeSeenOfBrotherhoodId(brotherhoodId);
			final int finderResult = this.configurationService.findConfiguration().getFinderResult();

			result = new ModelAndView("parade/listAnonimo");
			result.addObject("parades", parades);
			result.addObject("requestURI", "parade/list.do");
			result.addObject("pagesize", finderResult);
			result.addObject("AreInFinder", false);
			result.addObject("banner", banner);
			result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
			result.addObject("autoridad", "");
		}
		return result;

	}

	//Display------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int paradeId) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();
		final Parade paradeFound = this.paradeService.findOne(paradeId);

		if (paradeFound == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			result = new ModelAndView("parade/display");
			result.addObject("parade", paradeFound);
			result.addObject("banner", banner);

			try {
				final Sponsorship s = this.sponsorshipService.ramdomSponsorship(paradeId);

				if (s != null) {
					result.addObject("find", true);
					result.addObject("bannerSponsorship", s.getBanner());
				}

				else
					result.addObject("find", false);
			} catch (final Throwable oops) {
				result.addObject("find", false);
			}

		}

		return result;
	}
}
