package ar.com.coninf.doconline.shared.servicio;
//package ar.com.boldt.monedero.shared.service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.Serializable;
//
//import javax.activation.DataSource;
//import javax.mail.internet.MimeMessage;
//import javax.mail.util.ByteArrayDataSource;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import ar.com.coninf.doconline.rest.model.request.RequestMail;
//import ar.com.coninf.doconline.rest.model.response.ResponseMail;
//
//import com.lowagie.text.Document;
//import com.lowagie.text.pdf.PdfWriter;
//
//@Service
//public class MailSender implements Serializable {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -5346142370159700580L;
//	
//	@Autowired
//	private JavaMailSender mailSender;
//
//	@Autowired
//	@Qualifier("message")
//	private SimpleMailMessage simpleMailMessage;
//
//	@Value("#{mailProperties.mail_from}")
//	private String mailFrom;
//	@Value("#{mailProperties.logos_juegos}")
//	private String logosJuegos;
//
//	public ResponseMail enviarMail(String destino, String subject, String text) {
//		ResponseMail response = new ResponseMail();
//
//		MimeMessage message = mailSender.createMimeMessage();
//
//		try {
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//			helper.setFrom(mailFrom);
//			helper.setTo(destino);
//			helper.setSubject(subject);
//			helper.setText(text + "\n\n");
//
//			mailSender.send(message);
//			Logger.getLogger(this.getClass()).info("Enviando Mail a: " + destino + " Datos: " + subject + " - " + text);
//		} catch (Exception e) {
//            e.printStackTrace();
//            
//            response.setResult(false);
//            response.setCode(10);
//            response.setMessage(e.getMessage());
//            
//            return response;
//        }
//        
//        response.setResult(true);
//        return response;
//	}
//
//	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
//		this.simpleMailMessage = simpleMailMessage;
//	}
//
//	public void setMailSender(JavaMailSender mailSender) {
//		this.mailSender = mailSender;
//	}
//
//	public ResponseMail enviarMailApuesta(String destino, String subject, String text, RequestMail req) {
//		ResponseMail response = new ResponseMail();
//
//		MimeMessage message = mailSender.createMimeMessage();
//
//		try {
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//			helper.setFrom(mailFrom);
//			helper.setTo(destino);
//			helper.setSubject(subject);
//			helper.setText(text + "\n\n");
//
//        	Document doc = new Document();
//        	ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
//        	PdfWriter docWriter = PdfWriter.getInstance(doc, baosPDF);
//        	
//        	TicketApuestaQ6 pdfApuesta = new TicketApuestaQ6(doc, docWriter);
//        	pdfApuesta.generoPDF(req, logosJuegos);
//			
//            DataSource dataSource = new ByteArrayDataSource(baosPDF.toByteArray(), "application/pdf");
//			helper.addAttachment("cupon.pdf", dataSource);
//
//			mailSender.send(message);
//			Logger.getLogger(this.getClass()).info("Enviando Mail a: " + destino + " Datos: " + subject + " - " + text);
//		} catch (Exception e) {
//            e.printStackTrace();
//            
//            response.setResult(false);
//            response.setCode(10);
//            response.setMessage(e.getMessage());
//            
//            return response;
//        }
//        
//        response.setResult(true);
//        return response;
//	}
//
//	public ResponseMail enviarMailApuestaQ6(String destino, String subject, String text, RequestMail req) {
//		ResponseMail response = new ResponseMail();
//
//		MimeMessage message = mailSender.createMimeMessage();
//
//		try {
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//			helper.setFrom(mailFrom);
//			helper.setTo(destino);
//			helper.setSubject(subject);
//			helper.setText(text + "\n\n");
//
//        	Document doc = new Document();
//        	ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
//        	PdfWriter docWriter = PdfWriter.getInstance(doc, baosPDF);
//        	
//        	TicketApuestaQ6 pdfApuesta = new TicketApuestaQ6(doc, docWriter);
//        	pdfApuesta.generoPDF(req, logosJuegos);
//			
//            DataSource dataSource = new ByteArrayDataSource(baosPDF.toByteArray(), "application/pdf");
//			helper.addAttachment("cupon.pdf", dataSource);
//
//			mailSender.send(message);
//			Logger.getLogger(this.getClass()).info("Enviando Mail a: " + destino + " Datos: " + subject + " - " + text);
//		} catch (Exception e) {
//            e.printStackTrace();
//            
//            response.setResult(false);
//            response.setCode(10);
//            response.setMessage(e.getMessage());
//            
//            return response;
//        }
//        
//        response.setResult(true);
//        return response;
//	}
//}
