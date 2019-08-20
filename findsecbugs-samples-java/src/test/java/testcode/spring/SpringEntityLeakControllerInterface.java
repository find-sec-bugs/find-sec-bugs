package testcode.spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface SpringEntityLeakControllerInterface {
    @RequestMapping("/api1")
    SampleEntity api1(@RequestParam("url") String url);
}
