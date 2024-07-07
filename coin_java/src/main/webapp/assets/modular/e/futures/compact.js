layui.use(['table', 'admin', 'ax', 'ztree', 'laydate',"form"], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var $ZTree = layui.ztree;
    var form = layui.form;
    //渲染时间选择框
    laydate.render({
        elem: '#exitTime',
        range: true,
        max: Feng.currentDate()
    });

    var htmls = '<option value="">请选择交易对</option>'; //全局变量
    $.ajax({
        url: Feng.ctxPath + '/futures/getList',
        type: "post",
        dataType : "json",
        contentType : "application/json",
        async: false,//这得注意是同步
        success: function (result) {
            resultData = result.data;
            for(var x in resultData){
                htmls += '<option value = "' + resultData[x].symbol + '">' + resultData[x].symbol + '</option>';
            }
            $("#symbols").html(htmls);
        }
    });
   form.render('select');//需要渲染一下


    /**
 * 合约订单管理初始化
 */
var Compact = {
    tableId: "compactTable",	//表格id
    condition: {
        futuresCompactId: ""
    }
};

/**
 * 初始化表格的列
 */
Compact.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'futuresCompactId', hide: true, sort: true, title: 'id'},
         {field: 'orderNo', sort: true, title: '订单号'},
         // {field: 'memberId', sort: true, title: '用户'},
        {field: 'memberIdValue', sort: true, title: '用户',width:160},
        {field: 'phone', sort: true, title: '手机号码'},
        {field: 'email', sort: true, title: '邮箱'},
         {field: 'dealWay', sort: true, title: '交易方式'},
         {field: 'compactType', sort: true, title: '交易类型'},
         {field: 'symbols', sort: true, title: '交易对'},
         {field: 'unit', sort: true, title: '下单价'},
        {field: 'tradePrice', sort: true, title: '建仓/委托价'},
        {field: 'fee', sort: true, title: '开仓手续费'},
         {field: 'numbers', sort: true, title: '持仓手数'},
        {field: 'handNumber', sort: true, title: '每手价值'},
         {field: 'exitPrice', sort: true, title: '平仓价'},
        {field: 'closeNumber', sort: true, title: '平仓手数'},
        {field: 'handPrice', sort: true, title: '平仓价值'},
        {field: 'closeFeePrice', sort: true, title: '平仓手续费'},
         {field: 'stopLoss', sort: true, title: '止损价'},
         {field: 'stopProfit', sort: true, title: '止盈价'},
         {field: 'tpl', sort: true, title: '盈亏'},
         {field: 'exitType', sort: true, title: '平仓类型'},
         {field: 'exitTime', sort: true, title: '平仓时间'},
         {field: 'coin', sort: true, title: '下单币种'},
         {field: 'leverRate', sort: true, title: '杠杆倍率'},
         {field: 'positionPrice', sort: true, title: '仓位保证金'},
        {field: 'exitPositionPrice', sort: true, title: '平仓保证金'},
        {field: 'statusValue', sort: true, title: '状态'},
         {field: 'createTime', sort: true, title: '创建时间'},
    ]];
};


    /**
     * 点击查询按钮
     */
    Compact.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['phone'] = $("#phone").val();
        queryData['email'] = $("#email").val();
        queryData['dealWay'] = $("#dealWay").val();
        queryData['compactType'] = $("#compactType").val();
        queryData['symbols'] = $("#symbols").val();
        queryData['status'] = $("#status").val();
        queryData['exitType'] = $("#exitType").val();
        queryData['exitTime'] = $("#exitTime").val();
        queryData['compactId'] = Compact.condition.futuresCompactId;
        table.reload(Compact.tableId, {where: queryData});
        $.ajax({
            type:"post",
            url:Feng.ctxPath +"/futuresCompact/searchStatistics",
            data:queryData,
            dataType:'json',
            success:function(data){
                if(data.code=='200'){
                    document.getElementById("fee").innerText = data.data.fee;
                    document.getElementById("closeFeePrice").innerText = data.data.closeFeePrice;
                    document.getElementById("tpl").innerText = data.data.tpl;
                    document.getElementById("positionPrice").innerText = data.data.positionPrice;
                }
            },
            error:function(xhr,type,errorThrown){
            }
        });
    };



/**
 * 打开查看合约订单详情
 */
Compact.openCompactDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '合约订单详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/futuresCompact/compact_edit/' + Compact.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除合约订单
     *
     */
    Compact.onDeleteCompact = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/futuresCompact/delete", function () {
                Feng.success("删除成功!");
                table.reload(Compact.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("compactId", data.futuresCompactId);
            ajax.start();
        };
        Feng.confirm("是否删除合约订单 ?", operation);
    };

    /**
     * 弹出添加合约订单
     */
    Compact.openAddCompact = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加合约订单',
            area:["800px","420px"],
            content: Feng.ctxPath + '/futuresCompact/compact_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Compact.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Compact.exportExcel = function () {
        var checkRows = table.checkStatus(Compact.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑合约订单
     *
     */
     Compact.onEditCompact = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改合约订单',
            area:["800px","420px"],
            content: Feng.ctxPath + '/futuresCompact/compact_edit?compactId=' + data.futuresCompactId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Compact.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Compact.tableId,
        url: Feng.ctxPath + '/futuresCompact/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Compact.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Compact.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Compact.openAddCompact();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Compact.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Compact.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Compact.onEditCompact(data);
        } else if (layEvent === 'delete') {
            Compact.onDeleteCompact(data);
        }
    });
});