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
var typeList=["","周利率","月利率","季利率","年利率"];
/**
 * 初始化表格的列
 */
Currency.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'id', hide: true,  title: 'id'},
         {field: 'name',  title: '名称'},
        {field: 'type', title: '类型',templet:function (data) {
                return typeList[data.type];
            }},
         {field: 'rate',  title: '利率',templet:function (data) {
                 return data.rate+"%";
         }},
        {field: 'minNum', title: '最小金额'},
         {field: 'num', title: '最大金额'},
         {field: 'remark', title: '备注'},
         {field: 'updateTime', title: '更新时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
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
            content: Feng.ctxPath + '/periodic/periodic_edit/' + Currency.seItem.id
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
            var ajax = new $ax(Feng.ctxPath + "/periodic/delete", function () {
                Feng.success("删除成功!");
                table.reload(Currency.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("currencyId", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除周期利率 ?", operation);
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
            content: Feng.ctxPath + '/periodic/periodic_add',
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
            title: '修改期权账户',
            area:["800px","420px"],
            content: Feng.ctxPath + '/periodic/periodic_edit?currencyId=' + data.id,
            end: function () {
                admin.getTempData('formOk') && table.reload(Currency.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Currency.tableId,
        url: Feng.ctxPath + '/periodic/list',
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
        }
    });
});