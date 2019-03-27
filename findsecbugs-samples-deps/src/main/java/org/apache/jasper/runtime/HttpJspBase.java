
package org.apache.jasper.runtime;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.HttpJspPage;

//STUB note: directly referenced in JSPC-generated code
public abstract class HttpJspBase extends HttpServlet implements HttpJspPage {

   @Override
   public final void init(ServletConfig config) throws ServletException  {  }

   @Override
   public String getServletInfo() {
       return "" ;
   }

   @Override
   public final void destroy() {   }

   @Override
   public void jspInit() { }

   public void _jspInit() { }

   @Override
   public void jspDestroy() { }

   protected void _jspDestroy() { }

   @Override
   public abstract void _jspService(HttpServletRequest request,
                                    HttpServletResponse response)
       throws ServletException, IOException;
}

