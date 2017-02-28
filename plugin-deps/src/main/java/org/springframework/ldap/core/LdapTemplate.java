package org.springframework.ldap.core;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import java.util.List;

public class LdapTemplate implements LdapOperations {

    @Override
    public void search(Name base, String filter, SearchControls controls, NameClassPairCallbackHandler handler) throws NamingException {

    }

    @Override
    public void search(String base, String filter, SearchControls controls, NameClassPairCallbackHandler handler) throws NamingException {

    }

    @Override
    public void search(Name base, String filter, SearchControls controls, NameClassPairCallbackHandler handler, DirContextProcessor processor) throws NamingException {

    }

    @Override
    public <T> List<T> search(String base, String filter, SearchControls controls, AttributesMapper<T> mapper, DirContextProcessor processor) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(Name base, String filter, SearchControls controls, AttributesMapper<T> mapper, DirContextProcessor processor) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(String base, String filter, SearchControls controls, ContextMapper<T> mapper, DirContextProcessor processor) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(Name base, String filter, SearchControls controls, ContextMapper<T> mapper, DirContextProcessor processor) throws NamingException {
        return null;
    }

    @Override
    public void search(String base, String filter, SearchControls controls, NameClassPairCallbackHandler handler, DirContextProcessor processor) throws NamingException {

    }

    @Override
    public void search(Name base, String filter, int searchScope, boolean returningObjFlag, NameClassPairCallbackHandler handler) throws NamingException {

    }

    @Override
    public void search(String base, String filter, int searchScope, boolean returningObjFlag, NameClassPairCallbackHandler handler) throws NamingException {

    }

    @Override
    public void search(Name base, String filter, NameClassPairCallbackHandler handler) throws NamingException {

    }

    @Override
    public void search(String base, String filter, NameClassPairCallbackHandler handler) throws NamingException {

    }

    @Override
    public <T> List<T> search(Name base, String filter, int searchScope, String[] attrs, AttributesMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(String base, String filter, int searchScope, String[] attrs, AttributesMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(Name base, String filter, int searchScope, AttributesMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(String base, String filter, int searchScope, AttributesMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(Name base, String filter, AttributesMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(String base, String filter, AttributesMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(Name base, String filter, int searchScope, String[] attrs, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(String base, String filter, int searchScope, String[] attrs, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(Name base, String filter, int searchScope, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(String base, String filter, int searchScope, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(Name base, String filter, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(String base, String filter, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(String base, String filter, SearchControls controls, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(Name base, String filter, SearchControls controls, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(String base, String filter, SearchControls controls, AttributesMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> search(Name base, String filter, SearchControls controls, AttributesMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public void list(String base, NameClassPairCallbackHandler handler) throws NamingException {

    }

    @Override
    public void list(Name base, NameClassPairCallbackHandler handler) throws NamingException {

    }

    @Override
    public <T> List<T> list(String base, NameClassPairMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> list(Name base, NameClassPairMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public List<String> list(String base) throws NamingException {
        return null;
    }

    @Override
    public List<String> list(Name base) throws NamingException {
        return null;
    }

    @Override
    public void listBindings(String base, NameClassPairCallbackHandler handler) throws NamingException {

    }

    @Override
    public void listBindings(Name base, NameClassPairCallbackHandler handler) throws NamingException {

    }

    @Override
    public <T> List<T> listBindings(String base, NameClassPairMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> listBindings(Name base, NameClassPairMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public List<String> listBindings(String base) throws NamingException {
        return null;
    }

    @Override
    public List<String> listBindings(Name base) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> listBindings(String base, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> List<T> listBindings(Name base, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public Object lookup(Name dn) throws NamingException {
        return null;
    }

    @Override
    public Object lookup(String dn) throws NamingException {
        return null;
    }

    @Override
    public <T> T lookup(String dn, AttributesMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> T lookup(String dn, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> T lookup(String dn, String[] attributes, AttributesMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> T lookup(String dn, String[] attributes, ContextMapper<T> mapper) throws NamingException {
        return null;
    }

    @Override
    public <T> T searchForObject(String base, String filter, SearchControls searchControls, ContextMapper<T> mapper) {
        return null;
    }

    @Override
    public <T> T searchForObject(String base, String filter, ContextMapper<T> mapper) {
        return null;
    }
}
