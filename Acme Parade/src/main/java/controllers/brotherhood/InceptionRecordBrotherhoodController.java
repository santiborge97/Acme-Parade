
package controllers.brotherhood;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationService;
import services.HistoryService;
import services.InceptionRecordService;
import controllers.AbstractController;
import domain.History;
import domain.InceptionRecord;

@Controller
@RequestMapping("/inceptionRecord/brotherhood")
public class InceptionRecordBrotherhoodController extends AbstractController {

	// Services ---------------------------------------------------

	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@Autowired
	private HistoryService			historyService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	// Create -------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		InceptionRecord inceptionRecord;

		final String banner = this.configurationService.findConfiguration().getBanner();

		inceptionRecord = this.inceptionRecordService.create();
		result = this.create2ModelAndView(inceptionRecord);
		result.addObject("banner", banner);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int inceptionRecordId) {
		ModelAndView result;
		InceptionRecord inceptionRecord;
		Boolean security;

		final InceptionRecord find = this.inceptionRecordService.findOne(inceptionRecordId);
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (find == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordId);
			security = this.inceptionRecordService.securityInception(inceptionRecordId);
			if (security)
				result = this.create2ModelAndView(inceptionRecord);
			else
				result = new ModelAndView("redirect:/welcome/index.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save1")
	public ModelAndView save1(@Valid final InceptionRecord inceptionRecord, final BindingResult binding) {

		ModelAndView result;

		int id = 0;
		if (inceptionRecord.getId() != 0) {
			final History history = this.historyService.historyPerInceptionRecordId(inceptionRecord.getId());
			id = history.getBrotherhood().getId();
		} else
			id = this.brotherhoodService.findByPrincipal().getId();

		if (binding.hasErrors())
			result = this.create2ModelAndView(inceptionRecord);
		else
			try {
				final InceptionRecord ir = this.inceptionRecordService.save(inceptionRecord);
				final History history;
				history = this.historyService.create(ir);
				this.historyService.save(history);
				result = new ModelAndView("redirect:/history/brotherhood/display.do" + "?brotherhoodId=" + id);
			} catch (final Throwable oops) {
				result = this.create2ModelAndView(inceptionRecord, "inceptionRecord.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save2")
	public ModelAndView save2(@Valid final InceptionRecord inceptionRecord, final BindingResult binding) {

		ModelAndView result;

		int id = 0;
		if (inceptionRecord.getId() != 0) {
			final History history = this.historyService.historyPerInceptionRecordId(inceptionRecord.getId());
			id = history.getBrotherhood().getId();
		} else
			id = this.brotherhoodService.findByPrincipal().getId();

		if (binding.hasErrors())
			result = this.createEditModelAndView(inceptionRecord);
		else
			try {
				this.inceptionRecordService.save(inceptionRecord);
				result = new ModelAndView("redirect:/history/brotherhood/display.do" + "?brotherhoodId=" + id);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(inceptionRecord, "inceptionRecord.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(inceptionRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord, final String message) {

		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("inceptionRecord/brotherhood/edit");

		int id2 = 0;
		if (inceptionRecord.getId() != 0) {
			final History history = this.historyService.historyPerInceptionRecordId(inceptionRecord.getId());
			id2 = history.getBrotherhood().getId();
		} else
			id2 = this.brotherhoodService.findByPrincipal().getId();

		result.addObject("id2", id2);

		result.addObject("inceptionRecord", inceptionRecord);
		result.addObject("messageError", message);
		result.addObject("banner", banner);

		return result;
	}

	protected ModelAndView create2ModelAndView(final InceptionRecord inceptionRecord) {
		ModelAndView result;

		result = this.create2ModelAndView(inceptionRecord, null);

		return result;
	}

	protected ModelAndView create2ModelAndView(final InceptionRecord inceptionRecord, final String message) {

		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("inceptionRecord/brotherhood/edit");

		int id2 = 0;
		if (inceptionRecord.getId() != 0) {
			final History history = this.historyService.historyPerInceptionRecordId(inceptionRecord.getId());
			id2 = history.getBrotherhood().getId();
		} else
			id2 = this.brotherhoodService.findByPrincipal().getId();

		result.addObject("id2", id2);

		result.addObject("inceptionRecord", inceptionRecord);
		result.addObject("messageError", message);
		result.addObject("banner", banner);

		return result;
	}

}
