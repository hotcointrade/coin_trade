layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 网关记录管理初始化
 */
var GatewayRecord = {
    tableId: "gatewayRecordTable",	//表格id
    condition: {
        gatewayRecordId: ""
    }
};

/**
 * 初始化表格的列
 */
GatewayRecord.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'gatewayRecordId', hide: true, sort: true, title: 'id'},
         {field: 'interfaceCode', sort: true, title: '接口编码'},
         {field: 'requestData', sort: true, title: '请求参数'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    GatewayRecord.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['gatewayRecordId'] = GatewayRecord.condition.gatewayRecordId;
        table.reload(GatewayRecord.tableId, {where: queryData});
    };



/**
 * 打开查看网关记录详情
 */
GatewayRecord.openGatewayRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '网关记录详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/gatewayRecord/gatewayRecord_edit/' + GatewayRecord.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除网关记录
     *
     */
    GatewayRecord.onDeleteGatewayRecord = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/gatewayRecord/delete", function () {
                Feng.success("删除成功!");
                table.reload(GatewayRecord.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("gatewayRecordId", data.gatewayRecordId);
            ajax.start();
        };
        Feng.confirm("是否删除网关记录 ?", operation);
    };

    /**
     * 弹出添加网关记录
     */
    GatewayRecord.openAddGatewayRecord = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加网关记录',
            area:["800px","420px"],
            content: Feng.ctxPath + '/gatewayRecord/gatewayRecord_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(GatewayRecord.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    GatewayRecord.exportExcel = function () {
        var checkRows = table.checkStatus(GatewayRecord.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.gatewayRecord.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑网关记录
     *
     */
     GatewayRecord.onEditGatewayRecord = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改网关记录',
            area:["800px","420px"],
            content: Feng.ctxPath + '/gatewayRecord/gatewayRecord_edit?gatewayRecordId=' + data.gatewayRecordId,
            end: function () {
                admin.getTempData('formOk') && table.reload(GatewayRecord.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + GatewayRecord.tableId,
        url: Feng.ctxPath + '/gatewayRecord/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: GatewayRecord.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        GatewayRecord.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        GatewayRecord.openAddGatewayRecord();
    });

    // 导出excel
    $('#btnExp').click(function () {
        GatewayRecord.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + GatewayRecord.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            GatewayRecord.onEditGatewayRecord(data);
        } else if (layEvent === 'delete') {
            GatewayRecord.onDeleteGatewayRecord(data);
        }
    });
});