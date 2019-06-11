
package controllers.brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationService;
import services.ParadeService;
import services.SegmentService;
import domain.Brotherhood;
import domain.Parade;
import domain.Segment;
import forms.ContiguousSegmentForm;
import forms.FirstSegmentForm;

@Controller
@RequestMapping("/segment/brotherhood")
public class SegmentBrotherhoodController {

	@Autowired
	private SegmentService			segmentService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/path", method = RequestMethod.GET)
	public ModelAndView path(@RequestParam final int paradeId) {

		ModelAndView result;
		final Brotherhood member = this.brotherhoodService.findByPrincipal();
		final Parade parade = this.paradeService.findOne(paradeId);
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (parade == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else if (parade.getBrotherhood().equals(member)) {
			final Collection<Segment> segments = this.segmentService.findByParade(paradeId);

			result = new ModelAndView("segment/path");
			result.addObject("segments", segments);
			result.addObject("paradeId", paradeId);
			result.addObject("banner", banner);
		} else
			result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		final ModelAndView result;

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final Boolean exist = this.paradeService.exist(paradeId);

		final String banner = this.configurationService.findConfiguration().getBanner();

		if (exist) {

			if (brotherhood.getArea() == null)
				result = new ModelAndView("misc/noArea");
			else {

				final Boolean security = this.paradeService.paradeBrotherhoodSecurity(paradeId);

				if (security) {
					final Collection<Segment> segments = this.segmentService.segmentsPerParade(paradeId);

					if (segments.isEmpty()) {

						final FirstSegmentForm segment = new FirstSegmentForm();
						segment.setParadeId(paradeId);
						result = this.createEditModelAndView(segment);
					} else {
						final ContiguousSegmentForm segment = new ContiguousSegmentForm();
						segment.setParadeId(paradeId);
						result = this.createEditModelAndView(segment);
					}
				} else
					result = new ModelAndView("redirect:/welcome/index.do");

			}
		} else {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		}

		return result;

	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		final Segment segment = this.segmentService.findOne(paradeId);

		if (segment == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else if (this.paradeService.paradeBrotherhoodSecurity(segment.getParade().getId())) {

			result = new ModelAndView("segment/display");
			result.addObject("segment", segment);
			result.addObject("banner", banner);
		} else
			result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int segmentId) {
		ModelAndView result;
		Segment segment;
		Boolean security;
		final Boolean exist;

		final Segment contiguousAfter = this.segmentService.segmentContiguous(segmentId);
		Boolean delete = false;
		if (contiguousAfter == null)
			delete = true;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Brotherhood b;
		b = this.brotherhoodService.findByPrincipal();

		if (b.getArea() == null)
			result = new ModelAndView("misc/noArea");
		else {
			exist = this.segmentService.exist(segmentId);
			if (exist) {
				segment = this.segmentService.findOne(segmentId);
				security = this.paradeService.paradeBrotherhoodSecurity(segment.getParade().getId());

				if (security) {
					result = this.createEditModelAndView(segment, null);
					result.addObject("delete", delete);
				} else
					result = new ModelAndView("redirect:/welcome/index.do");
			} else {
				result = new ModelAndView("misc/notExist");
				result.addObject("banner", banner);
			}
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveComplete")
	public ModelAndView saveComplete(@ModelAttribute(value = "segment") final FirstSegmentForm form, final BindingResult binding) {
		ModelAndView result;

		Boolean security = false;

		final Segment segmentReconstruct = this.segmentService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(form);
		else {
			security = this.paradeService.paradeBrotherhoodSecurity(form.getParadeId());
			if (security)
				try {
					this.segmentService.save(segmentReconstruct);
					result = new ModelAndView("redirect:/segment/brotherhood/path.do?paradeId=" + form.getParadeId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(form, "segment.commit.error");
				}
			else
				result = new ModelAndView("redirect:/welcome/index.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveParcial")
	public ModelAndView saveParcial(@ModelAttribute(value = "segment") final ContiguousSegmentForm form, final BindingResult binding) {
		ModelAndView result;

		Boolean security = false;

		final Segment segmentReconstruct = this.segmentService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(form);
		else {

			security = this.paradeService.paradeBrotherhoodSecurity(form.getParadeId());

			if (security)
				try {
					this.segmentService.save(segmentReconstruct);
					result = new ModelAndView("redirect:/segment/brotherhood/path.do?paradeId=" + form.getParadeId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(form, "segment.commit.error");
				}
			else
				result = new ModelAndView("redirect:/welcome/index.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveEdit")
	public ModelAndView saveEdit(@ModelAttribute(value = "segment") final Segment form, final BindingResult binding) {
		ModelAndView result;

		Boolean security = false;

		final Segment segmentReconstruct = this.segmentService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(form);
		else {

			security = this.paradeService.paradeBrotherhoodSecurity(form.getParade().getId());

			if (security)
				try {
					this.segmentService.save(segmentReconstruct);
					result = new ModelAndView("redirect:/segment/brotherhood/path.do?paradeId=" + segmentReconstruct.getParade().getId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(form, "segment.commit.error");
				}
			else
				result = new ModelAndView("redirect:/welcome/index.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute(value = "segment") final Segment form, final BindingResult binding) {
		ModelAndView result;

		Boolean security = false;

		final Segment segmentReconstruct = this.segmentService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(form);
		else {

			security = this.paradeService.paradeBrotherhoodSecurity(form.getParade().getId());

			if (security)
				try {
					this.segmentService.delete(segmentReconstruct);
					result = new ModelAndView("redirect:/segment/brotherhood/path.do?paradeId=" + segmentReconstruct.getParade().getId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(form, "segment.commit.error");
				}
			else
				result = new ModelAndView("redirect:/welcome/index.do");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final FirstSegmentForm segment) {
		ModelAndView result;

		result = this.createEditModelAndView(segment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final FirstSegmentForm segment, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Segment contiguousAfter = this.segmentService.segmentContiguous(segment.getId());
		Boolean delete = false;
		if (contiguousAfter == null)
			delete = true;

		final Parade parade = this.paradeService.findOne(segment.getParadeId());
		result = new ModelAndView("segment/edit");
		result.addObject("segment", segment);
		result.addObject("banner", banner);
		result.addObject("complete", true);
		result.addObject("edit", false);
		result.addObject("name", "saveComplete");
		result.addObject("delete", delete);
		result.addObject("moment", parade.getOrganisationMoment());
		result.addObject("messageError", messageCode);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ContiguousSegmentForm segment) {
		ModelAndView result;

		result = this.createEditModelAndView(segment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ContiguousSegmentForm segment, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Segment contiguousAfter = this.segmentService.segmentContiguous(segment.getId());
		Boolean delete = false;
		if (contiguousAfter == null)
			delete = true;

		final Parade parade = this.paradeService.findOne(segment.getParadeId());

		result = new ModelAndView("segment/edit");
		result.addObject("segment", segment);
		result.addObject("banner", banner);
		result.addObject("complete", false);
		result.addObject("edit", false);
		result.addObject("name", "saveParcial");
		result.addObject("delete", delete);
		result.addObject("moment", parade.getOrganisationMoment());
		result.addObject("messageError", messageCode);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Segment segment) {
		ModelAndView result;

		result = this.createEditModelAndView(segment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Segment segment, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Segment contiguousAfter = this.segmentService.segmentContiguous(segment.getId());
		Boolean delete = false;
		if (contiguousAfter == null)
			delete = true;

		result = new ModelAndView("segment/edit");
		result.addObject("segment", segment);
		result.addObject("banner", banner);
		result.addObject("complete", true);
		result.addObject("edit", true);
		result.addObject("name", "saveEdit");
		result.addObject("delete", delete);
		result.addObject("moment", segment.getParade().getOrganisationMoment());
		result.addObject("messageError", messageCode);

		return result;
	}
}
