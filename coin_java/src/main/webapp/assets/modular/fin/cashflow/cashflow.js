layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 流水记录管理初始化
 */
var Cashflow = {
    tableId: "cashflowTable",	//表格id
    condition: {
        cashflowId: ""
        ,flowType:""
        ,itemCode:""
    }
};

/**
 * 初始化表格的列
 */
Cashflow.initColumn = function () {
    return [[
        {type: 'checkbox'},
         {field: 'cashflowId', hide: true, sort: true, title: 'id'},
         // {field: 'memberId',  hide: true,sort: true, title: '用户id'},
         {field: 'memberIdValue',width:125, title: '用户'},
         // {field: 'walletType',  hide: true,sort: true, title: '账户类型（BTC、  T:算力、POINT:积分）'},
         {field: 'walletTypeValue',width:125,  title: '账户类型'},
        {field: 'itemName', title: '币种账户'},
        {field: 'itemCode',  title: '账户子项'},

        {field: 'beforePrice',width:90,   title: '变更前'},
        {field: 'afterPrice',width:90,  title: '变更后'},
         // {field: 'flowOp',  hide: true,sort: true, title: '流水方向'},
         {field: 'flowOpValue',  title: '流水方向'},
         {field: 'fromIdValue',width:125,  title: '来处'},
         {field: 'toIdValue', width:125, title: '去向'},
         // {field: 'flowType',hide: true, sort: true, title: '流水类型'},
         {field: 'flowTypeValue',  title: '流水类型'},


         {field: 'flowPrice',width:90,  title: '流水金额'},
         {field: 'flowCoin', sort: true, title: '流水币种'},
         // {field: 'flowCoinValue',  title: '流水币种'},
         {field: 'actualPrice',width:90,  title: '实际金额'},
         {field: 'actualCoin', sort: true, title: '实际币种'},
         // {field: 'actualCoinValue',  title: '实际币种'},
         {field: 'fee',width:90, title: '手续费'},
         {field: 'feeCoin', sort: true, title: '手续费币种'},
         // {field: 'feeCoinValue', title: '手续费币种'},
         // {field: 'source', sort: true, title: '流水来源'},
         // {field: 'detail', sort: true, title: '明细（备用）'},
         {field: 'remark', sort: true, title: '备注'},
         {field: 'createTime',width:165,  sort: true, title: '创建时间'}
         // ,
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};

    $.ajax({
        type: "POST",
        url: "/cashflow/getTypeList",
        datatype: "json",
        success: function (data) {
            $.each(data, function (index, item) {
                $('#flowType').append(new Option(item.value, item.code));// 下拉菜单里添加元素
            });
            layui.form.render('select');
        }, error: function () {
            Feng.error("查询等级失败");
        }
    });
    /**
     * 点击查询按钮
     */
    Cashflow.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['flowType'] = $("#flowType").val();
        queryData['itemCode'] = $("#itemCode").val();
        queryData['cashflowId'] = Cashflow.condition.cashflowId;
        table.reload(Cashflow.tableId, {where: queryData});
    };



/**
 * 打开查看流水记录详情
 */
Cashflow.openCashflowDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '流水记录详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/cashflow/cashflow_edit/' + Cashflow.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除流水记录
     *
     */
    Cashflow.onDeleteCashflow = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/cashflow/delete", function () {
                Feng.success("删除成功!");
                table.reload(Cashflow.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("cashflowId", data.cashflowId);
            ajax.start();
        };
        Feng.confirm("是否删除流水记录 ?", operation);
    };

    /**
     * 弹出添加流水记录
     */
    Cashflow.openAddCashflow = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加流水记录',
            area:["800px","420px"],
            content: Feng.ctxPath + '/cashflow/cashflow_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Cashflow.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Cashflow.exportExcel = function () {
        var checkRows = table.checkStatus(Cashflow.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑流水记录
     *
     */
     Cashflow.onEditCashflow = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改流水记录',
            area:["800px","420px"],
            content: Feng.ctxPath + '/cashflow/cashflow_edit?cashflowId=' + data.cashflowId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Cashflow.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Cashflow.tableId,
        url: Feng.ctxPath + '/cashflow/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500,1000,2000,5000,10000],
        cols: Cashflow.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Cashflow.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Cashflow.openAddCashflow();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Cashflow.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Cashflow.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Cashflow.onEditCashflow(data);
        } else if (layEvent === 'delete') {
            Cashflow.onDeleteCashflow(data);
        }
    });
});