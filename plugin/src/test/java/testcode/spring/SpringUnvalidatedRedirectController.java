package testcode.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpringUnvalidatedRedirectController {

    @RequestMapping("/redirect1")
    public String redirect1(@RequestParam("url") String url) {
        return "redirect:" + url;
    }

    @RequestMapping("/redirect2")
    public String redirect2(@RequestParam("url") String url) {
        String val = "redirect:" + url;
        return val;
    }

    @RequestMapping("/redirect3")
    public String redirect3(@RequestParam("url") String url) {
        return buildRedirect(url);
    }

    private String buildRedirect(String u) {
        return "redirect:" + u;
    }

    @RequestMapping("/redirectfp")
    public String redirectfp() {
        return "redirect:/";
    }
}
