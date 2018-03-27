package com.rwbsystems.HiveUnitTesting.TestTemplates;

import com.google.common.collect.Sets;
import com.klarna.hiverunner.HiveShell;
import com.klarna.hiverunner.StandaloneHiveRunner;
import com.klarna.hiverunner.annotations.HiveSQL;
import com.rwbsystems.HiveUnitTesting.HiveColumnInfo;
import com.rwbsystems.HiveUnitTesting.HiveShellUtility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An abstract class that can be used to automatically handle basic drop/create test logic.  A class that wishes to
 * automatically run drop/create test logic, just inherits the class and implements the abstract methods.  Most methods
 * are public so customized logic can be implemented and overridden.
 */
@RunWith(StandaloneHiveRunner.class)
public abstract class TableTestBase {
    protected String scriptUnderTest = null; // this should be overwritten

    @HiveSQL(files = {}, autoStart = false)
    protected HiveShell shell;
    protected HiveShellUtility shellUtil = new HiveShellUtility();

    protected String expectedTableName = null;
    protected String expectedTableComment = null;
    protected String databaseForExpectedTable = "default"; // set as default in case developer doesn't care which DB

    protected Set<String> expectedTables = null;
    protected Map<String, HiveColumnInfo> expectedColumnInfos = new HashMap<String, HiveColumnInfo>();

    public TableTestBase(String scriptUnderTest, String databaseForExpectedTable, String expectedTableName) {
        this.scriptUnderTest = scriptUnderTest;
        this.databaseForExpectedTable = databaseForExpectedTable;
        this.expectedTableName = expectedTableName;
        this.expectedTables = Sets.newHashSet(expectedTableName);

        initializeExpectedTableComment();
        setupExpectedHiveColumnInfos();
    }

    /**
     * Sets up the test environment (variables, files, etc.), and starts the internal Hive shell.  This should be
     * overridden if the user would like to add custom variables, then can call {@code setupTestEnvironment()} within
     * the overridden method.
     */
    @Before
    public void setupTestEnvironment() {
        shell.start();

        shell.executeQuery(String.format("CREATE DATABASE IF NOT EXISTS %s", databaseForExpectedTable));
    }

    /**
     * Responsible for setting up the column information ({@code HiveColumnInfo objects}) for the table so it can be validated.
     */
    public abstract void setupExpectedHiveColumnInfos();

    /**
     * Sets the comment value that will be used to validate the Hive table's comment property (null for no comment).
     */
    public abstract void initializeExpectedTableComment();

    public void putHiveColumnInfo(int columnPosition, String columnName, String datatype, String comment) {
        expectedColumnInfos.put(columnName, new HiveColumnInfo(
                columnPosition++, columnName, datatype, comment));
    }

    protected void setExpectedTableComment(String expectedTableComment) {
        this.expectedTableComment = expectedTableComment;
    }

    protected Map<String, HiveColumnInfo> getExpectedColumnInfos() {
        return this.expectedColumnInfos;
    }

    protected void assertRecordCount() {
        assertEquals(0,
                shellUtil.getRowCountInTable(shell, databaseForExpectedTable, expectedTableName));
    }

    protected void assertColumnInfo() {
        Map<String, HiveColumnInfo> actualColumnInfos = shellUtil.getColumnInfoForTable(shell, databaseForExpectedTable, expectedTableName);
        assertEquals(expectedColumnInfos,
                shellUtil.getColumnInfoForTable(shell, databaseForExpectedTable, expectedTableName));
    }

    protected void assertTableExists(Set<String> actualTablesBefore) {
        Set<String> actualTables = shellUtil.getTablesInDatabase(shell, databaseForExpectedTable);
        actualTablesBefore.add(expectedTableName);

        assertEquals(actualTablesBefore, actualTables);
    }

    protected void assertTableComment() {
        String actualTableComment = shellUtil.getTableComment(shell, databaseForExpectedTable, expectedTableName);

        if(expectedTableComment == null || expectedTableComment.trim().isEmpty()) {
            assertEquals(
                    String.format("Table %s.%s does not have property: comment",
                            databaseForExpectedTable, expectedTableName),
                    actualTableComment);
        }
        else {
            assertEquals(expectedTableComment, actualTableComment);
        }
    }

    @Test
    public void testTableDoesntExist() {
        /*****  SETUP  *****/
        Set<String> actualTablesBefore =
                shellUtil.getTablesInDatabase(shell, databaseForExpectedTable);
        // table should not exist yet, so verify that //
        assertFalse(actualTablesBefore.contains(expectedTableName));

        /*****  EXECUTE SCRIPT UNDER TEST  *****/
        try {
            shellUtil.executeResourceHql(shell, scriptUnderTest);
        } catch(IOException ioe) {
            fail(String.format("Unable to run script under test: %s!", scriptUnderTest));
            ioe.printStackTrace();
        }

        /***** VERIFY RESULTS  *****/
        assertTableExists(actualTablesBefore);
        assertColumnInfo();
        assertRecordCount();
        assertTableComment();
    }

    @Test
    public void testTableAlreadyExists() {
        /*****   SETUP   *****/
        Set<String> actualTablesBefore =
                shellUtil.getTablesInDatabase(shell, databaseForExpectedTable);
        // table should not exist yet, so verify that //
        assertFalse(actualTablesBefore.contains(expectedTableName));

        // add the table so it exists in the database already with a different set of columns //
        shell.executeQuery(String.format("CREATE TABLE %s.%s (abcdefg INT, hijklm STRING)",
                databaseForExpectedTable, expectedTableName));

        // get list of tables again and verify it is already existing //
        assertTrue(shellUtil
                .getTablesInDatabase(shell, databaseForExpectedTable)
                .contains(expectedTableName)); // table should exist now

        /*****  EXECUTE SCRIPT UNDER TEST  *****/
        try {
            shellUtil.executeResourceHql(shell, scriptUnderTest);
        } catch(IOException ioe) {
            fail(String.format("Unable to run script under test: %s!", scriptUnderTest));
            ioe.printStackTrace();
        }

        /*****  VERIFY RESULTS  *****/
        assertTableExists(actualTablesBefore);
        assertColumnInfo();
        assertRecordCount();
        assertTableComment();
    }
}

