layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var form = layui.form;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 行情涨跌控制管理初始化
 */
var Quotes = {
    tableId: "quotesTable",	//表格id
    condition: {
        quotesId: ""
    }
};

/**
 * 初始化表格的列
 */
Quotes.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'quotesId', sort: true, title: 'id'},
         {field: 'symbols', title: '币种'},
         {field: 'value',  title: '拉升比例'},
         {field: 'keepTimes',  title: '持续时间'},
         {field: 'version',  title: '拉升次数'},
         {field: 'groupValue',  title: '当前拉升数值'},
         {field: 'status', templet: '#statusTpl', title: '状态'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Quotes.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['quotesId'] = Quotes.condition.quotesId;
        table.reload(Quotes.tableId, {where: queryData});
    };

    Quotes.changeUserStatus = function (quotesId, checked) {
        if (checked) {
            var ajax = new $ax(Feng.ctxPath + "/quotes/unfreeze", function (data) {

            }, function (data) {
                Feng.success("关闭成功!");
                table.reload(Quotes.tableId);
            });
            ajax.set("quotesId", quotesId);
            ajax.start();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/quotes/freeze", function (data) {

            }, function (data) {

                Feng.success("开启成功!");
                table.reload(Quotes.tableId);
            });
            ajax.set("quotesId", quotesId);
            ajax.start();
        }
    };
    // 修改user状态
    form.on('switch(status)', function (obj) {

        var quotesId = obj.elem.value;
        var checked = obj.elem.checked ? true : false;

        Quotes.changeUserStatus(quotesId, !checked);
    });


    /**
 * 打开查看行情涨跌控制详情
 */
Quotes.openQuotesDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '行情涨跌控制详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/quotes/quotes_edit/' + Quotes.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除行情涨跌控制
     *
     */
    Quotes.onDeleteQuotes = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/quotes/delete", function () {
                Feng.success("删除成功!");
                table.reload(Quotes.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("quotesId", data.quotesId);
            ajax.start();
        };
        Feng.confirm("是否删除行情涨跌控制 ?", operation);
    };

    /**
     * 弹出添加行情涨跌控制
     */
    Quotes.openAddQuotes = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加行情涨跌控制',
            area:["800px","420px"],
            content: Feng.ctxPath + '/quotes/quotes_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Quotes.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Quotes.exportExcel = function () {
        var checkRows = table.checkStatus(Quotes.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑行情涨跌控制
     *
     */
     Quotes.onEditQuotes = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改行情涨跌控制',
            area:["800px","420px"],
            content: Feng.ctxPath + '/quotes/quotes_edit?quotesId=' + data.quotesId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Quotes.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Quotes.tableId,
        url: Feng.ctxPath + '/quotes/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Quotes.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Quotes.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Quotes.openAddQuotes();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Quotes.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Quotes.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Quotes.onEditQuotes(data);
        } else if (layEvent === 'delete') {
            Quotes.onDeleteQuotes(data);
        }
    });
});