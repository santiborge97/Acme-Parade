
package controllers.brotherhood;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationService;
import services.HistoryService;
import services.LinkRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@Controller
@RequestMapping("/linkRecord/brotherhood")
public class LinkRecordBrotherhoodController extends AbstractController {

	// Services ---------------------------------------------------

	@Autowired
	private LinkRecordService		linkRecordService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private HistoryService			historyService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	// Creation ---------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = this.historyService.findByBrotherhoodId(brotherhood.getId());
		if (history != null) {

			final LinkRecord linkRecord;
			linkRecord = this.linkRecordService.create();

			result = this.createEditModelAndView(linkRecord);

		} else {
			result = new ModelAndView("misc/notHistory");
			result.addObject("banner", banner);
		}

		return result;
	}
	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId) {
		ModelAndView result;
		LinkRecord linkRecord;
		Boolean security;

		final LinkRecord find = this.linkRecordService.findOne(linkRecordId);
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (find == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			linkRecord = this.linkRecordService.findOne(linkRecordId);
			security = this.linkRecordService.securityLink(linkRecordId);

			if (security) {
				Assert.notNull(linkRecord);
				result = this.createEditModelAndView(linkRecord);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");

		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LinkRecord linkRecord, final BindingResult binding) {

		ModelAndView result;

		int id = 0;
		if (linkRecord.getId() != 0) {
			final History history = this.historyService.historyPerLinkRecordId(linkRecord.getId());
			id = history.getBrotherhood().getId();
		} else
			id = this.brotherhoodService.findByPrincipal().getId();

		if (binding.hasErrors())
			result = this.createEditModelAndView(linkRecord);
		else
			try {
				this.linkRecordService.save(linkRecord);

				result = new ModelAndView("redirect:/history/brotherhood/display.do" + "?brotherhoodId=" + id);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(linkRecord, "linkRecord.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LinkRecord linkRecord) {
		ModelAndView result;

		final LinkRecord linkRecordFind = this.linkRecordService.findOne(linkRecord.getId());
		final String banner = this.configurationService.findConfiguration().getBanner();

		final History history = this.historyService.historyPerLinkRecordId(linkRecord.getId());

		if (linkRecordFind == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else if (history.getBrotherhood().getId() != this.brotherhoodService.findByPrincipal().getId())
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			final int id = history.getBrotherhood().getId();
			try {
				this.linkRecordService.delete(linkRecordFind);
				result = new ModelAndView("redirect:/history/brotherhood/display.do" + "?brotherhoodId=" + id);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(linkRecordFind, "linkRecord.commit.error");
			}
		}
		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(linkRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord, final String message) {

		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("linkRecord/brotherhood/edit");
		int id = 0;
		if (linkRecord.getId() != 0) {
			final History history = this.historyService.historyPerLinkRecordId(linkRecord.getId());
			id = history.getBrotherhood().getId();
		} else
			id = this.brotherhoodService.findByPrincipal().getId();

		result.addObject("id", id);
		result.addObject("linkRecord", linkRecord);
		result.addObject("messageError", message);
		result.addObject("banner", banner);

		return result;
	}

}
