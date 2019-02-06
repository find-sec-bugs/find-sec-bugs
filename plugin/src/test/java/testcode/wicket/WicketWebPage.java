package testcode.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class WicketWebPage extends WebPage {

    public WicketWebPage(PageParameters parameters) {
        Long productId = parameters.getAsLong("id");
    }
}
