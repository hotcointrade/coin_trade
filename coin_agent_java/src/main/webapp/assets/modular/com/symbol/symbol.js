layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 币种管理初始化
 */
var Symbol = {
    tableId: "symbolTable",	//表格id
    condition: {
        symbolId: ""
    }
};

/**
 * 初始化表格的列
 */
Symbol.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'symbolId', hide: true, sort: true, title: 'id'},
         {field: 'name', sort: true, title: '名称'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Symbol.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['symbolId'] = Symbol.condition.symbolId;
        table.reload(Symbol.tableId, {where: queryData});
    };



/**
 * 打开查看币种详情
 */
Symbol.openSymbolDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '币种详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/symbol/symbol_edit/' + Symbol.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除币种
     *
     */
    Symbol.onDeleteSymbol = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/symbol/delete", function () {
                Feng.success("删除成功!");
                table.reload(Symbol.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("symbolId", data.symbolId);
            ajax.start();
        };
        Feng.confirm("是否删除币种 ?", operation);
    };

    /**
     * 弹出添加币种
     */
    Symbol.openAddSymbol = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加币种',
            area:["800px","420px"],
            content: Feng.ctxPath + '/symbol/symbol_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Symbol.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Symbol.exportExcel = function () {
        var checkRows = table.checkStatus(Symbol.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑币种
     *
     */
     Symbol.onEditSymbol = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改币种',
            area:["800px","420px"],
            content: Feng.ctxPath + '/symbol/symbol_edit?symbolId=' + data.symbolId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Symbol.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Symbol.tableId,
        url: Feng.ctxPath + '/symbol/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Symbol.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Symbol.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Symbol.openAddSymbol();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Symbol.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Symbol.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Symbol.onEditSymbol(data);
        } else if (layEvent === 'delete') {
            Symbol.onDeleteSymbol(data);
        }
    });
});