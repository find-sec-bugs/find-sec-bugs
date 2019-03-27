package testcode;

import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;

public class ApacheXmlRpc {
    public static void createClientAndServerConfigs() {

        XmlRpcServerConfigImpl serverConfig = new XmlRpcServerConfigImpl();
        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();

        boolean trueValue = true;

        clientConfig.setEnabledForExtensions(true); // BAD
        clientConfig.setEnabledForExtensions(trueValue); // BAD

        serverConfig.setEnabledForExtensions(true); // BAD
        serverConfig.setEnabledForExtensions(trueValue); // BAD

        boolean falseValue = false;

        clientConfig.setEnabledForExtensions(false); // GOOD
        clientConfig.setEnabledForExtensions(falseValue); // GOOD

        serverConfig.setEnabledForExtensions(false); // GOOD
        serverConfig.setEnabledForExtensions(falseValue); // GOOD

        boolean randomFlagForServer = Math.random() < 0.5;
        serverConfig.setEnabledForExtensions(randomFlagForServer); // BAD
        boolean randomFlagForClient = Math.random() < 0.5;
        clientConfig.setEnabledForExtensions(randomFlagForClient); // BAD

    }
}
