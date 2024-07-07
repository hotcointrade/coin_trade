layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

    /**
     * 系统管理--参数管理
     */
    var Config = {
        tableId: "configTable",
        condition: {
            configId: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Config.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'configId', hide: true, sort: true, title: 'id'},
            {field: 'name', sort: true, title: '名称'},
            {field: 'code', sort: true, title: '属性编码'},
            {field: 'value', sort: true, title: '属性值'},
            {field: 'remark', sort: true, title: '备注'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'updateTime', sort: true, title: '更新时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Config.search = function () {
        var queryData = {};
        queryData['condition'] = $("#name").val();
        queryData['configId'] = Config.condition.configId;
        table.reload(Config.tableId, {where: queryData});
    };

    /**
     * 选择参数时
     */
    Config.onClickConfig = function (e, treeId, treeNode) {
        Config.condition.configId = treeNode.id;
        Config.search();
    };

    /**
     * 弹出添加
     */
    Config.openAddConfig = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加参数',
            area:["450px","400px"],
            content: Feng.ctxPath + '/config/config_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Config.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Config.exportExcel = function () {
        var checkRows = table.checkStatus(Config.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击编辑参数
     *
     * @param data 点击按钮时候的行数据
     */
    Config.onEditConfig = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改参数',
            area:["450px","400px"],
            content: Feng.ctxPath + '/config/config_edit?configId=' + data.configId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Config.tableId);
            }
        });
    };

    /**
     * 点击删除参数
     *
     * @param data 点击按钮时候的行数据
     */
    Config.onDeleteConfig = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/config/delete", function () {
                Feng.success("删除成功!");
                table.reload(Config.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("configId", data.configId);
            ajax.start();
        };
        Feng.confirm("是否删除参数 " + data.name + "?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Config.tableId,
        url: Feng.ctxPath + '/config/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Config.initColumn()
    });
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Config.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Config.openAddConfig();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Config.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Config.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Config.onEditConfig(data);
        } else if (layEvent === 'delete') {
            Config.onDeleteConfig(data);
        }
    });
});
