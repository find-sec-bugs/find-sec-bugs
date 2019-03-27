package testcode.csrf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/test")
public class UnsafeSpringCsrfRequestMappingController {

    /**
     * `RequestMapping` maps to all the HTTP request methods by default, making it vulnerable to CSRF attacks.
     */
    @RequestMapping("/request-mapping-no-method")
    public void requestMappingNoMethod() {
    }

    /**
     * The default value of the annotation attribute `method` is an empty array,
     * so this boils down to the same thing as not specifying at all the annotation attribute `method`.
     */
    @RequestMapping(value = "request-mapping-method-empty", method = {})
    public void requestMappingEmptyMethod() {
    }

    /**
     * Mapping to several HTTP request methods is not OK if it's a mix of unprotected and protected HTTP request methods.
     */
    @RequestMapping(value = "/request-mapping-unprotected-and-protected-methods", method = {RequestMethod.GET, RequestMethod.POST})
    public void requestMappingUnprotectedAndProtectedMethods() {
    }

    /**
     * Mapping to several HTTP request methods is not OK if it's a mix of unprotected and protected HTTP request methods.
     */
    @RequestMapping(value = "/request-mapping-unprotected-and-protected-uncommon-methods", method = {RequestMethod.OPTIONS, RequestMethod.PATCH})
    public void requestMappingUnprotectedAndProtectedUncommonMethods() {
    }

    /**
     * Mapping to several HTTP request methods is not OK if it's a mix of unprotected and protected HTTP request methods.
     */
    @RequestMapping(value = "/request-mapping-all-unprotected-methods-and-one-protected-method",
            method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.TRACE, RequestMethod.OPTIONS, RequestMethod.PATCH})
    public void requestMappingAllUnprotectedMethodsAndOneProtectedMethod() {
    }

    /**
     * Mapping to several HTTP request methods is not OK if it's a mix of unprotected and protected HTTP request methods.
     */
    @RequestMapping(value = "/request-mapping-all-protected-methods-and-one-unprotected-method",
            method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.OPTIONS})
    public void requestMappingAllProtectedMethodsAndOneUnprotectedMethod() {
    }

}
