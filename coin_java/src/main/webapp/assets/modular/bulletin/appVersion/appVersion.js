layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 版本号管理初始化
 */
var AppVersion = {
    tableId: "appVersionTable",	//表格id
    condition: {
        appVersionId: ""
    }
};

/**
 * 初始化表格的列
 */
AppVersion.initColumn = function () {
    return [[
        // {type: 'checkbox'},
        {field: 'appVersionId', hide: true, sort: true, title: 'id'},
        {field: 'remark',  title: '系统',width:200},
        {field: 'version',  title: '版本',width:150},
        {field: 'address',  title: '地址',width:200},
        {field: 'content',  title: '更新内容',width:200},
        {field: 'updateTime',  title: '更新时间',width:230},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    AppVersion.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['appVersionId'] = AppVersion.condition.appVersionId;
        table.reload(AppVersion.tableId, {where: queryData});
    };



/**
 * 打开查看版本号详情
 */
AppVersion.openAppVersionDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '版本号详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/appVersion/appVersion_edit/' + AppVersion.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除版本号
     *
     */
    AppVersion.onDeleteAppVersion = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/appVersion/delete", function () {
                Feng.success("删除成功!");
                table.reload(AppVersion.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("appVersionId", data.appVersionId);
            ajax.start();
        };
        Feng.confirm("是否删除版本号 ?", operation);
    };

    /**
     * 弹出添加版本号
     */
    AppVersion.openAddAppVersion = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加版本号',
            area:["800px","420px"],
            content: Feng.ctxPath + '/appVersion/appVersion_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(AppVersion.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    AppVersion.exportExcel = function () {
        var checkRows = table.checkStatus(AppVersion.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.appVersion.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑版本号
     *
     */
     AppVersion.onEditAppVersion = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改版本号',
            area:["800px","420px"],
            content: Feng.ctxPath + '/appVersion/appVersion_edit?appVersionId=' + data.appVersionId,
            end: function () {
                admin.getTempData('formOk') && table.reload(AppVersion.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + AppVersion.tableId,
        url: Feng.ctxPath + '/appVersion/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: AppVersion.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        AppVersion.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        AppVersion.openAddAppVersion();
    });

    // 导出excel
    $('#btnExp').click(function () {
        AppVersion.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + AppVersion.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            AppVersion.onEditAppVersion(data);
        } else if (layEvent === 'delete') {
            AppVersion.onDeleteAppVersion(data);
        }
    });
});