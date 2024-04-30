package jakarta.servlet;

public interface ServletContext {

    RequestDispatcher getRequestDispatcher(String path);
}
