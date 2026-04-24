package jakarta.mail;

public abstract class Authenticator {

    protected PasswordAuthentication getPasswordAuthentication() {
        return null;
    }

}
