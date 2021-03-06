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
public class LegoPartsHqlTest extends TableTestBase {
  private String tableUnderTest = "parts";
  private String databaseUnderTest = "default";

  public LegoPartsHqlTest() {
    super("examples/lego_parts.hql", "default", "parts");
  }

  @Override
  public void setupExpectedHiveColumnInfos() {
    putHiveColumnInfo(0, "part_id", "string", "from deserializer");
    putHiveColumnInfo(1, "name", "string", "from deserializer");
    putHiveColumnInfo(2, "part_cat_id", "string", "from deserializer");
  }

  @Override
  public void initializeExpectedTableComment() {
    setExpectedTableComment("Contains lego parts data.");
  }

  @Test
  public void tableColumnsShouldBeCorrect() {

    //  SETUP
    shellUtil.executeResourceHqlIgnoreIOException(shell, "examples/lego_parts.hql", String.format("Unable to setup %s!", tableUnderTest));

    //  EXECUTE SCRIPT UNDER TEST
    try {
      shellUtil.executeResourceHql(shell, "examples/data_scripts/lego_parts_01.hql");
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unable to run script under test!");
    }

    //  VERIFY RESULTS
    List<String> actualRowData = shell.executeQuery(String.format("SELECT * FROM %s.%s ORDER BY PART_ID",
                                                                  databaseUnderTest, tableUnderTest));
    List<String> expectedRowData = Lists.newArrayList(
            "PART_1\tPART_1_NAME\t0",
            "PART_2\tPART_2_NAME\t1",
            "PART_3\tPART_3_NAME\t2",
            "PART_4\tPART_4_NAME\t2",
            "PART_5\tPART_5_NAME\t3",
            "PART_6\tPART_6_NAME\t3");

    assertEquals(expectedRowData, actualRowData);
  }
}
