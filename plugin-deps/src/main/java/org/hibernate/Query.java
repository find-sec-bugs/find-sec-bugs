package org.hibernate;

import java.util.List;

public interface Query {
    List   list();
    Object uniqueResult();
}
