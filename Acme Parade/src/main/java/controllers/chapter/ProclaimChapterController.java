
package controllers.chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChapterService;
import services.ConfigurationService;
import services.ProclaimService;
import controllers.AbstractController;
import domain.Chapter;
import domain.Proclaim;

@Controller
@RequestMapping("/proclaim/chapter")
public class ProclaimChapterController extends AbstractController {

	//Services
	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private ProclaimService			proclaimService;


	//Create-----------------------------------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();
		final Chapter c;
		c = this.chapterService.findByPrincipal();

		if (c == null) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("banner", banner);
		} else {
			final Proclaim proclaim = this.proclaimService.create();

			result = new ModelAndView("proclaim/create");
			result.addObject("proclaim", proclaim);
			result.addObject("banner", banner);
		}

		return result;

	}

	//Save solo para crear
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "proclaim") Proclaim proclaim, final BindingResult binding) {
		ModelAndView result;

		proclaim = this.proclaimService.reconstruct(proclaim, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(proclaim, null);
		else
			try {
				this.proclaimService.save(proclaim);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(proclaim, "float.commit.error");

			}
		return result;
	}

	//Other business methods---------------------------------------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Proclaim proclaim, final String messageCode) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("proclaim/create");
		result.addObject("proclaim", proclaim);
		result.addObject("messageError", messageCode);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;

	}
}
