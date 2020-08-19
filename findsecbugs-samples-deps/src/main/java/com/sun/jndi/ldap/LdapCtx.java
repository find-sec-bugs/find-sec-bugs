package com.sun.jndi.ldap;


import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LdapCtx {

    public NamingEnumeration<SearchResult> search(Name name, String filter, SearchControls cons) throws NamingException {
        return null;
    }

    public NamingEnumeration<SearchResult> search(Name name, String filterExpr, Object[] filterArgs, SearchControls cons) throws NamingException {
        return null;
    }

    public NamingEnumeration<SearchResult> search(String name, String filter, SearchControls cons) throws NamingException {
        return null;
    }

    public NamingEnumeration<SearchResult> search(String name, String filterExpr, Object[] filterArgs, SearchControls cons) throws NamingException {
        return null;
    }

}
