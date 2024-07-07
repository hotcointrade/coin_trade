layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 第三方接口记录表管理初始化
 */
var PayRecord = {
    tableId: "payRecordTable",	//表格id
    condition: {
        payRecordId: ""
    }
};

/**
 * 初始化表格的列
 */
PayRecord.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'payRecordId', hide: true, sort: true, title: 'id'},
         {field: 'payCode', sort: true, title: '接口编码'},
         {field: 'requestData', sort: true, title: '请求数据'},
         {field: 'responseData', sort: true, title: '响应数据'},
         {field: 'memberId', sort: true, title: '用户id'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    PayRecord.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['payRecordId'] = PayRecord.condition.payRecordId;
        table.reload(PayRecord.tableId, {where: queryData});
    };



/**
 * 打开查看第三方接口记录表详情
 */
PayRecord.openPayRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '第三方接口记录表详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/payRecord/payRecord_edit/' + PayRecord.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除第三方接口记录表
     *
     */
    PayRecord.onDeletePayRecord = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/payRecord/delete", function () {
                Feng.success("删除成功!");
                table.reload(PayRecord.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("payRecordId", data.payRecordId);
            ajax.start();
        };
        Feng.confirm("是否删除第三方接口记录表 ?", operation);
    };

    /**
     * 弹出添加第三方接口记录表
     */
    PayRecord.openAddPayRecord = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加第三方接口记录表',
            area:["800px","420px"],
            content: Feng.ctxPath + '/payRecord/payRecord_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(PayRecord.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    PayRecord.exportExcel = function () {
        var checkRows = table.checkStatus(PayRecord.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.payRecord.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑第三方接口记录表
     *
     */
     PayRecord.onEditPayRecord = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改第三方接口记录表',
            area:["800px","420px"],
            content: Feng.ctxPath + '/payRecord/payRecord_edit?payRecordId=' + data.payRecordId,
            end: function () {
                admin.getTempData('formOk') && table.reload(PayRecord.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + PayRecord.tableId,
        url: Feng.ctxPath + '/payRecord/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: PayRecord.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        PayRecord.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        PayRecord.openAddPayRecord();
    });

    // 导出excel
    $('#btnExp').click(function () {
        PayRecord.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + PayRecord.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            PayRecord.onEditPayRecord(data);
        } else if (layEvent === 'delete') {
            PayRecord.onDeletePayRecord(data);
        }
    });
});