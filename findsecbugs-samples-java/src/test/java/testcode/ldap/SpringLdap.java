package testcode.ldap;

import org.springframework.ldap.core.DefaultNameClassPairMapper;
import org.springframework.ldap.core.DirContextProcessor;
import org.springframework.ldap.core.LdapEntryIdentificationContextMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.CountNameClassPairCallbackHandler;
import org.springframework.ldap.core.support.DefaultIncrementalAttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.SearchControls;

public class SpringLdap {

    public void queryVulnerableToInjection(LdapTemplate template, String jndiInjectMe, SearchControls searchControls, DirContextProcessor dirContextProcessor) throws NamingException {
        template.list(jndiInjectMe);
        template.list(jndiInjectMe, new DefaultNameClassPairMapper());
        template.list(jndiInjectMe, new CountNameClassPairCallbackHandler());

        template.lookup(jndiInjectMe);
        template.lookup(jndiInjectMe, new DefaultIncrementalAttributesMapper());
        template.lookup(jndiInjectMe, new LdapEntryIdentificationContextMapper());

        template.search(jndiInjectMe,"dn=1",searchControls,new CountNameClassPairCallbackHandler());
        template.search(jndiInjectMe,"dn=1",searchControls,new DefaultIncrementalAttributesMapper(), dirContextProcessor);
        template.search(jndiInjectMe,"dn=1",searchControls,new LdapEntryIdentificationContextMapper(),dirContextProcessor);
        template.search(jndiInjectMe,"dn=1",searchControls,new CountNameClassPairCallbackHandler(),dirContextProcessor);
        template.search(jndiInjectMe,"dn=1",SearchControls.OBJECT_SCOPE,true,new CountNameClassPairCallbackHandler());
        template.search(jndiInjectMe,"dn=1",new CountNameClassPairCallbackHandler());
        template.search(jndiInjectMe,"dn=1",SearchControls.OBJECT_SCOPE,new String[0],new DefaultIncrementalAttributesMapper());
        template.search(jndiInjectMe,"dn=1",SearchControls.OBJECT_SCOPE,new DefaultIncrementalAttributesMapper());
        template.search(jndiInjectMe,"dn=1",new DefaultIncrementalAttributesMapper());
        template.search(jndiInjectMe,"dn=1",SearchControls.OBJECT_SCOPE,new String[0],new LdapEntryIdentificationContextMapper());
        template.search(jndiInjectMe,"dn=1",SearchControls.OBJECT_SCOPE,new LdapEntryIdentificationContextMapper());
        template.search(jndiInjectMe,"dn=1",new LdapEntryIdentificationContextMapper());
        template.search(jndiInjectMe,"dn=1",searchControls,new LdapEntryIdentificationContextMapper());
        template.search(jndiInjectMe,"dn=1",searchControls, new DefaultIncrementalAttributesMapper());
    }

    public void safeQuery(LdapTemplate template, SearchControls searchControls, DirContextProcessor dirContextProcessor) throws NamingException {
        String safeQuery = "uid=test";
        template.list(safeQuery);
        template.list(safeQuery, new DefaultNameClassPairMapper());
        template.list(safeQuery, new CountNameClassPairCallbackHandler());

        template.lookup(safeQuery);
        template.lookup(safeQuery, new DefaultIncrementalAttributesMapper());
        template.lookup(safeQuery, new LdapEntryIdentificationContextMapper());

        template.search(safeQuery,"dn=1",searchControls,new CountNameClassPairCallbackHandler());
        template.search(safeQuery,"dn=1",searchControls,new DefaultIncrementalAttributesMapper(), dirContextProcessor);
        template.search(safeQuery,"dn=1",searchControls,new LdapEntryIdentificationContextMapper(),dirContextProcessor);
        template.search(safeQuery,"dn=1",searchControls,new CountNameClassPairCallbackHandler(),dirContextProcessor);
        template.search(safeQuery,"dn=1",SearchControls.OBJECT_SCOPE,true,new CountNameClassPairCallbackHandler());
        template.search(safeQuery,"dn=1",new CountNameClassPairCallbackHandler());
        template.search(safeQuery,"dn=1",SearchControls.OBJECT_SCOPE,new String[0],new DefaultIncrementalAttributesMapper());
        template.search(safeQuery,"dn=1",SearchControls.OBJECT_SCOPE,new DefaultIncrementalAttributesMapper());
        template.search(safeQuery,"dn=1",new DefaultIncrementalAttributesMapper());
        template.search(safeQuery,"dn=1",SearchControls.OBJECT_SCOPE,new String[0],new LdapEntryIdentificationContextMapper());
        template.search(safeQuery,"dn=1",SearchControls.OBJECT_SCOPE,new LdapEntryIdentificationContextMapper());
        template.search(safeQuery,"dn=1",new LdapEntryIdentificationContextMapper());
        template.search(safeQuery,"dn=1",searchControls,new LdapEntryIdentificationContextMapper());
        template.search(safeQuery,"dn=1",searchControls, new DefaultIncrementalAttributesMapper());
    }
}
