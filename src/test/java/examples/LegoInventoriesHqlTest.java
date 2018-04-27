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
public class LegoInventoriesHqlTest extends TableTestBase {
  private String tableUnderTest = "inventories";
  private String databaseUnderTest = "default";

  public LegoInventoriesHqlTest() {
    super("examples/lego_inventories.hql", "default", "inventories");
  }

  @Override
  public void setupExpectedHiveColumnInfos() {
    putHiveColumnInfo(0, "inventory_id", "string", "from deserializer");
    putHiveColumnInfo(1, "version", "string", "from deserializer");
    putHiveColumnInfo(2, "set_id", "string", "from deserializer");
  }

  @Override
  public void initializeExpectedTableComment() {
    setExpectedTableComment("Contains lego inventories data.");
  }

  @Test
  public void tableColumnsShouldBeCorrect() {

    //  SETUP
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/lego_inventories.hql", String.format("Unable to setup %s!", tableUnderTest));

    //  EXECUTE SCRIPT UNDER TEST
    try {
      shellUtil.executeResourceHql(shell, "examples/data_scripts/lego_inventories_01.hql");
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unable to run script under test!");
    }

    //  VERIFY RESULTS
    List<String> actualRowData = shell.executeQuery(String.format("SELECT * FROM %s.%s ORDER BY inventory_id",
                                                                  databaseUnderTest, tableUnderTest));
    List<String> expectedRowData = Lists.newArrayList(
            "1\t1\tSET_1",
            "2\t1\tSET_2",
            "3\t2\tSET_3",
            "4\t1\tSET_3"
    );

    assertEquals(expectedRowData, actualRowData);
  }
}
