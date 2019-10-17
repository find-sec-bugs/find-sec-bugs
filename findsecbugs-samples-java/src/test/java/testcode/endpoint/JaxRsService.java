package testcode.endpoint;

import javax.ws.rs.Path;

@Path("/test")
public class JaxRsService {

    public String randomMethod() {
        return "Nothing to see..";
    }

    @Path("/hello")
    public String hello(String user) {
        return "Hello " + user;
    }
}
