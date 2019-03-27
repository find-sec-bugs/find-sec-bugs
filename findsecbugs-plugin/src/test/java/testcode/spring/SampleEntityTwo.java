package testcode.spring;

public class SampleEntityTwo extends SampleEntity{
	private String test;

	public SampleEntityTwo(String test) {
		super(test);
		this.test = test;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}