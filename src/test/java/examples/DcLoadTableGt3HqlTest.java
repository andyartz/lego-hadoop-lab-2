package examples;

import com.google.common.collect.Lists;
import com.klarna.hiverunner.StandaloneHiveRunner;
import com.rwbsystems.HiveUnitTesting.TestTemplates.TableTestBase;
import org.junit.runner.RunWith;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(StandaloneHiveRunner.class)
public class DcLoadTableGt3HqlTest extends TableTestBase {
    String databaseUnderTest = null;
    String tableUnderTest = null;

    public DcLoadTableGt3HqlTest() {
        super("examples/dc_load_table_gt3.hql",
                "default2",
                "table_gt3");
        this.databaseUnderTest = "default2";
        this.tableUnderTest = "table_gt3";
    }

    @Override
    public void setupExpectedHiveColumnInfos() {
        int columnPosition = 0;
        putHiveColumnInfo(columnPosition++, "id", "int", "");
        putHiveColumnInfo(columnPosition++, "col1", "string", "");
        putHiveColumnInfo(columnPosition, "col2", "TIMESTAMP", "");
    }

    @Override
    public void initializeExpectedTableComment() {
        setExpectedTableComment("A table with ids greater than 3.");
    }

    @Override
    public void setupTestEnvironment() {
        shell.setHiveVarValue("db_name", databaseUnderTest);
        super.setupTestEnvironment();

        shellUtil.executeResourceHqlIgnoreIOException(shell,
                "examples/dc_test_table.hql",
                String.format("Unable to setup %s.%s!", databaseUnderTest, tableUnderTest));
        shellUtil.executeResourceHqlIgnoreIOException(shell,
                "examples/data_scripts/populate_test_table_01.hql",
                String.format("Unable to populate %s.%s", databaseUnderTest, tableUnderTest));
    }

    @Override
    protected void assertRecordCount() {
        assertEquals(2,
                shellUtil.getRowCountInTable(shell, databaseForExpectedTable, expectedTableName));
    }

    @Test
    public void testValidRecordsLoaded_Id1Through5_2Records() {
        /*****  SETUP  *****/
        List<String> expectedRowData = Lists.newArrayList(
                "4\trecord 4\t2017-01-04 00:00:00.0",
                "5\trecord 5\t2017-01-05 00:00:00.0");

        /*****  EXECUTE SCRIPT UNDER TEST  *****/
        try {
            shellUtil.executeResourceHql(shell, scriptUnderTest);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unable to run script under test in testValidRecordsLoaded_Id1Through5_2Records()!");
        }

        /*****  VERIFY RESULTS  *****/
        List<String> actualRowData =
                shell.executeQuery(String.format("SELECT * FROM %s.%s ORDER BY id", databaseUnderTest, tableUnderTest));
        assertEquals(expectedRowData, actualRowData);
    }
}
