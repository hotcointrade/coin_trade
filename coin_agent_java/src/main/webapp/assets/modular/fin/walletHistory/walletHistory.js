layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 钱包历史变更管理初始化
 */
var WalletHistory = {
    tableId: "walletHistoryTable",	//表格id
    condition: {
        walletHistoryId: ""
    }
};

/**
 * 初始化表格的列
 */
WalletHistory.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'walletHistoryId', hide: true, sort: true, title: 'id'},
         {field: 'memberId', sort: true, title: ''},
         {field: 'totalPrice', sort: true, title: '钱包总额'},
         {field: 'returnPrice', sort: true, title: '平台返还总额'},
         {field: 'ticketPrice', sort: true, title: '消费券总额'},
         {field: 'type', sort: true, title: '类型：BTC 、CNY:人民币、 POINT：积分'},
         {field: 'version', sort: true, title: '版本'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    WalletHistory.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['walletHistoryId'] = WalletHistory.condition.walletHistoryId;
        table.reload(WalletHistory.tableId, {where: queryData});
    };



/**
 * 打开查看钱包历史变更详情
 */
WalletHistory.openWalletHistoryDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '钱包历史变更详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/walletHistory/walletHistory_edit/' + WalletHistory.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除钱包历史变更
     *
     */
    WalletHistory.onDeleteWalletHistory = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/walletHistory/delete", function () {
                Feng.success("删除成功!");
                table.reload(WalletHistory.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("walletHistoryId", data.walletHistoryId);
            ajax.start();
        };
        Feng.confirm("是否删除钱包历史变更 ?", operation);
    };

    /**
     * 弹出添加钱包历史变更
     */
    WalletHistory.openAddWalletHistory = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加钱包历史变更',
            area:["800px","420px"],
            content: Feng.ctxPath + '/walletHistory/walletHistory_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(WalletHistory.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    WalletHistory.exportExcel = function () {
        var checkRows = table.checkStatus(WalletHistory.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.walletHistory.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑钱包历史变更
     *
     */
     WalletHistory.onEditWalletHistory = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改钱包历史变更',
            area:["800px","420px"],
            content: Feng.ctxPath + '/walletHistory/walletHistory_edit?walletHistoryId=' + data.walletHistoryId,
            end: function () {
                admin.getTempData('formOk') && table.reload(WalletHistory.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + WalletHistory.tableId,
        url: Feng.ctxPath + '/walletHistory/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: WalletHistory.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        WalletHistory.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        WalletHistory.openAddWalletHistory();
    });

    // 导出excel
    $('#btnExp').click(function () {
        WalletHistory.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + WalletHistory.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            WalletHistory.onEditWalletHistory(data);
        } else if (layEvent === 'delete') {
            WalletHistory.onDeleteWalletHistory(data);
        }
    });
});