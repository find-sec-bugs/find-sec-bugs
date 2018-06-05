package org.springframework.web.servlet;

import java.util.Map;

public class ModelAndView {

	public ModelAndView() {
	}

	public ModelAndView(String viewName) {
	}

	public ModelAndView(View view) {

	}

	public ModelAndView(String viewName, Map model) {
	}

	public ModelAndView(String viewName, String modelName, Object modelObject) {
	}

	public void setViewName(String viewName) {
	}


	public ModelAndView addObject(Object attributeValue) {
		return this;
	}

	public ModelAndView addObject(String attributeName, Object attributeValue) {
		return this;
	}
}
