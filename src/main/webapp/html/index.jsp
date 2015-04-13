<%--
  Created by IntelliJ IDEA.
  User: NN
  Date: 2015/4/11
  Time: 17:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" type="text/css" href="html/res/aialm.css" />
<script type="application/javascript" src="html/res/js/jquery-1.11.1.js"></script>


<link rel="stylesheet" type="text/css" href="html/res/datatables-1.10.3/css/jquery.dataTables.min.css" />
<script src="html/res/datatables-1.10.3/js/jquery.dataTables.js"></script>
<head>
    <title>DTInAction</title>
</head>
<body>



<div id="dt-panel" >
  <table class="content_title">
    <tr><td>内容显示区</td></tr>
    <tr><td><input type="button" id="load1" value="sql1"/></td><td><input type="button" id="load2" value="sql2"></td></tr>
  </table>
  <table id="dt-table"></table>
</div>


<script type="text/javascript">
    $(function() {
      //global var
        var newTableData = {};
        var globalSQL = "";
        var firstLoad = true;//第一次初始化，需要绑定监听
        var dtTable;

        $("#load1").click(function(){
            globalSQL = "select a.req_id as ID,a.req_tag as 标签,a.req_name 需求名称  from ALM_REQUISITION a";
            retreiveTable();
        });

        $("#load2").click(function(){
            globalSQL = "select a.req_id ID,a.req_tag 标签,a.req_name 需求名称,a.REQ_DESC 需求描述 from ALM_REQUISITION a";
            retreiveTable();
        });



        function retreiveTable(sql){
        $.ajax("DTAction",{
            type:'post',
            dataType:'json',
            data:{
              sql:globalSQL,
              draw:0,
              start:0,
              length:10
            }
          }).done(function(data){
            reloadTable(data);
          });
        }
        function reloadTable(returnData){
            var tableOptions = buildTableOptions(returnData.column);
            newTableData = returnData;
            bindTableEvent(dtTable);
            if(firstLoad){
                dtTable = $('#dt-table').DataTable(tableOptions);
                firstLoad = false;
            }else{
                dtTable.destroy();
                $('#dt-table').empty();
                dtTable = $('#dt-table').DataTable(tableOptions);
            }
            //效率优化****
            //初始化以后及时清空newTableData集合，通过这个变量是否为空判断数据载入方式.来自点击还是table自身操作。
            newTableData = {};

        /*  dt.on("init.dt", function(event, settings, json) {
            //如果是用滚动条 通过table.children("input")来选择checkbook会漏选head中的checkbox。
            //因为组件会默认加一个<div:class=dataTables_scrollHead>，真正的Table HEADB被隐藏，显示的是另一个div中的head。
            //而此处dt对象是<div:class=dataTables_scrollBody>中的table。
            //dt.find("thead th input:checkbox");
            var wrapper_selector = dt.selector + "_wrapper";
            $(wrapper_selector).find("thead th input:checkbox").click(function() {
              if ($(this).prop("checked") == true) {
                dt.api().column(0).nodes().to$().children("input:checkbox").prop("checked", true);
                //alert(dt.api().column(0).nodes().to$().children("input:checkbox").attr("entity-id"));
              } else {
                dt.api().column(0).nodes().to$().children("input:checkbox").prop("checked", false);
              }
            });
          });

          $("#dt-search").children(":button[name=search]").click(function() {
            *//* $("#dt-search").children("input:text").each(function(){
             alert($(this).prop("name")+" : "+$(this).prop("value"));
             }); *//*
            var a = $("#dt-search").children("input:text").serializeArray();
            dt.one("preXhr.dt", function(e, settings, data) {
              data.search = JSON.stringify(a);
              console.log(data);
            });
            dt.api().ajax.reload();
          });
          $("#dt-search").children(":button[name=reset]").click(function() {
            $("#dt-search").children("input:text").prop("value", null);
          });*/
        }

        function adjustIndexColumn(table){
            table.column(0).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
                alert(cell.innerHTML);
            });
        }

        function buildTableOptions(columns){
            var TDOptions = {
                processing : true,//显示处理中的标签
                serverSide : true,//使用服务器端交互模式
                lengthChange: false,//用户可不可以自己修改显示的条数
                lengthMenu : [10],//每页显示多少条
                language : {
                    processing:   "载入数据...",
                    search   :   "查询",
                    lengthMenu:   "显示 _MENU_ 项结果",
                    zeroRecords:  "没有匹配结果",
                    info:         "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                    infoEmpty:    "显示第 0 至 0 项结果，共 0 项",
                    infoFiltered: "(查询结果： _MAX_ 项)",
                    infoPostFix:  "",
                    sUrl:          "",
                    emptyTable:     "表中数据为空",
                    loadingRecords: "载入中...",
                    sInfoThousands:  ",",
                    decimal       :  ",",
                    paginate: {
                        "first":    "首页",
                        "previous": "上页",
                        "next":     "下页",
                        "last":     "末页"
                    },
                    aria: {
                        "sortAscending":  ": 以升序排列此列",
                        "sortDescending": ": 以降序排列此列"
                    }
                },
                scrollY : "330",//table的高度
                scrollCollapse : false,//如果行数少于标准的条数，是否缩短表格高度
                //dom: "<'row'<'col-xs-12't>r><'row'<'col-xs-4'l><'col-xs-4 text-center'i><'col-xs-4'p>>",
                searching : false,//是否开启客户端查询，纯js过滤，我们可以考虑服务器端过滤。
                paging: true,//是否开启分页插件
                ordering:  false,//开启排序功能
                /* 推荐的ajax交互写法，目前这个插件不能动态增加列，所以不用这种方式。
                 ajax : {
                 "url" : "/DTAction",
                 "type" : "POST",
                 //"contentType":"application/json;charset=utf8",
                 //"dataSrc": "content",
                 "data" : function(d) {
                 var col_index = d.order[0].column;
                 var ordername = d.columns[col_index].data
                 var orderdir = d.order[0].dir
                 var rdata = {};
                 rdata.page = d.start / d.length;
                 rdata.size = d.length;
                 rdata.sort = ordername + "," + orderdir;
                 rdata.draw = d.draw;
                 return rdata;
                 }
                 },*/
                ajax : function(data,callback,oSettings){
                    if($.isEmptyObject(newTableData)){
                        //调用不是来自新构造的table，无需重建table
                        $.ajax("DTAction",{
                            type:'post',
                            dataType:'json',
                            data:{
                                sql:globalSQL,
                                draw:data.draw,
                                start:data.start,
                                length:data.length
                            }
                        }).done(function(data){
                            callback(data);
                        });
                    }else{
                        //调用来自reloadTable(),table数据已经得到，直接拿来用。
                        callback(newTableData);
                    }
                },
                columns : columns,
                // destroy:true//如果针对同一个jq对象，进行多次初始化，是否销毁已有的table重建一个。
                // 这里如果是true，则不能对同一个jq对象重复调用dataTable(),进行初始化。
            };
            return TDOptions;
        }

        function bindTableEvent(dtTable){
            dtTable.on( 'page.dt', function (e) {
                alert("turn page finished event");
                adjustIndexColumn(dtTable);
            } );

            dtTable.on('xhr.dt',function(){
                alert("xhr complete!");
            });

            dtTable.on('draw.dt',function(){
                alert("draw");
            });

            dtTable.on('init.dt',function(e,settings){
                alert("table init event");
                adjustIndexColumn(dtTable);
                //alert(settings.aoColumns);
            });

            dtTable.on('destroy.dt',function(e,settings){
                alert("table destroy");
            });

        }

  });

</script>

</body>
</html>
