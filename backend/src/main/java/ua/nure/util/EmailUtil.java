package ua.nure.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class EmailUtil {

    public static EmailBuilder builder() {
        return new EmailBuilder();
    }

    public static Properties properties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        return props;
    }

    public static Authenticator authenticator(String userName, String password) {
        return new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
    }

    public static void sendEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = createMessage(session, subject, toEmail);

            msg.setText(body, "UTF-8");

            Transport.send(msg);
            System.out.println("EMail Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendEmail(Session session, String toEmail, String subject, String body, String pathToAttachment) {
        try {
            MimeMessage msg = createMessage(session, subject, toEmail);

            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(body);

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Second part is attachment
            messageBodyPart = new MimeBodyPart();
            int lastSlash = pathToAttachment.lastIndexOf('/');
            String filename = pathToAttachment.substring(lastSlash + 1);
            DataSource source = new FileDataSource(pathToAttachment);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            msg.setContent(multipart);

            // Send message
            Transport.send(msg);
            System.out.println("EMail Sent Successfully with attachment!!");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static MimeMessage createMessage(Session session, String subject, String emailTo)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress("admin@purity-service.com", "Purity Service"));

        msg.setReplyTo(InternetAddress.parse("roman.kuznetsov@nure.ua", false));

        msg.setSubject(subject, "UTF-8");

        msg.setSentDate(new Date());

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false));

        return msg;
    }

    public static class EmailBuilder {

        private Session session;

        private String toEmail;

        private String subject;

        private String body;

        private String pathToAttachment;

        public EmailBuilder() {
        }

        public EmailBuilder session(Session session) {
            this.session = session;
            return this;
        }

        public EmailBuilder destination(String toEmail) {
            this.toEmail = toEmail;
            return this;
        }

        public EmailBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailBuilder body(String body) {
            this.body = body;
            return this;
        }

        public EmailBuilder pathToAttachment(String pathToAttachment) {
            this.pathToAttachment = pathToAttachment;
            return this;
        }

        public void send() {
            sendEmail(session, toEmail, subject, body);
        }

        public void sendWithAttachment() {
            sendEmail(session, toEmail, subject, body, pathToAttachment);
        }
    }
}

