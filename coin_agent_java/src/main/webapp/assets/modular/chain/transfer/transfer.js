layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 转账信息管理初始化
 */
var Transfer = {
    tableId: "transferTable",	//表格id
    condition: {
        transferId: ""
    }
};

/**
 * 初始化表格的列
 */
Transfer.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'transferId', hide: true, sort: true, title: 'id'},
         {field: 'orderNo', sort: true, title: '单号'},
         {field: 'memberId', sort: true, title: '用户'},
         {field: 'price', sort: true, title: '转账数量'},
         {field: 'actualPrice', sort: true, title: '实际金额'},
         {field: 'fee', sort: true, title: '手续费'},
         {field: 'type', sort: true, title: '转账币种'},
         {field: 'toAddress', sort: true, title: '地址'},
         // {field: 'txHash', sort: true, title: '哈希值'},
         // {field: 'response', sort: true, title: '返回信息'},
         {field: 'createTime', sort: true, title: '创建时间'},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Transfer.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['transferId'] = Transfer.condition.transferId;
        table.reload(Transfer.tableId, {where: queryData});
    };



/**
 * 打开查看转账信息详情
 */
Transfer.openTransferDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '转账信息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/transfer/transfer_edit/' + Transfer.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除转账信息
     *
     */
    Transfer.onDeleteTransfer = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/transfer/delete", function () {
                Feng.success("删除成功!");
                table.reload(Transfer.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("transferId", data.transferId);
            ajax.start();
        };
        Feng.confirm("是否删除转账信息 ?", operation);
    };

    /**
     * 弹出添加转账信息
     */
    Transfer.openAddTransfer = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加转账信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/transfer/transfer_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Transfer.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Transfer.exportExcel = function () {
        var checkRows = table.checkStatus(Transfer.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑转账信息
     *
     */
     Transfer.onEditTransfer = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改转账信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/transfer/transfer_edit?transferId=' + data.transferId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Transfer.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Transfer.tableId,
        url: Feng.ctxPath + '/transfer/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Transfer.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Transfer.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Transfer.openAddTransfer();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Transfer.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Transfer.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Transfer.onEditTransfer(data);
        } else if (layEvent === 'delete') {
            Transfer.onDeleteTransfer(data);
        }
    });
});