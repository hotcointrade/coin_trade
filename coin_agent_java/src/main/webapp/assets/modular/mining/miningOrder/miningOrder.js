layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 矿机订单管理初始化
     */
    var Obj = {
        tableId: "miningOrderTable",	//表格id
        condition: {
            miningOrderId: ""
            ,timeLimit:""
        }
    };
    var cycleTypeMap = {
        "1001":"年",
        "1002":"月",
        "1003":"日",
    }
    var statusMap = {
        "0":"待启动",
        "1":"挖矿中",
        "2":"已结束",
        "3":"待启动",
    }
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
             {field: 'miningOrderId', hide: true, sort: true, title: 'id'},
             {field: 'image', sort: true, templet: "#imgs",title: '矿机图片'},
             {field: 'title', sort: true, title: '矿机标题'},
            {field: 'image2', sort: true,templet: "#imgs2", title: '币种logo'},
             {field: 'memberIdValue', sort: true, title: '活动人'},
             // {field: 'miningId', sort: true, title: '矿机'},
             {field: 'name', sort: true, title: '矿机名称'},

            {field: 'cycleType', sort: true, title: '周期'
                , templet:function (data) {
                    return data.cycleNumber+cycleTypeMap[data.cycleType] ;
                }},
            {field: 'miningStatus', sort: true, title: '挖矿状态'
                , templet:function (data) {
                    return statusMap[data.miningStatus];
                }},
            {field: 'estimatedCapacity', sort: true, title: '目标产能',templet:function (data) {
                    return data.estimatedCapacity+data.miningCoin ;
                }},
            {field: 'estimatedStaticYield', sort: true, title: '静态化目标收益',templet:function (data) {
                    return data.estimatedStaticYield+data.staticCoin ;
                }},
            {field: 'fuelEnergyCoin', sort: true, title: '燃料能源', templet:function (data) {
                    return data.fuelEnergyNumber+data.fuelEnergyCoin ;
                }},
            {field: 'energyLossCoin', sort: true, title: '能源损耗', templet:function (data) {
                    return data.energyLossNumber+data.energyLossCoin ;
                }},
            {field: 'priceCoin', sort: true, title: '合约价格', templet:function (data) {
                    return data.price+data.priceCoin ;
                }},
            {field: 'miningedDays', sort: true, title: '挖矿天数'},
            {field: 'miningDays', sort: true, title: '静态天数'},
             // {field: 'miningDaysprofit', sort: true, title: '原始日产出'},
             // {field: 'currentDaysprofit', sort: true, title: '当前日产出'},
             // {field: 'totalProfit', sort: true, title: '总产出'},
             {field: 'received', sort: true, title: '产能收益'},
             {field: 'available', sort: true, title: '静态化收益'},
             // {field: 'miningInvite', sort: true, title: '邀请好友（且购买矿机/锁仓）产能增加百分比（为0则不增加）'},
             // {field: 'miningInvitelimit', sort: true, title: '产能增加上限百分比（为0则无上限）'},
             // {field: 'endTime', sort: true, title: '结束时间'},
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
            queryData['miningOrderId'] = Obj.condition.miningOrderId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看矿机订单详情
     */
    Obj.openMiningOrderDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '矿机订单详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/miningOrder/miningOrder_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除矿机订单
         *
         */
        Obj.onDeleteMiningOrder = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/miningOrder/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("miningOrderId", data.miningOrderId);
                ajax.start();
            };
            Feng.confirm("是否删除矿机订单 ?", operation);
        };

        /**
         * 弹出添加矿机订单
         */
        Obj.openAddMiningOrder = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加矿机订单',
                area:["800px","420px"],
                content: Feng.ctxPath + '/miningOrder/miningOrder_add',
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
         * 点击编辑矿机订单
         *
         */
         Obj.onEditMiningOrder = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改矿机订单',
                area:["800px","420px"],
                content: Feng.ctxPath + '/miningOrder/miningOrder_edit?miningOrderId=' + data.miningOrderId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/miningOrder/list',
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
            Obj.openAddMiningOrder();
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
                Obj.onEditMiningOrder(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteMiningOrder(data);
            }
        });
});
