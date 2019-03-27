package testcode.spelviewinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * This custom error page is intended to facilitate the exploitation of
 * the injection.
 * Error based injection is then possible and no script should be needed to extract value.
 * </p>
 * <p>
 * (The default error page will be too generic otherwise.)
 * </p>
 */
@Controller
public class VerboseErrorController implements ErrorController {

    private static final String PATH = "/error";

    private static final String ERROR_TEMPLATE = "<html><body><h1>${error.error}</h1><h2>${error.exception}</h2><p>${error.message}</p></body></html>";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(PATH)
    private ModelAndView error(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Map<String,Object> att = errorAttributes.getErrorAttributes(requestAttributes, true);

        //return new ModelAndView("error", att);
        ModelAndView model = new ModelAndView(new SpelView(ERROR_TEMPLATE));
        model.addObject("error", att);

        return model;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
