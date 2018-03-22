package org.springframework.boot;

import java.io.IOException;

public interface CommandLineRunner {

    void run(String... args);
}
