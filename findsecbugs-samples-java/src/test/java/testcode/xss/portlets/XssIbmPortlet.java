package testcode.xss.portlets;

import org.apache.jetspeed.portlet.PortletAdapter;
import org.apache.jetspeed.portlet.PortletConfig;
import org.apache.jetspeed.portlet.PortletException;
import org.apache.jetspeed.portlet.PortletRequest;
import org.apache.jetspeed.portlet.PortletResponse;
import org.apache.jetspeed.portlet.UnavailableException;

import java.io.IOException;
import java.io.PrintWriter;

public class XssIbmPortlet extends PortletAdapter {

    public void init (PortletConfig portletConfig) throws UnavailableException
    {
        super.init(portletConfig);
    }

    public void doView(PortletRequest request, PortletResponse response)
            throws PortletException, IOException
    {
        PrintWriter writer = response.getWriter();
        String user = request.getParameter("user");
        writer.println("<h1>Hello "+user+"!</h1>");
    }

}