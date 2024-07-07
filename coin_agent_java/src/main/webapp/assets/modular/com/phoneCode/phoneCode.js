layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 电话区号管理初始化
 */
var PhoneCode = {
    tableId: "phoneCodeTable",	//表格id
    condition: {
        phoneCodeId: ""
    }
};

/**
 * 初始化表格的列
 */
PhoneCode.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'phoneCodeId', hide: true, sort: true, title: 'id'},
         {field: 'country', sort: true, title: '国家'},
         {field: 'code', sort: true, title: '区号'},
         {field: 'type', sort: true, title: '类型 0：国内 1：国外'},
         {field: 'area', sort: true, title: '所在区域'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    PhoneCode.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['phoneCodeId'] = PhoneCode.condition.phoneCodeId;
        table.reload(PhoneCode.tableId, {where: queryData});
    };



/**
 * 打开查看电话区号详情
 */
PhoneCode.openPhoneCodeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '电话区号详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/phoneCode/phoneCode_edit/' + PhoneCode.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除电话区号
     *
     */
    PhoneCode.onDeletePhoneCode = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/phoneCode/delete", function () {
                Feng.success("删除成功!");
                table.reload(PhoneCode.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("phoneCodeId", data.phoneCodeId);
            ajax.start();
        };
        Feng.confirm("是否删除电话区号 ?", operation);
    };

    /**
     * 弹出添加电话区号
     */
    PhoneCode.openAddPhoneCode = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加电话区号',
            area:["800px","420px"],
            content: Feng.ctxPath + '/phoneCode/phoneCode_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(PhoneCode.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    PhoneCode.exportExcel = function () {
        var checkRows = table.checkStatus(PhoneCode.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.phoneCode.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑电话区号
     *
     */
     PhoneCode.onEditPhoneCode = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改电话区号',
            area:["800px","420px"],
            content: Feng.ctxPath + '/phoneCode/phoneCode_edit?phoneCodeId=' + data.phoneCodeId,
            end: function () {
                admin.getTempData('formOk') && table.reload(PhoneCode.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + PhoneCode.tableId,
        url: Feng.ctxPath + '/phoneCode/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: PhoneCode.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        PhoneCode.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        PhoneCode.openAddPhoneCode();
    });

    // 导出excel
    $('#btnExp').click(function () {
        PhoneCode.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + PhoneCode.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            PhoneCode.onEditPhoneCode(data);
        } else if (layEvent === 'delete') {
            PhoneCode.onDeletePhoneCode(data);
        }
    });
});