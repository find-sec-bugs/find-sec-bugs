package testcode;

import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;

public class ApacheXmlRpc {
    public static void createClientAndServerConfigs() {

        boolean trueValue = true;

        XmlRpcClientConfigImpl goodClientConfig = new XmlRpcClientConfigImpl();
        goodClientConfig.setEnabledForExtensions(true); // BAD
        goodClientConfig.setEnabledForExtensions(trueValue); // BAD

        XmlRpcServerConfigImpl goodServerConfig = new XmlRpcServerConfigImpl();
        goodServerConfig.setEnabledForExtensions(true); // BAD
        goodServerConfig.setEnabledForExtensions(trueValue); // BAD

        boolean falseValue = false;

        XmlRpcClientConfigImpl badClientConfig = new XmlRpcClientConfigImpl();
        badClientConfig.setEnabledForExtensions(false); // GOOD
        badClientConfig.setEnabledForExtensions(falseValue); // GOOD

        XmlRpcServerConfigImpl badServerConfig = new XmlRpcServerConfigImpl();
        badServerConfig.setEnabledForExtensions(false); // GOOD
        badServerConfig.setEnabledForExtensions(falseValue); // GOOD

        boolean randomed = Math.random() < 0.5;
        badServerConfig.setEnabledForExtensions(randomed); // BAD
    }
}
