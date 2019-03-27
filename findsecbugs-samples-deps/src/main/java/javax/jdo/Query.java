package javax.jdo;

public interface Query {
    void setFilter(String filter);

    void declareParameters(String parameters);

    void setGrouping(String field);
}
