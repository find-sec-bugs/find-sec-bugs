package testcode.gadget.cachedata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FileCacheData implements CacheData {

    public String filePath;
    public String value;

    public FileCacheData(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void refresh() {

        try {
            value = readFile(filePath);
        } catch (IOException e) {
        }
    }

    private String readFile(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner( new File(filePath) );
        return scanner.useDelimiter("\\A").next();
    }

    @Override
    public String getValue() {
        return value;
    }
}
