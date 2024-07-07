layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 杠杆倍率管理初始化
 */
var Leverage = {
    tableId: "leverageTable",	//表格id
    condition: {
        futuresLeverageId: ""
    }
};

/**
 * 初始化表格的列
 */
Leverage.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'futuresLeverageId', hide: true, sort: true, title: 'id'},
         {field: 'name', sort: true, title: '名称'},
         {field: 'value', sort: true, title: '倍率'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Leverage.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['leverageId'] = Leverage.condition.futuresLeverageId;
        table.reload(Leverage.tableId, {where: queryData});
    };



/**
 * 打开查看杠杆倍率详情
 */
Leverage.openLeverageDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '杠杆倍率详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/e_futures_leverage/leverage_edit/' + Leverage.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除杠杆倍率
     *
     */
    Leverage.onDeleteLeverage = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/e_futures_leverage/delete", function () {
                Feng.success("删除成功!");
                table.reload(Leverage.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("leverageId", data.futuresLeverageId);
            ajax.start();
        };
        Feng.confirm("是否删除杠杆倍率 ?", operation);
    };

    /**
     * 弹出添加杠杆倍率
     */
    Leverage.openAddLeverage = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加杠杆倍率',
            area:["800px","420px"],
            content: Feng.ctxPath + '/e_futures_leverage/leverage_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Leverage.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Leverage.exportExcel = function () {
        var checkRows = table.checkStatus(Leverage.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑杠杆倍率
     *
     */
     Leverage.onEditLeverage = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改杠杆倍率',
            area:["800px","420px"],
            content: Feng.ctxPath + '/e_futures_leverage/leverage_edit?leverageId=' + data.futuresLeverageId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Leverage.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Leverage.tableId,
        url: Feng.ctxPath + '/e_futures_leverage/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Leverage.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Leverage.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Leverage.openAddLeverage();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Leverage.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Leverage.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Leverage.onEditLeverage(data);
        } else if (layEvent === 'delete') {
            Leverage.onDeleteLeverage(data);
        }
    });
});