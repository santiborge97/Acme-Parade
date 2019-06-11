
package controllers.anonymous;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.BrotherhoodService;
import services.ConfigurationService;
import controllers.AbstractController;
import domain.Area;
import domain.Brotherhood;

@Controller
@RequestMapping("/brotherhood")
public class BrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodService.findAll();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("brotherhood/list");
		result.addObject("brotherhoods", brotherhoods);
		result.addObject("requestURI", "brotherhood/list.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
		result.addObject("autoridad", "");

		return result;

	}

	@RequestMapping(value = "/listByArea", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int areaId) {

		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		final Area area = this.areaService.findOne(areaId);

		if (area == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			final Collection<Brotherhood> brotherhoods;

			brotherhoods = this.brotherhoodService.findBrotherhoodsByAreaId(areaId);

			result = new ModelAndView("brotherhood/list");
			result.addObject("brotherhoods", brotherhoods);
			result.addObject("requestURI", "brotherhood/list.do");
			result.addObject("pagesize", 5);
			result.addObject("banner", banner);
			result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
			result.addObject("autoridad", "");
		}

		return result;

	}
}
