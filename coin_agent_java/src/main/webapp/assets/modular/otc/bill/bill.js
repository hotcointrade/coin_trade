layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 交易结算订单管理初始化
     */
    var Obj = {
        tableId: "billTable",	//表格id
        condition: {
            billId: ""
            ,timeLimit:""
            , buyAccount: ""
            , buyName: ""
            , sellAccount: ""
            , sellName: ""
            , payMethod: ""
            , orderNo: ""
            , markNo: ""
            , status: ""
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
             {field: 'billId', hide: true, sort: true, title: 'id'},
             {field: 'buyMid',hide: true, sort: true, title: '买方'},
             {field: 'buyMidValue', sort: true, title: '买方'},
             {field: 'buyName', sort: true, title: '买方昵称'},
             {field: 'sellMid',hide: true, sort: true, title: '卖方'},
             {field: 'sellMidValue', sort: true, title: '卖方'},
             {field: 'sellName', sort: true, title: '卖方昵称'},
             {field: 'orderNo', sort: true, title: '订单号'},
             {field: 'markNo', sort: true, title: '参考号'},
             {field: 'coin', sort: true, title: '交易币种'},
             {field: 'unitPrice', sort: true, title: '交易单价CNY'},
             {field: 'number', sort: true, title: '交易数量'},
             {field: 'cny', sort: true, title: '交易金额CNY'},
             {field: 'payMethod', sort: true, title: '交易方式'},
             {field: 'payTime', sort: true, title: '付款时间'},
             {field: 'status',hide: true,  sort: true, title: '状态'},
             {field: 'statusValue', sort: true, title: '状态'},
             {field: 'createTime', sort: true, title: '创建时间'},
            // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200},

        ]];
    };


        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['condition'] = $("#condition").val();
            queryData['billId'] = Obj.condition.billId;
            queryData['timeLimit'] = $("#timeLimit").val();

            queryData['buyAccount'] = $("#buyAccount").val();
            queryData['buyName'] = $("#buyName").val();
            queryData['sellAccount'] = $("#sellAccount").val();
            queryData['sellName'] = $("#sellName").val();
            queryData['payMethod'] = $("#payMethod").val();
            queryData['orderNo'] = $("#orderNo").val();
            queryData['markNo'] = $("#markNo").val();
            queryData['status'] = $("#status").val();

            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看交易结算订单详情
     */
    Obj.openBillDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '交易结算订单详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/bill/bill_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除交易结算订单
         *
         */
        Obj.onDeleteBill = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/bill/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("billId", data.billId);
                ajax.start();
            };
            Feng.confirm("是否删除交易结算订单 ?", operation);
        };

        /**
         * 弹出添加交易结算订单
         */
        Obj.openAddBill = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加交易结算订单',
                area:["800px","420px"],
                content: Feng.ctxPath + '/bill/bill_add',
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
         * 点击编辑交易结算订单
         *
         */
         Obj.onEditBill = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改交易结算订单',
                area:["800px","420px"],
                content: Feng.ctxPath + '/bill/bill_edit?billId=' + data.billId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/bill/list',
            page: true,
            height: "full-158",
            cellMinWidth: 100,
            limit:100,
            //limits: [100,200,300,400,500],
            cols: Obj.initColumn()
        });
     // 搜索按钮点击事件
        $('#btnSearch').click(function () {
            Obj.search();
        });

        // 添加按钮点击事件
        $('#btnAdd').click(function () {
            Obj.openAddBill();
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
                Obj.onEditBill(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteBill(data);
            }
        });
});