
package controllers.brotherhood;

import java.util.Collection;

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
import services.LegalRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Controller
@RequestMapping("/legalRecord/brotherhood")
public class LegalRecordBrotherhoodController extends AbstractController {

	// Services ---------------------------------------------------

	@Autowired
	private LegalRecordService		legalRecordService;

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
			final LegalRecord legalRecord;
			legalRecord = this.legalRecordService.create();

			result = this.createEditModelAndView(legalRecord);

		} else {
			result = new ModelAndView("misc/notHistory");
			result.addObject("banner", banner);
		}

		return result;
	}
	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalRecordId) {
		ModelAndView result;
		LegalRecord legalRecord;
		Boolean security;

		final LegalRecord find = this.legalRecordService.findOne(legalRecordId);
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (find == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			legalRecord = this.legalRecordService.findOne(legalRecordId);
			security = this.legalRecordService.securityLegal(legalRecordId);

			if (security) {
				Assert.notNull(legalRecord);
				result = this.createEditModelAndView(legalRecord);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");

		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LegalRecord legalRecord, final BindingResult binding) {

		ModelAndView result;

		int id = 0;
		if (legalRecord.getId() != 0) {
			final History history = this.historyService.historyPerLegalRecordId(legalRecord.getId());
			id = history.getBrotherhood().getId();
		} else
			id = this.brotherhoodService.findByPrincipal().getId();

		if (binding.hasErrors())
			result = this.createEditModelAndView(legalRecord);
		else
			try {
				this.legalRecordService.save(legalRecord);

				result = new ModelAndView("redirect:/history/brotherhood/display.do" + "?brotherhoodId=" + id);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "legalRecord.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LegalRecord legalRecord) {
		ModelAndView result;

		final LegalRecord legalRecordFind = this.legalRecordService.findOne(legalRecord.getId());
		final String banner = this.configurationService.findConfiguration().getBanner();

		final History history = this.historyService.historyPerLegalRecordId(legalRecord.getId());

		if (legalRecordFind == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else if (history.getBrotherhood().getId() != this.brotherhoodService.findByPrincipal().getId())
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			final int id = history.getBrotherhood().getId();
			try {
				this.legalRecordService.delete(legalRecordFind);
				result = new ModelAndView("redirect:/history/brotherhood/display.do" + "?brotherhoodId=" + id);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecordFind, "legalRecord.commit.error");
			}
		}
		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(legalRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord, final String message) {

		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("legalRecord/brotherhood/edit");

		int id = 0;
		if (legalRecord.getId() != 0) {
			final History history = this.historyService.historyPerLegalRecordId(legalRecord.getId());
			id = history.getBrotherhood().getId();
		} else
			id = this.brotherhoodService.findByPrincipal().getId();

		result.addObject("id", id);

		final Collection<String> laws = legalRecord.getLaws();

		result.addObject("legalRecord", legalRecord);
		result.addObject("laws", laws);
		result.addObject("messageError", message);
		result.addObject("banner", banner);

		return result;
	}

}
