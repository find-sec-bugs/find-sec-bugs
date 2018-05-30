package javax.mail;

public interface Part {

    void addHeader(String header_name, String header_value) throws MessagingException;

    void setDescription(String description) throws MessagingException;

    void setDisposition(String disposition) throws MessagingException;

    void setText(String text) throws MessagingException;

}
