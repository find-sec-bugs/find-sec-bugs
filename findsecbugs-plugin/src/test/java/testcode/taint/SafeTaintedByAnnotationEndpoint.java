package testcode.taint;

import org.hibernate.SessionFactory;
import org.jboss.seam.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
@RequestMapping("/testme")
public abstract class SafeTaintedByAnnotationEndpoint {


    @Autowired
    private SessionFactory sessionFactory;

    public abstract String getUnknownValue();

    //No tainted annotation therefore, the level of confidence should not be rise

    public void noTaintAnnotation(String user) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+user+"'"); //Medium
    }

    public void safeAnnotation1(@ModelAttribute("comment") CommentDto comment) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+getUnknownValue()+"'"); //Medium
    }

    //Some tainted annotation are present but they are not place to sink location

    public void safeAnnotation2(@RequestParam("comment") String comment, String input) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //Medium
    }

    public void safeAnnotation3(String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //Medium
    }

    public void safeAnnotation4(String unusedParameter, String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //Medium
    }

    public void safeAnnotation5(int unusedParameter1, String unusedParameter2, String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //Medium
    }

    //Makes sure that primitive types that takes two slots are properly consider

    public void safeAnnotation6(double unusedParameter1, String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //Medium
    }

    public void safeAnnotation7(String input, @RequestParam("unsafe") String unsafe, double unusedParameter1) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //Medium
    }

    public void safeAnnotation8(long unusedParameter1, String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //Medium
    }

    public void safeAnnotation9(String input, @RequestParam("unsafe") String unsafe, long unusedParameter1) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //Medium
    }

}
