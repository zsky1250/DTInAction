package com.udf.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NN on 2015/4/11.
 */
public class TableDef {
    private ArrayList<TableColumn> column;
    @JsonProperty(value = "data")
    private ArrayList<HashMap<String,String>> datas;
    private int draw;
    private int recordsTotal;
    private int recordsFilteredTotal;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public ArrayList<HashMap<String, String>> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<HashMap<String, String>> datas) {
        this.datas = datas;
    }

    public ArrayList<TableColumn> getColumn() {
        return column;
    }

    public void setColumn(ArrayList<TableColumn> column) {
        this.column = column;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFilteredTotal() {
        return recordsFilteredTotal;
    }

    public void setRecordsFilteredTotal(int recordsFilteredTotal) {
        this.recordsFilteredTotal = recordsFilteredTotal;
    }




}
