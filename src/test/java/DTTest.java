import com.fasterxml.jackson.core.JsonProcessingException;
import com.udf.bean.TableColumn;
import com.udf.bean.TableDef;
import com.udf.dao.DTDao;
import com.udf.service.TableDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NN on 2015/4/12.
 */

@RunWith(JUnit4.class)
public class DTTest {

    @Test
    public void testDAO() throws SQLException {
        DTDao dao = new DTDao();
        TableDef td = dao.excuteSQL("select a.req_id as 序号,a.req_tag as 标签,a.req_name 需求名称  from ALM_REQUISITION a",0, 10);
        ArrayList<TableColumn> tableColumns = td.getColumn();
        System.out.println("columns:========");
        for (TableColumn column : tableColumns) {
            System.out.println("name:"+column.getData()+"  title:"+column.getTitle());
        }
        System.out.println("DATA:========");
        ArrayList<HashMap<String,String>> tdData = td.getDatas();
        for (HashMap<String,String> data : tdData) {
            for (Map.Entry<String, String> dataEntry : data.entrySet()) {
                System.out.println(dataEntry.getKey()+":"+dataEntry.getValue());
            }
        }
    }

    @Test
    public void testTDService() throws JsonProcessingException {
        TableDataService tableDataService = new TableDataService();
        String result = tableDataService.build("select a.req_id as 序号,a.req_tag as 标签,a.req_name 需求名称  from ALM_REQUISITION a", 1,0,10);
        System.out.println(result);
    }

}
