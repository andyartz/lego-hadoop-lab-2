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
public class LegoPartsSetsHqlTest extends TableTestBase {
  String tableUnderTest = "lego_parts_sets";
  String databaseUnderTest = "default";

  public LegoPartsSetsHqlTest() {
    super("examples/lego_parts_sets.hql", "default", "lego_parts_sets");
  }

  @Override
  public void setupExpectedHiveColumnInfos() {
    int columnPosition = 0;
    putHiveColumnInfo(columnPosition++, "part_id", "string", "");
    putHiveColumnInfo(columnPosition++, "part_name", "string", "");
    putHiveColumnInfo(columnPosition++, "set_id", "string", "");
    putHiveColumnInfo(columnPosition++, "year", "int", "");
    putHiveColumnInfo(columnPosition++, "theme_id", "int", "");
    putHiveColumnInfo(columnPosition++, "num_parts", "int", "");
    putHiveColumnInfo(columnPosition++, "inventory_id", "int", "");
    putHiveColumnInfo(columnPosition++, "version", "int", "");
    putHiveColumnInfo(columnPosition++, "quantity", "int", "");
    putHiveColumnInfo(columnPosition++, "is_spare", "string", "");
    putHiveColumnInfo(columnPosition++, "color_id", "int", "");
  }

  @Override
  public void initializeExpectedTableComment() {
    setExpectedTableComment("Contains lego parts to sets data.");
  }

  @Override
  public void setupTestEnvironment() {
    super.setupTestEnvironment();
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/lego_sets.hql", "");
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/lego_inventories.hql", "");
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/lego_inventory_parts.hql", "");
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/lego_parts.hql", "");
  }

  @Test
  public void tableDataShouldBeCorrect() {
    /*****  SETUP  *****/
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/data_scripts/lego_parts_01.hql", "");
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/data_scripts/lego_sets_01.hql", "");
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/data_scripts/lego_inventory_parts_01.hql", "");
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/data_scripts/lego_inventories_01.hql", "");

    /*****  EXECUTE SCRIPT UNDER TEST  *****/
    try {
      shellUtil.executeResourceHql(shell, scriptUnderTest);
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unable to run script under test!");
    }

    /*****  VERIFY RESULTS  *****/
    List<String> actualRowData = shell.executeQuery(String.format("SELECT * FROM %s.%s ORDER BY set_id, part_id, quantity, color_id, is_spare",
                                                    databaseUnderTest, tableUnderTest));

    List<String> expectedRowData = Lists.newArrayList(
            "PART_1\tPART_1_NAME\tSET_1\t1999\t-1\t0\t1\t1\t1\tf\t1",
            "PART_2\tPART_2_NAME\tSET_1\t1999\t-1\t0\t1\t1\t10\tf\t2",
            "PART_2\tPART_2_NAME\tSET_1\t1999\t-1\t0\t1\t1\t10\tt\t3",

            "PART_1\tPART_1_NAME\tSET_2\t-2000\t0\t-20\t2\t1\t20\tf\t4",
            "PART_2\tPART_2_NAME\tSET_2\t-2000\t0\t-20\t2\t1\t20\tf\t4",
            "PART_2\tPART_2_NAME\tSET_2\t-2000\t0\t-20\t2\t1\t20\tt\t4",

            "PART_3\tPART_3_NAME\tSET_3\t2099\t1\t1000\t3\t2\t20\tf\t4",
            "PART_3\tPART_3_NAME\tSET_3\t2099\t1\t1000\t3\t2\t20\tf\t4",
            "PART_3\tPART_3_NAME\tSET_3\t2099\t1\t1000\t3\t2\t20\tt\t4",
            "PART_4\tPART_4_NAME\tSET_3\t2099\t1\t1000\t4\t1\t30\tf\t5",
            "PART_5\tPART_5_NAME\tSET_3\t2099\t1\t1000\t4\t1\t40\tf\t5",
            "PART_6\tPART_6_NAME\tSET_3\t2099\t1\t1000\t4\t1\t50\tt\t5"
    );

    assertEquals(expectedRowData, actualRowData);
  }
}
