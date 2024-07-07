layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 平台账户管理初始化
 */
var Company = {
    tableId: "companyTable",	//表格id
    condition: {
        companyId: ""
    }
};

/**
 * 初始化表格的列
 */
Company.initColumn = function () {
    return [[
        // {type: 'checkbox'},
        {field: 'companyId', hide: true, sort: true, title: 'id'},
        {field: 'companyName',  title: '公司名称'},
        {field: 'account',  title: '账号'},
        {field: 'bankName',  title: '开户行'},
        {field: 'branchName',  title: '支行'},
        {field: 'createTime',  title: '创建时间'},
        {field: 'updateTime',  title: '更新时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Company.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['companyId'] = Company.condition.companyId;
        table.reload(Company.tableId, {where: queryData});
    };



/**
 * 打开查看平台账户详情
 */
Company.openCompanyDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '平台账户详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/company/company_edit/' + Company.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除平台账户
     *
     */
    Company.onDeleteCompany = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/company/delete", function () {
                Feng.success("删除成功!");
                table.reload(Company.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("companyId", data.companyId);
            ajax.start();
        };
        Feng.confirm("是否删除平台账户 ?", operation);
    };

    /**
     * 弹出添加平台账户
     */
    Company.openAddCompany = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加平台账户',
            area:["800px","420px"],
            content: Feng.ctxPath + '/company/company_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Company.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Company.exportExcel = function () {
        var checkRows = table.checkStatus(Company.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.company.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑平台账户
     *
     */
     Company.onEditCompany = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改平台账户',
            area:["800px","420px"],
            content: Feng.ctxPath + '/company/company_edit?companyId=' + data.companyId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Company.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Company.tableId,
        url: Feng.ctxPath + '/company/list',
        page: false,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Company.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Company.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Company.openAddCompany();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Company.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Company.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Company.onEditCompany(data);
        } else if (layEvent === 'delete') {
            // Company.onDeleteCompany(data);
        }
    });
});