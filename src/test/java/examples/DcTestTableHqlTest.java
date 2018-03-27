package examples;

import com.rwbsystems.HiveUnitTesting.TestTemplates.TableTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DcTestTableHqlTest extends TableTestBase {

    public DcTestTableHqlTest() {
        super("examples/dc_test_table.hql", "my_test_db", "test_table");
    }

    @Override
    public void setupTestEnvironment() {
        shell.setHiveVarValue("db_name", "my_test_db");
        super.setupTestEnvironment();
    }

    @Override
    public void setupExpectedHiveColumnInfos() {
        int columnPosition = 0;
        putHiveColumnInfo(columnPosition++, "id", "int", "id comment");
        putHiveColumnInfo(columnPosition++, "col1", "string", "col1 comment");
        putHiveColumnInfo(columnPosition, "col2", "TIMESTAMP", "col2 comment");
    }

    @Override
    public void initializeExpectedTableComment() {
        setExpectedTableComment(null);
    }

    @Test
    public void insertAndSelectFromTable_5Records() {

        shellUtil.executeResourceHqlIgnoreIOException(shell, scriptUnderTest, "Unable to create test_table!");

        shellUtil.executeResourceHqlIgnoreIOException(
                shell,
                "examples/data_scripts/populate_test_table_01.hql",
                "Unable to populate test_table with data.");

        assertEquals(5, shellUtil.getRowCountInTable(shell, databaseForExpectedTable, expectedTableName));
    }

}
