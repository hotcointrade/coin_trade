layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 常见问题管理初始化
 */
var Problem = {
    tableId: "problemTable",	//表格id
    condition: {
        problemId: ""
    }
};

/**
 * 初始化表格的列
 */
Problem.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'problemId', hide: true, sort: true, title: 'id'},
         {field: 'title', sort: true, title: '标题'},
         {field: 'remark', sort: true, title: '类型'},
        {field: 'language', sort: true, title: '语言',width:200,templet: function (data) {
                switch (data.language) {
                    case 'zh':
                        return '中文简体'
                        break;
                    case 'hk':
                        return '中文繁体'
                        break;
                    case 'en':
                        return '英语'
                        break;
                    case 'jp':
                        return '日语'
                        break;
                    case 'hy':
                        return '韩语'
                        break;
                    default:
                        return '请设置语言'
                }}},
         {align: 'center', toolbar: '#tableBarDetail', title: '详情', minWidth: 200},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};

    /**
     * 内容详情，局限内容
     */
    Problem.bulletinDetail = function (param) {
        var ajax = new $ax(Feng.ctxPath + "/problem/content/" + param.problemId, function (data) {
            Problem.contentDetail("详情", data.content);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };


    Problem.contentDetail= function (title, info) {
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
            area: ['1200px', '850px'], //宽高
            // content: escape2Html(info)
            content: info
        });
    };

    /**
     * 点击查询按钮
     */
    Problem.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['problemId'] = Problem.condition.problemId;
        table.reload(Problem.tableId, {where: queryData});
    };



/**
 * 打开查看常见问题详情
 */
Problem.openProblemDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '常见问题详情',
            area:["1200px","850px"], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/problem/problem_edit/' + Problem.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除常见问题
     *
     */
    Problem.onDeleteProblem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/problem/delete", function () {
                Feng.success("删除成功!");
                table.reload(Problem.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("problemId", data.problemId);
            ajax.start();
        };
        Feng.confirm("是否删除 ?", operation);
    };

    /**
     * 弹出添加常见问题
     */
    Problem.openAddProblem = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加',
            area:["1200px","850px"],
            content: Feng.ctxPath + '/problem/problem_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Problem.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Problem.exportExcel = function () {
        var checkRows = table.checkStatus(Problem.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.problem.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑常见问题
     *
     */
     Problem.onEditProblem = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改',
            area:["1200px","850px"],
            content: Feng.ctxPath + '/problem/problem_edit?problemId=' + data.problemId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Problem.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Problem.tableId,
        url: Feng.ctxPath + '/problem/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Problem.initColumn(),
        done: function (res, curr, count) {
            $("[data-field='remark']").children().each(function () {
                if ($(this).text() == 'NEW') {
                    $(this).text("新手指南")
                } else if ($(this).text() == 'HELP') {
                    $(this).text("帮助中心")
                }
            });
        }
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Problem.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Problem.openAddProblem();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Problem.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Problem.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Problem.onEditProblem(data);
        } else if (layEvent === 'delete') {
            Problem.onDeleteProblem(data);
        } else if (layEvent === 'contentDetail') {
            //内容
            Problem.bulletinDetail(data);
        }
    });
});
