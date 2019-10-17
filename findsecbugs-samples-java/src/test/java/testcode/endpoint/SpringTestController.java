package testcode.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/hello3", method = RequestMethod.GET)
    public void hello3(HttpServletRequest req) {
        logger.fine("hello #3");
    }

    @RequestMapping(value = "/hello4", method = RequestMethod.GET)
    public void hello4(@RequestParam("param1") String param1) {
        logger.fine("hello #4");
    }

    @RequestMapping(value = "/hello5", method = RequestMethod.GET)
    public void hello5(@RequestParam("description") String description, HttpServletRequest req) {
        logger.fine("hello #5");
    }

    @RequestMapping(value = "/hello6", method = RequestMethod.GET)
    public void hello6(@RequestParam("description") String description, @RequestPart("file") MultipartFile file) {
        logger.fine("hello #6");
    }

    @GetMapping(value = "/hello-get")
    public void helloGet() {
        logger.fine("hello GET");
    }

    @PostMapping(value = "/hello-post")
    public void helloPost() {
        logger.fine("hello POST");
    }

    @PutMapping(value = "/hello-put")
    public void helloPut() {
        logger.fine("hello PUT");
    }

    @DeleteMapping(value = "/hello-delete")
    public void helloDelete() {
        logger.fine("hello DELETE");
    }

    @PatchMapping(value = "/hello-patch")
    public void helloPatch() {
        logger.fine("hello PATCH");
    }
}
