package testcode.smtp;

import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Properties;

public class SmtpClient {
    public static void main(String[] args) throws Exception {
        //Example of payload that would prove the injection
        sendEmail("Testing Subject\nX-Test: Override", "test\nDEF:2", "SVW:2\nXYZ", "ou\nlalala:22\n\rlili:22", "123;\n456=889","../../../etc/passwd");
    }

    public static void sendEmail(String input1, String input2, String input3, String input4, String input5,String input6) throws Exception {

        final String username = "email@gmail.com"; //Replace by test account
        final String password = ""; //Replace by app password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("source@gmail.com"));
        message.setRecipients(Message.RecipientType.TO,new InternetAddress[] {new InternetAddress("destination@gmail.com")});
        message.setSubject(input1); //Injectable API
        message.addHeader("ABC", input2); //Injectable API (value parameter)
        message.addHeader(input3,"aa"); //Injectable API (key parameter)
        message.setDescription(input4); //Injectable API
        message.setDisposition(input5); //Injectable API
        message.setText("This is just a test 2.");
        Transport.send(message);
        new FileDataSource("/path/traversal/here/"+input6);

        System.out.println("Done");
    }

    public static void sendEmailTainted(Session session,HttpServletRequest req) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setSubject(req.getParameter("user")+" is following you");
    }

    public static void sendEmailFP(Session session,String input) throws MessagingException {
        Message message = new MimeMessage(session);
        message.addHeader("abc", URLEncoder.encode(input));
    }
}
