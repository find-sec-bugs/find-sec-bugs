package testcode;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;

/**
 * Output should be as follow
 * <code>
 * {
 *   "args": {
 *     "lang": "test&test=123"
 *   },
 *   "headers": {
 *     "Accept-Encoding": "gzip,deflate",
 *     "Host": "httpbin.org",
 *     "User-Agent": "Apache-HttpClient/4.5.12 (Java/11.0.2)",
 *     "X-Amzn-Trace-Id": "Root=1-5f46bc45-61f85eca53eb3bfabb0be85a"
 *   },
 *   "origin": "198.48.216.239",
 *   "url": "https://httpbin.org/get?lang=test%26test%3D123"
 * }
 * </code>
 *
 * Maven dependency required to reproduce..
 * <code>
 * &lt;dependency&gt;
 *    &lt;groupId&gt;org.apache.httpcomponents&lt;/groupId&gt;
 *    &lt;artifactId&gt;httpclient&lt;/artifactId&gt;
 *    &lt;version&gt;4.5.12&lt;/version&gt;
 * &lt;/dependency&gt;
 * </code>
 */
public class HttpParameterPollutionFalsePositive extends HttpServlet {
    public static void main(String[] args) throws IOException, URISyntaxException {
        doRequest1("test1111&test=123");
        doRequest2("test22222&test=4321");
    }


    public static void doRequest1(String input) throws URISyntaxException, IOException {

        URIBuilder uriBuilder = new URIBuilder("https://httpbin.org/get");
        uriBuilder.addParameter("lang",input);

        HttpGet httpget = new HttpGet(uriBuilder.toString()); //OK

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse resp = httpclient.execute(httpget);


        resp.getEntity().writeTo(System.out);
    }

    public static void doRequest2(String input) throws URISyntaxException, IOException {

        URIBuilder uriBuilder = new URIBuilder("https://httpbin.org/get");
        uriBuilder.addParameter("lang",input);

        HttpGet httpget = new HttpGet(uriBuilder.build().toString()); //OK

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse resp = httpclient.execute(httpget);


        resp.getEntity().writeTo(System.out);
    }


}
