layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 收款方式管理初始化
 */
var Payment = {
    tableId: "paymentTable",	//表格id
    condition: {
        paymentId: ""
    }
};

/**
 * 初始化表格的列
 */
Payment.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'paymentId', hide: true, sort: true, title: 'id'},
         {field: 'memberId', hide: true,sort: true, title: '用户'},
         {field: 'memberIdValue', sort: true, title: '用户'},
         {field: 'name', sort: true, title: '姓名'},
         {field: 'idcard', sort: true, title: '账号'},
         {field: 'type', hide: true, sort: true, title: '类型'},
         {field: 'typeValue', sort: true, title: '类型'},
         // {field: 'img', sort: true, title: '收款码'},
         {field: 'bank', sort: true, title: '银行(银行卡类型)'},
         {field: 'branch', sort: true, title: '支行(银行卡类型)'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBarDetail', title: '详情', minWidth: 200},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};

    Payment.imgDetail = function (param) {
        var ajax = new $ax(Feng.ctxPath + "/payment/content/" + param.paymentId, function (data) {
            Feng.bulletinDetailImg("详情", data.imgs);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };

    Feng.bulletinDetailImg = function (title, info) {
        var el="<div><img src='"+info+"'/></div>"
        top.layer.open({
            title: title,
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['950px', '600px'], //宽高
            // content: escape2Html(info)
            content: el
        });
    };

    /**
     * 点击查询按钮
     */
    Payment.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['paymentId'] = Payment.condition.paymentId;
        table.reload(Payment.tableId, {where: queryData});
    };



/**
 * 打开查看收款方式详情
 */
Payment.openPaymentDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '收款方式详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/payment/payment_edit/' + Payment.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除收款方式
     *
     */
    Payment.onDeletePayment = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/payment/delete", function () {
                Feng.success("删除成功!");
                table.reload(Payment.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("paymentId", data.paymentId);
            ajax.start();
        };
        Feng.confirm("是否删除收款方式 ?", operation);
    };

    /**
     * 弹出添加收款方式
     */
    Payment.openAddPayment = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加收款方式',
            area:["800px","420px"],
            content: Feng.ctxPath + '/payment/payment_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Payment.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Payment.exportExcel = function () {
        var checkRows = table.checkStatus(Payment.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑收款方式
     *
     */
     Payment.onEditPayment = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改收款方式',
            area:["800px","420px"],
            content: Feng.ctxPath + '/payment/payment_edit?paymentId=' + data.paymentId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Payment.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Payment.tableId,
        url: Feng.ctxPath + '/payment/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Payment.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Payment.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Payment.openAddPayment();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Payment.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Payment.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Payment.onEditPayment(data);
        } else if (layEvent === 'delete') {
            Payment.onDeletePayment(data);
        }else if (layEvent === 'imgDetail') {//图片
            Payment.imgDetail(data);
        }
    });
});