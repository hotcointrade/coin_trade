layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 豆兑换信息管理初始化
 */
var Convert = {
    tableId: "convertTable",	//表格id
    condition: {
        convertId: ""
    }
};

/**
 * 初始化表格的列
 */
Convert.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'convertId', hide: true, sort: true, title: 'id'},
         {field: 'orderNo', sort: true, title: '单号'},
         {field: 'memberId', sort: true, title: '用户'},
         {field: 'price', sort: true, title: '兑换数量'},
         {field: 'actualPrice', sort: true, title: '实际金额'},
         {field: 'fee', sort: true, title: '手续费'},
         {field: 'type', sort: true, title: '币种'},
         {field: 'toPrice', sort: true, title: '到账数量'},
         {field: 'toCoin', sort: true, title: '到账币种'},
         {field: 'remark', sort: true, title: '兑换比例'},
         // {field: 'txHash', sort: true, title: '哈希值'},
         // {field: 'response', sort: true, title: '返回信息'},
         {field: 'createTime', sort: true, title: '创建时间'},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Convert.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['convertId'] = Convert.condition.convertId;
        table.reload(Convert.tableId, {where: queryData});
    };



/**
 * 打开查看豆兑换信息详情
 */
Convert.openConvertDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '豆兑换信息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/convert/convert_edit/' + Convert.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除豆兑换信息
     *
     */
    Convert.onDeleteConvert = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/convert/delete", function () {
                Feng.success("删除成功!");
                table.reload(Convert.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("convertId", data.convertId);
            ajax.start();
        };
        Feng.confirm("是否删除豆兑换信息 ?", operation);
    };

    /**
     * 弹出添加豆兑换信息
     */
    Convert.openAddConvert = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加豆兑换信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/convert/convert_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Convert.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Convert.exportExcel = function () {
        var checkRows = table.checkStatus(Convert.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑豆兑换信息
     *
     */
     Convert.onEditConvert = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改豆兑换信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/convert/convert_edit?convertId=' + data.convertId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Convert.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Convert.tableId,
        url: Feng.ctxPath + '/convert/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Convert.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Convert.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Convert.openAddConvert();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Convert.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Convert.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Convert.onEditConvert(data);
        } else if (layEvent === 'delete') {
            Convert.onDeleteConvert(data);
        }
    });
});