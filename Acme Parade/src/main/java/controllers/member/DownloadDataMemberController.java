
package controllers.member;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import services.BoxService;
import services.EnrolmentService;
import services.FinderService;
import services.MemberService;
import services.MessageService;
import services.RequestService;
import services.SocialProfileService;
import domain.Box;
import domain.Enrolment;
import domain.Finder;
import domain.Member;
import domain.Message;
import domain.Request;
import domain.SocialProfile;

@Controller
@RequestMapping("/data/member")
public class DownloadDataMemberController {

	@Autowired
	private MemberService			memberService;
	@Autowired
	private SocialProfileService	socialProfileService;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private EnrolmentService		enrolmentService;
	@Autowired
	private RequestService			requestService;
	@Autowired
	private BoxService				boxService;
	@Autowired
	private FinderService			finderService;


	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public void test(final HttpSession session, final HttpServletResponse response) throws IOException {

		final String language = LocaleContextHolder.getLocale().getLanguage();

		if (language == "en") {
			String myString = "Below these lines you can find all the data we have at Acme-Parade:\r\n";

			final Member m = this.memberService.findByPrincipal();
			final Collection<SocialProfile> sc = this.socialProfileService.findAllByActor(m.getId());
			final Collection<Message> msgs = this.messageService.messagePerActor(m.getId());
			final Collection<Box> bxs = this.boxService.findAllBoxByActor(m.getId());
			final Collection<Enrolment> ens = this.enrolmentService.findEnrolmentsByMemberId(m.getId());
			final Collection<Request> rqs = this.requestService.findRequestsByMemberId(m.getId());
			final Finder f = this.finderService.findFinderByMember(m);

			myString += "\r\n\r\n";

			myString += m.getName() + " " + m.getMiddleName() + " " + m.getSurname() + " " + m.getAddress() + " " + m.getEmail() + " " + m.getPhone() + " " + m.getPhoto() + " \r\n";
			myString += "\r\n\r\n";
			myString += "Social Profiles:\r\n";
			for (final SocialProfile s : sc)
				myString += s.getNick() + " " + s.getLink() + " " + s.getSocialName() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Boxes:\r\n";
			for (final Box b : bxs)
				myString += "Box name: " + b.getName() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Messages:\r\n\r\n";
			for (final Message msg : msgs)
				myString += "Sender: " + msg.getSender().getName() + " " + msg.getSender().getSurname() + " Recipient: " + msg.getRecipient().getName() + " " + msg.getRecipient().getSurname() + " Moment: " + msg.getMoment() + " Subject: "
					+ msg.getSubject() + " Body: " + msg.getBody() + " Tags: " + msg.getTags() + " Priority: " + msg.getPriority() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Enrolments:\r\n\r\n";
			for (final Enrolment e : ens)
				myString += "Brotherhood name: " + e.getBrotherhood().getName() + " Moment: " + e.getMoment() + " DropOut: " + e.getDropOutMoment() + " Position: " + e.getPosition().getEnglishName() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Requests:\r\n\r\n";
			for (final Request r : rqs)
				myString += "Parade title: " + r.getParade().getTitle() + " Status: " + r.getStatus() + " Column Number: " + r.getColumnNumber() + " Row Number: " + r.getRowNumber() + " Comment: " + r.getComment() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Finder:\r\n\r\n";
			myString += "Last Updated: " + f.getLastUpdate() + " Keyword: " + f.getKeyWord() + " Area: " + f.getArea();

			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename=my_data_as_member.txt");
			final ServletOutputStream out = response.getOutputStream();
			out.println(myString);
			out.flush();
			out.close();

		} else {

			String myString = "Debajo de estas lineas puedes encontrar todos los datos que tenemos de ti en Acme-Parade:\r\n";

			final Member m = this.memberService.findByPrincipal();
			final Collection<SocialProfile> sc = this.socialProfileService.findAllByActor(m.getId());
			final Collection<Message> msgs = this.messageService.messagePerActor(m.getId());
			final Collection<Box> bxs = this.boxService.findAllBoxByActor(m.getId());
			final Collection<Enrolment> ens = this.enrolmentService.findEnrolmentsByMemberId(m.getId());
			final Collection<Request> rqs = this.requestService.findRequestsByMemberId(m.getId());
			final Finder f = this.finderService.findFinderByMember(m);

			myString += "\r\n\r\n";

			myString += m.getName() + " " + m.getMiddleName() + " " + m.getSurname() + " " + m.getAddress() + " " + m.getEmail() + " " + m.getPhone() + " " + m.getPhoto() + " \r\n";
			myString += "\r\n\r\n";
			myString += "Perfiles Sociales:\r\n";
			for (final SocialProfile s : sc)
				myString += s.getNick() + " " + s.getLink() + " " + s.getSocialName() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Cajas:\r\n";
			for (final Box b : bxs)
				myString += "Nombre caja: " + b.getName() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Mensajes:\r\n\r\n";
			for (final Message msg : msgs)
				myString += "Emisor: " + msg.getSender().getName() + " " + msg.getSender().getSurname() + " Receptor: " + msg.getRecipient().getName() + " " + msg.getRecipient().getSurname() + " Momento: " + msg.getMoment() + " Asunto: "
					+ msg.getSubject() + " Body: " + msg.getBody() + " Tags: " + msg.getTags() + " Priority: " + msg.getPriority() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Enrolamiento:\r\n\r\n";
			for (final Enrolment e : ens)
				myString += "Nombre Brotherhood: " + e.getBrotherhood().getName() + " Momento: " + e.getMoment() + " Momento de salida: " + e.getDropOutMoment() + " Posicion: " + e.getPosition().getEnglishName() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Solicitudes:\r\n\r\n";
			for (final Request r : rqs)
				myString += "Titulo desfile: " + r.getParade().getTitle() + " Estado: " + r.getStatus() + " Numero columna: " + r.getColumnNumber() + " Numero fila: " + r.getRowNumber() + " Comentario: " + r.getComment() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Buscador:\r\n\r\n";
			myString += "Ultima actualizacion: " + f.getLastUpdate() + " Palabra clave: " + f.getKeyWord() + " Area: " + f.getArea();

			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename=my_data_as_member.txt");
			final ServletOutputStream out = response.getOutputStream();
			out.println(myString);
			out.flush();
			out.close();

		}
	}
}
