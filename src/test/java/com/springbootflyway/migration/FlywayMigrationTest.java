package com.springbootflyway.migration;

import com.springbootflyway.repository.DbContainerBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FlywayMigrationTest extends DbContainerBaseTest {

  @Test
  public void shouldHaveBaselineInPlace() throws SQLException {
    var resultSet = performQuery("SELECT count(*) from USERS;");

    assertThat(resultSet.getInt(1)).isEqualTo(0);
    var tableSet =
        getConnection().getMetaData().getTables(null, null, "USERS", new String[] {"TABLE"});

    assertThat(tableSet.next()).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"USERS", "CONTACT"})
  public void shouldHaveTablesInPlace(String tableName) throws SQLException {
    var tableSet =
        getConnection().getMetaData().getTables(null, null, tableName, new String[] {"TABLE"});

    assertThat(tableSet.next()).isTrue();
  }
}
