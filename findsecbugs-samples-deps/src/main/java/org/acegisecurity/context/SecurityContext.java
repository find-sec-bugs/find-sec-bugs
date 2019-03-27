package org.acegisecurity.context;


import org.acegisecurity.Authentication;

import java.io.Serializable;

public interface SecurityContext extends Serializable {

    Authentication getAuthentication();

    void setAuthentication(Authentication var1);
}