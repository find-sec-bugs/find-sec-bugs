package org.springframework.ldap.core;

import javax.naming.Binding;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import java.util.List;

public interface LdapOperations {

//    <T> T executeReadWrite(ContextExecutor<T> ce) throws NamingException;

    void search(Name base, String filter, SearchControls controls, NameClassPairCallbackHandler handler)
            throws NamingException;

    void search(String base, String filter, SearchControls controls, NameClassPairCallbackHandler handler)
            throws NamingException;

    void search(Name base, String filter, SearchControls controls, NameClassPairCallbackHandler handler,
                DirContextProcessor processor) throws NamingException;

    <T> List<T> search(String base, String filter, SearchControls controls, AttributesMapper<T> mapper,
                       DirContextProcessor processor) throws NamingException;

    <T> List<T> search(Name base, String filter, SearchControls controls, AttributesMapper<T> mapper,
                       DirContextProcessor processor) throws NamingException;

    <T> List<T> search(String base, String filter, SearchControls controls, ContextMapper<T> mapper, DirContextProcessor processor)
            throws NamingException;

    <T> List<T> search(Name base, String filter, SearchControls controls, ContextMapper<T> mapper, DirContextProcessor processor)
            throws NamingException;

    void search(String base, String filter, SearchControls controls, NameClassPairCallbackHandler handler,
                DirContextProcessor processor) throws NamingException;

    void search(Name base, String filter, int searchScope, boolean returningObjFlag,
                NameClassPairCallbackHandler handler) throws NamingException;

    void search(String base, String filter, int searchScope, boolean returningObjFlag,
                NameClassPairCallbackHandler handler) throws NamingException;

    void search(Name base, String filter, NameClassPairCallbackHandler handler) throws NamingException;

    void search(String base, String filter, NameClassPairCallbackHandler handler) throws NamingException;

    <T> List<T> search(Name base, String filter, int searchScope, String[] attrs, AttributesMapper<T> mapper)
            throws NamingException;

    <T> List<T> search(String base, String filter, int searchScope, String[] attrs, AttributesMapper<T> mapper)
            throws NamingException;

    <T> List<T> search(Name base, String filter, int searchScope, AttributesMapper<T> mapper) throws NamingException;

    <T> List<T> search(String base, String filter, int searchScope, AttributesMapper<T> mapper) throws NamingException;

    <T> List<T> search(Name base, String filter, AttributesMapper<T> mapper) throws NamingException;

    <T> List<T> search(String base, String filter, AttributesMapper<T> mapper) throws NamingException;

    <T> List<T> search(Name base, String filter, int searchScope, String[] attrs, ContextMapper<T> mapper) throws NamingException;

    <T> List<T> search(String base, String filter, int searchScope, String[] attrs, ContextMapper<T> mapper)
            throws NamingException;

    <T> List<T> search(Name base, String filter, int searchScope, ContextMapper<T> mapper) throws NamingException;

    <T> List<T> search(String base, String filter, int searchScope, ContextMapper<T> mapper) throws NamingException;

    <T> List<T> search(Name base, String filter, ContextMapper<T> mapper) throws NamingException;

    <T> List<T> search(String base, String filter, ContextMapper<T> mapper) throws NamingException;

    <T> List<T> search(String base, String filter, SearchControls controls, ContextMapper<T> mapper) throws NamingException;

    <T> List<T> search(Name base, String filter, SearchControls controls, ContextMapper<T> mapper) throws NamingException;

    <T> List<T> search(String base, String filter, SearchControls controls, AttributesMapper<T> mapper) throws NamingException;

    <T> List<T> search(Name base, String filter, SearchControls controls, AttributesMapper<T> mapper) throws NamingException;

    void list(String base, NameClassPairCallbackHandler handler) throws NamingException;

    void list(Name base, NameClassPairCallbackHandler handler) throws NamingException;

    <T> List<T> list(String base, NameClassPairMapper<T> mapper) throws NamingException;

    <T> List<T> list(Name base, NameClassPairMapper<T> mapper) throws NamingException;

    List<String> list(String base) throws NamingException;

    List<String> list(Name base) throws NamingException;

    void listBindings(final String base, NameClassPairCallbackHandler handler) throws NamingException;

    void listBindings(final Name base, NameClassPairCallbackHandler handler) throws NamingException;

    <T> List<T> listBindings(String base, NameClassPairMapper<T> mapper) throws NamingException;

    <T> List<T> listBindings(Name base, NameClassPairMapper<T> mapper) throws NamingException;

    List<String> listBindings(final String base) throws NamingException;

    List<String> listBindings(final Name base) throws NamingException;

    <T> List<T> listBindings(String base, ContextMapper<T> mapper) throws NamingException;

    <T> List<T> listBindings(Name base, ContextMapper<T> mapper) throws NamingException;

    Object lookup(Name dn) throws NamingException;

    Object lookup(String dn) throws NamingException;

    <T> T lookup(String dn, AttributesMapper<T> mapper) throws NamingException;

    <T> T lookup(String dn, ContextMapper<T> mapper) throws NamingException;

    <T> T lookup(String dn, String[] attributes, AttributesMapper<T> mapper) throws NamingException;

    <T> T lookup(String dn, String[] attributes, ContextMapper<T> mapper) throws NamingException;

//    void modifyAttributes(String dn, ModificationItem[] mods) throws NamingException;
//
//    void bind(Name dn, Object obj, Attributes attributes) throws NamingException;
//
//    void bind(String dn, Object obj, Attributes attributes) throws NamingException;
//
//    void unbind(Name dn) throws NamingException;
//
//    void unbind(String dn) throws NamingException;
//
//    void unbind(String dn, boolean recursive) throws NamingException;
//
//    void rebind(String dn, Object obj, Attributes attributes) throws NamingException;
//
//    void rename(final String oldDn, final String newDn) throws NamingException;
//
//    DirContextOperations lookupContext(String dn) throws NamingException, ClassCastException;

//    boolean authenticate(String base, String filter, String password);
//
//    boolean authenticate(String base, String filter, String password, AuthenticatedLdapEntryContextCallback callback);
//
//
//    boolean authenticate(String base, String filter, String password,
//                         AuthenticatedLdapEntryContextCallback callback,
//                         AuthenticationErrorCallback errorCallback);
//
//    boolean authenticate(String base, String filter, String password,
//                         AuthenticationErrorCallback errorCallback);

    <T> T searchForObject(String base, String filter, SearchControls searchControls, ContextMapper<T> mapper);

    <T> T searchForObject(String base, String filter, ContextMapper<T> mapper);

}
