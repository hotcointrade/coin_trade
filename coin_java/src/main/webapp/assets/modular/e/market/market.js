layui.use(['table', 'admin', 'ax', 'ztree','laydate','form'], function () {
    var form = layui.form;
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 合约交易对管理初始化
     */
    var Obj = {
        tableId: "marketTable",	//表格id
    };
    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });
    /**
     * 初始化表格的列
     */
    Obj.initColumn = function () {
        return [[
            // {type: 'checkbox'},
            {field: 'swapId', hide: true, sort: true, title: 'id'},
            {field: 'img',  title: '图标',templet: "#imgs" ,width:60},
            {field: 'symbol', sort: true, title: '交易对',width:120},
            // {field: 'marketOne', sort: false, title: '一口价',width:120},
            {field: 'marketPrice', sort: false, title: '价格涨跌',width:120},
            {field: 'marketRate', sort: false, title: '比例涨跌',width:120},
            // {field: 'marketRateDate', sort: false, title: '比例区间涨跌',width:120},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 重置行情
     */
    Obj.onResetMarket = function (data) {
        console.log('swapId',data.swapId)
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/market/reset?swapId="+data.swapId, function () {
                Feng.success("重置成功!");
                table.reload(Obj.tableId);
            }, function (data) {
                Feng.error("重置失败!" + data.message + "!");
            });
            ajax.set("swapId", data.swapId);
            ajax.start();
        };
        Feng.confirm("是否重置数据 ?", operation);
    };




    /**
     *
     * type  1一口价,  2价格涨跌,  3比例涨跌
     */
    Obj.onEditMarket = function (data,type) {
        admin.putTempData('formOk', false);
        var title;
        switch (type) {
            case 1:
                title = "一口价(请设置最新价格如:72533.02)"
                break;
            case 2:
                title = "价格涨跌(当前价格会加上/减去这个数值,如-100)"
                break;
            case 3:
                title = "比例涨跌(按比例乘以这个数值,如:-1)范围只能在-5~5区间"
                break;
        }
        top.layui.admin.open({
            type: 2,
            maxmin:true,
            title: title,
            area:["800px","420px"],
            content: Feng.ctxPath + '/market/market_edit?swapId=' + data.swapId+'&type='+type,
            end: function () {
                admin.getTempData('formOk') && table.reload(Obj.tableId);
            }
        });
    };

    /**
     * 区间比例涨跌
     */
    Obj.onDateEditMarket = function (data) {
        admin.putTempData('formOk', false);
        var index = top.layui.admin.open({
            type: 2,
            maxmin:true,
            title: "区间比例涨跌",
            area:["800px","420px"],
            content: Feng.ctxPath + '/market/market_date_rate_edit?swapId=' + data.swapId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Obj.tableId);
            },
        });
        // console.log(index)
        // console.log(top)
        // console.log(layui)
        top.layui.layer.full(index); //执行最大化
    };



    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Obj.tableId,
        url: Feng.ctxPath + '/market/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        //limit:100,
        //limits: [100,200,300,400,500],
        cols: Obj.initColumn()
    });

    // 工具条点击事件
    table.on('tool(' + Obj.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'one-edit') {
            // 一口价
            Obj.onEditMarket(data,1);
        }
        if (layEvent === 'price-edit') {
            // 价格
            Obj.onEditMarket(data,2);
        }
        if (layEvent === 'rate-edit') {
            // 百分比
            Obj.onEditMarket(data,3);
        }
        if (layEvent === 'date-rate-edit') {
            // 百分比
            Obj.onDateEditMarket(data);
        }
        else if (layEvent === 'reset') {
            // 重置
            Obj.onResetMarket(data);
        }
    });
});
