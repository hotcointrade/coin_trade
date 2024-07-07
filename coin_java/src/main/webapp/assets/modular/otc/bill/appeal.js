layui.use(['table', 'admin', 'ax', 'ztree', 'laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 申诉订单管理初始化
     */
    var Obj = {
        tableId: "billTable",	//表格id
        condition: {
            billId: ""
            , timeLimit: ""
            , buyAccount: ""
            , buyName: ""
            , sellAccount: ""
            , sellName: ""
            , payMethod: ""
            , orderNo: ""
            , markNo: ""
            , appealStatus: ""
            , duty: ""
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
            {field: 'buyMid', hide: true, sort: true, title: '买方'},
            {field: 'buyMidValue', sort: true, title: '买方'},
            {field: 'buyName', sort: true, title: '买方昵称'},
            {field: 'sellMid', hide: true, sort: true, title: '卖方'},
            {field: 'sellMidValue', sort: true, title: '卖方'},
            {field: 'sellName', sort: true, title: '卖方昵称'},
            {field: 'orderNo', sort: true, title: '订单号'},
            {field: 'markNo', sort: true, title: '参考号'},
            {field: 'coin', sort: true, title: '交易币种'},
            {field: 'unitPrice', sort: true, title: '交易单价USD'},
            {field: 'number', sort: true, title: '交易数量'},
            {field: 'cny', sort: true, title: '交易金额USD'},
            {field: 'payMethod', sort: true, title: '交易方式'},
            // {field: 'payMethodValue', sort: true, title: '交易方式'},
            {field: 'payTime', sort: true, title: '付款时间'},
            {field: 'appealType', hide: true, sort: true, title: '申诉方'},
            {field: 'appealTypeValue', hide: true, sort: true, title: '申诉方'},
            {field: 'content', sort: true, title: '买方申诉内容'},
            {field: 'img', toolbar: '#tableBarDetail', sort: true, title: '申诉凭证'},
            {field: 'atime', sort: true, title: '申诉时间'},
            {field: 'content1', sort: true, title: '卖方申诉内容'},
            {field: 'img1', toolbar: '#tableBarDetail1', sort: true, title: '申诉凭证'},
            {field: 'atime1', sort: true, title: '申诉时间'},

            {field: 'duty', hide: true, sort: true, title: '责任方'},
            {field: 'dutyValue', sort: true, title: '责任方'},
            {field: 'appealStatus', hide: true, sort: true, title: '状态'},
            {field: 'appealStatusValue', sort: true, title: '状态'},


            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200},
            {align: 'center', toolbar: '#tableBarDuty', title: '责任方', minWidth: 300}
        ]];
    };


    var contentApi = "/bill/content/";//

    /**
     * 内容详情，局限内容
     */
    Obj.bulletinDetail = function (param) {
        var id = param.billId;// 业务id
        var ajax = new $ax(Feng.ctxPath + contentApi + id, function (data) {
            Feng.contentDetail("详情", data.content);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };
    Obj.imgDetail = function (param,type) {
        var id = param.billId;// 业务id
        var ajax = new $ax(Feng.ctxPath + contentApi + id, function (data) {
            if(type==0){
                Feng.bulletinDetailImg("详情", data.imgs);//:多图:imgs 单图:img
            }
            else {
                Feng.bulletinDetailImg("详情", data.imgs1);//:多图:imgs 单图:img
            }
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };
    Feng.bulletinDetailImg = function (title, info) {
        console.log("info:" + JSON.stringify(info));
        var resultImg = '<div></div>'
        for (var i = 0; i < info.length; i++) {
            var el = "<div><img src='" + info[i] + "'/></div>"
            resultImg += el;
        }
        top.layer.open({
            title: title,
            type: 1,
            maxmin: true,
            skin: 'layui-layer-rim', //加上边框
            area: ['950px', '600px'], //宽高
            // content: escape2Html(info)
            content: resultImg
        });
    };

    Feng.contentDetail = function (title, info) {
        var display = "";
        if (typeof info === "string") {
            display = info;
        } else {
            if (info instanceof Array) {
                for (var x in info) {
                    display = display + info[x] + "<br/>";
                }
            } else {
                display = info;
            }
        }
        top.layer.open({
            title: title,
            type: 1,
            maxmin: true,
            skin: 'layui-layer-rim', //加上边框
            area: ['950px', '600px'], //宽高
            // content: escape2Html(info)
            content: info
        });
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
        queryData['appealStatus'] = $("#appealStatus").val();
        queryData['duty'] = $("#duty").val();

        table.reload(Obj.tableId, {where: queryData});
    };


    /**
     * 打开查看申诉订单详情
     */
    Obj.openAppealDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin: true,
                title: '申诉订单详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/bill/bill_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


    /**
     * 删除申诉订单
     *
     */
    Obj.onDeleteAppeal = function (data) {
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
        Feng.confirm("是否删除申诉订单 ?", operation);
    };


    Obj.onDutyBuy = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bill/dutyBuy", function () {
                Feng.success("成功!");
                table.reload(Obj.tableId);
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
            });
            ajax.set("billId", data.billId);
            ajax.start();
        };
        Feng.confirm("是否买方责任 ?", operation);
    };


    Obj.onDutySell = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bill/dutySell", function () {
                Feng.success("成功!");
                table.reload(Obj.tableId);
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
            });
            ajax.set("billId", data.billId);
            ajax.start();
        };
        Feng.confirm("是否卖方责任 ?", operation);
    };

    Obj.onDutyNull = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bill/dutyNull", function () {
                Feng.success("成功!");
                table.reload(Obj.tableId);
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
            });
            ajax.set("billId", data.billId);
            ajax.start();
        };
        Feng.confirm("是否双方无责 ?", operation);
    };

    Obj.onPass = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bill/pass", function (data) {
                if (data.code == 200) {
                    Feng.success("成功!");
                }
                else {
                    Feng.error(data.message);
                }

                table.reload(Obj.tableId);
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
            });
            ajax.set("billId", data.billId);
            ajax.start();
        };
        Feng.confirm("是否放币 ?", operation);
    };

    Obj.onFail = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bill/fail", function (data) {
                if (data.code == 200) {
                    Feng.success("成功!");
                }
                else {
                    Feng.error(data.message);
                }
                table.reload(Obj.tableId);
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
            });
            ajax.set("billId", data.billId);
            ajax.start();
        };
        Feng.confirm("是否取消 ?", operation);
    };


    /**
     * 弹出添加申诉订单
     */
    Obj.openAddAppeal = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            maxmin: true,
            title: '添加申诉订单',
            area: ["800px", "420px"],
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
     * 点击编辑申诉订单
     *
     */
    Obj.onEditAppeal = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            maxmin: true,
            title: '修改申诉订单',
            area: ["800px", "420px"],
            content: Feng.ctxPath + '/appeal/appeal_edit?appealId=' + data.appealId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Obj.tableId);
            }
        });
    };
    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Obj.tableId,
        url: Feng.ctxPath + '/bill/list1',
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
        Obj.openAddAppeal();
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
            Obj.onEditAppeal(data);
        } else if (layEvent === 'delete') {
            Obj.onDeleteAppeal(data);
        } else if (layEvent === 'dutyBuy') {
            Obj.onDutyBuy(data);
        } else if (layEvent === 'dutySell') {
            Obj.onDutySell(data);
        } else if (layEvent === 'dutyNull') {
            Obj.onDutyNull(data);
        } else if (layEvent === 'pass') {
            Obj.onPass(data);
        } else if (layEvent === 'fail') {
            Obj.onFail(data);
        } else if (layEvent === 'imgDetail') {//图片
            Obj.imgDetail(data,0);//买方
        }else if (layEvent === 'imgDetail1') {//图片
            Obj.imgDetail(data,1);//卖方
        }

    });
});
