package org.hibernate;

public interface Session {
    Criteria createCriteria(Class userEntityClass);

    Query createQuery(String hqlQuery);

    SQLQuery createSQLQuery(String sqlQuery);
}
