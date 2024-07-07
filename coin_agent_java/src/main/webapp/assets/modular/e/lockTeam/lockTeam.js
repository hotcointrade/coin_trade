layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 锁仓团队奖配置管理初始化
     */
    var Obj = {
        tableId: "lockTeamTable",	//表格id
        condition: {
            lockTeamId: ""
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
             {field: 'lockTeamId', hide: true, sort: true, title: 'id'},
             {field: 'code', sort: true, title: '编码'},
             {field: 'detail', sort: true, title: '描述'},
             {field: 'price', sort: true, title: '最低要求（USDT)'},
             {field: 'rate', sort: true, title: '团队奖收益率（%）'},
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
            queryData['lockTeamId'] = Obj.condition.lockTeamId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看锁仓团队奖配置详情
     */
    Obj.openLockTeamDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '锁仓团队奖配置详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/lockTeam/lockTeam_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除锁仓团队奖配置
         *
         */
        Obj.onDeleteLockTeam = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/lockTeam/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("lockTeamId", data.lockTeamId);
                ajax.start();
            };
            Feng.confirm("是否删除锁仓团队奖配置 ?", operation);
        };

        /**
         * 弹出添加锁仓团队奖配置
         */
        Obj.openAddLockTeam = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加锁仓团队奖配置',
                area:["800px","420px"],
                content: Feng.ctxPath + '/lockTeam/lockTeam_add',
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
         * 点击编辑锁仓团队奖配置
         *
         */
         Obj.onEditLockTeam = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改锁仓团队奖配置',
                area:["800px","420px"],
                content: Feng.ctxPath + '/lockTeam/lockTeam_edit?lockTeamId=' + data.lockTeamId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/lockTeam/list',
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
            Obj.openAddLockTeam();
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
                Obj.onEditLockTeam(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteLockTeam(data);
            }
        });
});