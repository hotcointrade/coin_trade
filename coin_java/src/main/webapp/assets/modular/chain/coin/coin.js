layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 用户钱包地址管理初始化
 */
var Coin = {
    tableId: "coinTable",	//表格id
    condition: {
        coinId: ""
    }
};

/**
 * 初始化表格的列
 */
Coin.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'chainCoinId', hide: true, sort: true, title: 'id'},
         {field: 'memberId', sort: true, title: '用户'},
         {field: 'address', sort: true, title: '币地址'},
         {field: 'remark', sort: true, title: '备注'},
         {field: 'createTime', sort: true, title: '创建时间'},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Coin.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['coinId'] = Coin.condition.coinId;
        table.reload(Coin.tableId, {where: queryData});
    };



/**
 * 打开查看用户钱包地址详情
 */
Coin.openCoinDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户钱包地址详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/coin/coin_edit/' + Coin.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除用户钱包地址
     *
     */
    Coin.onDeleteCoin = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/coin/delete", function () {
                Feng.success("删除成功!");
                table.reload(Coin.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("coinId", data.coinId);
            ajax.start();
        };
        Feng.confirm("是否删除用户钱包地址 ?", operation);
    };

    /**
     * 弹出添加用户钱包地址
     */
    Coin.openAddCoin = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加用户钱包地址',
            area:["800px","420px"],
            content: Feng.ctxPath + '/coin/coin_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Coin.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Coin.exportExcel = function () {
        var checkRows = table.checkStatus(Coin.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.coin.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑用户钱包地址
     *
     */
     Coin.onEditCoin = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改用户钱包地址',
            area:["800px","420px"],
            content: Feng.ctxPath + '/coin/coin_edit?coinId=' + data.coinId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Coin.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Coin.tableId,
        url: Feng.ctxPath + '/coin/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Coin.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Coin.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Coin.openAddCoin();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Coin.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Coin.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Coin.onEditCoin(data);
        } else if (layEvent === 'delete') {
            Coin.onDeleteCoin(data);
        }
    });
});