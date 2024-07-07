layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 购买挂单订单管理初始化
     */
    var Obj = {
        tableId: "buyTable",	//表格id
        condition: {
            buyId: ""
            ,timeLimit:""
            ,nickName:""
            ,orderNo:""
            ,coin:""
            ,payMethod:""
            ,status:""
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
             {field: 'buyId', hide: true, sort: true, title: 'id'},
             {field: 'memberId',hide: true, sort: true, title: '用户'},
             {field: 'memberIdValue', sort: true, title: '用户'},
             // {field: 'account', sort: true, title: '账号'},
             {field: 'nickName', sort: true, title: '昵称'},
             {field: 'orderNo', sort: true, title: '订单号'},
             {field: 'coin', sort: true, title: '币种'},
             {field: 'unitPrice', sort: true, title: '单价USD'},
             {field: 'number', sort: true, title: '数量'},
             {field: 'totalPrice', sort: true, title: '总额USD'},
             {field: 'lowNumber', sort: true, title: '单笔最小购买数量'},
             {field: 'lowPrice', sort: true, title: '单笔购买金额USD'},
             {field: 'payMethod', sort: true, title: '付款方式'},
             {field: 'restNumber', sort: true, title: '剩余数量'},
             {field: 'status',hide:true, sort: true, title: '是否仍在挂单'},
             {field: 'statusValue', sort: true, title: '是否仍在挂单'},
             // {field: 'finishNumber',hide: true, sort: true, title: '完成数量'},
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
            queryData['buyId'] = Obj.condition.buyId;
            queryData['timeLimit'] = $("#timeLimit").val();
            queryData['nickName'] = $("#nickName").val();
            queryData['orderNo'] = $("#orderNo").val();
            queryData['coin'] = $("#coin").val();
            queryData['payMethod'] = $("#payMethod").val();
            queryData['status'] = $("#status").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看购买挂单订单详情
     */
    Obj.openBuyDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '购买挂单订单详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/buy/buy_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除购买挂单订单
         *
         */
        Obj.onDeleteBuy = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/buy/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("buyId", data.buyId);
                ajax.start();
            };
            Feng.confirm("是否删除购买挂单订单 ?", operation);
        };


        Obj.cancel = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/buy/cancel", function () {
                    Feng.success("撤销成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("撤销失败!" + data.message + "!");
                });
                ajax.set("buyId", data.buyId);
                ajax.start();
            };
            Feng.confirm("是否撤销订单 ?", operation);
        };

        /**
         * 弹出添加购买挂单订单
         */
        Obj.openAddBuy = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加购买挂单订单',
                area:["800px","420px"],
                content: Feng.ctxPath + '/buy/buy_add',
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
         * 点击编辑购买挂单订单
         *
         */
         Obj.onEditBuy = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改购买挂单订单',
                area:["800px","420px"],
                content: Feng.ctxPath + '/buy/buy_edit?buyId=' + data.buyId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/buy/list',
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
            Obj.openAddBuy();
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
                Obj.onEditBuy(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteBuy(data);
            }else if (layEvent === 'cancel') {
                Obj.cancel(data);
            }
        });
});
