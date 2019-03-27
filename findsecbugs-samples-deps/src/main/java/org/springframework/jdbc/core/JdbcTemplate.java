package org.springframework.jdbc.core;

/**
 * Class is not abstract in the real API. It was add to avoid creating empty method.
 * The bytecode for method call should be the same.
 */
public abstract class JdbcTemplate implements JdbcOperations {

}
