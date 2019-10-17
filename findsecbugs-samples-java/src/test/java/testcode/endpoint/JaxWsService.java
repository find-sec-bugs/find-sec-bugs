package testcode.endpoint;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class JaxWsService {

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
