
package controllers.brotherhood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationService;
import services.HistoryService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;

@Controller
@RequestMapping("/history/brotherhood")
public class HistoryBrotherhoodController extends AbstractController {

	// Services ---------------------------------------------------

	@Autowired
	private HistoryService			historyService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ConfigurationService	configurationService;


	// Display ----------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		History history;
		Boolean owner = false;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Brotherhood find = this.brotherhoodService.findOne(brotherhoodId);

		if (this.brotherhoodService.findByPrincipal().getId() == brotherhoodId)
			owner = true;
		if (find == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			history = this.historyService.findByBrotherhoodId(brotherhoodId);
			result = new ModelAndView("history/display");
			result.addObject("owner", owner);
			result.addObject("history", history);
			result.addObject("banner", banner);
			result.addObject("requestURI", "history/display.do");

		}

		return result;

	}

}
