package testcode.crypto;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;

public class InsecureSmtpSsl {

    public static void main(String[] args) throws Exception {
        Email email = new SimpleEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSSLOnConnect(false); //OK

        Email email2 = new SimpleEmail();
        email2.setHostName("smtp2.googlemail.com");        
        email2.setSSLOnConnect(true); //BAD
        //email2.setSmtpPort(465);
        //email2.setAuthenticator(new DefaultAuthenticator("username", "password"));
        //email2.setFrom("user@gmail.com");
        //email2.setSubject("TestMail");
        //email2.setMsg("This is a test mail ... :-)");
        //email2.addTo("foo@bar.com");
        //email2.send();

        MultiPartEmail emailMulti = new MultiPartEmail();
        emailMulti.setHostName("mail.myserver.com");
        emailMulti.setSSLOnConnect(true); //BAD

        HtmlEmail htmlEmail = new HtmlEmail();
        htmlEmail.setHostName("mail.myserver.com");
        htmlEmail.setSSLOnConnect(true); //BAD

        ImageHtmlEmail imageEmail = new ImageHtmlEmail();
        imageEmail.setHostName("mail.myserver.com");
        imageEmail.setSSLOnConnect(true);
        imageEmail.setSSLCheckServerIdentity(true); //OK
        
        ImageHtmlEmail imageEmail2 = new ImageHtmlEmail();
        imageEmail2.setHostName("mail2.myserver.com");
        imageEmail2.setSSLCheckServerIdentity(true); //OK - reversed order
        imageEmail2.setSSLOnConnect(true);

        ImageHtmlEmail imageEmail3 = new ImageHtmlEmail();
        imageEmail3.setHostName("mail3.myserver.com");
        imageEmail3.setSSLOnConnect(true); //BAD
    }
}
