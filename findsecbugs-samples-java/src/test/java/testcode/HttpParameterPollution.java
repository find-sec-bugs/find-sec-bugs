package testcode;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.client.methods.HttpGet;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;

public class HttpParameterPollution extends HttpServlet{
   @SuppressWarnings( "deprecation" ) //URLEncoder.encode is deprecated but use to specially test this API.
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
       try{
           String item = request.getParameter("item");
           
           //in HttpClient 4.x, there is no GetMethod anymore. Instead there is HttpGet
           HttpGet httpget = new HttpGet("http://host.com?param=" + URLEncoder.encode(item)); //OK
           HttpGet httpget2 = new HttpGet("http://host.com?param=" + item); //BAD
           HttpGet httpget3 = new HttpGet("http://host.com?param=" + urlPathSegmentEscaper().escape(item)); //OK

           GetMethod get = new GetMethod("http://host.com?param=" + item); //BAD
           get.setQueryString("item=" + item); //BAD
           //get.execute();

       }catch(Exception e){
         System.out.println(e);
       }
   }
}
