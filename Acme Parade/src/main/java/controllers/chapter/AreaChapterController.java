
package controllers.chapter;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
@RequestMapping("/area/chapter")
public class AreaChapterController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private AreaService				areaService;


	@RequestMapping(value = "/listAreas", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Area> areas;
		final Chapter chapter = this.chapterService.findByPrincipal();

		if (chapter.getArea() == null)
			areas = this.areaService.areasNotCoordinatedAnyChapters();
		else {
			areas = new ArrayList<>();
			areas.add(chapter.getArea());
		}

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("area/list");
		result.addObject("areas", areas);
		result.addObject("banner", banner);
		result.addObject("requestURI", "area/chapter/listAreas.do");

		return result;
	}

	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public ModelAndView select(@RequestParam final int areaId) {
		ModelAndView result;
		final Area area;

		final Chapter chapter = this.chapterService.findByPrincipal();

		area = this.areaService.findOne(areaId);
		Assert.notNull(area);

		final Chapter chapterOwner = this.chapterService.findChapterByAreaId(areaId);

		if (chapter.getArea() != null)
			result = new ModelAndView("redirect:listAreas.do");
		else if (chapterOwner != null)
			result = new ModelAndView("misc/error");
		else
			try {
				chapter.setArea(area);
				this.chapterService.save(chapter);
				result = new ModelAndView("redirect:listAreas.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("misc/error");
				final String banner = this.configurationService.findConfiguration().getBanner();
				result.addObject("banner", banner);
			}
		return result;

	}
}
