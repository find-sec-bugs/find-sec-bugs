package testcode.taint;

import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tomas Polesovsky
 */
public class StaticContextTaintPropagation {
    private HttpServletRequest request;
    private SessionFactory sessionFactory;

    private static String simpleStaticVar = "Test";
    private static String computedStaticVar = "(" + surround(simpleStaticVar, "%") + ")";

    private static String propagatingStaticVar = "";

    private static String surround(String a, String b) {
        if (a == null) {
            return b + b;
        }

        return b + a + b;
    }

    public void falsePositive() {
        sessionFactory.openSession().createQuery("FROM comment WHERE userId like " + computedStaticVar);
    }

    public void taintedValue() {
        simpleStaticVar = request.getParameter("test");

        sessionFactory.openSession().createQuery("FROM comment WHERE userId like " + simpleStaticVar);
    }

    public void staticContextTaintPropagation() {
        propagatingStaticVar = request.getParameter("test");

        staticContextTaintPropagationAfter();
    }

    public void staticContextTaintPropagationAfter() {
        sessionFactory.openSession().createQuery("FROM comment WHERE userId like " + propagatingStaticVar);
    }


}
