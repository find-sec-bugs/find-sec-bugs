package testcode.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JakartaSpringEntityLeakController {

    @RequestMapping("/jakartaApi")
    public JakartaSampleEntity jakartaApi(@RequestParam("url") String url) {
        return new JakartaSampleEntity("entity1");
    }
}