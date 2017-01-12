package org.springframework.web.servlet;

import java.util.Map;

public class ModelAndView {

	public ModelAndView() {
	}

	public ModelAndView(String viewName) {
	}

	public ModelAndView(String viewName, Map model) {
	}

	public ModelAndView(String viewName, String modelName, Object modelObject) {
	}

	public void setViewName(String viewName) {
	}
}
