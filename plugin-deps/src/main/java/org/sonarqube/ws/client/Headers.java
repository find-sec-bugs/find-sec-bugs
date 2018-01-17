package org.sonarqube.ws.client;

import java.util.Set;

public interface Headers {

    java.util.Optional<String> getValue(String name);

    Set<String> getNames();
}
