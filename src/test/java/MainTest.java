import com.tiza.util.config.Constant;
import org.junit.Test;

/**
 * Description: MainTest
 * Author: DIYILIU
 * Update: 2016-03-23 9:39
 */
public class MainTest {

    @Test
    public void test() {

        String sql = "insert abc values";

        System.out.println(getSQLID(sql));
    }

    public static int getSQLID(String sql) {

        if (sql.toUpperCase().contains("INSERT")) {
            return Constant.SQL.INSERT;
        }

        if (sql.toUpperCase().contains("UPDATE")) {
            return Constant.SQL.UPDATE;
        }

        return -1;
    }

    @Test
    public void testErro() {
        /**
        byte[] bytes = new byte[]{64 65 73 20 74 67 33 77 72 69};

        System.out.println(new String(bytes));
        bytes = new byte[]{73 74 6E 20 65 69 20 6D 20 65 61 65 37 20};
        System.out.println(new String(bytes));
         */
    }
}
