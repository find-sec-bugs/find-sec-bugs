package testcode.template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerUsage {

    public void simple(String inputFile) {

        //Freemarker configuration object
        Configuration cfg = new Configuration();
        try {
            //Load template from source folder
            Template template = cfg.getTemplate(inputFile);

            // Build the data-model
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("message", "Hello World!");

            //List parsing
            List<String> countries = new ArrayList<String>();
            countries.add("India");
            countries.add("United States");
            countries.add("Germany");
            countries.add("France");

            data.put("countries", countries);

            // Console output
            Writer out = new OutputStreamWriter(System.out);
            template.process(data, out); //Vuln here
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    public void allSignatures(String inputFile) throws IOException, TemplateException {
        Configuration cfg = new Configuration();
        Template template = cfg.getTemplate(inputFile);
        Map<String, Object> data = new HashMap<String, Object>();

        template.process(data, new OutputStreamWriter(System.out)); //TP
        template.process(data, new OutputStreamWriter(System.out), null); //TP
        template.process(data, new OutputStreamWriter(System.out), null, null); //TP
    }


}
