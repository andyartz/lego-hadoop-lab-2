package examples;

import com.klarna.hiverunner.HiveShell;
import com.klarna.hiverunner.StandaloneHiveRunner;
import com.google.common.collect.Sets;
import com.klarna.hiverunner.annotations.HiveSQL;
import com.rwbsystems.HiveUnitTesting.HiveShellUtility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RunWith(StandaloneHiveRunner.class)
public class CreateDatabaseGenericHqlTest {
    @HiveSQL(files = {}, autoStart = false)
    private HiveShell shell;
    private HiveShellUtility shellUtil = new HiveShellUtility();

    private String scriptUnderTest = "examples/create_database_generic.hql";

    private String testDatabaseName = "mytestdb";
    private String testDatabaseComment = "A temporary database for automated testing.";

    @Before
    public void startShell() {
        shell.setHiveVarValue("new_db_name", testDatabaseName);
        shell.setHiveVarValue("new_db_comment", testDatabaseComment);
        shell.start();
    }

    private void verifyNewDatabase(Set<String> expectedDatabasesAfter, String expectedComment) {
        Set<String> actualDatabasesAfter = this.getDatabaseList();

        // VALIDATE DB EXISTS //
        assertEquals(expectedDatabasesAfter, actualDatabasesAfter);
        assertTrue(
                "Database listing contains new database.",
                actualDatabasesAfter.contains(testDatabaseName));

        // VALIDATE THE COMMENT ON THE DB IS CORRECT //
        String actualComment = shell.executeQuery(String.format(
                "DESCRIBE DATABASE %s", testDatabaseName)
        ).get(0).split("\\t")[1];
        assertEquals(expectedComment, actualComment);
    }

    private void runHqlScriptUnderTest() {
        try {
            shellUtil.executeResourceHql(shell, scriptUnderTest);
        } catch (IOException e) {
            fail(String.format("Unable to load script under test: %s", scriptUnderTest));
        }
    }

    private Set<String> getDatabaseList() {
        return new HashSet<String>(this.shell.executeQuery("show databases"));
    }

    @Test
    public void testCreateDatabase_DoesntExist() {
        // SETUP //
        Set<String> expectedDatabasesAfter = Sets.newHashSet("default", testDatabaseName);

        // ACTION UNDER TEST //
        this.runHqlScriptUnderTest();

        // TEST VERIFICATIONS //
        this.verifyNewDatabase(expectedDatabasesAfter, testDatabaseComment);
    }

    @Test
    public void testCreateDatabase_AlreadyExists() {
        /***** TEST SETUP *****/
        Set<String> expectedDatabasesAfter = Sets.newHashSet("default", testDatabaseName);
        String alternateComment = "Another temporary database for automated testing.";
        String testTableName = "testtable1";

        this.shell.executeQuery(String.format("CREATE DATABASE %s COMMENT '%s'",
                testDatabaseName, alternateComment));
        verifyNewDatabase(expectedDatabasesAfter, alternateComment);
        this.shell.executeQuery(String.format("CREATE TABLE %s.%s (id INT)", testDatabaseName, testTableName));

        /***** ACTION UNDER TEST *****/
        this.runHqlScriptUnderTest();

        /***** VERIFICATION *****/
        this.verifyNewDatabase(expectedDatabasesAfter, alternateComment);
        // verify table testtable1 still exists //
        Set<String> actualTablesInDatabase = new HashSet<String>(
                this.shell.executeQuery(String.format("SHOW TABLES IN %s", testDatabaseName)));
        assertEquals(Sets.newHashSet(testTableName), actualTablesInDatabase);
    }
}
