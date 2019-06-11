
package controllers.anonymous;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.ChapterService;
import services.ConfigurationService;
import controllers.AbstractController;
import domain.Area;
import domain.Chapter;

@Controller
@RequestMapping("/area")
public class AreaController extends AbstractController {

	@Autowired
	private AreaService				areaService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int chapterId) {

		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Chapter chapter = this.chapterService.findOne(chapterId);

		if (chapter == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else if (chapter.getArea() != null) {
			final Collection<Area> areas = new ArrayList<>();
			areas.add(chapter.getArea());

			result = new ModelAndView("area/list");
			result.addObject("areas", areas);
			result.addObject("requestURI", "area/list.do");
			result.addObject("pagesize", 5);
			result.addObject("banner", banner);
			result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
			result.addObject("autoridad", "");
		} else {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("banner", banner);
		}

		return result;

	}
}
