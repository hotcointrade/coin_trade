layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var form = layui.form;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

    /**
     * 系统管理--参数管理
     */
    var Config = {
        tableId: "configTable",
        condition: {
            robotId: ""
        }
    };

    /**
     * 初始化表格的列
     *
     * */
    Config.initColumn = function () {
        return [[
            // {type: 'checkbox'},
            {field: 'robotId', hide: true, sort: true, title: 'id'},
            {field: 'robotUserId', hide: true,sort: true, title: '系统账号ID'},
            {field: 'robotAccount', sort: true, title: '机器人账号'},
            // {field: 'code', sort: true, title: '属性编码'},
            {field: 'symbol', sort: true, title: '交易对'},
            {field: 'maxValuee', sort: true, title: '数量最大值'},
            {field: 'userId', hide: true,sort: true, title: '用户id'},
            {field: 'account', sort: true, title: '撮合对象'},
            {field: 'tradeModeStr' ,sort: true, title: '撮合模式'},
             {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'updateTime', sort: true, title: '更新时间'},
            {field: 'status', sort: true, templet: '#statusTpl', title: '状态'},
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
            content: Feng.ctxPath + '/robot/config_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Config.tableId);
            }
        });
    };



    /**
     * 点击编辑参数
     *
     * @param data 点击按钮时候的行数据
     */
    Config.onEditConfig = function (data) {
        admin.putTempData('formOk', false);
        console.log(data)
        top.layui.admin.open({
            type: 2,
            title: '修改参数',
            area:["450px","400px"],
            content: Feng.ctxPath + '/robot/config_edit?configId=' + data.robotId,
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
            var ajax = new $ax(Feng.ctxPath + "/robot/delete", function () {
                Feng.success("删除成功!");
                table.reload(Config.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("configId", data.robotId);
            ajax.start();
        };
        Feng.confirm("是否删除参数 " + data.robotAccount + "?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Config.tableId,
        url: Feng.ctxPath + '/robot/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
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

    changeUserStatus = function (userId, checked) {
        if (checked) {
            var ajax = new $ax(Feng.ctxPath + "/robot/upStatus", function (data) {
                Feng.success("开启成功!");
            }, function (data) {
                Feng.error("开启失败!");
                table.reload(Config.tableId);
            });
            ajax.set("robotId", userId);
            ajax.start();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/robot/upStatus", function (data) {
                Feng.success("关闭成功!");
            }, function (data) {
                Feng.error("关闭失败!" + data.responseJSON.message + "!");
                table.reload(Config.tableId);
            });
            ajax.set("robotId", userId);
            ajax.start();
        }
    };

    // 修改user状态
    form.on('switch(status)', function (obj) {

        var userId = obj.elem.value;
        var checked = obj.elem.checked ? true : false;

        changeUserStatus(userId, checked);
    });
});
