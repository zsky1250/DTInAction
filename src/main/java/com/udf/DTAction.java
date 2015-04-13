package com.udf;

import com.udf.service.TableDataService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by NN on 2015/4/11.
 */
public class DTAction extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int draw = Integer.valueOf(req.getParameter("draw")).intValue();
        int start = Integer.valueOf(req.getParameter("start")).intValue();
        int length = Integer.valueOf(req.getParameter("length")).intValue();
        TableDataService tdService = new TableDataService();
        String result = tdService.build(draw,start,length);
        PrintWriter writer = resp.getWriter();
        writer.write(result);
    }
}
