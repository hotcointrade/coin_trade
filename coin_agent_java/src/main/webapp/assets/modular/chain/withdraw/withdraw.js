layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;
/**
 * 提币信息管理初始化
 */
var Withdraw = {
    tableId: "withdrawTable",	//表格id
    condition: {
        withdrawId: ""
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
Withdraw.initColumn = function () {
    return [[
        {type: 'checkbox'},
         {field: 'withdrawId', hide: true, sort: true, title: 'id'},
         {field: 'orderNo', sort: true, title: '单号'},
         {field: 'memberId',  hide: true,sort: true, title: '用户'},
         {field: 'memberIdValue', sort: true, title: '用户'},
         {field: 'price', sort: true, title: '提现数额'},
         {field: 'actualPrice', sort: true, title: '实际数额'},
         {field: 'fee', sort: true, title: '手续费'},
         {field: 'type', sort: true, title: '提币币种'},
         {field: 'toAddress', sort: true, title: '地址'},
         {field: 'txHash', sort: true, title: '交易hash'},
/*         {field: 'remark', sort: true, title: '矿工费'},*/
         {field: 'status',hide: true,sort: true, title: '状态'},
         {field: 'statusValue', sort: true, title: '状态'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Withdraw.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['withdrawId'] = Withdraw.condition.withdrawId;
        queryData['toAddress'] = $("#toAddress").val();
        queryData['txHash'] = $("#txHash").val();
        queryData['type'] = $("#type").val();
        queryData['status'] = $("#status").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(Withdraw.tableId, {where: queryData});
        total();
    };

    $(function () {
        total();
    })
    function total(){
        $.ajax({
            url:Feng.ctxPath+'/withdraw/total',
            data:{
                "condition":$("#condition").val()
                , "toAddress":$("#toAddress").val()
                , "txHash":$("#txHash").val()
                , "type":$("#type").val()
                , "status":$("#status").val()
                , "timeLimit":$("#timeLimit").val()
            },
            success:function (data) {
                if(data!=null)
                {
                    // $("#usdt").text(data.usdt);
                    // $("#eth").text(data.eth);
                    // $("#mge").text(data.mge);
                    // $("#mgm").text(data.mgm);
                    $("#totalWithdraw").text(data.totalWithdraw);
                    $("#totalGas").text(data.totalGas);
                    $("#totalFee").text(data.totalFee);
                    $("#totalActual").text(data.totalActual);
                }
            }
        })
    }

    /**
     * 审核 Pass =
     *
     */
    Withdraw.onCheckPass = function (data) {


        var num = 0;
        var operation = function () {
            num += 1;
            if (num == 1) {
                var ajax = new $ax(Feng.ctxPath + "/withdraw/pass", function () {
                    Feng.success("成功!");
                    table.reload(Withdraw.tableId);
                }, function (data) {
                    var info= data.responseText;
                    console.log(info)
                    if(info!=null)
                    {
                        var msg=info
                        data.message=msg;
                    }
                    Feng.error("失败!" + data.message + "!");
                });
                ajax.set("id", data.withdrawId);
                ajax.start();
            }
        };
        Feng.confirm("是否通过 ?", operation);
    };

    /**
     * 审核 Fail =
     *
     */
    Withdraw.onCheckFail = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/withdraw/fail", function () {
                Feng.success("成功!");
                table.reload(Withdraw.tableId);
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
            });
            ajax.set("id", data.withdrawId);
            ajax.start();
        };
        Feng.confirm("是否拒绝 ?", operation);
    };


/**
 * 打开查看提币信息详情
 */
Withdraw.openWithdrawDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '提币信息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/withdraw/withdraw_edit/' + Withdraw.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除提币信息
     *
     */
    Withdraw.onDeleteWithdraw = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/withdraw/delete", function () {
                Feng.success("删除成功!");
                table.reload(Withdraw.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("withdrawId", data.withdrawId);
            ajax.start();
        };
        Feng.confirm("是否删除提币信息 ?", operation);
    };

    /**
     * 弹出添加提币信息
     */
    Withdraw.openAddWithdraw = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加提币信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/withdraw/withdraw_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Withdraw.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Withdraw.exportExcel = function () {
        var checkRows = table.checkStatus(Withdraw.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑提币信息
     *
     */
     Withdraw.onEditWithdraw = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改提币信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/withdraw/withdraw_edit?withdrawId=' + data.withdrawId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Withdraw.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Withdraw.tableId,
        url: Feng.ctxPath + '/withdraw/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Withdraw.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Withdraw.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Withdraw.openAddWithdraw();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Withdraw.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Withdraw.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Withdraw.onEditWithdraw(data);
        } else if (layEvent === 'delete') {
            Withdraw.onDeleteWithdraw(data);
        }else if (layEvent === 'checkPass') {
            Withdraw.onCheckPass(data);
        }
        else if (layEvent === 'checkFail') {
            Withdraw.onCheckFail(data);
        }
    });
});