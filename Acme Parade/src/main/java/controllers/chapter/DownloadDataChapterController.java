
package controllers.chapter;

import java.io.IOException;
import java.util.Arrays;
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
import services.ChapterService;
import services.MessageService;
import services.ProclaimService;
import services.SocialProfileService;
import controllers.AbstractController;
import domain.Box;
import domain.Chapter;
import domain.Message;
import domain.Proclaim;
import domain.SocialProfile;

@Controller
@RequestMapping("/data/chapter")
public class DownloadDataChapterController extends AbstractController {

	@Autowired
	private ChapterService			chapterService;
	@Autowired
	private ProclaimService			proclaimService;
	@Autowired
	private SocialProfileService	socialProfileService;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private BoxService				boxService;


	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public void test(final HttpSession session, final HttpServletResponse response) throws IOException {

		final String language = LocaleContextHolder.getLocale().getLanguage();

		if (language == "en") {
			String myString = "Below these lines you can find all the data we have at Acme-Parade:\r\n";

			final Chapter c = this.chapterService.findByPrincipal();
			final Collection<SocialProfile> sc = this.socialProfileService.findAllByActor(c.getId());
			final Collection<Message> msgs = this.messageService.messagePerActor(c.getId());
			final Collection<Box> bxs = this.boxService.findAllBoxByActor(c.getId());
			final Collection<Proclaim> proclaims = this.proclaimService.proclaimsPerChapter(c.getId());

			myString += "\r\n\r\n";

			myString += c.getTitle() + " " + c.getName() + " " + c.getMiddleName() + " " + c.getSurname() + " " + c.getAddress() + " " + c.getEmail() + " " + c.getPhone() + " " + c.getPhoto() + " \r\n";
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
			myString += "Proclaims:\r\n\r\n";
			for (final Proclaim p : proclaims)
				myString += "Moment: " + p.getMoment() + " Description: " + p.getDescription() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Area you coordinate:\r\n\r\n";
			myString += "Name: " + c.getArea().getName() + " Pictures: " + Arrays.toString(c.getArea().getPictures().toArray());
			myString += "\r\n";

			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename=my_data_as_chapter.txt");
			final ServletOutputStream out = response.getOutputStream();
			out.println(myString);
			out.flush();
			out.close();

		} else {
			String myString = "Debajo de estas lineas puedes encontrar todos los datos que tenemos de ti en Acme-Parade:\r\n";

			final Chapter c = this.chapterService.findByPrincipal();
			final Collection<SocialProfile> sc = this.socialProfileService.findAllByActor(c.getId());
			final Collection<Message> msgs = this.messageService.messagePerActor(c.getId());
			final Collection<Box> bxs = this.boxService.findAllBoxByActor(c.getId());
			final Collection<Proclaim> proclaims = this.proclaimService.proclaimsPerChapter(c.getId());

			myString += "\r\n\r\n";

			myString += c.getName() + " " + c.getMiddleName() + " " + c.getSurname() + " " + c.getAddress() + " " + c.getEmail() + " " + c.getPhone() + " " + c.getPhoto() + " \r\n";
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
					+ msg.getSubject() + " Cuerpo: " + msg.getBody() + " Etiquetas: " + msg.getTags() + " Prioridad: " + msg.getPriority() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Anuncios:\r\n\r\n";
			for (final Proclaim p : proclaims)
				myString += "Momento: " + p.getMoment() + " Descripcion: " + p.getDescription() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Area que coordinas:\r\n\r\n";
			myString += "Nombre: " + c.getArea().getName() + " Imagenes: " + Arrays.toString(c.getArea().getPictures().toArray());
			myString += "\r\n";

			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename=my_data_as_chapter.txt");
			final ServletOutputStream out = response.getOutputStream();
			out.println(myString);
			out.flush();
			out.close();
		}
	}
}
