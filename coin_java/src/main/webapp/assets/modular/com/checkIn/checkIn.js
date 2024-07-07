layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 签到管理初始化
 */
var CheckIn = {
    tableId: "checkInTable",	//表格id
    condition: {
        checkInId: ""
    }
};

/**
 * 初始化表格的列
 */
CheckIn.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'checkInId', hide: true, sort: true, title: 'id'},
         {field: 'memberId',  hide: true,sort: true, title: '用户id'},
         {field: 'memberIdValue', sort: true, title: '用户'},
        {field: 'createTime', sort: true, title: '签到时间'},
        {field: 'createTime', sort: true, title: '奖励',templet:function (data) {
                return data.price+data.coin ;
            }},
         // ,
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};

3
    /**
     * 点击查询按钮
     */
    CheckIn.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['checkInId'] = CheckIn.condition.checkInId;
        table.reload(CheckIn.tableId, {where: queryData});
    };



/**
 * 打开查看签到详情
 */
CheckIn.openCheckInDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '签到详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/checkIn/checkIn_edit/' + CheckIn.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除签到
     *
     */
    CheckIn.onDeleteCheckIn = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/checkIn/delete", function () {
                Feng.success("删除成功!");
                table.reload(CheckIn.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("checkInId", data.checkInId);
            ajax.start();
        };
        Feng.confirm("是否删除签到 ?", operation);
    };

    /**
     * 弹出添加签到
     */
    CheckIn.openAddCheckIn = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加签到',
            area:["800px","420px"],
            content: Feng.ctxPath + '/checkIn/checkIn_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(CheckIn.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    CheckIn.exportExcel = function () {
        var checkRows = table.checkStatus(CheckIn.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.checkIn.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑签到
     *
     */
     CheckIn.onEditCheckIn = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改签到',
            area:["800px","420px"],
            content: Feng.ctxPath + '/checkIn/checkIn_edit?checkInId=' + data.checkInId,
            end: function () {
                admin.getTempData('formOk') && table.reload(CheckIn.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + CheckIn.tableId,
        url: Feng.ctxPath + '/checkIn/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: CheckIn.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        CheckIn.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        CheckIn.openAddCheckIn();
    });

    // 导出excel
    $('#btnExp').click(function () {
        CheckIn.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + CheckIn.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            CheckIn.onEditCheckIn(data);
        } else if (layEvent === 'delete') {
            CheckIn.onDeleteCheckIn(data);
        }
    });
});
