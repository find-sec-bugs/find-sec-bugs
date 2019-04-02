package testcode.taint;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/testme")
public abstract class UnsafeTaintedByAnnotationEndpoint {


    @Autowired
    private SessionFactory sessionFactory;

    public abstract String getUnknownValue();

    //Single tainted annotation

    public void taintAnnotation(@RequestParam("input") String user) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+user+"'"); //High
    }

    public void unsafeAnnotation1(@ModelAttribute("comment") CommentDto comment,@RequestParam("input") String input) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    //Multiple tainted annotations

    public void unsafeAnnotation2(@RequestParam("comment") String comment, @RequestParam("input") String input) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void unsafeAnnotation3(@RequestParam("input") String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void unsafeAnnotation4(String unusedParameter, @RequestParam("input") String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void unsafeAnnotation5(int unusedParameter1, String unusedParameter2, @RequestParam("input") String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    //Makes sure that primitive types that takes two slots are properly consider

    public void unsafeAnnotation6(double unusedParameter1, @RequestParam("input") String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void unsafeAnnotation7(@RequestParam("input") String input, @RequestParam("unsafe") String unsafe, double unusedParameter1) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void unsafeAnnotation8(long unusedParameter1, @RequestParam("input") String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void unsafeAnnotation9(@RequestParam("input") String input, @RequestParam("unsafe") String unsafe, long unusedParameter1) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

}
