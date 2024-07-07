layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

    /**
     * 账户信息管理初始化
     */
    var Wallet = {
        tableId: "walletTable",	//表格id
        condition: {
            walletId: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Wallet.initColumn = function () {
        return [[
            // {type: 'checkbox'},
            {field: 'walletId', hide: true, sort: true, title: 'id'},
            // {field: 'memberId', sort: true, title: '用户id'},
            {field: 'memberIdValue', sort: true, title: '用户'},
            {field: 'usedPrice', sort: true, title: '可用额度'},
            {field: 'lockedPrice', sort: true, title: '锁定额度'},
            // {field: 'mortgagePrice', sort: true, title: '质押额度'},
            // {field: 'financesPrice', sort: true, title: '理财额度'},
            {field: 'type', sort: true, title: '类型'},
            // {field: 'version', sort: true, title: '版本'},
            {field: 'updateTime', sort: true, title: '更新时间'},
            // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


    /**
     * 点击查询按钮
     */
    Wallet.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['walletId'] = Wallet.condition.walletId;
        table.reload(Wallet.tableId, {where: queryData});
    };


    /**
     * 打开查看账户信息详情
     */
    Wallet.openWalletDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                title: '账户信息详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/wallet/wallet_edit/' + Wallet.seItem.id
            });
            this.layerIndex = index;
        }
    };


    /**
     * 删除账户信息
     *
     */
    Wallet.onDeleteWallet = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/wallet/delete", function () {
                Feng.success("删除成功!");
                table.reload(Wallet.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("walletId", data.walletId);
            ajax.start();
        };
        Feng.confirm("是否删除账户信息 ?", operation);
    };

    Wallet.releaseFot = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/wallet/releaseFot", function () {
                Feng.success("冻结释放中，耐心等待。。。。");
                table.reload(Wallet.tableId);
            }, function (data) {
                Feng.error("失败!" + data.responseJSON.message + "!");
            });
            ajax.start();
        };
        Feng.confirm("是否一键释放FOT，谨慎操作 ?", operation);
    };

    /**
     * 弹出添加账户信息
     */
    Wallet.openAddWallet = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加账户信息',
            area: ["800px", "420px"],
            content: Feng.ctxPath + '/wallet/wallet_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Wallet.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Wallet.exportExcel = function () {
        var checkRows = table.checkStatus(Wallet.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
    /**
     * 点击编辑账户信息
     *
     */
    Wallet.onEditWallet = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改账户信息',
            area: ["800px", "420px"],
            content: Feng.ctxPath + '/wallet/wallet_edit?walletId=' + data.walletId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Wallet.tableId);
            }
        });
    };
    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Wallet.tableId,
        url: Feng.ctxPath + '/wallet/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit: 100,
        limits: [100, 200, 300, 400, 500],
        cols: Wallet.initColumn()
    });
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Wallet.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Wallet.openAddWallet();
    });

    // 一键释放fot
    $('#btnReleaseFot').click(function () {
        Wallet.releaseFot();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Wallet.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Wallet.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Wallet.onEditWallet(data);
        } else if (layEvent === 'delete') {
            Wallet.onDeleteWallet(data);
        }
    });
});
