import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHandler {
    private static String user = "raceforthegalaxy@gmail.com";
    private static String pass = ""; // Git doesn't allow passwords see local pass.txt

    public static void sendMessage(String from, String to, String subject, String text) throws MessagingException {
        // Assuming you are sending email from localhost
        String host = "localhost";

        // Setup message properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        //properties.put("mail.smtp.user", user);
        //properties.put("mail.smtp.pass", pass);

        // Get the  Session object.
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("raceforthegalaxy@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(text);
        message.setSentDate(new Date());

        // Send message
        Transport.send(message);
    }
}
