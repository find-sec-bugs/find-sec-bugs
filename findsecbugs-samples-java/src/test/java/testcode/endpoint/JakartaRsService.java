package testcode.endpoint;

import jakarta.ws.rs.Path;

@Path("/test")
public class JakartaRsService {

    public String randomMethod() {
        return "Nothing to see..";
    }

    @Path("/hello")
    public String hello(String user) {
        return "Hello " + user;
    }
}
