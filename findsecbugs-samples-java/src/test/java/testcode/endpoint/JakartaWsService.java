package testcode.endpoint;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public class JakartaWsService {

    @WebMethod(operationName = "timestamp")
    public long ping() {
        return System.currentTimeMillis();
    }

    @WebMethod
    public String hello(String user) {
        return "Hello " + user;
    }

    public int notAWebMethod() {
        return 8000;
    }
}
