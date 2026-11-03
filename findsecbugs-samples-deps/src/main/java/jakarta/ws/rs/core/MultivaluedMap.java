package jakarta.ws.rs.core;

import java.util.List;
import java.util.Map;

public interface MultivaluedMap<K,V> extends Map<K,List<V>> {
    V getFirst(K key);
}
