
package controllers.brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.FloatService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Actor;
import domain.Brotherhood;
import domain.Float;
import domain.Parade;

@Controller
@RequestMapping("/float/brotherhood")
public class FloatBrotherhoodController extends AbstractController {

	//Services -----------------------------------------------------------
	@Autowired
	private FloatService			floatService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ParadeService			paradeService;


	//List----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Collection<Float> floats;
		int brotherhoodId;

		brotherhoodId = this.actorService.findByPrincipal().getId();

		floats = this.floatService.findFloatsByBrotherhoodId(brotherhoodId);

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("float/list");
		result.addObject("floats", floats);
		result.addObject("requestURI", "float/brotherhood/list.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);

		return result;

	}

	@RequestMapping(value = "/listByParade", method = RequestMethod.GET)
	public ModelAndView listByParade(@RequestParam final int paradeId) {

		final ModelAndView result;
		final Collection<Float> floats;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Parade parade = this.paradeService.findOne(paradeId);
		if (parade == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			final Boolean security = this.paradeService.paradeBrotherhoodSecurity(paradeId);
			if (security) {
				floats = parade.getFloats();

				result = new ModelAndView("float/list");
				result.addObject("floats", floats);
				result.addObject("requestURI", "float/brotherhood/listByParade.do");
				result.addObject("pagesize", 5);
				result.addObject("banner", banner);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");

		}
		return result;

	}
	//Display------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int floatId) {
		final ModelAndView result;
		final Float floatt;
		final Brotherhood login;
		final Brotherhood owner;

		final String banner = this.configurationService.findConfiguration().getBanner();
		final Float floatNotFound = this.floatService.findOne(floatId);

		if (floatNotFound == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			login = this.brotherhoodService.findByPrincipal();
			owner = this.floatService.findOne(floatId).getBrotherhood();

			if (login.getId() == owner.getId()) {
				floatt = this.floatService.findOne(floatId);

				result = new ModelAndView("float/display");
				result.addObject("floatt", floatt);
				result.addObject("banner", banner);

			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	//Add Float to Parade------------------------------------------------------------
	@RequestMapping(value = "/floatAddParade", method = RequestMethod.GET)
	public ModelAndView addFloatToParadeList(@RequestParam final int paradeId) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();
		final Parade parade = this.paradeService.findOne(paradeId);

		if (parade == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			final Boolean security = this.paradeService.paradeBrotherhoodSecurity(paradeId);
			if (security) {
				final Collection<Float> floatsResult = this.floatService.findFloatsByBrotherhoodId(parade.getBrotherhood().getId());
				floatsResult.removeAll(parade.getFloats());

				result = new ModelAndView("float/listAdd");
				result.addObject("floats", floatsResult);
				result.addObject("requestURI", "float/brotherhood/floatAddParade.do");
				result.addObject("pagesize", 5);
				result.addObject("banner", banner);
				result.addObject("paradeId", paradeId);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;

	}

	@RequestMapping(value = "/floatAddParadePost", method = RequestMethod.GET)
	public ModelAndView addFloatToParadeList(@RequestParam final int paradeId, @RequestParam final int floatId) {
		ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);
		final Float floatt = this.floatService.findOne(floatId);
		final String banner = this.configurationService.findConfiguration().getBanner();
		Boolean security1;
		Boolean security2;

		if (parade == null || floatt == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			security1 = this.floatService.floatBrotherhoodSecurity(floatId);
			security2 = this.paradeService.paradeBrotherhoodSecurity(paradeId);

			if (security1 && security2)
				try {
					this.floatService.addFloatToParade(floatt, parade);

					final Collection<Float> floatsResult = this.floatService.findFloatsByBrotherhoodId(parade.getBrotherhood().getId());
					final Parade paradeNew = this.paradeService.findOne(paradeId);
					floatsResult.removeAll(paradeNew.getFloats());

					result = new ModelAndView("float/listAdd");
					result.addObject("banner", banner);
					result.addObject("floats", floatsResult);
					result.addObject("paradeId", paradeId);

				} catch (final Throwable oops) {
					final Collection<Float> floatsResult = this.floatService.findFloatsByBrotherhoodId(parade.getBrotherhood().getId());
					final Parade paradeNew = this.paradeService.findOne(paradeId);
					floatsResult.removeAll(paradeNew.getFloats());

					result = new ModelAndView("float/listAdd");
					result.addObject("banner", banner);
					result.addObject("messageError", "float.commit.error");
					result.addObject("floats", floatsResult);
					result.addObject("paradeId", paradeId);

				}
			else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}
	//Create------------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;

		final Float floatt = this.floatService.create();
		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("float/edit");
		result.addObject("floatt", floatt);
		result.addObject("banner", banner);

		return result;

	}
	//Edit--------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int floatId) {
		ModelAndView result;
		Float floatt;
		Boolean security;

		final String banner = this.configurationService.findConfiguration().getBanner();

		floatt = this.floatService.findOne(floatId);

		if (floatt == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			security = this.floatService.floatBrotherhoodSecurity(floatId);

			if (security)
				result = this.createEditModelAndView(floatt, null);
			else
				result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("banner", banner);
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "floatt") Float floatt, final BindingResult binding) {
		ModelAndView result;

		floatt = this.floatService.reconstruct(floatt, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(floatt, null);
		else
			try {
				this.floatService.save(floatt);
				result = new ModelAndView("redirect:/float/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(floatt, "float.commit.error");

			}

		return result;
	}

	//Delete--------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Float floatt) {
		ModelAndView result;

		final Actor actor = this.actorService.findByPrincipal();
		floatt = this.floatService.findOne(floatt.getId());

		if (floatt.getBrotherhood().getId() == actor.getId())
			try {
				Assert.isTrue(actor.getId() == floatt.getBrotherhood().getId());
				this.floatService.delete(floatt);
				result = new ModelAndView("redirect:/float/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(floatt, "float.commit.error");
			}
		else
			result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}
	//Other business methods---------------------------------------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Float floatt, final String messageCode) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Collection<String> pictures = floatt.getPictures();

		result = new ModelAndView("float/edit");
		result.addObject("floatt", floatt);
		result.addObject("pictures", pictures);
		result.addObject("messageError", messageCode);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;

	}

}
