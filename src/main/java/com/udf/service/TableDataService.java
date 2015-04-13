package com.udf.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udf.bean.TableColumn;
import com.udf.bean.TableDef;
import com.udf.dao.DTDao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by NN on 2015/4/11.
 */
public class TableDataService {

    public String build(String sql, int draw, int start, int length) throws JsonProcessingException {
        DTDao dao = new DTDao();
        TableDef tableDef = null;
        try {
            tableDef = dao.excuteSQL(sql,start,length);
        } catch (SQLException e) {
            e.printStackTrace();
            tableDef.setError(e.getMessage());
        }
        tableDef.setDraw(draw+1);

        ObjectMapper mapper = new ObjectMapper();
        String result =  mapper.writeValueAsString(tableDef);
        System.out.println(result);
        return result;
    }

}
