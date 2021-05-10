package testcode.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public final class SafeEntityResolver implements EntityResolver {

    private final EntityResolverConsumer consumer = new EntityResolverConsumer();
    private final EntityResolver entityResolver = (a, b) -> null;

    public SafeEntityResolver(SafeEntityResolver entityResolver) {
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        return null;
    }

    public void methodWithArgument(SafeEntityResolver entityResolver) {

    }

    public void methodUsingThisReference() {
        consumer.accept(this);
    }

    public void methodUsingField() {
        consumer.accept(entityResolver);
    }

    public void methodUsingLocalVariable() {
        EntityResolver localEntityResolver = (a, b) -> null;
        consumer.accept(localEntityResolver);
    }

    public void methodUsingNewInstance() {
        consumer.accept((a, b) -> null);
    }

    public void methodUsingResolveEntityMethod() {
        resolveEntity("", "");
    }

    public static final class EntityResolverConsumer {

        public void accept(EntityResolver entityResolver) {

        }
    }
}