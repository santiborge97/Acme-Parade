
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
import services.PeriodRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Controller
@RequestMapping("/periodRecord/brotherhood")
public class PeriodRecordBrotherhoodController extends AbstractController {

	// Services ---------------------------------------------------

	@Autowired
	private PeriodRecordService		periodRecordService;

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

			final PeriodRecord periodRecord;
			periodRecord = this.periodRecordService.create();

			result = this.createEditModelAndView(periodRecord);

		} else {
			result = new ModelAndView("misc/notHistory");
			result.addObject("banner", banner);
		}
		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId) {
		ModelAndView result;
		PeriodRecord periodRecord;
		Boolean security;

		final PeriodRecord find = this.periodRecordService.findOne(periodRecordId);
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (find == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			periodRecord = this.periodRecordService.findOne(periodRecordId);
			security = this.periodRecordService.securityPeriod(periodRecordId);

			if (security) {
				Assert.notNull(periodRecord);
				result = this.createEditModelAndView(periodRecord);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");

		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PeriodRecord periodRecord, final BindingResult binding) {

		ModelAndView result;

		int id = 0;
		if (periodRecord.getId() != 0) {
			final History history = this.historyService.historyPerPeriodRecordId(periodRecord.getId());
			id = history.getBrotherhood().getId();
		} else
			id = this.brotherhoodService.findByPrincipal().getId();

		if (binding.hasErrors())
			result = this.createEditModelAndView(periodRecord);
		else
			try {
				this.periodRecordService.save(periodRecord);

				result = new ModelAndView("redirect:/history/brotherhood/display.do" + "?brotherhoodId=" + id);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final PeriodRecord periodRecord) {
		ModelAndView result;

		final PeriodRecord periodRecordFind = this.periodRecordService.findOne(periodRecord.getId());
		final String banner = this.configurationService.findConfiguration().getBanner();
		final History history = this.historyService.historyPerPeriodRecordId(periodRecord.getId());

		if (periodRecordFind == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else if (history.getBrotherhood().getId() != this.brotherhoodService.findByPrincipal().getId())
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			final int id = history.getBrotherhood().getId();
			try {
				this.periodRecordService.delete(periodRecordFind);
				result = new ModelAndView("redirect:/history/brotherhood/display.do" + "?brotherhoodId=" + id);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(periodRecordFind, "periodRecord.commit.error");
			}
		}
		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(periodRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord, final String message) {

		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("periodRecord/brotherhood/edit");
		int id = 0;
		if (periodRecord.getId() != 0) {
			final History history = this.historyService.historyPerPeriodRecordId(periodRecord.getId());
			id = history.getBrotherhood().getId();
		} else
			id = this.brotherhoodService.findByPrincipal().getId();

		result.addObject("id", id);
		result.addObject("periodRecord", periodRecord);
		result.addObject("messageError", message);
		result.addObject("banner", banner);

		return result;
	}

}
