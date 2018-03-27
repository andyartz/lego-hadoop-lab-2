package com.rwbsystems.HiveUnitTesting;

import com.google.common.collect.Sets;
import com.klarna.hiverunner.HiveShell;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.*;

/**
 * Contains common functionality for interacting with the Hive shell.
 */
public class HiveShellUtility {

    /**
     * Executes the code of a single resource file in the {@code hiveShell}.
     * <p><i>PRECONDITION: {@code hiveShell} is already started and is running.</i></p>
     * @param runningShell A HiveShell that is already running.
     * @param resourceFilename The relative path for an included resource file.
     * @throws IOException If the resource file cannot be opened, an IOException is thrown.
     */
    public void executeResourceHql(HiveShell runningShell, String resourceFilename) throws IOException {
        runningShell.execute(
                IOUtils.toString(
                        HiveShellUtility.class.getClassLoader()
                                .getResourceAsStream(resourceFilename)
                )
        );
    }

    /**
     * Executes the code of a single resource file in the {@code hiveShell}, but if an exception
     * occurs, it is caught and the {@code exceptionMessage} is printed to standard output.
     * <p><i>PRECONDITION: {@code hiveShell} is already started and is running.</i></p>
     * @param runningShell A HiveShell that is already running.
     * @param resourceFilename The relative path for an included resource file.
     * @param exceptionMessage The message to be printed if an IOException is caught.
     */
    public void executeResourceHqlIgnoreIOException(HiveShell runningShell, String resourceFilename, String exceptionMessage) {
        try {
            executeResourceHql(runningShell, resourceFilename);
        } catch (IOException e) {
            System.out.println(exceptionMessage);
            e.printStackTrace();
        }
    }

    /**
     * Runs a Hive script, replacing usage of Snappy compression with Gzip because Snappy isn't supported in HiveRunner.
     * @param runningShell A HiveShell that is already running.
     * @param resourceFilename The relative path for an included resource file.
     * @throws IOException
     */
    public void executeResourceHqlReplaceSnappyWithGzip(HiveShell runningShell, String resourceFilename) throws IOException {
        runningShell.execute(
                IOUtils.toString(
                        HiveShellUtility.class.getClassLoader()
                                .getResourceAsStream(resourceFilename))
                        .replaceAll("(?i)mapreduce\\.output\\.fileoutputformat\\.compress\\.codec\\s*=\\s*org\\.apache\\.hadoop\\.io\\.compress\\.SnappyCodec", "mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.GzipCodec;")
                        .replaceAll("(?i)hive\\.exec\\.orc\\.default\\.compress\\s*=\\s*SNAPPY", "hive.exec.orc.default.compress = ZLIB")
                        .replaceAll("(?i)\"orc\\.compress\"\\s*=\\s*\"SNAPPY\"", "\"orc.compress\"=\"ZLIB\""));
    }

    /**
     * Gets the Set of database names listed in the running HiveShell instance.
     * @param runningShell A HiveShell that is already running.
     * @return A Set of unique database names in the running HiveShell instance.
     */
    public Set<String> getDatabases(HiveShell runningShell) {
        return Sets.newHashSet(runningShell.executeQuery("show databases"));
    }

    /**
     * Gets a set of table names for tables that exist within a database.
     * @param runningShell A HiveShell that is already running.
     * @param database The name of the database that the list of tables will be generated for.
     * @return A Set containing one entry for each table name in {@code database}
     */
    public Set<String> getTablesInDatabase(HiveShell runningShell, String database) {
        return Sets.newHashSet(
                runningShell.executeQuery(String.format("show tables in %s", database))
        );
    }

    /**
     * Gets the column information as a Set of {@code HiveColumnInfo} objects for a table.
     * @param runningShell A HiveShell that is already running.
     * @param database The database name where the table exists.
     * @param table The name of the table to get column information for.
     * @return A Set of {@code HiveColumnInfo} objects representing the column attributes for the table.
     */
    public Map<String, HiveColumnInfo> getColumnInfoForTable(HiveShell runningShell, String database, String table) {
        Map<String, HiveColumnInfo> hiveColumnInfos= new HashMap<String, HiveColumnInfo>();
        String blankColumnLine = "\tNULL\tNULL";
        int columnPosition = 0;

        List<String> describeResult =
                runningShell.executeQuery(String.format("describe formatted %s.%s", database, table));
        boolean isReadingColumns = false;
        for(String line : describeResult) {
            if(isReadingColumns) {
                if(line.trim().isEmpty() || line.equals(blankColumnLine)) { // skip blank lines in results
                    continue;
                }
                else if(line.startsWith("#")) { // done reading column information
                    break;
                }

                // valid column line -- ingest it //
                String[] lineFields = line.split("\\t");
                hiveColumnInfos.put(lineFields[0],
                        new HiveColumnInfo(columnPosition++, lineFields[0], lineFields[1], lineFields.length == 2 ? "" : lineFields[2]));
            }
            else if(line.startsWith("# col_name")) {
                isReadingColumns = true;
            }
        }

        return hiveColumnInfos;
    }

    /**
     * Gets the row count in a given table.
     * @param runningShell A HiveShell that is already running.
     * @param database The database name where the table exists.
     * @param table The table name for getting a row count.
     * @return An integer representing the number of rows in the table.
     */
    public int getRowCountInTable(HiveShell runningShell, String database, String table) {
        return Integer.parseInt(
                runningShell.executeQuery(
                        String.format("SELECT count(*) from %s.%s",
                                database, table)).get(0));

    }

    /**
     * Get's the comment attribute of a Hive table.
     * @param runningShell A HiveShell that is already running.
     * @param database The database name where the table exists.
     * @param table The table name that the comment is being retrieved for.
     * @return
     */
    public String getTableComment(HiveShell runningShell, String database, String table) {
        return runningShell.executeQuery(
                String.format("SHOW TBLPROPERTIES %s.%s(\"comment\")", database, table)).get(0).split("\\t")[0];
    }
}

