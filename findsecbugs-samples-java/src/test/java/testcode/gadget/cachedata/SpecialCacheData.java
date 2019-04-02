package testcode.gadget.cachedata;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SpecialCacheData implements CacheData {

    String command;
    String value;

    public SpecialCacheData(String command) {
        this.command = command;
    }

    @Override
    public void refresh() {
        value = executeCommand(command);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String executeCommand(String command, int arg2, long arg3) {
        return executeCommand(command);
    }

    private String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }
}
