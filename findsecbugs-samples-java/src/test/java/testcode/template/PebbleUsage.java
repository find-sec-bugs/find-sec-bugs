package testcode.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

public class PebbleUsage {
    public void simple(String inputFile) throws IOException {

        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getLiteralTemplate(inputFile);

        Map<String, Object> context = new HashMap<>();
        context.put("name", "Shivam");

        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);

        String output = writer.toString();
    }

    public void allSignatures(String inputFile) throws IOException {
        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getLiteralTemplate(inputFile);

        Map<String, Object> data = new HashMap<String, Object>();
        compiledTemplate.evaluate(new StringWriter(), data, Locale.US);
    }
}
