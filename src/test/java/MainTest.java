import com.tiza.util.config.Constant;
import org.junit.Test;

/**
 * Description: MainTest
 * Author: DIYILIU
 * Update: 2016-03-23 9:39
 */
public class MainTest {

    @Test
    public void test(){

        String sql = "insert abc values";

        System.out.println(getSQLID(sql));
    }

    public static  int getSQLID(String sql){

        if (sql.toUpperCase().contains("INSERT")){
            return Constant.SQL.INSERT;
        }

        if (sql.toUpperCase().contains("UPDATE")){
            return Constant.SQL.UPDATE;
        }

        return -1;
    }
}
