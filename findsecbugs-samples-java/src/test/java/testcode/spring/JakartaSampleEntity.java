package testcode.spring;

import jakarta.persistence.Entity;

@Entity
public class JakartaSampleEntity {
	private String test;

	public JakartaSampleEntity(String test) {
		this.test = test;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}
