package org.apache.commons.fileupload.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ServletFileUpload {
    public ServletFileUpload(DiskFileItemFactory diskFileItemFactory) {

    }

    public List<FileItem> parseRequest(HttpServletRequest req) {
        return null;
    }
}
