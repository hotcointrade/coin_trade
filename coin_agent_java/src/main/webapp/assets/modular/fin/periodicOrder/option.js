layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 币币账户管理初始化
 */
var Currency = {
    tableId: "currencyTable",	//表格id
    condition: {
        currencyId: ""
    }
};
var resultList=["","待结算","已结算","已提前结算","已撤销"];
/**
 * 初始化表格的列
 */
Currency.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'id', hide: true,  title: 'id'},
         {field: 'orderNo',  title: '订单编号'},
         {field: 'acount',  title: '用户账号'},

         {field: 'amount',  title: '存入金额',templet:function (data) {
                 return data.amount;
         }},
        {field: 'periodicName', title: '利率名称'},
        {field: 'rate',  title: '利率',templet:function (data) {
                return data.rate+'%';
            }},
         {field: 'winN', title: '收益'},
         {field: 'result', title: '状态',templet:function (data) {
                 return resultList[data.result];
             }},
        {field: 'createTime', title: '下单时间'},
        {field: 'endTime', title: '结算时间',templet:function (data) {
                return data.endTime;
            }},
         // {field: 'updateTime', title: '更新时间'},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Currency.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
       // queryData['currencyId'] = Currency.condition.currencyId;
        table.reload(Currency.tableId, {where: queryData});
    };



/**
 * 打开查看币币账户详情
 */
Currency.openCurrencyDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '周期利率详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/periodicOrder/periodic_edit/' + Currency.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除币币账户
     *
     */
    Currency.onDeleteCurrency = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/periodicOrder/delete", function () {
                Feng.success("删除成功!");
                table.reload(Currency.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("currencyId", data.id);
            ajax.start();
        };
        Feng.confirm("订单状态为"+resultList[data.result]+",是否删除订单 ?", operation);
    };
    /**
     * 提前结算
     *
     */
    Currency.advanceSettlement = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/periodicOrder/advanceSettlement", function () {
                Feng.success("结算成功!");
                table.reload(Currency.tableId);
            }, function (data) {
                Feng.error("结算失败!" + data.responseJSON.message + "!");
            });
            ajax.set("currencyId", data.id);
            if(data.result!='1'){
                Feng.success("结算失败!订单状态不是 未结算");
            }else{
                ajax.start();
            }

        };
        Feng.confirm("是否提前结算 ?", operation);
    };
    /**
     * 撤销
     *
     */
    Currency.revoke = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/periodicOrder/revoke", function () {
                Feng.success("撤销成功!");
                table.reload(Currency.tableId);
            }, function (data) {
                Feng.error("撤销失败!" + data.responseJSON.message + "!");
            });
            ajax.set("currencyId", data.id);
            if(data.result!='1'){
                Feng.success("撤销失败!订单状态不是 未结算");
            }else{
                ajax.start();
            }
        };
        Feng.confirm("是否撤销 ?", operation);

    };

    /**
     * 弹出添加币币账户
     */
    Currency.openAddCurrency = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加周期利率',
            area:["800px","420px"],
            content: Feng.ctxPath + '/periodicOrder/periodic_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Currency.tableId);
            }
        });
    };


   /**
     * 点击编辑币币账户
     *
     */
     Currency.onEditCurrency = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改订单',
            area:["800px","420px"],
            content: Feng.ctxPath + '/periodicOrder/periodic_edit?currencyId=' + data.id,
            end: function () {
                admin.getTempData('formOk') && table.reload(Currency.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Currency.tableId,
        url: Feng.ctxPath + '/periodicOrder/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Currency.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Currency.search();
    });
    //
    // // 提前结算按钮点击事件
    // $('#advanceSettlement').click(function () {
    //     Currency.advanceSettlement()
    // });
    // // 撤销按钮点击事件
    // $('#revoke').click(function () {
    //     Currency.revoke()
    // });



    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Currency.openAddCurrency();
    });



    // 工具条点击事件
    table.on('tool(' + Currency.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Currency.onEditCurrency(data);
        } else if (layEvent === 'delete') {
            Currency.onDeleteCurrency(data);
        }else if (layEvent === 'delete') {
            Currency.onDeleteCurrency(data);
        }else if (layEvent === 'advanceSettlement') {
            Currency.advanceSettlement(data);
        }else if (layEvent === 'revoke') {
            Currency.revoke(data);
        }
    });
});
