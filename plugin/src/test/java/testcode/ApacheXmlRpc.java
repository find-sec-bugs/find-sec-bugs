package testcode;

import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;

public class ApacheXmlRpc {
    public static void createClientAndServerConfigs() {

        boolean trueValue = true;

        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        clientConfig.setEnabledForExtensions(true);
        clientConfig.setEnabledForExtensions(trueValue);

        XmlRpcServerConfigImpl serverConfig = new XmlRpcServerConfigImpl();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setEnabledForExtensions(trueValue);
    }
}
