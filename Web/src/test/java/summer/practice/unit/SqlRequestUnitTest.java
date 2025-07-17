package summer.practice.unit;

import org.junit.jupiter.api.Test;
import summer.practice.dto.SqlRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SqlRequestUnitTest {
    @Test
    void testSqlRequestGettersAndSetters() {
        SqlRequest request = new SqlRequest();

        String testQuery = "SELECT * FROM users";
        request.setSqlQuery(testQuery);

        assertEquals(testQuery, request.getSqlQuery());
    }

    @Test
    void testSqlRequestNullValue() {
        SqlRequest request = new SqlRequest();

        request.setSqlQuery(null);

        assertNull(request.getSqlQuery());
    }

    @Test
    void testSqlRequestEmptyString() {
        SqlRequest request = new SqlRequest();

        request.setSqlQuery("");

        assertEquals("", request.getSqlQuery());
    }
}
