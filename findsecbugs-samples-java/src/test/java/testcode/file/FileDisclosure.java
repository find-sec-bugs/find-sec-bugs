package testcode.file;

import java.io.IOException;
import org.apache.struts.action.ActionForward;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class FileDisclosure extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
            String returnURL = request.getParameter("returnURL");
            
            /******Struts ActionForward vulnerable code tests******/
            ActionForward forward = new ActionForward(returnURL); //BAD

            ActionForward forward2 = new ActionForward(returnURL, true); //BAD

            ActionForward forward3 = new ActionForward("name", returnURL, true); //BAD

            ActionForward forward4 = new ActionForward("name", returnURL, true, true); //BAD

            ActionForward forward5 = new ActionForward();
            forward5.setPath(returnURL); //BAD

            //false positive test - returnURL moved from path to name (safe argument)
            ActionForward forward6 = new ActionForward(returnURL, "path", true); //OK
            
            /******Spring ModelAndView vulnerable code tests******/
            ModelAndView mv = new ModelAndView(returnURL); //BAD
                       
            ModelAndView mv2 = new ModelAndView(returnURL, new HashMap()); //BAD

            ModelAndView mv3 = new ModelAndView(returnURL, "modelName", new Object()); //BAD

            ModelAndView mv4 = new ModelAndView();
            mv4.setViewName(returnURL); //BAD
            
            //false positive test - returnURL moved from viewName to modelName (safe argument)
            ModelAndView mv5 = new ModelAndView("viewName", returnURL, new Object()); //OK

        }catch(Exception e){
         System.out.println(e);
       }
    }

    public void doGet2(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
            String jspFile = request.getParameter("jspFile");

            RequestDispatcher requestDispatcher = request.getRequestDispatcher(jspFile);

            requestDispatcher.include(request, response);

            requestDispatcher = request.getServletContext().getRequestDispatcher(jspFile);

            requestDispatcher.forward(request, response);

        }catch(Exception e){
            System.out.println(e);
        }
    }
}