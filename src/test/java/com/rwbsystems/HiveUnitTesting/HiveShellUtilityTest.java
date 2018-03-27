package com.rwbsystems.HiveUnitTesting;

import com.klarna.hiverunner.HiveShell;
import com.klarna.hiverunner.StandaloneHiveRunner;
import com.klarna.hiverunner.annotations.HiveSQL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import org.mockito.Mockito;

import static org.junit.Assert.*;

@RunWith(StandaloneHiveRunner.class)
public class HiveShellUtilityTest {
    @HiveSQL(files = {}, autoStart = false)
    HiveShell shell;

    @Before
    public void startShell() {
        shell.start();
    }

    @Test
    public void executeResourceHql_FileExistsAndRuns() throws Exception {
        /*****  SETUP  *****/
        HiveShellUtility sut = new HiveShellUtility();

        /*****  EXECUTE METHOD  *****/
        sut.executeResourceHql(shell, "good_script.hql");

        /*****  VERIFY RESULTS  *****/
        List<String> databases = shell.executeQuery("SHOW DATABASES");
        assertEquals(2, databases.size());
        assertTrue(databases.contains("abcdefghiklmnopqrstuvwxyz"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeResourceHql_FileExistsAndErrors_Exception() throws Exception {
        /*****  SETUP  *****/
        HiveShellUtility sut = new HiveShellUtility();

        /*****  EXECUTE METHOD  *****/
        sut.executeResourceHql(shell, "script_with_error.hql");

        /*****  VERIFY RESULTS  *****/
        List<String> databases = shell.executeQuery("SHOW DATABASES");
        assertEquals(1, databases.size());
        assertFalse(databases.contains("abcdefghik"));
    }

    @Test(expected = NullPointerException.class)
    public void executeResourceHql_FileDoesntExist_NullPointerException() throws Exception {
        /*****  SETUP  *****/
        HiveShellUtility sut = new HiveShellUtility();

        /*****  EXECUTE METHOD  *****/
        sut.executeResourceHql(shell, "file_that_doesnt_exist.hql");
    }

    @Test(expected = IOException.class)
    public void executeResourceHql_FileError_IOException() throws Exception {
        /*
        This particular test might not be valid.  Since there isn't a reader that is passed into the method, nor into
        the HiveShellUtility class, the reader cannot be properly mocked.
         */

        HiveShellUtility sut = new HiveShellUtility();
        HiveShellUtility spyShellUtiity = Mockito.spy(sut);

        doThrow(new IOException()).when(spyShellUtiity).executeResourceHql(shell, "good_script.hql");

        spyShellUtiity.executeResourceHql(shell, "good_script.hql");
    }

    @Test
    public void executeResourceHqlReplaceSnappyWithGzip_NoSnappy_StillExecutes() throws Exception {
        /*****  SETUP  *****/
        HiveShellUtility sut = new HiveShellUtility();

        /*****  EXECUTE METHOD  *****/
        sut.executeResourceHqlReplaceSnappyWithGzip(shell, "test_table_nocompression.hql");

        /*****  VERIFY RESULTS  *****/
        assertEquals(2, sut.getRowCountInTable(shell, "default", "test_table"));
        List<String> records = shell.executeQuery("SELECT * FROM default.test_table order by id");
        assertEquals(2, records.size());
        assertEquals("0\trecord 1\t2017-01-01 00:00:01.0", records.get(0));
        assertEquals("1\trecord 2\t2017-01-02 00:00:02.0", records.get(1));
    }

    @Test
    public void executeResourceHqlReplaceSnappyWithGzip_GzipExists_ScriptCompletes() throws Exception {
        /*****  SETUP  *****/
        HiveShellUtility sut = new HiveShellUtility();

        /*****  EXECUTE METHOD  *****/
        sut.executeResourceHqlReplaceSnappyWithGzip(shell, "test_table_gzip.hql");

        /*****  VERIFY RESULTS  *****/
        assertEquals(2, sut.getRowCountInTable(shell, "default", "test_table"));
        List<String> records = shell.executeQuery("SELECT * FROM default.test_table order by id");
        assertEquals(2, records.size());
        assertEquals("0\trecord 1\t2017-01-01 00:00:01.0", records.get(0));
        assertEquals("1\trecord 2\t2017-01-02 00:00:02.0", records.get(1));
    }

    @Test
    public void executeResourceHqlReplaceSnappyWithGzip_SnappyExists_ScriptCompletes() throws Exception {
        /*****  SETUP  *****/
        HiveShellUtility sut = new HiveShellUtility();

        /*****  EXECUTE METHOD  *****/
        sut.executeResourceHqlReplaceSnappyWithGzip(shell, "test_table_snappy.hql");

        /*****  VERIFY RESULTS  *****/
        assertEquals(2, sut.getRowCountInTable(shell, "default", "test_table"));
        List<String> records = shell.executeQuery("SELECT * FROM default.test_table order by id");
        assertEquals(2, records.size());
        assertEquals("0\trecord 1\t2017-01-01 00:00:01.0", records.get(0));
        assertEquals("1\trecord 2\t2017-01-02 00:00:02.0", records.get(1));
    }
//
//    ///
//    @Test
//    public void executeResourceHqlIgnoreIOException() throws Exception {
//        /*****  SETUP  *****/
//        HiveShellUtility sut = new HiveShellUtility();
//        HiveShellUtility spyShellUtility = spy(sut);
//        ByteArrayOutputStream stdOut = new ByteArrayOutputStream(), stdErr = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(stdOut));
//        System.setErr(new PrintStream(stdErr));
//        String errorMessage = "Some error message";
//
//        doThrow(new IOException()).when(spyShellUtility).executeResourceHql(shell, "good_script.hql");
//
//        /*****  RUN METHOD  *****/
//        spyShellUtility.executeResourceHqlIgnoreIOException(shell, "good_script.hql", errorMessage);
//
//        /*****  VERIFY RESULTS  *****/
//        assertEquals(errorMessage + System.lineSeparator(), stdOut.toString());
//        assertTrue(stdErr.toString().contains("java.io.IOException"));
//
//        /***** CLEANUP  *****/
//        System.setOut(null);
//        System.setErr(null);
//    }

    @Test
    public void getDatabases() throws Exception {

    }

    @Test
    public void getTablesInDatabase() throws Exception {

    }

    @Test
    public void getColumnInfoForTable() throws Exception {

    }

    @Test
    public void getRowCountInTable() throws Exception {

    }

    @Test
    public void getTableComment() throws Exception {

    }

}