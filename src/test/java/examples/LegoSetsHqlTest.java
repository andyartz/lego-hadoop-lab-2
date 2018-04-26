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
    putHiveColumnInfo(columnPosition++, "set_id", "string", "from deserializer");
    putHiveColumnInfo(columnPosition++, "name", "string", "from deserializer");
    putHiveColumnInfo(columnPosition++, "year", "string", "from deserializer");
    putHiveColumnInfo(columnPosition++, "theme_id", "string", "from deserializer");
    putHiveColumnInfo(columnPosition++, "num_parts", "string", "from deserializer");
  }

  @Override
  public void initializeExpectedTableComment() {
    setExpectedTableComment("Contains lego sets data.");
  }

  @Override
  public void setupTestEnvironment() {
    super.setupTestEnvironment();
  }

  @Test
  public void tableColumnsShouldBeCorrect() {
    /*****  SETUP  *****/
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/lego_sets.hql", "");

    /*****  EXECUTE SCRIPT UNDER TEST  *****/
    try {
      shellUtil.executeResourceHql(shell, "examples/data_scripts/lego_sets_01.hql");
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unable to run script under test!");
    }

    /*****  VERIFY RESULTS  *****/
    List<String> expectedRowData = Lists.newArrayList("SET_1\tSET_1_NAME\t1999\t-1\t0", "SET_2\tset_2_name\t-2000\t0\t-20", "SET_3\tSet_3_name\t2099\t1\t1000", "SET_4\tSet_4_Name\t2018\t100\t100");

    List<String> actualRowData =
            shell.executeQuery(
                    String.format("SELECT * FROM %s.%s ORDER BY SET_ID", databaseUnderTest, tableUnderTest));
    assertEquals(expectedRowData, actualRowData);
  }
}
