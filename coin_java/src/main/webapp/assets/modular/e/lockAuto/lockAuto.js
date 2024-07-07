layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 锁仓动态收益配置管理初始化
     */
    var Obj = {
        tableId: "lockAutoTable",	//表格id
        condition: {
            lockAutoId: ""
            ,timeLimit:""
        }
    };
    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
     });
    /**
     * 初始化表格的列
     */
    Obj.initColumn = function () {
        return [[
            // {type: 'checkbox'},
             {field: 'lockAutoId', hide: true, sort: true, title: 'id'},
             {field: 'code', sort: true, title: '编码'},
             {field: 'detail', sort: true, title: '描述'},
             {field: 'price', sort: true, title: '最低要求(USDT)'},
             {field: 'rate', sort: true, title: '动态收益率(%)'},
             {field: 'updateTime', sort: true, title: '更新时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['condition'] = $("#condition").val();
            queryData['lockAutoId'] = Obj.condition.lockAutoId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看锁仓动态收益配置详情
     */
    Obj.openLockAutoDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '锁仓动态收益配置详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/lockAuto/lockAuto_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除锁仓动态收益配置
         *
         */
        Obj.onDeleteLockAuto = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/lockAuto/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("lockAutoId", data.lockAutoId);
                ajax.start();
            };
            Feng.confirm("是否删除锁仓动态收益配置 ?", operation);
        };

        /**
         * 弹出添加锁仓动态收益配置
         */
        Obj.openAddLockAuto = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加锁仓动态收益配置',
                area:["800px","420px"],
                content: Feng.ctxPath + '/lockAuto/lockAuto_add',
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };

        /**
         * 导出excel按钮
         */
        Obj.exportExcel = function () {
            var checkRows = table.checkStatus(Obj.tableId);
            if (checkRows.data.length === 0) {
                Feng.error("请选择要导出的数据");
            } else {
                table.exportFile(tableResult.config.id, checkRows.data, 'xls');
            }
        };
       /**
         * 点击编辑锁仓动态收益配置
         *
         */
         Obj.onEditLockAuto = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改锁仓动态收益配置',
                area:["800px","420px"],
                content: Feng.ctxPath + '/lockAuto/lockAuto_edit?lockAutoId=' + data.lockAutoId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/lockAuto/list',
            page: true,
            height: "full-158",
            cellMinWidth: 100,
            //limit:100,
            //limits: [100,200,300,400,500],
            cols: Obj.initColumn()
        });
     // 搜索按钮点击事件
        $('#btnSearch').click(function () {
            Obj.search();
        });

        // 添加按钮点击事件
        $('#btnAdd').click(function () {
            Obj.openAddLockAuto();
        });

        // 导出excel
        $('#btnExp').click(function () {
            Obj.exportExcel();
        });

        // 工具条点击事件
        table.on('tool(' + Obj.tableId + ')', function (obj) {
            var data = obj.data;
            var layEvent = obj.event;
            if (layEvent === 'edit') {
                Obj.onEditLockAuto(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteLockAuto(data);
            }
        });
});