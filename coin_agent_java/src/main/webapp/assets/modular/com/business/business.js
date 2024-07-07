layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 行业资讯管理初始化
 */
var Business = {
    tableId: "businessTable",	//表格id
    condition: {
        businessId: ""
    }
};

/**
 * 初始化表格的列
 */
Business.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'businessId', hide: true, sort: true, title: 'id'},
         // {field: 'type', sort: true, title: '文章类型'},
         {field: 'mainImg', sort: true, title: '主图',templet: "#img"},
         // {field: 'subImg', sort: true, title: '子图JSON'},
         {field: 'title', sort: true, title: '标题'},
         // {field: 'content', sort: true, title: '内容'},
         {field: 'remark', sort: true, title: '来源'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Business.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['businessId'] = Business.condition.businessId;
        table.reload(Business.tableId, {where: queryData});
    };



/**
 * 打开查看行业资讯详情
 */
Business.openBusinessDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '行业资讯详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/business/business_edit/' + Business.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除行业资讯
     *
     */
    Business.onDeleteBusiness = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/business/delete", function () {
                Feng.success("删除成功!");
                table.reload(Business.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("businessId", data.businessId);
            ajax.start();
        };
        Feng.confirm("是否删除行业资讯 ?", operation);
    };

    /**
     * 弹出添加行业资讯
     */
    Business.openAddBusiness = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加行业资讯',
            area:["800px","420px"],
            content: Feng.ctxPath + '/business/business_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Business.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Business.exportExcel = function () {
        var checkRows = table.checkStatus(Business.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.business.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑行业资讯
     *
     */
     Business.onEditBusiness = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改行业资讯',
            area:["800px","620px"],
            content: Feng.ctxPath + '/business/business_edit?businessId=' + data.businessId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Business.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Business.tableId,
        url: Feng.ctxPath + '/business/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Business.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Business.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Business.openAddBusiness();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Business.exportExcel();
    });
    /**
     * 内容详情，局限内容
     */
    Business.bulletinDetail = function (param) {
        var ajax = new $ax(Feng.ctxPath + "/business/content/" + param.businessId, function (data) {
            Business.bulletinDetailContent("内容", data.content);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };
    Business.imgDetail= function (param) {
        var ajax = new $ax(Feng.ctxPath + "/business/content/" + param.businessId, function (data) {
            Business.bulletinDetailImg("图片", data.mainImg);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };


    Business.bulletinDetailContent = function (title, info) {
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
            skin: 'layui-layer-rim', //加上边框
            area: ['1000px', '800px'],
            // content: escape2Html(info)
            content: info
        });
    };
    Business.bulletinDetailImg = function (title, info) {
        console.log("info:"+JSON.stringify(info));
        var resultImg='<div></div>'

        var el='<div><img style="width: 800px;height: 600px" src="'+info+'"/></div>'
        resultImg+=el;

        top.layer.open({
            title: title,
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['950px', '650px'], //宽高
            // content: escape2Html(info)
            content: resultImg
        });
    };
    // 工具条点击事件
    table.on('tool(' + Business.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Business.onEditBusiness(data);
        } else if (layEvent === 'delete') {
            Business.onDeleteBusiness(data);
        }else if(layEvent==='detail'){
            Business.bulletinDetail(data);
        }else if(layEvent==='imgDetail'){
            Business.imgDetail(data);
        }
           
    });
});