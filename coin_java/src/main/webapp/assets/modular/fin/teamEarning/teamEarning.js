layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 团队佣金管理初始化
     */
    var Obj = {
        tableId: "teamEarningTable",	//表格id
        condition: {
            teamEarningId: ""
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
             {field: 'walletId', hide: true, sort: true, title: 'id'},
             {field: 'memberId', hide: true,sort: true, title: '用户id'},
             {field: 'account', sort: true, title: '账户'},
             {field: 'usedPrice', sort: true, title: '可用额度'},
             {field: 'withdrawPrice', sort: true, title: '已提现'},
             {field: 'blockedPrice', hide: true,sort: true, title: '封禁额度'},
             {field: 'createTime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['condition'] = $("#condition").val();
            queryData['teamEarningId'] = Obj.condition.walletId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看团队佣金详情
     */
    Obj.openTeamEarningDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '团队佣金详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/teamEarning/teamEarning_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除团队佣金
         *
         */
        Obj.onDeleteTeamEarning = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/teamEarning/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("teamEarningId", data.walletId);
                ajax.start();
            };
            Feng.confirm("是否删除团队佣金 ?", operation);
        };

        /**
         * 弹出添加团队佣金
         */
        Obj.openAddTeamEarning = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加团队佣金',
                area:["800px","420px"],
                content: Feng.ctxPath + '/teamEarning/teamEarning_add',
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
         * 点击编辑团队佣金
         *
         */
         Obj.onEditTeamEarning = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改团队佣金',
                area:["800px","420px"],
                content: Feng.ctxPath + '/teamEarning/teamEarning_edit?teamEarningId=' + data.walletId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/teamEarning/list',
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
            Obj.openAddTeamEarning();
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
                Obj.onEditTeamEarning(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteTeamEarning(data);
            }
        });
});
