layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

    /**
     * 生活支付打款管理初始化
     */
    var LiveRecord = {
        tableId: "liveRecordTable",	//表格id
        condition: {
            liveRecordId: ""
            , orderNo: ""
            , status: ""

        }
    };

    /**
     * 初始化表格的列
     */
    LiveRecord.initColumn = function () {
        return [[
            // {type: 'checkbox'},
            {field: 'liveRecordId', hide: true, sort: true, title: 'id'},
            {field: 'orderNo', sort: true, title: '订单号'},
            {field: 'memberId', sort: true, title: '用户'},
            {field: 'account', sort: true, title: '用户'},
            {field: 'cny', sort: true, title: '付款金额（CNY）'},
            {field: 'md', sort: true, title: '支付数量（MGE）'},
            {field: 'link', hide: true, sort: true, title: '链接'},
            {field: 'linkCode',toolbar: '#tableBarDetail', sort: true, title: '二维码'},
            {field: 'content', sort: true, title: '打款备注'},
            {field: 'payTime', sort: true, title: '付款时间'},
            {field: 'statusValue', sort: true, title: '订单状态'},
            {field: 'status',hide: true, sort: true, title: '订单状态'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


    /**
     * 点击查询按钮
     */
    LiveRecord.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['liveRecordId'] = LiveRecord.condition.liveRecordId;
        queryData['orderNo'] = $("#orderNo").val();
        queryData['status'] = $("#status").val();
        table.reload(LiveRecord.tableId, {where: queryData});
    };


    /**
     * 打开查看生活支付打款详情
     */
    LiveRecord.openLiveRecordDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                title: '生活支付打款详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/liveRecord/liveRecord_edit/' + LiveRecord.seItem.id
            });
            this.layerIndex = index;
        }
    };


    /**
     * 删除生活支付打款
     *
     */
    LiveRecord.onDeleteLiveRecord = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/liveRecord/delete", function () {
                Feng.success("删除成功!");
                table.reload(LiveRecord.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("liveRecordId", data.liveRecordId);
            ajax.start();
        };
        Feng.confirm("是否删除生活支付打款 ?", operation);
    };

    LiveRecord.onFinishLiveRecord = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/liveRecord/finish", function () {
                Feng.success("成功!");
                table.reload(LiveRecord.tableId);
            }, function (data) {
                Feng.error("失败!" + data.responseJSON.message + "!");
            });
            ajax.set("liveRecordId", data.liveRecordId);
            ajax.start();
        };
        Feng.confirm("是否删除生活支付打款 ?", operation);
    };

    /**
     * 弹出添加生活支付打款
     */
    LiveRecord.openAddLiveRecord = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加生活支付打款',
            area: ["800px", "420px"],
            content: Feng.ctxPath + '/liveRecord/liveRecord_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(LiveRecord.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    LiveRecord.exportExcel = function () {
        var checkRows = table.checkStatus(LiveRecord.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    LiveRecord.imgDetail = function (param) {
        var ajax = new $ax(Feng.ctxPath + "/liveRecord/content/" + param.liveRecordId, function (data) {
            Feng.bulletinDetailImg("详情", data.linkCode);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };

    Feng.bulletinDetailImg = function (title, info) {
        var el="<div><img style='width: 300px;height: 300px;' src='"+info+"'/></div>"
        top.layer.open({
            title: title,
            type: 1,
            maxmin:true,
            // skin: 'layui-layer-rim', //加上边框
            // area: ['400px', '600px'], //宽高
            // content: escape2Html(info)
            content: el
        });
    };


    /**
     * 点击编辑生活支付打款
     *
     */
    LiveRecord.onEditLiveRecord = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改生活支付打款',
            area: ["800px", "420px"],
            content: Feng.ctxPath + '/liveRecord/liveRecord_edit?liveRecordId=' + data.liveRecordId,
            end: function () {
                admin.getTempData('formOk') && table.reload(LiveRecord.tableId);
            }
        });
    };
    // 渲染表格
    var tableResult = table.render({
        elem: '#' + LiveRecord.tableId,
        url: Feng.ctxPath + '/liveRecord/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit: 100,
        limits: [100, 200, 300, 400, 500],
        cols: LiveRecord.initColumn()

    });
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        LiveRecord.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        LiveRecord.openAddLiveRecord();
    });

    // 导出excel
    $('#btnExp').click(function () {
        LiveRecord.exportExcel();
    });


    // 工具条点击事件
    table.on('tool(' + LiveRecord.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            LiveRecord.onEditLiveRecord(data);
        } else if (layEvent === 'delete') {
            LiveRecord.onDeleteLiveRecord(data);
        } else if (layEvent === 'finish') {
            LiveRecord.onFinishLiveRecord(data);
        }else if (layEvent === 'imgDetail') {//图片
            LiveRecord.imgDetail(data);
        }
    });
});