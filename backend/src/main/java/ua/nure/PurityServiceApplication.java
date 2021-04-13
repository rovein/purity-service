package ua.nure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.nure.util.EmailUtil;

import javax.mail.Session;

@SpringBootApplication
public class PurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PurityServiceApplication.class, args);

        final String fromEmail = "roman.kuznetsov@nure.ua";
        final String password = "rgrsxgnmaxzmumyw";
        final String toEmail = "roman.a.kuznetsov@nixsolutions.com";

        System.out.println("TLSEmail Start");

        Session session = Session.getInstance(EmailUtil.properties(), EmailUtil.authenticator(fromEmail, password));

        EmailUtil.builder()
                .session(session)
                .destination(toEmail)
                .subject("Test from Roma")
                .body("Hi, I am sending it to you from Java program")
                .pathToAttachment("src/main/resources/backup_data.sql")
                .sendWithAttachment();
    }


}
