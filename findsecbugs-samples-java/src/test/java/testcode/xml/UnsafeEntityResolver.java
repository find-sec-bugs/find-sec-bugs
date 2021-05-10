package testcode.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class UnsafeEntityResolver implements EntityResolver {

    private final EntityResolverConsumer consumer = new EntityResolverConsumer();

    public UnsafeEntityResolver(UnsafeEntityResolver entityResolver) {
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        return null;
    }

    public void methodWithArgument(UnsafeEntityResolver entityResolver) {

    }

    public void methodUsingThisReference() {
        consumer.accept(this);
        consumer.accept("a", this, "b");
    }

    public void methodUsingResolveEntityMethod() {
        resolveEntity("", "");
    }

    public static final class EntityResolverConsumer {

        public void accept(EntityResolver entityResolver) {

        }

        public void accept(Object a, EntityResolver entityResolver, Object b) {

        }
    }
}