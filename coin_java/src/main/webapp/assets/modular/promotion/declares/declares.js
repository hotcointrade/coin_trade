layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 用户协议管理初始化
 */
var Declares = {
    tableId: "declaresTable",	//表格id
    condition: {
        declareId: ""
    }
};

/**
 * 初始化表格的列
 */
Declares.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'declareId', hide: true, sort: true, title: 'id'},
        {align: 'center', toolbar: '#tableBarDetail', title: '详情', minWidth: 200},
        {field: 'language', sort: true, title: '语言',width:200,templet: function (data) {
                return Feng.LanguageMap[data.language]
        }},
         {field: 'createTime', sort: true, title: '创建时间'},
         {field: 'updateTime', sort: true, title: '更新时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};
    /**
     * 内容详情，局限内容
     */
    Declares.bulletinDetail = function (param) {
        var ajax = new $ax(Feng.ctxPath + "/declares/content/" + param.declareId, function (data) {
            Declares.contentDetail("详情", data.content);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };

    /**
     * 点击查询按钮
     */
    Declares.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['declareId'] = Declares.condition.declareId;
        table.reload(Declares.tableId, {where: queryData});
    };
    Declares.contentDetail= function (title, info) {
        var display = "";
        if (typeof info === "string") {
            display = info;
        } else {
            if (info instanceof Array) {
                for (var x in info) {
                    display = display + info[x] + "<br/>";
                }
            } else {
                display = info;
            }
        }
        top.layer.open({
            title: title,
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['1200px', '850px'], //宽高
            // content: escape2Html(info)
            content: info
        });
    };


/**
 * 打开查看用户协议详情
 */
Declares.openDeclaresDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户协议详情',
            area: ['1200px', '850px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/declares/declares_edit/' + Declares.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除用户协议
     *
     */
    Declares.onDeleteDeclares = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/declares/delete", function () {
                Feng.success("删除成功!");
                table.reload(Declares.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("declareId", data.declareId);
            ajax.start();
        };
        Feng.confirm("是否删除用户协议 ?", operation);
    };

    /**
     * 弹出添加用户协议
     */
    Declares.openAddDeclares = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加用户协议',
            area:["1200px","850px"],
            content: Feng.ctxPath + '/declares/declares_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Declares.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Declares.exportExcel = function () {
        var checkRows = table.checkStatus(Declares.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.declares.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑用户协议
     *
     */
     Declares.onEditDeclares = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改用户协议',
            area:["1200px","850px"],
            content: Feng.ctxPath + '/declares/declares_edit?declareId=' + data.declareId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Declares.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Declares.tableId,
        url: Feng.ctxPath + '/declares/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Declares.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Declares.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Declares.openAddDeclares();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Declares.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Declares.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Declares.onEditDeclares(data);
        } else if (layEvent === 'delete') {
            Declares.onDeleteDeclares(data);
        }
        else if (layEvent === 'contentDetail') {
            //内容
            Declares.bulletinDetail(data);
        }
    });
});
