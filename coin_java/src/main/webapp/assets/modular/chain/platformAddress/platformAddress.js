layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 平台地址管理初始化
 */
var PlatformAddress = {
    tableId: "platformAddressTable",	//表格id
    condition: {
        platformAddressId: ""
    }
};

/**
 * 初始化表格的列
 */
PlatformAddress.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'platformAddressId', hide: true, sort: true, title: 'id'},
        {field: 'type', sort: true, title: '币种'},
         {field: 'address', sort: true, title: '地址'},
        {field: 'remark', sort: true, title: 'Memo值'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    PlatformAddress.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['platformAddressId'] = PlatformAddress.condition.platformAddressId;
        table.reload(PlatformAddress.tableId, {where: queryData});
    };



/**
 * 打开查看平台地址详情
 */
PlatformAddress.openPlatformAddressDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '平台地址详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/platformAddress/platformAddress_edit/' + PlatformAddress.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除平台地址
     *
     */
    PlatformAddress.onDeletePlatformAddress = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/platformAddress/delete", function () {
                Feng.success("删除成功!");
                table.reload(PlatformAddress.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("platformAddressId", data.platformAddressId);
            ajax.start();
        };
        Feng.confirm("是否删除平台地址 ?", operation);
    };

    /**
     * 弹出添加平台地址
     */
    PlatformAddress.openAddPlatformAddress = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加平台地址',
            area:["800px","420px"],
            content: Feng.ctxPath + '/platformAddress/platformAddress_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(PlatformAddress.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    PlatformAddress.exportExcel = function () {
        var checkRows = table.checkStatus(PlatformAddress.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑平台地址
     *
     */
     PlatformAddress.onEditPlatformAddress = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改平台地址',
            area:["800px","420px"],
            content: Feng.ctxPath + '/platformAddress/platformAddress_edit?platformAddressId=' + data.platformAddressId,
            end: function () {
                admin.getTempData('formOk') && table.reload(PlatformAddress.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + PlatformAddress.tableId,
        url: Feng.ctxPath + '/platformAddress/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: PlatformAddress.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        PlatformAddress.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        PlatformAddress.openAddPlatformAddress();
    });

    // 导出excel
    $('#btnExp').click(function () {
        PlatformAddress.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + PlatformAddress.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            PlatformAddress.onEditPlatformAddress(data);
        } else if (layEvent === 'delete') {
            PlatformAddress.onDeletePlatformAddress(data);
        }
    });
});