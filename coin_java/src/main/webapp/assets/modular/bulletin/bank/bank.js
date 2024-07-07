layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 银行列表管理初始化
 */
var Bank = {
    tableId: "bankTable",	//表格id
    condition: {
        bankId: ""
    }
};

/**
 * 初始化表格的列
 */
Bank.initColumn = function () {
    return [[
        // {type: 'checkbox'},
        {field: 'bankId', hide: true, sort: true, title: 'id'},
        {field: 'name',  title: '名称',width:200},
        // {field: 'createTime', sort: true, title: '创建时间',width:200},
        {align: 'center', toolbar: '#tableBar', title: '操作', width: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Bank.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['bankId'] = Bank.condition.bankId;
        table.reload(Bank.tableId, {where: queryData});
    };



/**
 * 打开查看银行列表详情
 */
Bank.openBankDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '银行列表详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bank/bank_edit/' + Bank.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除银行列表
     *
     */
    Bank.onDeleteBank = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bank/delete", function () {
                Feng.success("删除成功!");
                table.reload(Bank.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("bankId", data.bankId);
            ajax.start();
        };
        Feng.confirm("是否删除银行列表 ?", operation);
    };

    /**
     * 弹出添加银行列表
     */
    Bank.openAddBank = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加银行列表',
            area:["800px","420px"],
            content: Feng.ctxPath + '/bank/bank_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Bank.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Bank.exportExcel = function () {
        var checkRows = table.checkStatus(Bank.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑银行列表
     *
     */
     Bank.onEditBank = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改银行列表',
            area:["800px","420px"],
            content: Feng.ctxPath + '/bank/bank_edit?bankId=' + data.bankId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Bank.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Bank.tableId,
        url: Feng.ctxPath + '/bank/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Bank.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Bank.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Bank.openAddBank();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Bank.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Bank.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Bank.onEditBank(data);
        } else if (layEvent === 'delete') {
            Bank.onDeleteBank(data);
        }
    });
});