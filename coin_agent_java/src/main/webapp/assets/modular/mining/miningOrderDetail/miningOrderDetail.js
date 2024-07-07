layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 矿机收益管理初始化
     */
    var Obj = {
        tableId: "miningOrderDetailTable",	//表格id
        condition: {
            miningOrderDetailId: ""
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
             {field: 'id', hide: true, sort: true, title: 'id'},
             {field: 'memberIdValue', sort: true, title: '用户'},
            {field: 'image', sort: true,templet: "#imgs", title: '矿机logo'},
             {field: 'miningName', sort: true, title: '矿机'},
            {field: 'image2', sort: true,templet: "#imgs2", title: '币种logo'},

            {field: 'output', sort: true, title: '矿机当期产出'},
             {field: 'miningUnit', sort: true, title: '币种'},

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
            queryData['miningOrderDetailId'] = Obj.condition.miningOrderDetailId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看矿机收益详情
     */
    Obj.openMiningOrderDetailDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '矿机收益详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/miningOrderDetail/miningOrderDetail_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除矿机收益
         *
         */
        Obj.onDeleteMiningOrderDetail = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/miningOrderDetail/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("miningOrderDetailId", data.id);
                ajax.start();
            };
            Feng.confirm("是否删除矿机收益 ?", operation);
        };

        /**
         * 弹出添加矿机收益
         */
        Obj.openAddMiningOrderDetail = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加矿机收益',
                area:["800px","420px"],
                content: Feng.ctxPath + '/miningOrderDetail/miningOrderDetail_add',
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
         * 点击编辑矿机收益
         *
         */
         Obj.onEditMiningOrderDetail = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改矿机收益',
                area:["800px","420px"],
                content: Feng.ctxPath + '/miningOrderDetail/miningOrderDetail_edit?miningOrderDetailId=' + data.miningOrderDetailId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/miningOrderDetail/list',
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
            Obj.openAddMiningOrderDetail();
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
                Obj.onEditMiningOrderDetail(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteMiningOrderDetail(data);
            }
        });
});
