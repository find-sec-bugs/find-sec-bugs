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

    //No tainted annotation therefore, the level of confidence should not be rise

    public void noTaintAnnotation(@RequestParam("input") String user) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+user+"'"); //High
    }

    public void safeAnnotation1(@ModelAttribute("comment") CommentDto comment,@RequestParam("input") String input) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    //Some tainted annotation are present but they are not place to sink location

    public void safeAnnotation2(@RequestParam("comment") String comment, @RequestParam("input") String input) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void safeAnnotation3(@RequestParam("input") String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void safeAnnotation4(String unusedParameter, @RequestParam("input") String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void safeAnnotation5(int unusedParameter1, String unusedParameter2, @RequestParam("input") String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    //Makes sure that primitive types that takes two slots are properly consider

    public void safeAnnotation6(double unusedParameter1, @RequestParam("input") String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void safeAnnotation7(@RequestParam("input") String input, @RequestParam("unsafe") String unsafe, double unusedParameter1) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void safeAnnotation8(long unusedParameter1, @RequestParam("input") String input, @RequestParam("unsafe") String unsafe) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void safeAnnotation9(@RequestParam("input") String input, @RequestParam("unsafe") String unsafe, long unusedParameter1) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

}
