package testcode.struts2;

import java.util.logging.Logger;

/**
 * Plain Old Java Object (POJO).
 * The guess is taken because of the execute method.
 */
public class StrutsV2Endpoint {

    private static final Logger logger = Logger.getLogger(StrutsV2Endpoint.class.getName());

    private String itemId;
    private String price;

    public String execute() {
        logger.fine("Entering Struts 2 endpoint");

        return "SUCCESS";
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
