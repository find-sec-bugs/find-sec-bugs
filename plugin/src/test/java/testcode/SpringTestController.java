package testcode;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.logging.Logger;

@Controller
@RequestMapping("/test")
public class SpringTestController {

    private static final Logger logger = Logger.getLogger(SpringTestController.class.getName());

    @RequestMapping(value = "/hello1/{text}", method = RequestMethod.GET)
    public String hello1(@PathVariable String text) {
        logger.fine("hello #1");
        return "redirect:/somewhere";
    }

    @RequestMapping(value = "/hello2", method = RequestMethod.POST)
    public void hello2() {
        logger.fine("hello #2");
    }
}
