layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 退还押金管理初始化
     */
    var Obj = {
        tableId: "backTable",	//表格id
        condition: {
            backId: ""
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
             {field: 'backId', hide: true, sort: true, title: 'id'},
             {field: 'memberId',hide: true, sort: true, title: '用户'},
             {field: 'memberIdValue', sort: true, title: '用户'},
             {field: 'nickName', sort: true, title: '昵称'},
             {field: 'coin', sort: true, title: '币种'},
             {field: 'number',sort: true, title: '数量'},
             {field: 'status',hide: true,  sort: true, title: '状态'},
             {field: 'statusValue', sort: true, title: '状态'},
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
            queryData['backId'] = Obj.condition.backId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };

    /**
     * 审核 Pass =
     *
     */
    var passApi="/back/pass" ;//
    var failApi="/back/fail";//
    Obj.onCheckPass = function (data) {
        var num = 0;
        var operation = function () {
            num += 1;
            if (num == 1) {
                var ajax = new $ax(Feng.ctxPath + passApi, function () {
                    Feng.success("成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("失败!" + data.message + "!");
                });
                ajax.set("id", data.backId);//
                ajax.start();
            }
        };
        Feng.confirm("是否通过?", operation);
    };


    /**
     * 打开查看退还押金详情
     */
    Obj.openBackDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '退还押金详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/back/back_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除退还押金
         *
         */
        Obj.onDeleteBack = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/back/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("backId", data.backId);
                ajax.start();
            };
            Feng.confirm("是否删除退还押金 ?", operation);
        };

        /**
         * 弹出添加退还押金
         */
        Obj.openAddBack = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加退还押金',
                area:["800px","420px"],
                content: Feng.ctxPath + '/back/back_add',
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
         * 点击编辑退还押金
         *
         */
         Obj.onEditBack = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改退还押金',
                area:["800px","420px"],
                content: Feng.ctxPath + '/back/back_edit?backId=' + data.backId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/back/list',
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
            Obj.openAddBack();
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
                Obj.onEditBack(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteBack(data);
            }else if (layEvent === 'checkPass') {
                Obj.onCheckPass(data);
            }
        });
});