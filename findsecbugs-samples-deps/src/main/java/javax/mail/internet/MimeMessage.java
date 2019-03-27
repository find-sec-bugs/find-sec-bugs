package javax.mail.internet;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

public class MimeMessage extends Message {

    public MimeMessage(Session session) {
    }

    @Override
    public void setFrom(Address address) throws MessagingException {

    }

    @Override
    public void setRecipients(RecipientType type, Address[] addresses) {

    }

    @Override
    public void setSubject(String subject) throws MessagingException {

    }

    @Override
    public void addHeader(String header_name, String header_value) throws MessagingException {

    }

    @Override
    public void setDescription(String description) throws MessagingException {

    }

    @Override
    public void setDisposition(String disposition) throws MessagingException {

    }

    @Override
    public void setText(String text) throws MessagingException {

    }
}
