package javax.mail;

import java.io.Serializable;

public abstract class Message implements Part {
    public abstract void setFrom(Address address) throws MessagingException;

    public abstract void setRecipients(RecipientType type, Address[] addresses);

    public abstract void setSubject(String subject) throws MessagingException;

    public static class RecipientType implements Serializable {
        public static final RecipientType TO = new RecipientType("To");

        protected RecipientType(String type) {
        }
    }
}
