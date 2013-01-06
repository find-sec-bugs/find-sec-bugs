package org.hibernate;

import org.hibernate.criterion.Criterion;

public interface Criteria {

    Criteria add(Criterion criterion);
}
