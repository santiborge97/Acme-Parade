
package controllers.brotherhood;

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
import services.BrotherhoodService;
import services.FloatService;
import services.MessageService;
import services.ParadeService;
import services.SegmentService;
import services.SocialProfileService;
import domain.Box;
import domain.Brotherhood;
import domain.Float;
import domain.Message;
import domain.Parade;
import domain.Segment;
import domain.SocialProfile;

@Controller
@RequestMapping("/data/brotherhood")
public class DownloadDataBrotherhoodController {

	@Autowired
	private BrotherhoodService		brotherhoodService;
	@Autowired
	private SegmentService			segmentService;
	@Autowired
	private SocialProfileService	socialProfileService;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private BoxService				boxService;
	@Autowired
	private FloatService			floatService;
	@Autowired
	private ParadeService			paradeService;


	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public void test(final HttpSession session, final HttpServletResponse response) throws IOException {
		final String language = LocaleContextHolder.getLocale().getLanguage();

		if (language == "en") {

			String myString = "Below these lines you can find all the data we have at Acme-Parade:\r\n";

			final Brotherhood m = this.brotherhoodService.findByPrincipal();
			final Collection<SocialProfile> sc = this.socialProfileService.findAllByActor(m.getId());
			final Collection<Message> msgs = this.messageService.messagePerActor(m.getId());
			final Collection<Box> bxs = this.boxService.findAllBoxByActor(m.getId());
			final Collection<Float> fs = this.floatService.findFloatsByBrotherhoodId(m.getId());
			final Collection<Parade> ens = this.paradeService.findParadeByBrotherhoodId(m.getId());

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
			myString += "Floats:\r\n\r\n";
			for (final Float f : fs)
				myString += "Title: " + f.getTitle() + " Description: " + f.getDescription() + " Pictures: " + Arrays.toString(f.getPictures().toArray()) + "\r\n";
			myString += "\r\n\r\n";
			myString += "Parades:\r\n";
			for (final Parade r : ens) {
				myString += "Parade title: " + r.getTitle() + " Description: " + r.getDescription() + " Organisation Moment: " + r.getOrganisationMoment() + "\r\n";

				final Collection<Segment> segments = this.segmentService.findByParade(r.getId());
				myString += "Path:\r\n\r\n";
				Integer i = 1;
				for (final Segment s : segments) {
					myString += i.toString() + ". Origin: " + s.getOrigin() + " Destination: " + s.getDestination() + " Time Origin Expected " + s.getTimeOrigin() + " Time Destination Expected " + s.getDestination() + "\r\n";
					i++;
				}
			}
			myString += "\r\n";

			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename=my_data_as_brotherhood.txt");
			final ServletOutputStream out = response.getOutputStream();
			out.println(myString);
			out.flush();
			out.close();

		} else {
			String myString = "Debajo de estas lineas puedes encontrar todos los datos que tenemos de ti en Acme-Parade:\r\n";

			final Brotherhood m = this.brotherhoodService.findByPrincipal();
			final Collection<SocialProfile> sc = this.socialProfileService.findAllByActor(m.getId());
			final Collection<Message> msgs = this.messageService.messagePerActor(m.getId());
			final Collection<Box> bxs = this.boxService.findAllBoxByActor(m.getId());
			final Collection<Float> fs = this.floatService.findFloatsByBrotherhoodId(m.getId());
			final Collection<Parade> ens = this.paradeService.findParadeByBrotherhoodId(m.getId());

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
					+ msg.getSubject() + " Cuerpo: " + msg.getBody() + " Etiquetas: " + msg.getTags() + " Prioridades: " + msg.getPriority() + "\r\n";
			myString += "\r\n\r\n";
			myString += "Pasos:\r\n\r\n";
			for (final Float f : fs)
				myString += "Título: " + f.getTitle() + " Descripción: " + f.getDescription() + " Imágenes: " + Arrays.toString(f.getPictures().toArray()) + "\r\n";
			myString += "\r\n\r\n";
			myString += "Desfiles:\r\n";
			for (final Parade r : ens) {
				myString += "Título desfile: " + r.getTitle() + " Descripción: " + r.getDescription() + " Momento de Organización: " + r.getOrganisationMoment() + "\r\n";

				final Collection<Segment> segments = this.segmentService.findByParade(r.getId());
				myString += "Camino:\r\n\r\n";
				Integer i = 1;
				for (final Segment s : segments) {
					myString += i.toString() + ". Origen: " + s.getOrigin() + " Destino: " + s.getDestination() + " Tiempo de espera al origen " + s.getTimeOrigin() + " Tiempo de espera al destino " + s.getDestination() + "\r\n";
					i++;
				}
			}
			myString += "\r\n";
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename=my_data_as_brotherhood.txt");
			final ServletOutputStream out = response.getOutputStream();
			out.println(myString);
			out.flush();
			out.close();
		}
	}
}
