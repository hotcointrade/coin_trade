layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 实名认证管理初始化
 */
var Verify = {
    tableId: "verifyTable",	//表格id
    condition: {
        verifyId: ""
    }
};

/**
 * 初始化表格的列
 */
Verify.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'verifyId', hide: true, sort: true, title: 'id'},
         {field: 'memberId', sort: true,hide: true,  title: '用户id'},
         {field: 'memberIdValue', sort: true, title: '用户'},
         {field: 'fistName', sort: true, title: '姓'},
         {field: 'lastName', sort: true, title: '名'},
         {field: 'idCard', sort: true, title: '身份证号'},
         {field: 'type', sort: true, title: '证件类型'},
         {field: 'statusValue', sort: true, title: '状态'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBarDetail', title: '详情', minWidth: 200},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Verify.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['verifyId'] = Verify.condition.verifyId;
        table.reload(Verify.tableId, {where: queryData});
    };



/**
 * 打开查看实名认证详情
 */
Verify.openVerifyDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '实名认证详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/verify/verify_edit/' + Verify.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除实名认证
     *
     */
    Verify.onDeleteVerify = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/verify/delete", function () {
                Feng.success("删除成功!");
                table.reload(Verify.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("verifyId", data.verifyId);
            ajax.start();
        };
        Feng.confirm("是否删除实名认证 ?", operation);
    };

    /**
     * 弹出添加实名认证
     */
    Verify.openAddVerify = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加实名认证',
            area:["800px","420px"],
            content: Feng.ctxPath + '/verify/verify_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Verify.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Verify.exportExcel = function () {
        var checkRows = table.checkStatus(Verify.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑实名认证
     *
     */
     Verify.onEditVerify = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改实名认证',
            area:["800px","420px"],
            content: Feng.ctxPath + '/verify/verify_edit?verifyId=' + data.verifyId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Verify.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Verify.tableId,
        url: Feng.ctxPath + '/verify/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Verify.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Verify.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Verify.openAddVerify();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Verify.exportExcel();
    });


    /**
     * 审核 Pass =
     *
     */
    Verify.onCheckPass = function (data) {
        var num = 0;
        var operation = function () {
            num += 1;
            if (num == 1) {
                var ajax = new $ax(Feng.ctxPath + "/verify/pass", function () {
                    Feng.success("成功!");
                    table.reload(Verify.tableId);
                }, function (data) {
                    Feng.error("失败!" + data.message + "!");
                });
                ajax.set("id", data.verifyId);
                ajax.start();
            }
        };
        Feng.confirm("是否通过?", operation);
    };

    /**
     * 审核 Fail =
     *
     */
    Verify.onCheckFail = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/verify/fail", function () {
                Feng.success("成功!");
                table.reload(Verify.tableId);
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
            });
            ajax.set("id", data.verifyId);
            ajax.start();
        };
        Feng.confirm("是否拒绝 ?", operation);
    };
    Verify.imgDetail = function (param) {
        var ajax = new $ax(Feng.ctxPath + "/verify/content/" + param.verifyId, function (data) {
            Feng.bulletinDetailImg("详情", data.imgs);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };
    Feng.bulletinDetailImg = function (title, info) {
        console.log("info:" + JSON.stringify(info));
        var resultImg = '<div></div>'
        for (var i = 0; i < info.length; i++) {
            var el = "<div><img style='width: 50%;margin-top: 20px;margin-left: 220px;' src='" + info[i] + "'/></div>"
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

    // 工具条点击事件
    table.on('tool(' + Verify.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Verify.onEditVerify(data);
        } else if (layEvent === 'checkPass') {
            Verify.onCheckPass(data);
        }
        else if (layEvent === 'checkFail') {
            Verify.onCheckFail(data);
        }else if (layEvent === 'imgDetail') {//图片
            Verify.imgDetail(data);
        }
    });
});