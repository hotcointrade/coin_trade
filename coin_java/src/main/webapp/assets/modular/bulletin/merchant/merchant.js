layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

    /**
     * 商户管理初始化
     */
    var Merchant = {
        tableId: "merchantTable",	//表格id
        condition: {
            merchantId: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Merchant.initColumn = function () {
        return [[
            // {type: 'checkbox'},
            {field: 'merchantId', hide: true, sort: true, title: 'id'},
            {field: 'openid',  title: 'UID', width: 210},
            {field: 'merchantName', title: '商户名称', width: 188},
            {field: 'registerTime', sort: true, title: '入驻时间', width: 162},
            {field: 'name', title: '联系人', width: 100},
            {field: 'phone', title: '联系方式', width: 130},
            {field: 'totalPrice', sort: true, title: '账户余额', width: 110},
            {field: 'rate', hide: true, sort: true, title: '手续费率', width: 100},
            {field: 'rateValue', title: '手续费率', width: 120},
            {field: 'remark', title: '商户链接', width: 250},
            {field: 'account', title: '商户账号', width: 120},
            {field: 'password', title: '商户key', width: 250},
            {field: 'orderCount', title: '发起支付订单总数', width: 120},
            {field: 'orderOkCount', title: '订单成功数', width: 120},
            {field: 'orderTotalPrice', title: '订单价格总额', width: 120},
            {field: 'orderOkTotalPrice', title: '订单成交总额', width: 120},
            {align: 'center',fixed: 'right', toolbar: '#tableBar', title: '操作', width: 200}
        ]];
    };


    /**
     * 点击查询按钮
     */
    Merchant.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['merchantId'] = Merchant.condition.merchantId;
        table.reload(Merchant.tableId, {where: queryData});
    };


    /**
     * 打开查看商户详情
     */
    Merchant.openMerchantDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                title: '商户详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/merchant/merchant_edit/' + Merchant.seItem.id
            });
            this.layerIndex = index;
        }
    };


    /**
     * 删除商户
     *
     */
    Merchant.onDeleteMerchant = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/merchant/delete", function () {
                Feng.success("删除成功!");
                table.reload(Merchant.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("merchantId", data.merchantId);
            ajax.start();
        };
        Feng.confirm("是否删除商户 ?", operation);
    };

    /**
     * 重置商户密码
     *
     */
    Merchant.resetPwd = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/merchant/resetPwd", function () {
                Feng.success("重置成功!");
                table.reload(Merchant.tableId);
            }, function (data) {
                Feng.error("重置失败!!");
            });
            ajax.set("merchantId", data.merchantId);
            ajax.start();
        };
        Feng.confirm("是否重置商户密码（重置后密码为：123456） ?", operation);
    };

    /**
     * 弹出添加商户
     */
    Merchant.openAddMerchant = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加商户',
            area: ["800px", "420px"],
            content: Feng.ctxPath + '/merchant/merchant_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Merchant.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Merchant.exportExcel = function () {
        var checkRows = table.checkStatus(Merchant.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.merchant.id, checkRows.data, 'xls');
        }
    };
    /**
     * 点击编辑商户
     *
     */
    Merchant.onEditMerchant = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改商户',
            area: ["800px", "420px"],
            content: Feng.ctxPath + '/merchant/merchant_edit?merchantId=' + data.merchantId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Merchant.tableId);
            }
        });
    };
    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Merchant.tableId,
        url: Feng.ctxPath + '/merchant/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit: 100,
        limits: [100, 200, 300, 400, 500],
        cols: Merchant.initColumn()
    });
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Merchant.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Merchant.openAddMerchant();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Merchant.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Merchant.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Merchant.onEditMerchant(data);
        } else if (layEvent === 'delete') {
            Merchant.onDeleteMerchant(data);
        } else if (layEvent === 'resetPwd') {
            Merchant.resetPwd(data);
        }

    });


    //商户数量及总余额
    var ajax = new $ax(Feng.ctxPath + "/merchant/statistics");
    var result = ajax.start();
    console.log("result:" + JSON.stringify(result));
    $("#merchantNum").html(result.merchantNum);
    $("#merchantTotalPrice").html(result.merchantTotalPrice);


});