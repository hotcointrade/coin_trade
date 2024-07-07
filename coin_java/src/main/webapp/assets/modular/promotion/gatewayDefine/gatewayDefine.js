layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 网关定义管理初始化
 */
var GatewayDefine = {
    tableId: "gatewayDefineTable",	//表格id
    condition: {
        gatewayDefineId: ""
    }
};

/**
 * 初始化表格的列
 */
GatewayDefine.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'gatewayDefineId', hide: true, sort: true, title: 'id'},
         {field: 'code', sort: true, title: '网关编码'},
         {field: 'name', sort: true, title: '网关名称'},
         {field: 'value', sort: true, title: '网关值'},
         {field: 'statusValue', sort: true, title: '状态'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    GatewayDefine.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['gatewayDefineId'] = GatewayDefine.condition.gatewayDefineId;
        table.reload(GatewayDefine.tableId, {where: queryData});
    };



/**
 * 打开查看网关定义详情
 */
GatewayDefine.openGatewayDefineDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '网关定义详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/gatewayDefine/gatewayDefine_edit/' + GatewayDefine.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除网关定义
     *
     */
    GatewayDefine.onDeleteGatewayDefine = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/gatewayDefine/delete", function () {
                Feng.success("删除成功!");
                table.reload(GatewayDefine.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("gatewayDefineId", data.gatewayDefineId);
            ajax.start();
        };
        Feng.confirm("是否删除网关定义 ?", operation);
    };

    /**
     * 弹出添加网关定义
     */
    GatewayDefine.openAddGatewayDefine = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加网关定义',
            area:["800px","420px"],
            content: Feng.ctxPath + '/gatewayDefine/gatewayDefine_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(GatewayDefine.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    GatewayDefine.exportExcel = function () {
        var checkRows = table.checkStatus(GatewayDefine.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.gatewayDefine.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑网关定义
     *
     */
     GatewayDefine.onEditGatewayDefine = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改网关定义',
            area:["800px","420px"],
            content: Feng.ctxPath + '/gatewayDefine/gatewayDefine_edit?gatewayDefineId=' + data.gatewayDefineId,
            end: function () {
                admin.getTempData('formOk') && table.reload(GatewayDefine.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + GatewayDefine.tableId,
        url: Feng.ctxPath + '/gatewayDefine/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: GatewayDefine.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        GatewayDefine.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        GatewayDefine.openAddGatewayDefine();
    });

    // 导出excel
    $('#btnExp').click(function () {
        GatewayDefine.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + GatewayDefine.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            GatewayDefine.onEditGatewayDefine(data);
        } else if (layEvent === 'delete') {
            GatewayDefine.onDeleteGatewayDefine(data);
        }
    });
});