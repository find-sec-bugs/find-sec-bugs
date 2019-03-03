package testcode.spring;

import javax.persistence.Entity;

@Entity
public class SampleEntity {
	private String test;

	public SampleEntity(String test) {
		this.test = test;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}
