package com.springbootflyway.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbContainerBaseTest {

  @Container
  protected static MySQLContainer mySQLContainer =
      new MySQLContainer("mysql:8.0")
          .withDatabaseName("userDb")
          .withUsername("test")
          .withPassword("test");

  static {
    mySQLContainer.start();
  }

  @DynamicPropertySource
  public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
  }

  protected ResultSet performQuery(String sql) throws SQLException {
    var statement = getDataSource(mySQLContainer).getConnection().createStatement();
    statement.execute(sql);
    var resultSet = statement.getResultSet();
    resultSet.next();
    return resultSet;
  }

  protected Connection getConnection() throws SQLException {
    return getDataSource(mySQLContainer).getConnection();
  }

  private DataSource getDataSource(JdbcDatabaseContainer<?> container) {
    var hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(container.getJdbcUrl());
    hikariConfig.setUsername(container.getUsername());
    hikariConfig.setPassword(container.getPassword());
    hikariConfig.setDriverClassName(container.getDriverClassName());
    return new HikariDataSource(hikariConfig);
  }
}
