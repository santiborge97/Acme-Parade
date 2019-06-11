
package controllers.sponsor;

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
import services.MessageService;
import services.SocialProfileService;
import services.SponsorService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Box;
import domain.Message;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Sponsorship;

@Controller
@RequestMapping("/data/sponsor")
public class DownloadDataSponsorController extends AbstractController {

	@Autowired
	private SponsorService			sponsorService;
	@Autowired
	private SponsorshipService		sponsorshipService;
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

			final Sponsor spo = this.sponsorService.findByPrincipal();
			final Collection<SocialProfile> sc = this.socialProfileService.findAllByActor(spo.getId());
			final Collection<Message> msgs = this.messageService.messagePerActor(spo.getId());
			final Collection<Box> bxs = this.boxService.findAllBoxByActor(spo.getId());
			final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAllBySponsorId(spo.getId());

			myString += "\r\n\r\n";

			myString += spo.getName() + " " + spo.getMiddleName() + " " + spo.getSurname() + " " + spo.getAddress() + " " + spo.getEmail() + " " + spo.getPhone() + " " + spo.getPhoto() + " \r\n";
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
			myString += "Sponsorships:\r\n\r\n";
			for (final Sponsorship sponsorship : sponsorships) {
				myString += "Banner: " + sponsorship.getBanner() + " target URL " + sponsorship.getTargetUrl() + "\r\n";
				myString += "Credit Card-> " + " Holder Name: " + sponsorship.getCreditCard().getHolderName() + " Make: " + sponsorship.getCreditCard().getMake() + " Number: " + sponsorship.getCreditCard().getNumber() + " Expiration Month: "
					+ sponsorship.getCreditCard().getExpMonth() + " Expiration Year: " + sponsorship.getCreditCard().getExpYear() + " CVV: " + sponsorship.getCreditCard().getCvv() + "\r\n";
				myString += "Activated: " + sponsorship.getActivated() + "\r\n\r\n";
			}
			myString += "\r\n";

			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename=my_data_as_sponsor.txt");
			final ServletOutputStream out = response.getOutputStream();
			out.println(myString);
			out.flush();
			out.close();

		} else {
			String myString = "Debajo de estas lineas puedes encontrar todos los datos que tenemos de ti en Acme-Parade:\r\n";

			final Sponsor spo = this.sponsorService.findByPrincipal();
			final Collection<SocialProfile> sc = this.socialProfileService.findAllByActor(spo.getId());
			final Collection<Message> msgs = this.messageService.messagePerActor(spo.getId());
			final Collection<Box> bxs = this.boxService.findAllBoxByActor(spo.getId());
			final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAllBySponsorId(spo.getId());

			myString += "\r\n\r\n";

			myString += spo.getName() + " " + spo.getMiddleName() + " " + spo.getSurname() + " " + spo.getAddress() + " " + spo.getEmail() + " " + spo.getPhone() + " " + spo.getPhoto() + " \r\n";
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
			myString += "Patrocinios:\r\n\r\n";
			for (final Sponsorship sponsorship : sponsorships) {
				myString += "Banner: " + sponsorship.getBanner() + " URL objetivo " + sponsorship.getTargetUrl() + "\r\n";
				myString += "Tarjeta de Crédito-> " + " Propietario: " + sponsorship.getCreditCard().getHolderName() + " Marca: " + sponsorship.getCreditCard().getMake() + " Número: " + sponsorship.getCreditCard().getNumber() + " Mes de Caducidad: "
					+ sponsorship.getCreditCard().getExpMonth() + " Año de caducidad: " + sponsorship.getCreditCard().getExpYear() + " CVV: " + sponsorship.getCreditCard().getCvv() + "\r\n";
				myString += "Activado: " + sponsorship.getActivated() + "\r\n\r\n";
			}
			myString += "\r\n";

			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename=my_data_as_sponsor.txt");
			final ServletOutputStream out = response.getOutputStream();
			out.println(myString);
			out.flush();
			out.close();
		}
	}
}
