package org.apache.commons.collections4.comparators;

public interface Transformer<I, O> {
    O transform(I var1);
}