package ar.com.coninf.doconline.shared.servicio;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service(value = "mailService")
public class MailService {
	@Value("#{mailProperties.mail_host}")
	private String mailHost;
	@Value("#{mailProperties.mail_user}")
	private String mailUser;
	@Value("#{mailProperties.mail_password}")
	private String mailPassword;
	@Value("#{mailProperties.mail_auth}")
	private String mailAuth;
	
	@Value("#{mailProperties.mail_from}")
	private String mailFrom;
	@Value("#{mailProperties.mail_to}")
	private String mailTo;

	public MailService() {
		
	}
	
	public Boolean enviarMail(String subject, String text) {
		return this.enviarMail(mailFrom, mailTo, subject, text);
	}
	
	public Boolean enviarMail(String origen, String destino, String subject, String text) {
        try
        {
            // Propiedades de la conexion
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", mailHost);
            props.setProperty("mail.smtp.user", mailUser);
            props.setProperty("mail.smtp.auth", mailAuth);

            Session session = Session.getDefaultInstance(props);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(origen));
            message.addRecipients(Message.RecipientType.TO, destino);
            
            message.setSubject(subject);
            message.setText(text);

            Transport t = session.getTransport("smtp");
            t.connect(mailUser, mailPassword);
            t.sendMessage(message, message.getAllRecipients());

            t.close();
			Logger.getLogger(this.getClass()).info("Enviando Mail a: " + destino + " Datos: " + subject + " - " + text);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
	}

	public Boolean VerificarSmtp() {
        try
        {
	        // Propiedades de la conexion
	        Properties props = new Properties();
	        props.setProperty("mail.smtp.host", mailHost);
	        props.setProperty("mail.smtp.user", mailUser);
	        props.setProperty("mail.smtp.auth", mailAuth);
	
	        Session session = Session.getDefaultInstance(props);
	
	        MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("avisos@boldt.com.ar"));
	        message.addRecipients(Message.RecipientType.TO, "no-reply");
	        
	        message.setSubject("no-reply");
	        message.setText("no-reply");
	
	        Transport t = session.getTransport("smtp");
	        t.connect(mailUser, mailPassword);
	        t.sendMessage(message, message.getAllRecipients());
	
	        t.close();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	        if (e.getCause().getMessage()!=null || e.getCause().getMessage().contains("Connection timed out")) {
	        	//: connect
	        	return false;
	        }
	    }
	    return true;
	}
	
	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getMailUser() {
		return mailUser;
	}

	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	public String getMailAuth() {
		return mailAuth;
	}

	public void setMailAuth(String mailAuth) {
		this.mailAuth = mailAuth;
	}
}
