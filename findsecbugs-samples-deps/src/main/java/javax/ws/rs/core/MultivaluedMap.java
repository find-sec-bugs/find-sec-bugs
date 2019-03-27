package javax.ws.rs.core;

import java.util.List;
import java.util.Map;

public interface MultivaluedMap<K,V> extends Map<K,List<V>> {
    V getFirst(K key);
    //TODO: Test tainted input from MultivaluedMap in java-ee.txt
}
