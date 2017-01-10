package testcode;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import java.util.HashMap;
import java.util.Enumeration;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

public class BeanInjection extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        User user = new User();
        HashMap map = new HashMap();
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            map.put(name, request.getParameterValues(name));
        }
        try{
            BeanUtils.populate(user, map); //BAD

            BeanUtilsBean beanUtl = BeanUtilsBean.getInstance();
            beanUtl.populate(user, map); //BAD
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private class User implements java.io.Serializable {

        private String name;

        public String getName(){
            return this.name;
        }
        
        public void setName(String name){
            this.name = name;
        }
    }
}
