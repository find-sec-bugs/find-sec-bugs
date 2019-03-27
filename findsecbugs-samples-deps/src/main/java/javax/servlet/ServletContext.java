package javax.servlet;

public interface ServletContext {

    RequestDispatcher getRequestDispatcher(String path);
}
