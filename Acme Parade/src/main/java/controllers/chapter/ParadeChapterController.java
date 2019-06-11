
package controllers.chapter;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChapterService;
import services.ConfigurationService;
import services.MessageService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Chapter;
import domain.Parade;

@Controller
@RequestMapping("/parade/chapter")
public class ParadeChapterController extends AbstractController {

	//Services
	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private MessageService			messageService;


	//Listar parades publicados(finalMode=true) por los brotherhoods del area del chapter
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Chapter chapter = this.chapterService.findByPrincipal();
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (chapter.getArea() == null) {
			result = new ModelAndView("misc/noArea");
			result.addObject("banner", banner);

		} else {

			final Collection<Parade> parades = new ArrayList<>();

			final Collection<Brotherhood> brotherhoods = chapter.getArea().getBrotherhoods();
			//Cuidado que el area puede ser null
			for (final Brotherhood brotherhood : brotherhoods)
				parades.addAll(this.paradeService.findParadeCanBeSeenOfBrotherhoodIdForChapter(brotherhood.getId()));

			result = new ModelAndView("parade/list");
			result.addObject("parades", parades);
			result.addObject("banner", banner);
			result.addObject("requestURI", "parade/chapter/list.do");

		}
		return result;
	}
	// Accept------------------------------------------------------------
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int paradeId) {
		final ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);
		final String banner = this.configurationService.findConfiguration().getBanner();
		Boolean security;

		if (parade == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else if (!parade.getStatus().equals("SUBMITTED")) {
			result = new ModelAndView("misc/error");
			result.addObject("banner", banner);
		} else {
			final Chapter chapter = this.chapterService.findByPrincipal();
			security = chapter.getArea().getBrotherhoods().contains(parade.getBrotherhood());

			if (security) {
				parade.setStatus("ACCEPTED");
				this.paradeService.save(parade);
				this.messageService.NotificationNewParade(parade, parade.getBrotherhood());
				result = new ModelAndView("redirect:/parade/chapter/list.do");

			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;

	}
	// Reject------------------------------------------------------------
	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int paradeId) {
		final ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);

		final String banner = this.configurationService.findConfiguration().getBanner();
		Boolean security;

		if (parade == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else if (!parade.getStatus().equals("SUBMITTED")) {
			result = new ModelAndView("misc/error");
			result.addObject("banner", banner);
		} else {
			final Chapter chapter = this.chapterService.findByPrincipal();
			security = chapter.getArea().getBrotherhoods().contains(parade.getBrotherhood());

			if (security) {
				result = new ModelAndView("parade/reject");
				result.addObject("parade", parade);
				result.addObject("banner", banner);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}
	//Save de Reject --------------------------------------------------------------------
	@RequestMapping(value = "/reject", method = RequestMethod.POST, params = "saveReject")
	public ModelAndView saveReject(@ModelAttribute(value = "parade") Parade parade, final BindingResult binding) {
		ModelAndView result;
		parade = this.paradeService.reconstructReject(parade, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(parade, null, "reject");
		else
			try {
				Assert.isTrue(parade.getRejectedComment() != null);
				parade.setStatus("REJECTED");
				this.paradeService.save(parade);
				result = new ModelAndView("redirect:/parade/chapter/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(parade, "parade.error", "reject");//cambiar ese mensaje de error
			}
		return result;
	}

	//Ancillary methods---------------------------------
	protected ModelAndView createEditModelAndView(final Parade parade, final String messageCode, final String function) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("parade/" + function);
		result.addObject("parade", parade);
		result.addObject("messageError", messageCode);
		result.addObject("banner", banner);

		return result;
	}

}
