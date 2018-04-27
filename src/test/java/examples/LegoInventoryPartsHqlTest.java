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
public class LegoInventoryPartsHqlTest extends TableTestBase {
  private String tableUnderTest = "inventory_parts";
  private String databaseUnderTest = "default";

  public LegoInventoryPartsHqlTest() {
    super("examples/lego_inventory_parts.hql", "default", "inventory_parts");
  }

  @Override
  public void setupExpectedHiveColumnInfos() {
    putHiveColumnInfo(0, "inventory_id", "string", "from deserializer");
    putHiveColumnInfo(1, "part_id", "string", "from deserializer");
    putHiveColumnInfo(2, "color_id", "string", "from deserializer");
    putHiveColumnInfo(3, "quantity", "string", "from deserializer");
    putHiveColumnInfo(4, "is_spare", "string", "from deserializer");
  }

  @Override
  public void initializeExpectedTableComment() {
    setExpectedTableComment("Contains lego inventory to parts data.");
  }

  @Test
  public void tableColumnsShouldBeCorrect() {

    //  SETUP
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/lego_inventory_parts.hql", String.format("Unable to setup %s!", tableUnderTest));

    //  EXECUTE SCRIPT UNDER TEST
    try {
      shellUtil.executeResourceHql(shell, "examples/data_scripts/lego_inventory_parts_01.hql");
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unable to run script under test!");
    }

    //  VERIFY RESULTS
    List<String> actualRowData = shell.executeQuery(String.format("SELECT * FROM %s.%s ORDER BY INVENTORY_ID, PART_ID",
                                                                  databaseUnderTest, tableUnderTest));
    List<String> expectedRowData = Lists.newArrayList(
            "1\tPART_1\t1\t1\tf",
            "1\tPART_2\t3\t10\tt",
            "1\tPART_2\t2\t10\tf",
            "2\tPART_1\t4\t20\tf",
            "2\tPART_2\t4\t20\tt",
            "2\tPART_2\t4\t20\tf",
            "3\tPART_3\t4\t20\tt",
            "3\tPART_3\t4\t20\tf",
            "3\tPART_3\t4\t20\tf",
            "4\tPART_4\t5\t30\tf",
            "4\tPART_5\t5\t40\tf",
            "4\tPART_6\t5\t50\tt"
    );

    assertEquals(expectedRowData, actualRowData);
  }
}
