package org.hibernate.criterion;

import org.hibernate.type.Type;

public class Restrictions {
    public static Criterion sqlRestriction(String sql) {
        return null;
    }

    public static Criterion sqlRestriction(String sql,Object[] values, Type[] types) {
        return null;
    }

    public static Criterion sqlRestriction(String sql,Object value, Type types) {
        return null;
    }
}
