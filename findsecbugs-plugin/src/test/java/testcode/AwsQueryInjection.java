package testcode;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;


public class AwsQueryInjection extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
            String customerID = request.getParameter("customerID");
		    BasicAWSCredentials awsCredentials = new BasicAWSCredentials("test", "test");
            AmazonSimpleDBClient sdbc = new AmazonSimpleDBClient(awsCredentials);
            
            String query = "select * from invoices where customerID = '"
                + customerID;
            SelectResult sdbResult = sdbc.select(new SelectRequest(query)); //BAD

            SelectResult sdbResult2 = sdbc.select(new SelectRequest(query, false)); //BAD

            SelectRequest sdbRequest = new SelectRequest();
            SelectResult sdbResult3 = sdbc.select(sdbRequest.withSelectExpression(query)); //BAD
            
            String query2 = "select * from invoices where customerID = 123";
            SelectResult sdbResult4 = sdbc.select(new SelectRequest(query2)); //OK

        }catch(Exception e){
            System.out.println(e);
       }
    }
}
