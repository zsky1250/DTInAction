package com.udf.dao;

import java.sql.*;

public class DBHelper {

    /**
     * 为什么要写一个工具类呢？
     *
     * 因为这样可以简化我们的Dao类的写法。更何况一个实体类对应一个Dao。
     * 如果每一个Dao都按照上面一篇文章写，那是很费事的啊！！
     */

    //1.下面是几个常量
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/ai";
    private static final String DB_NAME = "root";
    private static final String DB_PASSWORD = "root";
//    private static final String DRIVER ="oracle.jdbc.driver.OracleDriver";// "oracle.jdbc.OracleDriver";
//    private static final String URL = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";
//    private static final String DB_NAME = "ai";
//    private static final String DB_PASSWORD = "ai";

    /**
     * 获取数据库的连接
     * @return
     */
    public Connection getConnection() {
        Connection con = null;

        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, DB_NAME, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     *  该方法用来执行insert,update,delete操作
     * @param sql SQL语句
     * @param args  不定项参数
     * @return
     */
    public int executeSQL(String sql,Object...args) {
        Connection con = getConnection();
        PreparedStatement sta = null;
        int rows = 0;

        try {
            sta = con.prepareStatement(sql);

            //★注意下面的循环语句★
            for (int i = 0; i < args.length; i++) {
                sta.setObject(i+1, args[i]); //为什么是i+1呢？因为你从前面的文章知道，那是从1开始的！
            }

            rows = sta.executeUpdate();
            if(rows > 0) {
                System.out.println("operate successfully !");
            } else {
                System.out.println("fail!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(sta, con);
        }
        return rows;
    }

    /**
     * rs,sta,con的关闭
     * @param rs
     * @param sta
     * @param con
     */
    public void close(ResultSet rs, PreparedStatement sta, Connection con) {
        try {
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(sta != null) {
                    sta.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * sta,con的关闭方法
     * @param sta
     * @param con
     */
    public void close(PreparedStatement sta, Connection con) {
        this.close(null, sta, con);
    }

    //--------------------------------------
}