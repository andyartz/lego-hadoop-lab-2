package examples;

import com.google.common.collect.Lists;
import com.klarna.hiverunner.StandaloneHiveRunner;
import com.rwbsystems.HiveUnitTesting.TestTemplates.TableTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(StandaloneHiveRunner.class)
public class LegoSetsHqlTest extends TableTestBase {
  String tableUnderTest = "sets";
  String databaseUnderTest = "default";

  public LegoSetsHqlTest() {
    super("examples/lego_sets.hql",
        "default",
        "sets");
  }

  @Override
  public void setupExpectedHiveColumnInfos() {
    int columnPosition = 0;
    putHiveColumnInfo(columnPosition++, "SET_ID", "string", "");
    putHiveColumnInfo(columnPosition++, "NAME", "string", "");
    putHiveColumnInfo(columnPosition++, "YEAR", "int", "");
    putHiveColumnInfo(columnPosition++, "THEME_ID", "int", "");
    putHiveColumnInfo(columnPosition++, "NUM_PARTS", "int", "");
  }

  @Override
  public void initializeExpectedTableComment() {
    setExpectedTableComment("Contains lego sets data.");
  }

  @Override
  public void setupTestEnvironment() {
    super.setupTestEnvironment();
    shellUtil.executeResourceHqlIgnoreIOException(shell,
            "examples/lego_sets.hql",
            String.format("Unable to setup %s!", tableUnderTest));
  }

  @Test
  public void tableColumnsShouldBeCorrect() {
    /*****  SETUP  *****/
    List<String> expectedRowData = Lists.newArrayList("SET_1\tSET_1_NAME\t1999\t-1\t0", "SET_2\tset_2_name\t-2000\t0\t-20", "SET_3\tSet_3_name\t2099\t1\t1000", "SET_4\tSet_4_Name\t2018\t100\t100");
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/data_scripts/lego_sets_01.hql", "");

    /*****  EXECUTE SCRIPT UNDER TEST  *****/
    try {
      shellUtil.executeResourceHql(shell, scriptUnderTest);
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unable to run script under test!");
    }

    /*****  VERIFY RESULTS  *****/
    List<String> actualRowData =
            shell.executeQuery(
                    String.format("SELECT * FROM %s.%s ORDER BY SET_ID", databaseUnderTest, tableUnderTest));
    assertEquals(expectedRowData, actualRowData);
  }

//  @Test
//  public void lowerCaseChannels() {
//    /*****  SETUP  *****/
//    // expected results in large_sales should be record 2 (Store, 220), 3 (Unknown, 330), 4 (Online, 100)
//    List<String> expectedRowData = Lists.newArrayList("2\tStore\t220.0", "3\tNULL\t330.0", "4\tOnline\t100.0");
//    shellUtil.executeResourceHqlIgnoreIOException(shell,
//        "examples/data_scripts/dc_large_sales_02.hql", "");
//
//    /*****  EXECUTE SCRIPT UNDER TEST  *****/
//    try {
//      shellUtil.executeResourceHql(shell, scriptUnderTest);
//    } catch (IOException e) {
//      e.printStackTrace();
//      fail("Unable to run script under test in lowerCaseChannels()!");
//    }
//
//    /*****  VERIFY RESULTS  *****/
//    // get the actual row data by querying the result table //
//    List<String> actualRowData =
//        shell.executeQuery(
//            String.format("SELECT * FROM %s.%s ORDER BY tran_id",
//                databaseUnderTest, tableUnderTest));
//    // assert the expected results are equal to the actual results //
//    assertEquals(expectedRowData, actualRowData);
//  }
}
