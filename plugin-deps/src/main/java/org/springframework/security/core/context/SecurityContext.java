package org.springframework.security.core.context;


import org.springframework.security.core.Authentication;

import java.io.Serializable;

public interface SecurityContext extends Serializable {

    Authentication getAuthentication();

    void setAuthentication(Authentication var1);
}
