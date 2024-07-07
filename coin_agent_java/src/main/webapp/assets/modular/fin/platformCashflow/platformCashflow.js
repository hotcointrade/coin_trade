layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 平台流水管理初始化
 */
var PlatformCashflow = {
    tableId: "platformCashflowTable",	//表格id
    condition: {
        platformCashflowId: ""
    }
};

/**
 * 初始化表格的列
 */
PlatformCashflow.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'platformCashflowId', hide: true, sort: true, title: 'id'},
         {field: 'memberId', sort: true, title: '用户id'},
         {field: 'walletType', sort: true, title: '账户类型（BTC、  T:算力、POINT:积分）'},
         {field: 'flowOp', sort: true, title: '流水方向（1：流入(+)      0 :流出(-) ）'},
         {field: 'flowType', sort: true, title: '流水类型'},
         {field: 'itemCode', sort: true, title: '流水记录编码(备用）'},
         {field: 'itemName', sort: true, title: '名称（备用）'},
         {field: 'beforePrice', sort: true, title: '变更前额度'},
         {field: 'afterPrice', sort: true, title: '变更后额度'},
         {field: 'flowPrice', sort: true, title: '流水金额'},
         {field: 'flowCoin', sort: true, title: '流水币种（BTC、  T:算力、POINT:积分）'},
         {field: 'actualPrice', sort: true, title: '实际金额'},
         {field: 'actualCoin', sort: true, title: '实际币种BTC、T:算力、POINT:积分）'},
         {field: 'fee', sort: true, title: '手续费'},
         {field: 'feeCoin', sort: true, title: '手续费币种'},
         {field: 'source', sort: true, title: '流水来源'},
         {field: 'fromId', sort: true, title: '来处'},
         {field: 'toId', sort: true, title: '去向'},
         {field: 'detail', sort: true, title: '明细（备用）'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    PlatformCashflow.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['platformCashflowId'] = PlatformCashflow.condition.platformCashflowId;
        table.reload(PlatformCashflow.tableId, {where: queryData});
    };



/**
 * 打开查看平台流水详情
 */
PlatformCashflow.openPlatformCashflowDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '平台流水详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/platformCashflow/platformCashflow_edit/' + PlatformCashflow.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除平台流水
     *
     */
    PlatformCashflow.onDeletePlatformCashflow = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/platformCashflow/delete", function () {
                Feng.success("删除成功!");
                table.reload(PlatformCashflow.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("platformCashflowId", data.platformCashflowId);
            ajax.start();
        };
        Feng.confirm("是否删除平台流水 ?", operation);
    };

    /**
     * 弹出添加平台流水
     */
    PlatformCashflow.openAddPlatformCashflow = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加平台流水',
            area:["800px","420px"],
            content: Feng.ctxPath + '/platformCashflow/platformCashflow_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(PlatformCashflow.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    PlatformCashflow.exportExcel = function () {
        var checkRows = table.checkStatus(PlatformCashflow.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑平台流水
     *
     */
     PlatformCashflow.onEditPlatformCashflow = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改平台流水',
            area:["800px","420px"],
            content: Feng.ctxPath + '/platformCashflow/platformCashflow_edit?platformCashflowId=' + data.platformCashflowId,
            end: function () {
                admin.getTempData('formOk') && table.reload(PlatformCashflow.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + PlatformCashflow.tableId,
        url: Feng.ctxPath + '/platformCashflow/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: PlatformCashflow.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        PlatformCashflow.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        PlatformCashflow.openAddPlatformCashflow();
    });

    // 导出excel
    $('#btnExp').click(function () {
        PlatformCashflow.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + PlatformCashflow.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            PlatformCashflow.onEditPlatformCashflow(data);
        } else if (layEvent === 'delete') {
            PlatformCashflow.onDeletePlatformCashflow(data);
        }
    });
});