package testcode.xml;

public class XmlInjection {
    public static String badXmlStringParam(String amount) {
        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>" + amount
                + "</amount></product>";
        return xmlString;
    }

    public static String goodXmlStringParam(String amount) {
        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>" + sanitize(amount)
                + "</amount></product>";
        return xmlString;
    }

    public static String badXmlStringUserInput() {
        String amount = System.console().readLine();

        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>" + amount
                + "</amount></product>";
        return xmlString;
    }

    public static String goodXmlStringUserInput() {
        String amount = System.console().readLine();

        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>" + sanitize(amount)
                + "</amount></product>";
        return xmlString;
    }

    private static String unreliableAmount = System.console().readLine();

    public static String badXmlStringField() {
        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>" + unreliableAmount
                + "</amount></product>";
        return xmlString;
    }

    public static String goodXmlStringField() {
        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>"
                + sanitize(unreliableAmount) + "</amount></product>";
        return xmlString;
    }

    private static String sanitize(String s) {
        return s.replaceAll("<", "&langle;").replaceAll(">", "&rangle;");
    }
}
