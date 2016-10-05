package testcode.template;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

public class VelocityUsage {

    public void usage1(String inputFile) throws FileNotFoundException {
       Velocity.init();

        VelocityContext context = new VelocityContext();

        context.put("author", "Elliot A.");
        context.put("address", "217 E Broadway");
        context.put("phone", "555-1337");

        FileInputStream file = new FileInputStream(inputFile);

        //Evaluate
        StringWriter swOut = new StringWriter();
        Velocity.evaluate(context, swOut, "test", file);

        String result =  swOut.getBuffer().toString();
        System.out.println(result);
    }

    public void allSignatures(InputStream inputStream, Reader fileReader, String template) throws FileNotFoundException {
        VelocityContext context = new VelocityContext();
        StringWriter swOut = new StringWriter();

        Velocity.evaluate(context, swOut, "test", inputStream);
        Velocity.evaluate(context, swOut, "test", fileReader);
        Velocity.evaluate(context, swOut, "test", template);
    }

    public void falsePositive() throws FileNotFoundException {
        VelocityContext context = new VelocityContext();
        StringWriter swOut = new StringWriter();

        Velocity.evaluate(context, swOut, "test", "Hello $user !");
    }
}
