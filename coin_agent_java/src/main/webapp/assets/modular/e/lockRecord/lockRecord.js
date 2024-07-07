layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 锁仓记录管理初始化
     */
    var Obj = {
        tableId: "lockRecordTable",	//表格id
        condition: {
            lockRecordId: ""
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
             {field: 'lockRecordId', hide: true, sort: true, title: 'id'},
             {field: 'memberId',  hide: true,sort: true, title: '用户'},
             {field: 'memberIdValue', sort: true, title: '用户'},
             {field: 'lockId', hide: true, sort: true, title: '锁仓id'},
             {field: 'lockName', sort: true, title: '锁仓周期'},
             {field: 'days', sort: true, title: '锁仓天数'},
             {field: 'rate', sort: true, title: '每日静态收益率（%）'},
             {field: 'number', sort: true, title: '锁仓数量（MGE）'},
             {field: 'price', sort: true, title: '锁仓价值(USDT)'},
             {field: 'startTime', hide: true, sort: true, title: '锁仓起始时间'},
             {field: 'endTime', sort: true, title: '解锁时间'},
             {field: 'profitCount', sort: true, title: '收益次数'},
             {field: 'createTime', sort: true, title: '创建时间'},
            // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['condition'] = $("#condition").val();
            queryData['lockRecordId'] = Obj.condition.lockRecordId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看锁仓记录详情
     */
    Obj.openLockRecordDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '锁仓记录详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/lockRecord/lockRecord_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除锁仓记录
         *
         */
        Obj.onDeleteLockRecord = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/lockRecord/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("lockRecordId", data.lockRecordId);
                ajax.start();
            };
            Feng.confirm("是否删除锁仓记录 ?", operation);
        };

        /**
         * 弹出添加锁仓记录
         */
        Obj.openAddLockRecord = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加锁仓记录',
                area:["800px","420px"],
                content: Feng.ctxPath + '/lockRecord/lockRecord_add',
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
         * 点击编辑锁仓记录
         *
         */
         Obj.onEditLockRecord = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改锁仓记录',
                area:["800px","420px"],
                content: Feng.ctxPath + '/lockRecord/lockRecord_edit?lockRecordId=' + data.lockRecordId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/lockRecord/list',
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
            Obj.openAddLockRecord();
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
                Obj.onEditLockRecord(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteLockRecord(data);
            }
        });
});