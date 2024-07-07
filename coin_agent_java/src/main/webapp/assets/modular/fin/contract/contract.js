layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 合约账户管理初始化
 */
var Contract = {
    tableId: "contractTable",	//表格id
    condition: {
        contractId: ""
    }
};

/**
 * 初始化表格的列
 */
Contract.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'contractId', hide: true, sort: true, title: 'id'},
         {field: 'memberId',hide: true,  sort: true, title: '用户id'},
         {field: 'memberIdValue', sort: true, title: '用户'},
         // {field: 'worthPrice', sort: true, title: '权益账户'},
         // {field: 'noPl', sort: true, title: '未实现盈亏'},
         // {field: 'positionPrice', sort: true, title: '持仓保证金'},
         // {field: 'usedPrice', sort: true, title: '可用保证金'},
         // {field: 'entrustPrice', hide: true,sort: true, title: '配资资产'},
         // {field: 'givePrice',hide: true, sort: true, title: '持仓配资'},
         // {field: 'rate',hide: true, sort: true, title: '保证金率'},
         // {field: 'type', sort: true, title: '类型'},
        {field: 'rate',hide: true,  sort: true, title: '保证金率'},
        {field: 'type', sort: true, title: '类型'},
        {field: 'worthPrice', sort: true, title: '合约权益资产'},
         // {field: 'version', sort: true, title: '版本'},
         // {field: 'createTime', sort: true, title: '创建时间'},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Contract.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['contractId'] = Contract.condition.contractId;
        table.reload(Contract.tableId, {where: queryData});
    };



/**
 * 打开查看合约账户详情
 */
Contract.openContractDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '合约账户详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/contract/contract_edit/' + Contract.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除合约账户
     *
     */
    Contract.onDeleteContract = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/contract/delete", function () {
                Feng.success("删除成功!");
                table.reload(Contract.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("contractId", data.contractId);
            ajax.start();
        };
        Feng.confirm("是否删除合约账户 ?", operation);
    };

    /**
     * 弹出添加合约账户
     */
    Contract.openAddContract = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加合约账户',
            area:["800px","420px"],
            content: Feng.ctxPath + '/contract/contract_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Contract.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Contract.exportExcel = function () {
        var checkRows = table.checkStatus(Contract.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑合约账户
     *
     */
     Contract.onEditContract = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改合约账户',
            area:["800px","420px"],
            content: Feng.ctxPath + '/contract/contract_edit?contractId=' + data.contractId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Contract.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Contract.tableId,
        url: Feng.ctxPath + '/contract/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Contract.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Contract.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Contract.openAddContract();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Contract.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Contract.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Contract.onEditContract(data);
        } else if (layEvent === 'delete') {
            Contract.onDeleteContract(data);
        }
    });
});