package org.acegisecurity;

import org.acegisecurity.context.GrantedAuthority;

import java.io.Serializable;
import java.security.Principal;

public interface Authentication extends Principal, Serializable {
    GrantedAuthority[] getAuthorities();

    Object getCredentials();

    Object getDetails();

    Object getPrincipal();

    boolean isAuthenticated();

    void setAuthenticated(boolean var1) throws IllegalArgumentException;
}