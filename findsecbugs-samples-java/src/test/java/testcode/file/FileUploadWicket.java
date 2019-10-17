package testcode.file;

import org.apache.wicket.util.upload.FileItem;
import org.apache.wicket.util.upload.FileUploadException;
import org.apache.wicket.util.upload.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FileUploadWicket {
    public void handleFile(HttpServletRequest req) throws FileUploadException {
        ServletFileUpload upload = new ServletFileUpload();
        List<FileItem> fileItems = upload.parseRequest(req);

        for (FileItem item : fileItems) {
            System.out.println("Saving " + item.getName() + "...");
        }
    }
}
