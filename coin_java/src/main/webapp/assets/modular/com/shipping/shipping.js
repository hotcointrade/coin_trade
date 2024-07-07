layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 收货地址管理初始化
 */
var Shipping = {
    tableId: "shippingTable",	//表格id
    condition: {
        shippingId: ""
    }
};

/**
 * 初始化表格的列
 */
Shipping.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'shippingId', hide: true, sort: true, title: 'id'},
         {field: 'memberId', sort: true, title: '用户'},
         {field: 'name', sort: true, title: '收货人姓名'},
         {field: 'phone', sort: true, title: '电话'},
         {field: 'address', sort: true, title: '收货地址'},
         {field: 'createTime', sort: true, title: '创建时间'},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Shipping.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['shippingId'] = Shipping.condition.shippingId;
        table.reload(Shipping.tableId, {where: queryData});
    };



/**
 * 打开查看收货地址详情
 */
Shipping.openShippingDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '收货地址详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/shipping/shipping_edit/' + Shipping.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除收货地址
     *
     */
    Shipping.onDeleteShipping = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/shipping/delete", function () {
                Feng.success("删除成功!");
                table.reload(Shipping.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("shippingId", data.shippingId);
            ajax.start();
        };
        Feng.confirm("是否删除收货地址 ?", operation);
    };

    /**
     * 弹出添加收货地址
     */
    Shipping.openAddShipping = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加收货地址',
            area:["800px","420px"],
            content: Feng.ctxPath + '/shipping/shipping_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Shipping.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Shipping.exportExcel = function () {
        var checkRows = table.checkStatus(Shipping.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.shipping.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑收货地址
     *
     */
     Shipping.onEditShipping = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改收货地址',
            area:["800px","420px"],
            content: Feng.ctxPath + '/shipping/shipping_edit?shippingId=' + data.shippingId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Shipping.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Shipping.tableId,
        url: Feng.ctxPath + '/shipping/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Shipping.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Shipping.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Shipping.openAddShipping();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Shipping.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Shipping.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Shipping.onEditShipping(data);
        } else if (layEvent === 'delete') {
            Shipping.onDeleteShipping(data);
        }
    });
});