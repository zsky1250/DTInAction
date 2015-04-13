package com.udf.dao;

import com.udf.bean.TableColumn;
import com.udf.bean.TableData;
import com.udf.bean.TableDef;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NN on 2015/4/11.
 */
public class DTDao {

    private DBHelper engine = new DBHelper();

    public TableDef excuteSQL(String sql,int startRow,int rowPerPage) throws SQLException {
        //String sql = "select a.req_id as 序号,a.req_tag as 标签,a.req_name 需求名称  from ALM_REQUISITION a";
        Connection conn = engine.getConnection();
        PreparedStatement psta = null;
        ResultSet rs = null;
        try {
            psta = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            psta.setFetchSize(rowPerPage);
            rs = psta.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int totalColumn = rsmd.getColumnCount();
            ArrayList<TableColumn> tableColumns = buildColumnInfo(rsmd,totalColumn);
            int totalRow = getTotalRow(rs);
            if(startRow>0){
                //如果不是第一页，将游标移动到相应起始位置
                rs.absolute(startRow);
            }else{
                //如果第一页，不能用rs.absolute(0);移动到第一行之前
                rs.beforeFirst();
            }
            ArrayList<HashMap<String,String>> tableDatas = new ArrayList<>();
            for(int i = 0;rs.next()&&i<rowPerPage;i++){
                HashMap<String,String> data = new HashMap<>();
                for(int j = 0 ;j<=totalColumn;j++){
                    if(j==0){
                        //第一列填充为空数据，为了前台显示序号用。
                        data.put("tableID","");
                    }else{
                        data.put(rsmd.getColumnName(j), rs.getString(j));
                    }
                }
                tableDatas.add(data);
            }

            TableDef tableDef = new TableDef();
            tableDef.setColumn(tableColumns);
            tableDef.setDatas(tableDatas);
            tableDef.setRecordsTotal(totalRow);
            tableDef.setRecordsFiltered(totalRow);
            return tableDef;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(rs!=null) rs.close();
            if(psta!=null)  psta.close();
            if(conn!=null)  conn.close();

        }
        return null;
    }

    public ArrayList<TableColumn> buildColumnInfo(ResultSetMetaData rsmd,int totalColumn) throws SQLException {
        ArrayList<TableColumn> tableColumns = new ArrayList<TableColumn>();
        for(int i = 0;i<=totalColumn;i++){
            TableColumn tc = new TableColumn();
            if(i==0){
                //rs下标从1开始，这里附件一个序号列。
                tc.setData("tableID");
                tc.setTitle("序号");
            }else{
                tc.setData(rsmd.getColumnName(i));
                tc.setTitle(rsmd.getColumnLabel(i));
            }
            tableColumns.add(tc);
        }
        return tableColumns;
    }

    public int getTotalRow(ResultSet rs) throws SQLException {
        rs.last();
        return rs.getRow();
    }

}
