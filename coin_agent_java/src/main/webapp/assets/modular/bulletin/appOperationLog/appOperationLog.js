layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 日志管理初始化
 */
var AppOperationLog = {
    tableId: "appOperationLogTable",	//表格id
    condition: {
        appOperationLogId: ""
    }
};

/**
 * 初始化表格的列
 */
AppOperationLog.initColumn = function () {
    return [[
        // {type: 'checkbox'},
        {field: 'appOperationLogId', hide: true, sort: true, title: 'id'},
        {field: 'account', title: '用户账号',width:140},
        {field: 'phone', title: '用户手机号',width:130},
        {field: 'message', sort: true, title: '操作',width:200},
        {field: 'createTime', sort: true, title: '创建时间',width:200},
        // {align: 'center', toolbar: '#tableBar', title: '操作', width: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    AppOperationLog.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['appOperationLogId'] = AppOperationLog.condition.appOperationLogId;
        table.reload(AppOperationLog.tableId, {where: queryData});
    };



/**
 * 打开查看日志详情
 */
AppOperationLog.openAppOperationLogDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '日志详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/appOperationLog/appOperationLog_edit/' + AppOperationLog.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除日志
     *
     */
    AppOperationLog.onDeleteAppOperationLog = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/appOperationLog/delete", function () {
                Feng.success("删除成功!");
                table.reload(AppOperationLog.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("appOperationLogId", data.appOperationLogId);
            ajax.start();
        };
        Feng.confirm("是否删除日志 ?", operation);
    };

    /**
     * 弹出添加日志
     */
    AppOperationLog.openAddAppOperationLog = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加日志',
            area:["800px","420px"],
            content: Feng.ctxPath + '/appOperationLog/appOperationLog_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(AppOperationLog.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    AppOperationLog.exportExcel = function () {
        var checkRows = table.checkStatus(AppOperationLog.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.appOperationLog.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑日志
     *
     */
     AppOperationLog.onEditAppOperationLog = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改日志',
            area:["800px","420px"],
            content: Feng.ctxPath + '/appOperationLog/appOperationLog_edit?appOperationLogId=' + data.appOperationLogId,
            end: function () {
                admin.getTempData('formOk') && table.reload(AppOperationLog.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + AppOperationLog.tableId,
        url: Feng.ctxPath + '/appOperationLog/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: AppOperationLog.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        AppOperationLog.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        AppOperationLog.openAddAppOperationLog();
    });

    // 导出excel
    $('#btnExp').click(function () {
        AppOperationLog.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + AppOperationLog.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            AppOperationLog.onEditAppOperationLog(data);
        } else if (layEvent === 'delete') {
            AppOperationLog.onDeleteAppOperationLog(data);
        }
    });
});