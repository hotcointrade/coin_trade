layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 异常信息管理初始化
 */
var Exception = {
    tableId: "exceptionTable",	//表格id
    condition: {
        exceptionId: ""
    }
};

/**
 * 初始化表格的列
 */
Exception.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'exceptionId', hide: true, sort: true, title: 'id'},
         {field: 'method', sort: true, title: '异常方法'},
         {field: 'msg', sort: true, title: '异常信息'},
         {field: 'content', sort: true, title: '详情内容'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Exception.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['exceptionId'] = Exception.condition.exceptionId;
        table.reload(Exception.tableId, {where: queryData});
    };



/**
 * 打开查看异常信息详情
 */
Exception.openExceptionDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '异常信息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/exception/exception_edit/' + Exception.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除异常信息
     *
     */
    Exception.onDeleteException = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/exception/delete", function () {
                Feng.success("删除成功!");
                table.reload(Exception.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("exceptionId", data.exceptionId);
            ajax.start();
        };
        Feng.confirm("是否删除异常信息 ?", operation);
    };

    /**
     * 弹出添加异常信息
     */
    Exception.openAddException = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加异常信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/exception/exception_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Exception.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Exception.exportExcel = function () {
        var checkRows = table.checkStatus(Exception.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.exception.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑异常信息
     *
     */
     Exception.onEditException = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改异常信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/exception/exception_edit?exceptionId=' + data.exceptionId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Exception.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Exception.tableId,
        url: Feng.ctxPath + '/exception/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Exception.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Exception.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Exception.openAddException();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Exception.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Exception.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Exception.onEditException(data);
        } else if (layEvent === 'delete') {
            Exception.onDeleteException(data);
        }
    });
});