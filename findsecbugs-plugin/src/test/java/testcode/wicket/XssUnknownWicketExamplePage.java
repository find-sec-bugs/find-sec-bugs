package testcode.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class should NOT get flag for potential XSS.
 */
public class XssUnknownWicketExamplePage extends WebPage {

    private boolean escapeConfig;

    public XssUnknownWicketExamplePage(PageParameters pageParameters) {

        add(new Label("test").setEscapeModelStrings(getEscapeConfig()));
    }

    public boolean getEscapeConfig() {
        return escapeConfig;
    }
}
