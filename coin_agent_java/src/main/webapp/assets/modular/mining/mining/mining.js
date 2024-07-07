layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 矿机管理初始化
     */
    var Obj = {
        tableId: "miningTable",	//表格id
        condition: {
            miningId: ""
            ,timeLimit:""
        }
    };
    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
     });
    var cycleTypeMap = {
        "1001":"年",
        "1002":"月",
        "1003":"日",
    }
    var stepMap = {
        "0":"待启动",
        "1":"进行中",
        "2":"已结束",
        "3":"已结束",
    }
    /**
     * 初始化表格的列
     */
    Obj.initColumn = function () {
        return [[
            // {type: 'checkbox'},
             {field: 'id', hide: true, sort: true, title: 'id'},
             {field: 'type', hide: true,sort: true, title: '矿机类型（0：一般矿机）'},
             {field: 'image', sort: true,templet: "#imgs", title: '矿机图片'},
             {field: 'title', sort: true, title: '矿机标题'},
             {field: 'name', sort: true, title: '矿机名称'},
            {field: 'image2', sort: true,templet: "#imgs2", title: '币种logo'},
             {field: 'introduction', sort: true, title: '简介'},
            {field: 'step',  sort: true, title: '进度',templet:function (data) {
                    return stepMap[data.step]  ;
                }},
            {field: 'miningCoin',hide: true, sort: true, title: '产能币种'},
             {field: 'estimatedCapacity', sort: true, title: '产能',templet:function (data) {
            return data.estimatedCapacity+data.miningCoin ;
            }},
             {field: 'estimatedStaticYield', sort: true, title: '静态化收益',templet:function (data) {
            return data.estimatedStaticYield+data.staticCoin ;
            }},

             {field: 'cycleType', sort: true, title: '周期', templet:function (data) {
                     return data.cycleNumber+cycleTypeMap[data.cycleType] ;
                 }},
             {field: 'cycleNumber',hide: true, sort: true, title: '周期量'},
                {field: 'fuelEnergyCoin', sort: true, title: '燃料能源', templet:function (data) {
                    return data.fuelEnergyNumber+data.fuelEnergyCoin ;
                }},
             {field: 'fuelEnergyCoin',hide: true ,sort: true, title: '燃料能源币种'},
             {field: 'fuelEnergyNumber',hide: true ,sort: true, title: '燃料能源数量'},
            {field: 'energyLossCoin', sort: true, title: '能源损耗', templet:function (data) {
                    return data.energyLossNumber+data.energyLossCoin ;
                }},
             {field: 'energyLossCoin',hide: true , sort: true, title: '能源损耗币种'},
             {field: 'energyLossNumber',hide: true , sort: true, title: '能源损耗数量'},
            {field: 'calculatingPowerCoin', sort: true, title: '算力币种', templet:function (data) {
                    return data.calculatingPowerNumber+data.calculatingPowerCoin ;
                }},
            {field: 'calculatingPowerCoin',hide: true, sort: true, title: '算力币种'},
             {field: 'calculatingPowerNumber',hide: true, sort: true, title: '算力数量'},
            {field: 'priceCoin', sort: true, title: '合约价格', templet:function (data) {
                    return data.price+data.priceCoin ;
                }},

             {field: 'priceCoin',hide: true, sort: true, title: '合约价格币种'},
             {field: 'price',hide: true, sort: true, title: '合约价格数量'},

             {field: 'startTime',hide: true, sort: true, title: '开始时间'},
             {field: 'endTime',hide: true, sort: true, title: '结束时间'},
             {field: 'miningType',hide: true, sort: true, title: '活动类型(0:未知，1：首发抢购，2：首发分摊，3：持仓瓜分，4：自由认购，5：云矿机， 6：锁仓释放）'},
             {field: 'progress',hide: true, sort: true, title: '进度值（例98 = 98%）,type=4（认购活动）时有效'},
             {field: 'totalSupply', sort: true, title: '发行总量'},
             {field: 'tradedAmount', sort: true, title: '已售总量'},
             // {field: 'priceScale', sort: true, title: '价格精度'},
             // {field: 'numberScale', sort: true, title: '数量精度'},
             {field: 'limitTimes', sort: true, title: '限购次数'},

             {field: 'releaseType',hide: true, sort: true, title: '释放类型 0：等额释放，1：等比释放'},
             {field: 'miningDaysprofit',hide: true, sort: true, title: '产出多少'},
             {field: 'miningInvite',hide: true, sort: true, title: '邀请好友（且购买矿机/锁仓）产能增加百分比（为0则不增加）'},
             {field: 'miningInvitelimit',hide: true, sort: true, title: '产能增加上限百分比（为0则无上限）'},
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
            queryData['miningId'] = Obj.condition.miningId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看矿机详情
     */
    Obj.openMiningDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '矿机详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/mining/mining_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除矿机
         *
         */
        Obj.onDeleteMining = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/mining/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("miningId", data.id);
                ajax.start();
            };
            Feng.confirm("是否删除矿机 ?", operation);
        };

        /**
         * 弹出添加矿机
         */
        Obj.openAddMining = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加矿机',
                area:["800px","420px"],
                content: Feng.ctxPath + '/mining/mining_add',
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
         * 点击编辑矿机
         *
         */
         Obj.onEditMining = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改矿机',
                area:["800px","420px"],
                content: Feng.ctxPath + '/mining/mining_edit?id=' + data.id,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/mining/list',
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
            Obj.openAddMining();
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
                Obj.onEditMining(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteMining(data);
            }
        });
});
