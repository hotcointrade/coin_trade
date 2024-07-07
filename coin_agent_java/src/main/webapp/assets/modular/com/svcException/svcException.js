layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 异常信息管理初始化
 */
var SvcException = {
    tableId: "svcExceptionTable",	//表格id
    condition: {
        svcExceptionId: ""
    }
};

/**
 * 初始化表格的列
 */
SvcException.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'svcExceptionId', hide: true, sort: true, title: 'id'},
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
    SvcException.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['svcExceptionId'] = SvcException.condition.svcExceptionId;
        table.reload(SvcException.tableId, {where: queryData});
    };



/**
 * 打开查看异常信息详情
 */
SvcException.openSvcExceptionDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '异常信息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/svcException/svcException_edit/' + SvcException.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除异常信息
     *
     */
    SvcException.onDeleteSvcException = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/svcException/delete", function () {
                Feng.success("删除成功!");
                table.reload(SvcException.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("svcExceptionId", data.svcExceptionId);
            ajax.start();
        };
        Feng.confirm("是否删除异常信息 ?", operation);
    };

    /**
     * 弹出添加异常信息
     */
    SvcException.openAddSvcException = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加异常信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/svcException/svcException_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(SvcException.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    SvcException.exportExcel = function () {
        var checkRows = table.checkStatus(SvcException.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.svcException.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑异常信息
     *
     */
     SvcException.onEditSvcException = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改异常信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/svcException/svcException_edit?svcExceptionId=' + data.svcExceptionId,
            end: function () {
                admin.getTempData('formOk') && table.reload(SvcException.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + SvcException.tableId,
        url: Feng.ctxPath + '/svcException/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: SvcException.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        SvcException.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        SvcException.openAddSvcException();
    });

    // 导出excel
    $('#btnExp').click(function () {
        SvcException.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + SvcException.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            SvcException.onEditSvcException(data);
        } else if (layEvent === 'delete') {
            SvcException.onDeleteSvcException(data);
        }
    });
});