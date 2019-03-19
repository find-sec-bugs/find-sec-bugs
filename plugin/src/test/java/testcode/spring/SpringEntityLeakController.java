package testcode.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpringEntityLeakController {
	@RequestMapping("/api1")
	public SampleEntity api1(@RequestParam("url") String url) {
		return new SampleEntity("entity1");
	}

	@RequestMapping("/api2")
	public SampleEntityTwo api2(@RequestParam("url") String url) {
		return new SampleEntityTwo("entity1");
	}

	@RequestMapping("/api3")
	public void api3(SampleEntity sampleEntity) {
		if (sampleEntity.getTest().equals("test"))
			return;
	}
}