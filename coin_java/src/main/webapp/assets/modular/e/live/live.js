layui.use(['table', 'admin', 'ax', 'ztree','form'], function () {
    var form = layui.form;
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 生活支付开通记录管理初始化
 */
var Live = {
    tableId: "liveTable",	//表格id
    condition: {
        liveId: ""
    }
};
    var Obj=Live;//对象
    var apiPrefix="live";//api
    /**
     * 修改状态
     *
     * @param contactId id
     * @param checked 是否选中（true,false），选中就是启用，未选中就是禁用
     */
    Obj.changeStatus = function (id, checked) {
        if (checked) {
            var ajax = new $ax(Feng.ctxPath + "/"+apiPrefix+"/status/Y", function (data) {
                Feng.success("已启用!");
            }, function (data) {
                Feng.error("失败!");
                table.reload(Obj.tableId);
            });
            ajax.set("id", id);
            ajax.start();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/"+apiPrefix+"/status/N", function (data) {
                Feng.success("已禁用!");
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
                table.reload(Obj.tableId);
            });
            ajax.set("id", id);
            ajax.start();
        }
    };

// 状态监听
    form.on('switch(status)', function (obj) {
        var id = obj.elem.value;
        var checked = obj.elem.checked ? true : false;
        Obj.changeStatus(id, checked);
    });
/**
 * 初始化表格的列
 */
Live.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'liveId', hide: true, sort: true, title: 'id'},
         {field: 'memberId',  hide: true,sort: true, title: '用户'},
         {field: 'memberIdValue', sort: true, title: '用户'},
         {field: 'price', sort: true, title: '金额'},
         {field: 'createTime', sort: true, title: '创建时间'},
         {field: 'status', templet: '#statusTpl', title: '状态'},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Live.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['liveId'] = Live.condition.liveId;
        table.reload(Live.tableId, {where: queryData});
    };



/**
 * 打开查看生活支付开通记录详情
 */
Live.openLiveDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '生活支付开通记录详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/live/live_edit/' + Live.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除生活支付开通记录
     *
     */
    Live.onDeleteLive = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/live/delete", function () {
                Feng.success("删除成功!");
                table.reload(Live.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("liveId", data.liveId);
            ajax.start();
        };
        Feng.confirm("是否删除生活支付开通记录 ?", operation);
    };

    /**
     * 弹出添加生活支付开通记录
     */
    Live.openAddLive = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加生活支付开通记录',
            area:["800px","420px"],
            content: Feng.ctxPath + '/live/live_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Live.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Live.exportExcel = function () {
        var checkRows = table.checkStatus(Live.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑生活支付开通记录
     *
     */
     Live.onEditLive = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改生活支付开通记录',
            area:["800px","420px"],
            content: Feng.ctxPath + '/live/live_edit?liveId=' + data.liveId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Live.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Live.tableId,
        url: Feng.ctxPath + '/live/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Live.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Live.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Live.openAddLive();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Live.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Live.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Live.onEditLive(data);
        } else if (layEvent === 'delete') {
            Live.onDeleteLive(data);
        }
    });
});