package org.apache.commons.io.input;

import java.io.*;
import java.lang.reflect.Proxy;

/**
 * A special ObjectInputStream that loads a class based on a specified
 * <code>ClassLoader</code> rather than the system default.
 * <p>
 * This is useful in dynamic container environments.
 *
 * @since 1.1
 */
public class ClassLoaderObjectInputStream extends ObjectInputStream {

    /**
     * The class loader to use.
     */
    private final ClassLoader classLoader;

    /**
     * Constructs a new ClassLoaderObjectInputStream.
     *
     * @param classLoader the ClassLoader from which classes should be loaded
     * @param inputStream the InputStream to work on
     * @throws IOException              in case of an I/O error
     * @throws StreamCorruptedException if the stream is corrupted
     */
    public ClassLoaderObjectInputStream(
            final ClassLoader classLoader, final InputStream inputStream)
            throws IOException, StreamCorruptedException {
        super(inputStream);
        this.classLoader = classLoader;
    }

    /**
     * Resolve a class specified by the descriptor using the
     * specified ClassLoader or the super ClassLoader.
     *
     * @param objectStreamClass descriptor of the class
     * @return the Class object described by the ObjectStreamClass
     * @throws IOException            in case of an I/O error
     * @throws ClassNotFoundException if the Class cannot be found
     */
    @Override
    protected Class<?> resolveClass(final ObjectStreamClass objectStreamClass)
            throws IOException, ClassNotFoundException {

        try {
            return Class.forName(objectStreamClass.getName(), false, classLoader);
        } catch (final ClassNotFoundException cnfe) {
            // delegate to super class loader which can resolve primitives
            return super.resolveClass(objectStreamClass);
        }
    }

    /**
     * Create a proxy class that implements the specified interfaces using
     * the specified ClassLoader or the super ClassLoader.
     *
     * @param interfaces the interfaces to implement
     * @return a proxy class implementing the interfaces
     * @throws IOException            in case of an I/O error
     * @throws ClassNotFoundException if the Class cannot be found
     * @see java.io.ObjectInputStream#resolveProxyClass(java.lang.String[])
     * @since 2.1
     */
    @Override
    protected Class<?> resolveProxyClass(final String[] interfaces) throws IOException,
            ClassNotFoundException {
        final Class<?>[] interfaceClasses = new Class[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            interfaceClasses[i] = Class.forName(interfaces[i], false, classLoader);
        }
        try {
            return Proxy.getProxyClass(classLoader, interfaceClasses);
        } catch (final IllegalArgumentException e) {
            return super.resolveProxyClass(interfaces);
        }
    }
}
