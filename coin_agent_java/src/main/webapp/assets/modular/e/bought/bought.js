layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 法币交易管理初始化
 */
var Bought = {
    tableId: "boughtTable",	//表格id
    condition: {
        boughtId: ""
    }
};

/**
 * 初始化表格的列
 */
Bought.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'boughtId', hide: true, sort: true, title: 'id'},
         {field: 'orderNo', sort: true, title: '单号'},
         {field: 'memberIdValue', sort: true, title: '用户'},
         {field: 'price', sort: true, title: '金额'},
         {field: 'actualPrice', sort: true, title: '实际金额'},
         {field: 'numbers', sort: true, title: '购买数量'},
         {field: 'unit', sort: true, title: '单价'},
         // {field: 'rechargeAddress', sort: true, title: ''},
         // {field: 'fromAddress', sort: true, title: ''},
         // {field: 'txHash', sort: true, title: '唯一标识'},
         // {field: 'chainType', sort: true, title: '链类型(Omni ERC20 TRC20)'},
         {field: 'type', sort: true, title: '币种'},
         {field: 'statusValue', sort: true, title: '状态'},
         {field: 'createTime', sort: true, title: '创建时间'},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Bought.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['boughtId'] = Bought.condition.boughtId;
        table.reload(Bought.tableId, {where: queryData});
    };



/**
 * 打开查看法币交易详情
 */
Bought.openBoughtDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '法币交易详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bought/bought_edit/' + Bought.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除法币交易
     *
     */
    Bought.onDeleteBought = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bought/delete", function () {
                Feng.success("删除成功!");
                table.reload(Bought.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("boughtId", data.boughtId);
            ajax.start();
        };
        Feng.confirm("是否删除法币交易 ?", operation);
    };

    /**
     * 弹出添加法币交易
     */
    Bought.openAddBought = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加法币交易',
            area:["800px","420px"],
            content: Feng.ctxPath + '/bought/bought_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Bought.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Bought.exportExcel = function () {
        var checkRows = table.checkStatus(Bought.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑法币交易
     *
     */
     Bought.onEditBought = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改法币交易',
            area:["800px","420px"],
            content: Feng.ctxPath + '/bought/bought_edit?boughtId=' + data.boughtId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Bought.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Bought.tableId,
        url: Feng.ctxPath + '/bought/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Bought.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Bought.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Bought.openAddBought();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Bought.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Bought.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Bought.onEditBought(data);
        } else if (layEvent === 'delete') {
            Bought.onDeleteBought(data);
        }
    });
});