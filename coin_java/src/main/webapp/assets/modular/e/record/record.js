layui.use(['table', 'admin', 'ax', 'ztree', 'laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

/**
 * 委托单信息管理初始化
 */
var Match = {
    tableId: "matchTable",	//表格id
    condition: {
        matchId: ""
        ,timeLimit:""
        ,status:""
    }
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
Match.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'matchId', hide: true, sort: true, title: 'id'},
         {field: 'orderNo', sort: true, title: '订单号'},
         // {field: 'memberId', sort: true, title: '用户'},
         {field: 'memberIdValue', sort: true, title: '用户'},
        {field: 'dealWay', sort: true, title: '交易方式'},
        {field: 'matchType',hide: true, sort: true, title: '交易类型'},
        {field: 'matchTypeValue', sort: true, title: '交易类型',templet:"#matchType"},
        {field: 'symbols', sort: true, title: '交易对'},
         // {field: 'trade', sort: true, title: '交易方式'},
         // {field: 'number', sort: true, title: '交易数量'},
         {field: 'unit', sort: true, title: '单价'},
         {field: 'totalPrice', sort: true, title: '交易总价',width:160},
         {field: 'will', sort: true, title: '委托量',width:160},
         {field: 'ok', sort: true, title: '成交量',width:160},
         {field: 'finishNumber',hide: true, sort: true, title: '完成数量',width:160},
         {field: 'unFinishNumebr',hide: true, sort: true, title: '未完成数量',width:160},
         // {field: 'restFrozen', sort: true, title: '剩余冻结'},
         {field: 'fee',hide: true, sort: true, title: '手续费'},
         {field: 'rate', sort: true, title: '手续费比例'},
         {field: 'convertNumber',hide: true, sort: true, title: '撮合到账量'},

        {field: 'statusValue', sort: true, title: '状态'},
        {field: 'createTime', sort: true, title: '创建时间'},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Match.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['matchId'] = Match.condition.matchId;
        queryData['timeLimit'] = $("#timeLimit").val();
        queryData['status'] = $("#status").val();
        table.reload(Match.tableId, {where: queryData});
    };



/**
 * 打开查看委托单信息详情
 */
Match.openMatchDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '委托单信息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/record/record_edit/' + Match.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除委托单信息
     *
     */
    Match.onDeleteMatch = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/record/delete", function () {
                Feng.success("删除成功!");
                table.reload(Match.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("matchId", data.matchId);
            ajax.start();
        };
        Feng.confirm("是否删除委托单信息 ?", operation);
    };

    /**
     * 弹出添加委托单信息
     */
    Match.openAddMatch = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加委托单信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/record/record_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Match.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Match.exportExcel = function () {
        var checkRows = table.checkStatus(Match.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑委托单信息
     *
     */
     Match.onEditMatch = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改委托单信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/record/record_edit?matchId=' + data.matchId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Match.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Match.tableId,
        url: Feng.ctxPath + '/record/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Match.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Match.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Match.openAddMatch();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Match.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Match.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Match.onEditMatch(data);
        } else if (layEvent === 'delete') {
            Match.onDeleteMatch(data);
        }
    });
});