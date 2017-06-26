package testcode.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringUnvalidatedRedirectController {

    @RequestMapping("/redirect1")
    public String redirect1(@RequestParam("url") String url) {
        return "redirect:" + url;
    }

    @RequestMapping("/redirect2")
    public String redirect2(@RequestParam("url") String url) {
        String view = "redirect:" + url;
        return view;
    }

    @RequestMapping("/redirect3")
    public String redirect3(@RequestParam("url") String url) {
        return buildRedirect(url);
    }

    private String buildRedirect(String u) {
        return "redirect:" + u;
    }

    @RequestMapping("/redirect4")
    public ModelAndView redirect4(@RequestParam("url") String url) {
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping("/redirect5")
    public ModelAndView redirect5(@RequestParam("url") String url) {
        String view = "redirect:" + url;
        return new ModelAndView(view);
    }

    @RequestMapping("/redirectfp")
    public String redirectfp() {
        return "redirect:/";
    }
}
