package testcode.taint;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public class VariousTaintedAnnotation {

    @Autowired
    private SessionFactory sessionFactory;

    public void requestParam(@RequestParam("input") String input) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void pathVariable(@PathVariable("input") String input) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void requestBody(@RequestBody String input) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

    public void requestHeader(@RequestHeader("input") String input) {
        sessionFactory.openSession().createQuery("FROM comment WHERE user='"+input+"'"); //High
    }

}
