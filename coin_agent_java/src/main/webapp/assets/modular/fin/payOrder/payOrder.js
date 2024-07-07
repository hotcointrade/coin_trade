layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 支付订单管理初始化
 */
var PayOrder = {
    tableId: "payOrderTable",	//表格id
    condition: {
        payOrderId: ""
    }
};

/**
 * 初始化表格的列
 */
PayOrder.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'payOrderId', hide: true, sort: true, title: 'id'},
         {field: 'memberId', sort: true, title: '用户id'},
         {field: 'orderNo', sort: true, title: '订单号'},
         {field: 'totalPrice', sort: true, title: '金额'},
         {field: 'unitPrice', sort: true, title: '单价'},
         {field: 'number', sort: true, title: '数量'},
         {field: 'actualPrice', sort: true, title: '实际金额'},
         {field: 'fee', sort: true, title: '手续费'},
         {field: 'payType', sort: true, title: '支付平台(微信、支付宝、银联)'},
         {field: 'platformNumber', sort: true, title: '支付流水号（第三方接口返回流水号）'},
         {field: 'platformStatus', sort: true, title: '支付状态（第三方接口返回状态）'},
         {field: 'device', sort: true, title: '支付设备 IOS ANDROID'},
         {field: 'closeTime', sort: true, title: '订单关闭时间'},
         {field: 'finishTime', sort: true, title: '订单完成时间'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    PayOrder.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['payOrderId'] = PayOrder.condition.payOrderId;
        table.reload(PayOrder.tableId, {where: queryData});
    };



/**
 * 打开查看支付订单详情
 */
PayOrder.openPayOrderDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '支付订单详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/payOrder/payOrder_edit/' + PayOrder.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除支付订单
     *
     */
    PayOrder.onDeletePayOrder = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/payOrder/delete", function () {
                Feng.success("删除成功!");
                table.reload(PayOrder.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("payOrderId", data.payOrderId);
            ajax.start();
        };
        Feng.confirm("是否删除支付订单 ?", operation);
    };

    /**
     * 弹出添加支付订单
     */
    PayOrder.openAddPayOrder = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加支付订单',
            area:["800px","420px"],
            content: Feng.ctxPath + '/payOrder/payOrder_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(PayOrder.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    PayOrder.exportExcel = function () {
        var checkRows = table.checkStatus(PayOrder.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.payOrder.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑支付订单
     *
     */
     PayOrder.onEditPayOrder = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改支付订单',
            area:["800px","420px"],
            content: Feng.ctxPath + '/payOrder/payOrder_edit?payOrderId=' + data.payOrderId,
            end: function () {
                admin.getTempData('formOk') && table.reload(PayOrder.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + PayOrder.tableId,
        url: Feng.ctxPath + '/payOrder/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: PayOrder.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        PayOrder.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        PayOrder.openAddPayOrder();
    });

    // 导出excel
    $('#btnExp').click(function () {
        PayOrder.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + PayOrder.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            PayOrder.onEditPayOrder(data);
        } else if (layEvent === 'delete') {
            PayOrder.onDeletePayOrder(data);
        }
    });
});