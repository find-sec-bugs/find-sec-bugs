package testcode.struts1;

import org.apache.struts.action.ActionForm;

public class TestForm extends ActionForm {

    private String itemId;
    private String price;

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
