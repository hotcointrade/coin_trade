layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 系统汇率管理初始化
     */
    var Obj = {
        tableId: "platfomrRateTable",	//表格id
        condition: {
            platformRateId: ""
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
             {field: 'platformRateId', hide: true, sort: true, title: 'id'},
             {field: 'rateName', sort: true, title: '名称'},
             {field: 'rateCode', sort: true, title: '语言代码'},
             {field: 'number', sort: true, title: '汇率'},
             {field: 'sign', sort: true, title: '符号'},
             {field: 'sort', sort: true, title: '排序'},
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
            queryData['platformRateId'] = Obj.condition.platformRateId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看系统汇率详情
     */
    Obj.openPlatfomrRateDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '系统汇率详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/platfomrRate/platfomrRate_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除系统汇率
         *
         */
        Obj.onDeletePlatfomrRate = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/platfomrRate/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("platformRateId", data.platformRateId);
                ajax.start();
            };
            Feng.confirm("是否删除系统汇率 ?", operation);
        };

        /**
         * 弹出添加系统汇率
         */
        Obj.openAddPlatfomrRate = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加系统汇率',
                area:["800px","420px"],
                content: Feng.ctxPath + '/platfomrRate/platfomrRate_add',
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
         * 点击编辑系统汇率
         *
         */
         Obj.onEditPlatfomrRate = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改系统汇率',
                area:["800px","420px"],
                content: Feng.ctxPath + '/platfomrRate/platfomrRate_edit?platformRateId=' + data.platformRateId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/platfomrRate/list',
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
            Obj.openAddPlatfomrRate();
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
                Obj.onEditPlatfomrRate(data);
            } else if (layEvent === 'delete') {
                Obj.onDeletePlatfomrRate(data);
            }
        });
});
