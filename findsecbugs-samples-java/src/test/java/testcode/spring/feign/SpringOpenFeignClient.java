package testcode.spring.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import testcode.spring.SampleEntity;

@FeignClient("feignClient")
public interface SpringOpenFeignClient {
    @RequestMapping("/api1")
    SampleEntity api1(@RequestParam("url") String url);
}
