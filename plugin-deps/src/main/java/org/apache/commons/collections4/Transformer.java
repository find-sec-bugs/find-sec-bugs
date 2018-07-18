package org.apache.commons.collections4;

public interface Transformer<I, O> {
    O transform(I var1);
}