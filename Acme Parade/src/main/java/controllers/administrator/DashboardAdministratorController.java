
package controllers.administrator;

import java.util.Collection;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AreaService;
import services.BrotherhoodService;
import services.ChapterService;
import services.ConfigurationService;
import services.EnrolmentService;
import services.FinderService;
import services.HistoryService;
import services.MemberService;
import services.ParadeService;
import services.PositionService;
import services.RequestService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Actor;
import domain.Brotherhood;
import domain.Chapter;
import domain.Parade;
import domain.Position;
import forms.ParadeIdForm;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private RequestService			requestService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private EnrolmentService		enrolmentService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private HistoryService			historyService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private SponsorshipService		sponsorshipService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;

		final ParadeIdForm paradeIdForm = new ParadeIdForm();

		result = new ModelAndView("dashboard/display");

		//--------------------------------------------------------------------------------------------------------------

		final String avgMemberPerBrotherhood = this.brotherhoodService.avgMemberPerBrotherhood();
		result.addObject("avgMemberPerBrotherhood", avgMemberPerBrotherhood);
		final String minMemberPerBrotherhood = this.brotherhoodService.minMemberPerBrotherhood();
		result.addObject("minMemberPerBrotherhood", minMemberPerBrotherhood);
		final String maxMemberPerBrotherhood = this.brotherhoodService.maxMemberPerBrotherhood();
		result.addObject("maxMemberPerBrotherhood", maxMemberPerBrotherhood);
		final String stddevMemberPerBrotherhood = this.brotherhoodService.stddevMemberPerBrotherhood();
		result.addObject("stddevMemberPerBrotherhood", stddevMemberPerBrotherhood);

		final String MemberPerBrotherhoodThingsEn = "{label: 'Avg Member Per Brotherhood', backgroundColor: '#d75c11', data: ['" + avgMemberPerBrotherhood + "']}, " + "{label: 'Min Member Per Brotherhood', backgroundColor: '#6a3b0b', data: ['"
			+ minMemberPerBrotherhood + "']}, " + "{label: 'Max Member Per Brotherhood', backgroundColor: '#857cd6', data: ['" + maxMemberPerBrotherhood + "']}, " + "{label: 'Stddev Member Per Brotherhood', backgroundColor: '#ecb752', data: ['"
			+ stddevMemberPerBrotherhood + "']}";

		result.addObject("MemberPerBrotherhoodThingsEn", MemberPerBrotherhoodThingsEn);

		final String MemberPerBrotherhoodThingsEs = "{label: 'Media Miembros Por Hermandad', backgroundColor: '#d75c11', data: ['" + avgMemberPerBrotherhood + "']}, " + "{label: 'Min Miembros Por Hermandad', backgroundColor: '#6a3b0b', data: ['"
			+ minMemberPerBrotherhood + "']}, " + "{label: 'Max Miembros Por Hermandad', backgroundColor: '#857cd6', data: ['" + maxMemberPerBrotherhood + "']}, " + "{label: 'Desv tipica Miembros Por Hermandad', backgroundColor: '#ecb752', data: ['"
			+ stddevMemberPerBrotherhood + "']}";

		result.addObject("MemberPerBrotherhoodThingsEs", MemberPerBrotherhoodThingsEs);

		//--------------------------------------------------------------------------------------------------------------
		final Collection<Brotherhood> theLargestBrotherhoods = this.brotherhoodService.theLargestBrotherhoods();
		if (theLargestBrotherhoods.isEmpty())
			result.addObject("theLargestBrotherhoods", "N/A");
		else
			result.addObject("theLargestBrotherhoods", theLargestBrotherhoods);

		//--------------------------------------------------------------------------------------------------------------
		final Collection<Brotherhood> theSmallestBrotherhoods = this.brotherhoodService.theSmallestBrotherhoods();
		if (theSmallestBrotherhoods.isEmpty())
			result.addObject("theSmallestBrotherhoods", "N/A");
		else
			result.addObject("theSmallestBrotherhoods", theSmallestBrotherhoods);

		//--------------------------------------------------------------------------------------------------------------
		final String approvedRatio = "N/A";
		final String pendingRatio = "N/A";
		final String rejectedRatio = "N/A";
		result.addObject("approvedRatio", approvedRatio);
		result.addObject("pendingRatio", pendingRatio);
		result.addObject("rejectedRatio", rejectedRatio);

		Boolean existParade = true;
		Boolean existChapter = true;

		try {
			final Collection<Parade> parades = this.paradeService.findAll();
			result.addObject("parades", parades);
		} catch (final Throwable oops) {
			existParade = false;
		}

		try {
			this.chapterService.findAll();
		} catch (final Throwable oops) {
			existChapter = false;
		}

		result.addObject("existParade", existParade);
		result.addObject("existChapter", existChapter);

		result.addObject("paradeIdForm", paradeIdForm);

		//--------------------------------------------------------------------------------------------------------------
		try {
			final Collection<String> findParadesLessThirtyDays = this.paradeService.findParadesLessThirtyDays();
			if (findParadesLessThirtyDays.isEmpty())
				result.addObject("findParadesLessThirtyDays", "N/A");
			else
				result.addObject("findParadesLessThirtyDays", findParadesLessThirtyDays);
		} catch (final Throwable oops) {
			result.addObject("findParadesLessThirtyDays", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------

		try {
			final Double avgParadesCoordinatedByChapters = this.paradeService.avgParadesCoordinatedByChapters();
			final Integer minParadesCoordinatedByChapters = this.paradeService.minParadesCoordinatedByChapters();
			final Integer maxParadesCoordinatedByChapters = this.paradeService.maxParadesCoordinatedByChapters();
			final Double stddevParadesCoordinatedByChapters = this.paradeService.stddevParadesCoordinatedByChapters();
			final Collection<Chapter> chaptersCoordinatesMoreThan10Percent = this.paradeService.chaptersCoordinatesMoreThan10Percent();

			result.addObject("avgParadesCoordinatedByChapters", avgParadesCoordinatedByChapters);
			result.addObject("minParadesCoordinatedByChapters", minParadesCoordinatedByChapters);
			result.addObject("maxParadesCoordinatedByChapters", maxParadesCoordinatedByChapters);
			result.addObject("stddevParadesCoordinatedByChapters", stddevParadesCoordinatedByChapters);
			result.addObject("chaptersCoordinatesMoreThan10Percent", chaptersCoordinatesMoreThan10Percent);
		} catch (final Throwable oops) {
			result.addObject("avgParadesCoordinatedByChapters", "N/A");
			result.addObject("minParadesCoordinatedByChapters", "N/A");
			result.addObject("maxParadesCoordinatedByChapters", "N/A");
			result.addObject("stddevParadesCoordinatedByChapters", "N/A");
			result.addObject("chaptersCoordinatesMoreThan10Percent", "N/A");
		}

		try {
			final Double ratioAreasNotCoordinatedAnyChapters = this.areaService.ratioAreasNotCoordinatedAnyChapters();
			result.addObject("ratioAreasNotCoordinatedAnyChapters", ratioAreasNotCoordinatedAnyChapters);

		} catch (final Throwable oops) {
			result.addObject("ratioAreasNotCoordinatedAnyChapters", "N/A");
		}
		//--------------------------------------------------------------------------------------------------------------
		try {
			final Collection<Double> ratiosRequest = this.requestService.ratiosRequest();
			result.addObject("ratiosRequest", ratiosRequest);
		} catch (final Throwable oops) {
			result.addObject("ratiosRequest", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------
		final Collection<String> membersTenPerCent = this.memberService.membersTenPerCent();
		if (membersTenPerCent.isEmpty())
			result.addObject("membersTenPerCent", "N/A");
		else
			result.addObject("membersTenPerCent", membersTenPerCent);

		//--------------------------------------------------------------------------------------------------------------
		final String ratioBrotherhoodPerArea = this.areaService.ratioBrotherhoodPerArea();
		result.addObject("ratioBrotherhoodPerArea", ratioBrotherhoodPerArea);

		final String countBrotherhoodPerArea = this.areaService.countBrotherhoodPerArea();
		result.addObject("countBrotherhoodPerArea", countBrotherhoodPerArea);

		final String minBrotherhoodPerArea = this.areaService.minBrotherhoodPerArea();
		result.addObject("minBrotherhoodPerArea", minBrotherhoodPerArea);

		final String maxBrotherhoodPerArea = this.areaService.maxBrotherhoodPerArea();
		result.addObject("maxBrotherhoodPerArea", maxBrotherhoodPerArea);

		final String avgBrotherhoodPerArea = this.areaService.avgBrotherhoodPerArea();
		result.addObject("avgBrotherhoodPerArea", avgBrotherhoodPerArea);

		final String stddevBrotherhoodPerArea = this.areaService.stddevBrotherhoodPerArea();
		result.addObject("stddevBrotherhoodPerArea", stddevBrotherhoodPerArea);

		final String BrotherhoodPerAreaThings = "{label: 'Ratio Brotherhood Per Area', backgroundColor: '#f2dc01', data: ['" + ratioBrotherhoodPerArea + "']}, " + "{label: 'Count Area', backgroundColor: '#02da57', data: ['" + countBrotherhoodPerArea
			+ "']}, " + "{label: 'Min Brotherhood Per Area', backgroundColor: '#bf6a4c', data: ['" + minBrotherhoodPerArea + "']}, " + "{label: 'Max Brotherhood Per Area', backgroundColor: '#0d49c9', data: ['" + maxBrotherhoodPerArea + "']}, "
			+ "{label: 'Avg Brotherhood Per Area', backgroundColor: '#e32a05', data: ['" + avgBrotherhoodPerArea + "']}, " + "{label: 'Stddev Brotherhood Per Area', backgroundColor: '#c61ba6', data: ['" + stddevBrotherhoodPerArea + "']}";

		result.addObject("BrotherhoodPerAreaThingsEn", BrotherhoodPerAreaThings);

		final String BrotherhoodPerAreaThingsEs = "{label: 'Ratio Hermandades Por Area', backgroundColor: '#f2dc01', data: ['" + ratioBrotherhoodPerArea + "']}, " + "{label: 'Cantidad de Areas', backgroundColor: '#02da57', data: ['"
			+ countBrotherhoodPerArea + "']}, " + "{label: 'Min Hermandades Por Area', backgroundColor: '#bf6a4c', data: ['" + minBrotherhoodPerArea + "']}, " + "{label: 'Max Hermandades Por Area', backgroundColor: '#0d49c9', data: ['"
			+ maxBrotherhoodPerArea + "']}, " + "{label: 'Media Hermandades Por Area', backgroundColor: '#e32a05', data: ['" + avgBrotherhoodPerArea + "']}, " + "{label: 'Desv tipica Hermandades Por Area', backgroundColor: '#c61ba6', data: ['"
			+ stddevBrotherhoodPerArea + "']}";

		result.addObject("BrotherhoodPerAreaThingsEs", BrotherhoodPerAreaThingsEs);

		//--------------------------------------------------------------------------------------------------------------
		final Double ratioArea = this.areaService.ratioArea();
		try {
			result.addObject("ratioArea", ratioArea);
		} catch (final Throwable oops) {
			result.addObject("ratioArea", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------
		final String minResultPerFinder = this.finderService.minResultPerFinder();
		result.addObject("minResultPerFinder", minResultPerFinder);

		final String maxResultPerFinder = this.finderService.maxResultPerFinder();
		result.addObject("maxResultPerFinder", maxResultPerFinder);

		final String avgResultPerFinder = this.finderService.avgResultPerFinder();
		result.addObject("avgResultPerFinder", avgResultPerFinder);

		final String stddevResultPerFinder = this.finderService.stddevResultPerFinder();
		result.addObject("stddevResultPerFinder", stddevResultPerFinder);

		//--------------------------------------------------------------------------------------------------------------
		try {
			final Double ratioEmptyFinders = this.finderService.ratioEmptyFinders();
			result.addObject("ratioEmptyFinders", ratioEmptyFinders);
		} catch (final Throwable oops) {
			result.addObject("ratioEmptyFinders", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------
		final Collection<Position> positions = this.positionService.findAll();
		String histogramaEn = "";
		String histogramaEs = "";

		final String alphaNumeric = "abcdef1234567890";

		for (final Position position : positions) {
			final StringBuilder salt = new StringBuilder();
			final Random rnd = new Random();
			while (salt.length() < 6) { // length of the random string.
				final int index = (int) (rnd.nextFloat() * alphaNumeric.length());
				salt.append(alphaNumeric.charAt(index));
			}
			final String randomAlphaNumeric = salt.toString();

			histogramaEn = histogramaEn + "{label: '" + position.getEnglishName() + "', backgroundColor: '#" + randomAlphaNumeric + "', data: ['" + this.enrolmentService.findEnrolmentPerPosition(position.getId()).size() + "']}, ";
			histogramaEs = histogramaEs + "{label: '" + position.getSpanishName() + "', backgroundColor: '#" + randomAlphaNumeric + "', data: ['" + this.enrolmentService.findEnrolmentPerPosition(position.getId()).size() + "']}, ";
		}

		result.addObject("histogramaEn", histogramaEn);
		result.addObject("histogramaEs", histogramaEs);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		final Collection<Actor> membersSpammer = this.actorService.actorSpammer();
		final Collection<Actor> membersNoSpammer = this.actorService.findAll();
		membersNoSpammer.removeAll(membersSpammer);

		final String spammers = membersNoSpammer.size() + ", " + membersSpammer.size();

		result.addObject("spammers", spammers);

		final Integer rango1 = this.actorService.actorScore1().size();
		final Integer rango2 = this.actorService.actorScore2().size();
		final Integer rango3 = this.actorService.actorScore3().size();
		final Integer rango4 = this.actorService.actorScore4().size();
		final Integer rango5 = this.actorService.actorScore5().size();
		final Integer rango6 = this.actorService.actorScore6().size();
		final Integer rango7 = this.actorService.actorScore7().size();
		final Integer rango8 = this.actorService.actorScore8().size();

		final String scores = rango1 + ", " + rango2 + ", " + rango3 + ", " + rango4 + ", " + rango5 + ", " + rango6 + ", " + rango7 + ", " + rango8;

		result.addObject("scores", scores);

		//--------------------------------------------------------------------------------------------------------------
		try {
			final Double ratioDraftFinalModeParade = this.paradeService.ratioDraftFinalModeParade();
			result.addObject("ratioDraftFinalModeParade", ratioDraftFinalModeParade);
		} catch (final Throwable oops) {
			result.addObject("ratioDraftFinalModeParade", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------
		try {
			final Double ratioSubmitted = this.paradeService.ratioSubmitted();
			result.addObject("ratioSubmitted", ratioSubmitted);
		} catch (final Throwable oops) {
			result.addObject("ratioSubmitted", "N/A");
		}

		try {
			final Double ratioRejected = this.paradeService.ratioRejected();
			result.addObject("ratioRejected", ratioRejected);
		} catch (final Throwable oops) {
			result.addObject("ratioRejected", "N/A");
		}

		try {
			final Double ratioAccepted = this.paradeService.ratioAccepted();
			result.addObject("ratioAccepted", ratioAccepted);
		} catch (final Throwable oops) {
			result.addObject("ratioAccepted", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------

		final Double avgRecordPerHistory = this.historyService.avgRecordPerHistory();
		result.addObject("avgRecordPerHistory", avgRecordPerHistory);
		final Double minRecordPerHistory = this.historyService.minRecordPerHistory();
		result.addObject("minRecordPerHistory", minRecordPerHistory);
		final Double maxRecordPerHistory = this.historyService.maxRecordPerHistory();
		result.addObject("maxRecordPerHistory", maxRecordPerHistory);
		final Double stddevRecordPerHistory = this.historyService.stddevRecordPerHistory();
		result.addObject("stddevRecordPerHistory", stddevRecordPerHistory);

		final Collection<Brotherhood> largestBrotherhood = this.historyService.largestBrotherhood();
		result.addObject("largestBrotherhood", largestBrotherhood);

		final Collection<Brotherhood> brotherhoodsMoreThanAverage = this.historyService.brotherhoodsMoreThanAverage();
		result.addObject("brotherhoodsMoreThanAverage", brotherhoodsMoreThanAverage);

		final String banner = this.configurationService.findConfiguration().getBanner();
		result.addObject("banner", banner);

		//-------------------------------------------------------------------------------------------------

		final Double averageActiveSponsorshipsPerSponsor = this.sponsorshipService.averageActiveSponsorshipsPerSponsor();
		result.addObject("averageActiveSponsorshipsPerSponsor", averageActiveSponsorshipsPerSponsor);
		final Integer minActiveSponsorshipsPerSponsor = this.sponsorshipService.minActiveSponsorshipsPerSponsor();
		result.addObject("minActiveSponsorshipsPerSponsor", minActiveSponsorshipsPerSponsor);
		final Integer maxActiveSponsorshipsPerSponsor = this.sponsorshipService.maxActiveSponsorshipsPerSponsor();
		result.addObject("maxActiveSponsorshipsPerSponsor", maxActiveSponsorshipsPerSponsor);
		final Double standartDeviationOfActiveSponsorshipsPerSponsor = this.sponsorshipService.standartDeviationOfActiveSponsorshipsPerSponsor();
		result.addObject("standartDeviationOfActiveSponsorshipsPerSponsor", standartDeviationOfActiveSponsorshipsPerSponsor);

		try {
			final Double ratioOfActiveSponsorships = this.sponsorshipService.ratioOfActiveSponsorships();
			result.addObject("ratioOfActiveSponsorships", ratioOfActiveSponsorships);
		} catch (final Throwable oops) {
			result.addObject("ratioOfActiveSponsorships", "N/A");
		}

		final Collection<String> top5SporsorsActivedSponsorships = this.sponsorshipService.top5SporsorsActivedSponsorships();
		result.addObject("top5SporsorsActivedSponsorships", top5SporsorsActivedSponsorships);

		return result;

	}
	@RequestMapping(value = "/calculate", method = RequestMethod.POST, params = "save")
	public ModelAndView displayAfter(@Valid final ParadeIdForm id, final BindingResult binding) {
		ModelAndView result;

		final ParadeIdForm paradeIdForm = new ParadeIdForm();

		result = new ModelAndView("dashboard/display");

		//--------------------------------------------------------------------------------------------------------------
		final String avgMemberPerBrotherhood = this.brotherhoodService.avgMemberPerBrotherhood();
		result.addObject("avgMemberPerBrotherhood", avgMemberPerBrotherhood);
		final String minMemberPerBrotherhood = this.brotherhoodService.minMemberPerBrotherhood();
		result.addObject("minMemberPerBrotherhood", minMemberPerBrotherhood);
		final String maxMemberPerBrotherhood = this.brotherhoodService.maxMemberPerBrotherhood();
		result.addObject("maxMemberPerBrotherhood", maxMemberPerBrotherhood);
		final String stddevMemberPerBrotherhood = this.brotherhoodService.stddevMemberPerBrotherhood();
		result.addObject("stddevMemberPerBrotherhood", stddevMemberPerBrotherhood);

		final String MemberPerBrotherhoodThingsEn = "{label: 'Avg Member Per Brotherhood', backgroundColor: '#d75c11', data: ['" + avgMemberPerBrotherhood + "']}, " + "{label: 'Min Member Per Brotherhood', backgroundColor: '#6a3b0b', data: ['"
			+ minMemberPerBrotherhood + "']}, " + "{label: 'Max Member Per Brotherhood', backgroundColor: '#857cd6', data: ['" + maxMemberPerBrotherhood + "']}, " + "{label: 'Stddev Member Per Brotherhood', backgroundColor: '#ecb752', data: ['"
			+ stddevMemberPerBrotherhood + "']}";

		result.addObject("MemberPerBrotherhoodThingsEn", MemberPerBrotherhoodThingsEn);

		final String MemberPerBrotherhoodThingsEs = "{label: 'Media Miembros Por Hermandad', backgroundColor: '#d75c11', data: ['" + avgMemberPerBrotherhood + "']}, " + "{label: 'Min Miembros Por Hermandad', backgroundColor: '#6a3b0b', data: ['"
			+ minMemberPerBrotherhood + "']}, " + "{label: 'Max Miembros Por Hermandad', backgroundColor: '#857cd6', data: ['" + maxMemberPerBrotherhood + "']}, " + "{label: 'Desv tipica Miembros Por Hermandad', backgroundColor: '#ecb752', data: ['"
			+ stddevMemberPerBrotherhood + "']}";

		result.addObject("MemberPerBrotherhoodThingsEs", MemberPerBrotherhoodThingsEs);

		//--------------------------------------------------------------------------------------------------------------
		final Collection<Brotherhood> theLargestBrotherhoods = this.brotherhoodService.theLargestBrotherhoods();
		if (theLargestBrotherhoods.isEmpty())
			result.addObject("theLargestBrotherhoods", "N/A");
		else
			result.addObject("theLargestBrotherhoods", theLargestBrotherhoods);

		//--------------------------------------------------------------------------------------------------------------
		final Collection<Brotherhood> theSmallestBrotherhoods = this.brotherhoodService.theSmallestBrotherhoods();
		if (theSmallestBrotherhoods.isEmpty())
			result.addObject("theSmallestBrotherhoods", "N/A");
		else
			result.addObject("theSmallestBrotherhoods", theSmallestBrotherhoods);

		//--------------------------------------------------------------------------------------------------------------
		final String approvedRatio = this.requestService.ratioParadeAccepted(id.getId());
		final String pendingRatio = this.requestService.ratioParadePending(id.getId());
		final String rejectedRatio = this.requestService.ratioParadeRejected(id.getId());
		result.addObject("approvedRatio", approvedRatio);
		result.addObject("pendingRatio", pendingRatio);
		result.addObject("rejectedRatio", rejectedRatio);

		Boolean existParade = true;

		try {
			final Collection<Parade> parades = this.paradeService.findAll();
			result.addObject("parades", parades);
		} catch (final Throwable oops) {
			existParade = false;
		}

		result.addObject("existParade", existParade);

		result.addObject("paradeIdForm", paradeIdForm);

		//--------------------------------------------------------------------------------------------------------------
		try {
			final Collection<String> findParadesLessThirtyDays = this.paradeService.findParadesLessThirtyDays();
			if (findParadesLessThirtyDays.isEmpty())
				result.addObject("findParadesLessThirtyDays", "N/A");
			else
				result.addObject("findParadesLessThirtyDays", findParadesLessThirtyDays);
		} catch (final Throwable oops) {
			result.addObject("findParadesLessThirtyDays", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------
		try {
			final Collection<Double> ratiosRequest = this.requestService.ratiosRequest();
			result.addObject("ratiosRequest", ratiosRequest);
		} catch (final Throwable oops) {
			result.addObject("ratiosRequest", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------
		final Collection<String> membersTenPerCent = this.memberService.membersTenPerCent();
		if (membersTenPerCent.isEmpty())
			result.addObject("membersTenPerCent", "N/A");
		else
			result.addObject("membersTenPerCent", membersTenPerCent);

		//--------------------------------------------------------------------------------------------------------------
		final String ratioBrotherhoodPerArea = this.areaService.ratioBrotherhoodPerArea();
		result.addObject("ratioBrotherhoodPerArea", ratioBrotherhoodPerArea);

		final String countBrotherhoodPerArea = this.areaService.countBrotherhoodPerArea();
		result.addObject("countBrotherhoodPerArea", countBrotherhoodPerArea);

		final String minBrotherhoodPerArea = this.areaService.minBrotherhoodPerArea();
		result.addObject("minBrotherhoodPerArea", minBrotherhoodPerArea);

		final String maxBrotherhoodPerArea = this.areaService.maxBrotherhoodPerArea();
		result.addObject("maxBrotherhoodPerArea", maxBrotherhoodPerArea);

		final String avgBrotherhoodPerArea = this.areaService.avgBrotherhoodPerArea();
		result.addObject("avgBrotherhoodPerArea", avgBrotherhoodPerArea);

		final String stddevBrotherhoodPerArea = this.areaService.stddevBrotherhoodPerArea();
		result.addObject("stddevBrotherhoodPerArea", stddevBrotherhoodPerArea);

		final String BrotherhoodPerAreaThings = "{label: 'Ratio Brotherhood Per Area', backgroundColor: '#f2dc01', data: ['" + ratioBrotherhoodPerArea + "']}, " + "{label: 'Count Area', backgroundColor: '#02da57', data: ['" + countBrotherhoodPerArea
			+ "']}, " + "{label: 'Min Brotherhood Per Area', backgroundColor: '#bf6a4c', data: ['" + minBrotherhoodPerArea + "']}, " + "{label: 'Max Brotherhood Per Area', backgroundColor: '#0d49c9', data: ['" + maxBrotherhoodPerArea + "']}, "
			+ "{label: 'Avg Brotherhood Per Area', backgroundColor: '#e32a05', data: ['" + avgBrotherhoodPerArea + "']}, " + "{label: 'Stddev Brotherhood Per Area', backgroundColor: '#c61ba6', data: ['" + stddevBrotherhoodPerArea + "']}";

		result.addObject("BrotherhoodPerAreaThingsEn", BrotherhoodPerAreaThings);

		final String BrotherhoodPerAreaThingsEs = "{label: 'Ratio Hermandades Por Area', backgroundColor: '#f2dc01', data: ['" + ratioBrotherhoodPerArea + "']}, " + "{label: 'Cantidad de Area', backgroundColor: '#02da57', data: ['"
			+ countBrotherhoodPerArea + "']}, " + "{label: 'Min Hermandades Por Area', backgroundColor: '#bf6a4c', data: ['" + minBrotherhoodPerArea + "']}, " + "{label: 'Max Hermandades Por Area', backgroundColor: '#0d49c9', data: ['"
			+ maxBrotherhoodPerArea + "']}, " + "{label: 'Media Hermandades Por Area', backgroundColor: '#e32a05', data: ['" + avgBrotherhoodPerArea + "']}, " + "{label: 'Desv tipica Hermandades Por Area', backgroundColor: '#c61ba6', data: ['"
			+ stddevBrotherhoodPerArea + "']}";

		result.addObject("BrotherhoodPerAreaThingsEs", BrotherhoodPerAreaThingsEs);

		//--------------------------------------------------------------------------------------------------------------
		try {
			final Double ratioArea = this.areaService.ratioArea();
			result.addObject("ratioArea", ratioArea);
		} catch (final Throwable oops) {
			result.addObject("ratioArea", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------
		final String minResultPerFinder = this.finderService.minResultPerFinder();
		result.addObject("minResultPerFinder", minResultPerFinder);

		final String maxResultPerFinder = this.finderService.maxResultPerFinder();
		result.addObject("maxResultPerFinder", maxResultPerFinder);

		final String avgResultPerFinder = this.finderService.avgResultPerFinder();
		result.addObject("avgResultPerFinder", avgResultPerFinder);

		final String stddevResultPerFinder = this.finderService.stddevResultPerFinder();
		result.addObject("stddevResultPerFinder", stddevResultPerFinder);

		//--------------------------------------------------------------------------------------------------------------
		try {
			final Double ratioEmptyFinders = this.finderService.ratioEmptyFinders();
			result.addObject("ratioEmptyFinders", ratioEmptyFinders);
		} catch (final Throwable oops) {
			result.addObject("ratioEmptyFinders", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------
		final Collection<Position> positions = this.positionService.findAll();
		String histogramaEn = "";
		String histogramaEs = "";

		final String alphaNumeric = "abcdef1234567890";

		for (final Position position : positions) {
			final StringBuilder salt = new StringBuilder();
			final Random rnd = new Random();
			while (salt.length() < 6) { // length of the random string.
				final int index = (int) (rnd.nextFloat() * alphaNumeric.length());
				salt.append(alphaNumeric.charAt(index));
			}
			final String randomAlphaNumeric = salt.toString();

			histogramaEn = histogramaEn + "{label: '" + position.getEnglishName() + "', backgroundColor: '#" + randomAlphaNumeric + "', data: ['" + this.enrolmentService.findEnrolmentPerPosition(position.getId()).size() + "']}, ";
			histogramaEs = histogramaEs + "{label: '" + position.getSpanishName() + "', backgroundColor: '#" + randomAlphaNumeric + "', data: ['" + this.enrolmentService.findEnrolmentPerPosition(position.getId()).size() + "']}, ";
		}

		result.addObject("histogramaEn", histogramaEn);
		result.addObject("histogramaEs", histogramaEs);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		final Collection<Actor> membersSpammer = this.actorService.actorSpammer();
		final Collection<Actor> membersNoSpammer = this.actorService.findAll();
		membersNoSpammer.removeAll(membersSpammer);

		final String spammers = membersNoSpammer.size() + ", " + membersSpammer.size();

		result.addObject("spammers", spammers);

		final Integer rango1 = this.actorService.actorScore1().size();
		final Integer rango2 = this.actorService.actorScore2().size();
		final Integer rango3 = this.actorService.actorScore3().size();
		final Integer rango4 = this.actorService.actorScore4().size();
		final Integer rango5 = this.actorService.actorScore5().size();
		final Integer rango6 = this.actorService.actorScore6().size();
		final Integer rango7 = this.actorService.actorScore7().size();
		final Integer rango8 = this.actorService.actorScore8().size();

		final String scores = rango1 + ", " + rango2 + ", " + rango3 + ", " + rango4 + ", " + rango5 + ", " + rango6 + ", " + rango7 + ", " + rango8;

		result.addObject("scores", scores);

		//--------------------------------------------------------------------------------------------------------------
		try {
			final Double ratioDraftFinalModeParade = this.paradeService.ratioDraftFinalModeParade();
			result.addObject("ratioDraftFinalModeParade", ratioDraftFinalModeParade);
		} catch (final Throwable oops) {
			result.addObject("ratioDraftFinalModeParade", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------
		try {
			final Double ratioSubmitted = this.paradeService.ratioSubmitted();
			result.addObject("ratioSubmitted", ratioSubmitted);
		} catch (final Throwable oops) {
			result.addObject("ratioSubmitted", "N/A");
		}

		try {
			final Double ratioRejected = this.paradeService.ratioRejected();
			result.addObject("ratioRejected", ratioRejected);
		} catch (final Throwable oops) {
			result.addObject("ratioRejected", "N/A");
		}

		try {
			final Double ratioAccepted = this.paradeService.ratioAccepted();
			result.addObject("ratioAccepted", ratioAccepted);
		} catch (final Throwable oops) {
			result.addObject("ratioAccepted", "N/A");
		}

		//--------------------------------------------------------------------------------------------------------------

		final Double avgRecordPerHistory = this.historyService.avgRecordPerHistory();
		result.addObject("avgRecordPerHistory", avgRecordPerHistory);
		final Double minRecordPerHistory = this.historyService.minRecordPerHistory();
		result.addObject("minRecordPerHistory", minRecordPerHistory);
		final Double maxRecordPerHistory = this.historyService.maxRecordPerHistory();
		result.addObject("maxRecordPerHistory", maxRecordPerHistory);
		final Double stddevRecordPerHistory = this.historyService.stddevRecordPerHistory();
		result.addObject("stddevRecordPerHistory", stddevRecordPerHistory);

		final Collection<Brotherhood> largestBrotherhood = this.historyService.largestBrotherhood();
		result.addObject("largestBrotherhood", largestBrotherhood);

		final Collection<Brotherhood> brotherhoodsMoreThanAverage = this.historyService.brotherhoodsMoreThanAverage();
		result.addObject("brotherhoodsMoreThanAverage", brotherhoodsMoreThanAverage);
		//-------------------------------------------------------------------------------------------------

		final Double averageActiveSponsorshipsPerSponsor = this.sponsorshipService.averageActiveSponsorshipsPerSponsor();
		result.addObject("averageActiveSponsorshipsPerSponsor", averageActiveSponsorshipsPerSponsor);
		final Integer minActiveSponsorshipsPerSponsor = this.sponsorshipService.minActiveSponsorshipsPerSponsor();
		result.addObject("minActiveSponsorshipsPerSponsor", minActiveSponsorshipsPerSponsor);
		final Integer maxActiveSponsorshipsPerSponsor = this.sponsorshipService.maxActiveSponsorshipsPerSponsor();
		result.addObject("maxActiveSponsorshipsPerSponsor", maxActiveSponsorshipsPerSponsor);
		final Double standartDeviationOfActiveSponsorshipsPerSponsor = this.sponsorshipService.standartDeviationOfActiveSponsorshipsPerSponsor();
		result.addObject("standartDeviationOfActiveSponsorshipsPerSponsor", standartDeviationOfActiveSponsorshipsPerSponsor);

		try {
			final Double ratioOfActiveSponsorships = this.sponsorshipService.ratioOfActiveSponsorships();
			result.addObject("ratioOfActiveSponsorships", ratioOfActiveSponsorships);
		} catch (final Throwable oops) {
			result.addObject("ratioOfActiveSponsorships", "N/A");
		}

		final Collection<String> top5SporsorsActivedSponsorships = this.sponsorshipService.top5SporsorsActivedSponsorships();
		result.addObject("top5SporsorsActivedSponsorships", top5SporsorsActivedSponsorships);

		final String banner = this.configurationService.findConfiguration().getBanner();
		result.addObject("banner", banner);

		return result;

	}
}
