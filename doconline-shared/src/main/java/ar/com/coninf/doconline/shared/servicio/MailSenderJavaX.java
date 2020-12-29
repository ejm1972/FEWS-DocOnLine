package ar.com.coninf.doconline.shared.servicio;
//package ar.com.boldt.monedero.shared.service;
//
//
//import java.io.ByteArrayOutputStream;
//import java.io.Serializable;
//import java.util.Date;
//import java.util.Properties;
//
//import javax.activation.CommandMap;
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.MailcapCommandMap;
//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.Multipart;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import javax.mail.util.ByteArrayDataSource;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import ar.com.coninf.doconline.rest.model.response.ResponseMail;
//
//import com.lowagie.text.Document;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.pdf.PdfWriter;
//
//
//@Service
//public class MailSenderJavaX implements Serializable {
//
//	private static final long serialVersionUID = 1174624332365213896L;
//	
//	@Value("#{mailProperties.mail_host}")
//	private String mailHost;
//	@Value("#{mailProperties.mail_user}")
//	private String mailUser;
//	@Value("#{mailProperties.mail_password}")
//	private String mailPassword;
//	@Value("#{mailProperties.mail_auth}")
//	private String mailAuth;
//	
//	@Value("#{mailProperties.mail_from}")
//	private String mailFrom;
//
//	@Value("#{mailProperties.logos_juegos}")
//	private String logosJuegos;
//
//	static {
//        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();  
//        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");  
//        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");  
//        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");  
//        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");  
//        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");  
//        CommandMap.setDefaultCommandMap(mc);
//	}
//	
//	public MailSenderJavaX() {
//		
//	}
//	
//	public ResponseMail enviarMail(String mailTo, String subject, String text) {
//		return this.enviarMail(mailFrom, mailTo, subject, text, null);
//	}
//	
//	public ResponseMail enviarMail(String mailTo, String subject, String text, String attachmentData) {
//		return this.enviarMail(mailFrom, mailTo, subject, text, attachmentData);
//	}
//
//	private ResponseMail enviarMail(String origen, String destino, String subject, String text, String attachmentData) {
//		ResponseMail response = new ResponseMail();
//		
//        try
//        {
//            // Propiedades de la conexion
//            Properties props = new Properties();
//            props.setProperty("mail.smtp.host", mailHost);
//            props.setProperty("mail.smtp.user", mailUser);
//            props.setProperty("mail.smtp.auth", mailAuth);
//
//            Session session = Session.getDefaultInstance(props);
//            
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(origen));
//            message.addRecipients(Message.RecipientType.TO, destino);
//            
//            message.setSubject(subject);
//            
//            if (attachmentData != null) {
//            	Document doc = new Document();
//            	ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
//            	PdfWriter docWriter = PdfWriter.getInstance(doc, baosPDF);
//            	
//    			doc.open();
//
//    			doc.add(new Paragraph("This document was created by a class named: " + this.getClass().getName()));
//
//           		doc.add(new Paragraph("This document was created on " + new java.util.Date()));
//
//           		doc.close();
//           		docWriter.close();
//
//                Multipart multipart = new MimeMultipart();
//
//                // texto
//            	BodyPart messageBodyPart = new MimeBodyPart();
//                messageBodyPart.setText(text);
//
//                multipart.addBodyPart(messageBodyPart);
//                
//                // attachment
//                messageBodyPart = new MimeBodyPart();
//                DataSource dataSource = new ByteArrayDataSource(baosPDF.toByteArray(), "application/pdf");
//                messageBodyPart.setHeader("Content-Transfer-Encoding", "base64");
//                messageBodyPart.setDataHandler(new DataHandler(dataSource));
//                messageBodyPart.setFileName("test.pdf");
//                
//                multipart.addBodyPart(messageBodyPart);
//                
//                message.setHeader("Content-Type", "multipart/mixed");
//                message.setSentDate(new Date());
//                message.setContent(multipart);
//            } else {
//                message.setText(text);
//            }
//
//            Transport t = session.getTransport("smtp");
//            t.connect(mailUser, mailPassword);
//            t.sendMessage(message, message.getAllRecipients());
//
//            t.close();
//			Logger.getLogger(this.getClass()).info("Enviando Mail a: " + destino + " Datos: " + subject + " - " + text);
//        }
//        catch (Exception e)
//        {
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
//        
//        return response;
//	}
//
//	public Boolean VerificarSmtp() {
//        try
//        {
//	        // Propiedades de la conexion
//	        Properties props = new Properties();
//	        props.setProperty("mail.smtp.host", mailHost);
//	        props.setProperty("mail.smtp.user", mailUser);
//	        props.setProperty("mail.smtp.auth", mailAuth);
//	
//	        Session session = Session.getDefaultInstance(props);
//	
//	        MimeMessage message = new MimeMessage(session);
//	        message.setFrom(new InternetAddress("avisos@boldt.com.ar"));
//	        message.addRecipients(Message.RecipientType.TO, "no-reply");
//	        
//	        message.setSubject("no-reply");
//	        message.setText("no-reply");
//	
//	        Transport t = session.getTransport("smtp");
//	        t.connect(mailUser, mailPassword);
//	        t.sendMessage(message, message.getAllRecipients());
//	
//	        t.close();
//	    }
//	    catch (Exception e)
//	    {
//	        e.printStackTrace();
//	        if (e.getCause().getMessage()!=null || e.getCause().getMessage().contains("Connection timed out")) {
//	        	//: connect
//	        	return false;
//	        }
//	    }
//	    return true;
//	}
//	
//	public String getMailHost() {
//		return mailHost;
//	}
//
//	public void setMailHost(String mailHost) {
//		this.mailHost = mailHost;
//	}
//
//	public String getMailUser() {
//		return mailUser;
//	}
//
//	public void setMailUser(String mailUser) {
//		this.mailUser = mailUser;
//	}
//
//	public String getMailPassword() {
//		return mailPassword;
//	}
//
//	public void setMailPassword(String mailPassword) {
//		this.mailPassword = mailPassword;
//	}
//
//	public String getMailAuth() {
//		return mailAuth;
//	}
//
//	public void setMailAuth(String mailAuth) {
//		this.mailAuth = mailAuth;
//	}
//}