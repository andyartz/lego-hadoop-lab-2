package examples;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.klarna.hiverunner.StandaloneHiveRunner;
import com.rwbsystems.HiveUnitTesting.TestTemplates.TableTestBase;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(StandaloneHiveRunner.class)
public class DcLargeOnlineSalesHqlTest extends TableTestBase {
  String tableUnderTest = "large_sales";
  String databaseUnderTest = "default";

  public DcLargeOnlineSalesHqlTest() {
    super("examples/dc_large_sales.hql",
        "default",
        "large_sales");
  }

  @Override
  public void setupExpectedHiveColumnInfos() {
    int columnPosition = 0;
    putHiveColumnInfo(columnPosition++, "tran_id", "int", "");
    putHiveColumnInfo(columnPosition++, "sell_chan", "string", "");
    putHiveColumnInfo(columnPosition++, "total", "double", "");
  }

  @Override
  public void initializeExpectedTableComment() {
    setExpectedTableComment("Contains only large online transactions over $100 total.");
  }

  @Override
  public void setupTestEnvironment() {
    super.setupTestEnvironment();
    shellUtil.executeResourceHqlIgnoreIOException(shell,
        "examples/dc_sales.hql",
        String.format("Unable to setup %s!", tableUnderTest));
  }

  @Test
  public void greaterThanOrEqualTo100() {
    /*****  SETUP  *****/
    // expected results in large_sales should be record 2 (Store, 220), 3 (Unknown, 330), 4 (Online, 100)
    List<String> expectedRowData = Lists.newArrayList("2\tStore\t220.0", "3\tNULL\t330.0", "4\tOnline\t100.0");
    shellUtil.executeResourceHqlIgnoreIOException(shell,
        "examples/data_scripts/dc_large_sales_01.hql", "");

    /*****  EXECUTE SCRIPT UNDER TEST  *****/
    try {
      shellUtil.executeResourceHql(shell, scriptUnderTest);
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unable to run script under test in greaterThanOrEqualTo100()!");
    }

    /*****  VERIFY RESULTS  *****/
    List<String> actualRowData =
        shell.executeQuery(
            String.format("SELECT * FROM %s.%s ORDER BY tran_id",
                databaseUnderTest, tableUnderTest));
    assertEquals(expectedRowData, actualRowData);
  }

  @Test
  public void lowerCaseChannels() {
    /*****  SETUP  *****/
    // expected results in large_sales should be record 2 (Store, 220), 3 (Unknown, 330), 4 (Online, 100)
    List<String> expectedRowData = Lists.newArrayList("2\tStore\t220.0", "3\tNULL\t330.0", "4\tOnline\t100.0");
    shellUtil.executeResourceHqlIgnoreIOException(shell,
        "examples/data_scripts/dc_large_sales_02.hql", "");

    /*****  EXECUTE SCRIPT UNDER TEST  *****/
    try {
      shellUtil.executeResourceHql(shell, scriptUnderTest);
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unable to run script under test in lowerCaseChannels()!");
    }

    /*****  VERIFY RESULTS  *****/
    // get the actual row data by querying the result table //
    List<String> actualRowData =
        shell.executeQuery(
            String.format("SELECT * FROM %s.%s ORDER BY tran_id",
                databaseUnderTest, tableUnderTest));
    // assert the expected results are equal to the actual results //
    assertEquals(expectedRowData, actualRowData);
  }
}
