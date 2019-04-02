package testcode.ldap;

import javax.naming.directory.SearchControls;

public class LdapEntryPoisoning {

    private int scope;
    private int countLimit;
    private int timeLimit;
    private String[] attributes;
    private boolean deref;

    public void unsafe1() {
        new SearchControls(scope,
                countLimit, timeLimit,
                attributes,
                true, //!! It will flag line 14 ... the beginning of the call
                deref);
    }

    public void unsafe2() {
        SearchControls ctrl = new SearchControls();
        ctrl.setReturningObjFlag(true); //!!
    }

    public void safe1() {
        new SearchControls(scope,
                countLimit, timeLimit,
                attributes,
                false, //OK
                deref);
    }

    public void safe2() {
        SearchControls ctrl = new SearchControls();
        ctrl.setReturningObjFlag(false); //OK
    }

}
