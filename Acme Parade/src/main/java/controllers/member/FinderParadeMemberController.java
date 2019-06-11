
package controllers.member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.FinderService;
import services.MemberService;
import domain.Actor;
import domain.Finder;
import domain.Parade;

@Controller
@RequestMapping("/finderParade/member")
public class FinderParadeMemberController {

	@Autowired
	MemberService					memberService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public ModelAndView showFinder() {
		ModelAndView result;
		final Finder finder;
		Collection<Parade> parades = new ArrayList<Parade>();

		final Actor actor = this.actorService.findByPrincipal();
		finder = this.finderService.findFinderByMember(actor);

		final Date currentTime = new Date(System.currentTimeMillis() - 1000);
		final Interval interval = new Interval(finder.getLastUpdate().getTime(), currentTime.getTime());

		final Integer timeOut = this.configurationService.findConfiguration().getFinderTime();
		final Integer pagesize = this.configurationService.findConfiguration().getFinderResult();

		final String banner = this.configurationService.findConfiguration().getBanner();

		if (interval.toDuration().getStandardHours() < timeOut)
			parades = finder.getParades();

		result = new ModelAndView("parade/listAnonimo");
		result.addObject("parades", parades);
		result.addObject("finder", finder);
		result.addObject("requestURI", "finderParade/member/find.do");
		result.addObject("AreInFinder", true);
		result.addObject("requestAction", "finderParade/member/find.do");
		result.addObject("banner", banner);
		result.addObject("pagesize", pagesize);

		return result;

	}

	@RequestMapping(value = "/find", method = RequestMethod.POST, params = "find")
	public ModelAndView editFinder(Finder finder, final BindingResult binding) {
		ModelAndView result;

		finder = this.finderService.reconstruct(finder, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(finder, null);
		else
			try {
				this.finderService.save(finder);
				result = new ModelAndView("redirect:find.do");
			} catch (final Throwable oops) {
				System.out.println(oops.getMessage());
				result = this.createEditModelAndView(finder, "finder.commit.error");

			}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder) {
		ModelAndView result;

		result = this.createEditModelAndView(finder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("parade/listAnonimo");

		final String banner = this.configurationService.findConfiguration().getBanner();

		result.addObject("finder", finder);
		result.addObject("messageError", messageCode);
		result.addObject("banner", banner);

		return result;
	}

}
