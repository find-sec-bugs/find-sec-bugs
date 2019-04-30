package testcode.taint;

import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tomas Polesovsky
 */
public class ParameterTaintBackPropagation {
    private HttpServletRequest request;
    private SessionFactory sessionFactory;

    public void taintedUsingPropagatedParameter(){
        StringBuffer sb = new StringBuffer();

        appendRequestParameter1(0, sb, 0, "param", 0);

        sessionFactory.openSession().createQuery("FROM comment WHERE userId="+sb.toString());
    }

    public void safeByParameterBackPropagation(){
        StringBuffer sb = new StringBuffer();

        appendString(sb, "param");

        sessionFactory.openSession().createQuery("FROM comment WHERE userId="+sb.toString());
    }

    public void safeByTagBackPropagation(){
        String tainted = request.getParameter("tainted");

        checkIsNum(tainted);

        sessionFactory.openSession().createQuery("FROM comment WHERE userId=" + tainted);
    }

    private boolean appendRequestParameter1(long l1, StringBuffer sb, long l2, String name, long l3) {
        return appendRequestParameter(l1, sb, l2, name, l3);
    }

    private boolean appendRequestParameter(long l1, StringBuffer sb, long l2, String name, long l3) {
        String parameter = request.getParameter(name);

        if (parameter != null) {
            sb.append(parameter);

            return true;
        }
        else {
            sb.append(l1 + l2 + l3);

            return false;
        }
    }

    private boolean appendString(StringBuffer sb, String str) {
        sb.append(str);

        return true;
    }

    private void checkIsNum(String value) {
        Long.parseLong(value);
    }

}
