package testcode.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class XssWicketExamplePage extends WebPage {

    public XssWicketExamplePage(PageParameters pageParameters) {

        add(new Label("test").setEscapeModelStrings(false));
    }
}
