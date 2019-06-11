
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
import services.MiscellaneousRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;

@Controller
@RequestMapping("/miscellaneousRecord/brotherhood")
public class MiscellaneousRecordBrotherhoodController extends AbstractController {

	// Services ---------------------------------------------------

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private ConfigurationService		configurationService;

	@Autowired
	private HistoryService				historyService;

	@Autowired
	private BrotherhoodService			brotherhoodService;


	// Creation ---------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = this.historyService.findByBrotherhoodId(brotherhood.getId());
		if (history != null) {
			final MiscellaneousRecord miscellaneousRecord;
			miscellaneousRecord = this.miscellaneousRecordService.create();

			result = this.createEditModelAndView(miscellaneousRecord);
		} else {
			result = new ModelAndView("misc/notHistory");
			result.addObject("banner", banner);
		}

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;
		Boolean security;

		final MiscellaneousRecord find = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (find == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			security = this.miscellaneousRecordService.securityMiscellaneous(miscellaneousRecordId);

			if (security) {
				Assert.notNull(miscellaneousRecord);
				result = this.createEditModelAndView(miscellaneousRecord);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");

		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {

		ModelAndView result;

		int id = 0;
		if (miscellaneousRecord.getId() != 0) {
			final History history = this.historyService.historyPerMiscellaneousRecordId(miscellaneousRecord.getId());
			id = history.getBrotherhood().getId();
		} else
			id = this.brotherhoodService.findByPrincipal().getId();

		if (binding.hasErrors())
			result = this.createEditModelAndView(miscellaneousRecord);
		else
			try {
				this.miscellaneousRecordService.save(miscellaneousRecord);

				result = new ModelAndView("redirect:/history/brotherhood/display.do" + "?brotherhoodId=" + id);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(miscellaneousRecord, "miscellaneousRecord.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MiscellaneousRecord miscellaneousRecord) {
		ModelAndView result;

		final MiscellaneousRecord miscellaneousRecordFind = this.miscellaneousRecordService.findOne(miscellaneousRecord.getId());
		final String banner = this.configurationService.findConfiguration().getBanner();

		final History history = this.historyService.historyPerMiscellaneousRecordId(miscellaneousRecord.getId());

		if (miscellaneousRecordFind == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else if (history.getBrotherhood().getId() != this.brotherhoodService.findByPrincipal().getId())
			result = new ModelAndView("redirect:/welcome/index.do");
		else {

			final int id = history.getBrotherhood().getId();
			try {
				this.miscellaneousRecordService.delete(miscellaneousRecordFind);
				result = new ModelAndView("redirect:/history/brotherhood/display.do" + "?brotherhoodId=" + id);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(miscellaneousRecordFind, "miscellaneousRecord.commit.error");
			}
		}
		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(miscellaneousRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord, final String message) {

		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("miscellaneousRecord/brotherhood/edit");

		int id = 0;
		if (miscellaneousRecord.getId() != 0) {
			final History history = this.historyService.historyPerMiscellaneousRecordId(miscellaneousRecord.getId());
			id = history.getBrotherhood().getId();
		} else
			id = this.brotherhoodService.findByPrincipal().getId();

		result.addObject("id", id);

		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("messageError", message);
		result.addObject("banner", banner);

		return result;
	}

}
