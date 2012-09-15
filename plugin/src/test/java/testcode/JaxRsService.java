package testcode;

import javax.ws.rs.Path;

@Path("/com.h3xstream.findbugs.test")
public class JaxRsService {

    public String randomMethod() {
        return "Nothing to see..";
    }

    @Path("/hello")
    public String hello(String user) {
        return "Hello "+user;
    }
}
