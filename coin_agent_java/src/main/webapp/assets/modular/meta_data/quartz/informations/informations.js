layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

    /**
     * 系统管理--参数管理
     */
    var Informations = {
        tableId: "informationsTable",
        condition: {
            informationsId: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Informations.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, sort: true, title: 'id'},
            {field: 'taskNo', sort: true, title: '任务编号'},
            {field: 'taskName', sort: true, title: '任务名称'},
            {field: 'schedulerRule', sort: true, title: '定时配置cron'},
            {field: 'frozenStatus', hide: true,  sort: true, title: '冻结状态'},
            {field: 'statusName', sort: true, title: '冻结状态'},
            {field: 'executorNo',hide: true, sort: true, title: '执行方'},
            {field: 'executorNoValue', sort: true, title: '执行方'},
            {field: 'sendType', sort: true, title: '执行方式'},
            {field: 'createTime',hide: true, sort: true, title: '创建时间'},
            {field: 'createTimeValue', sort: true, title: '创建时间'},
            {field: 'lastModifyTime',hide: true, sort: true, title: '最近修改时间'},
            {field: 'lastModifyTimeValue', sort: true, title: '最近修改时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 300}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Informations.search = function () {
        var queryData = {};
        queryData['condition'] = $("#name").val();
        queryData['informationsId'] = Informations.condition.informationsId;
        table.reload(Informations.tableId, {where: queryData});
    };

    /**
     * 选择参数时
     */
    Informations.onClickInformations = function (e, treeId, treeNode) {
        Informations.condition.informationsId = treeNode.id;
        Informations.search();
    };

    /**
     * 弹出添加
     */
    Informations.openAddInformations = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加定时任务',
            area:["700px","600px"],
            content: Feng.ctxPath + '/task/info_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Informations.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Informations.exportExcel = function () {
        var checkRows = table.checkStatus(Informations.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.informations.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击编辑参数
     *
     * @param data 点击按钮时候的行数据
     */
    Informations.onEditInformations = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改',
            area:["700px","600px"],
            content: Feng.ctxPath + '/task/info_edit?id=' + data.id,
            end: function () {
                admin.getTempData('formOk') && table.reload(Informations.tableId);
            }
        });
    };

    /**
     * 点击删除参数
     *
     * @param data 点击按钮时候的行数据
     */
    Informations.onDeleteInformations = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/informations/delete", function () {
                Feng.success("删除成功!");
                table.reload(Informations.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("informationsId", data.informationsId);
            ajax.start();
        };
        Feng.confirm("是否删除参数 " + data.name + "?", operation);
    };

    /**
     * 立即执行一次
     * @param data
     */
    Informations.onStartNow=function(data)
    {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/task/info/start_now", function () {
                Feng.success("执行成功!");
                table.reload(Informations.tableId);
            }, function (data) {
                Feng.error("执行失败!" + data.responseJSON.message + "!");
            });
            ajax.set("taskNo", data.taskNo);
            ajax.start();
        };
        Feng.confirm("是否立即执行任务： " + data.taskNo + "?", operation);
    }

    /**
     * 启动 或 暂停 任务
     */
    Informations.onOptionTask=function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/task/info/option_task", function () {
                Feng.success("执行成功!");
                table.reload(Informations.tableId);
            }, function (data) {
                Feng.error("执行失败!" + data.responseJSON.message + "!");
            });
            ajax.set("taskNo", data.taskNo);
            ajax.start();
        };
        Feng.confirm("是否切换任务状态： " + data.taskNo + "?", operation);
    }


        // 渲染表格
    var tableResult = table.render({
        elem: '#' + Informations.tableId,
        url: Feng.ctxPath + '/task/info/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Informations.initColumn()
    });
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Informations.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Informations.openAddInformations();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Informations.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Informations.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Informations.onEditInformations(data);
        } else if (layEvent === 'delete') {
            Informations.onDeleteInformations(data);
        }else if(layEvent==='start_now')
        {
            if(data.frozenStatus==='UNFROZEN')
            {
                Informations.onStartNow(data);
            }else {
                Feng.error("未激活任务");
            }

        }else if(layEvent==='option_task')
        {
            Informations.onOptionTask(data);
        }
    });
});
