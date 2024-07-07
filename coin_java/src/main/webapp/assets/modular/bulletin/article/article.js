layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 简介文章管理初始化
 */
var Article = {
    tableId: "articleTable",	//表格id
    condition: {
        articleId: ""
    }
};

/**
 * 初始化表格的列
 */
Article.initColumn = function () {
    return [[
        // {type: 'checkbox'},
        {field: 'articleId', hide: true, sort: true, title: 'id'},
        {field: 'articleType', hide: true,sort: true, title: '文章类型'},
        // {field: 'articleTypeValue', sort: true, title: '文章'},
        {field: 'title', sort: true, title: '条款列表'},
        {field: 'language', sort: true, title: '语言',width:200,templet: function (data) {
                return Feng.LanguageMap[data.language]
        }},
        {field: 'createTime', sort: true, title: '发布时间'},
        {field: 'updateTime', sort: true, title: '最后时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Article.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['articleId'] = Article.condition.articleId;
        table.reload(Article.tableId, {where: queryData});
    };



/**
 * 打开查看简介文章详情
 */
Article.openArticleDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '隐私条款详情',
            area: ['1225px', '750px'],
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/article/article_edit/' + Article.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除简介文章
     *
     */
    Article.onDeleteArticle = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/article/delete", function () {
                Feng.success("删除成功!");
                table.reload(Article.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("articleId", data.articleId);
            ajax.start();
        };
        Feng.confirm("是否删除 ?", operation);
    };

    /**
     * 弹出添加简介文章
     */
    Article.openAddArticle = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加内容',
            area: ['1200px', '750px'],
            content: Feng.ctxPath + '/article/article_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Article.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Article.exportExcel = function () {
        var checkRows = table.checkStatus(Article.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.article.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑简介文章
     *
     */
     Article.onEditArticle = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改内容',
            area: ['1200px', '750px'],
            content: Feng.ctxPath + '/article/article_edit?articleId=' + data.articleId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Article.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Article.tableId,
        url: Feng.ctxPath + '/article/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Article.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Article.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Article.openAddArticle();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Article.exportExcel();
    });
    /**
     * 内容详情，局限内容
     */
    Article.bulletinDetail = function (param) {
        var ajax = new $ax(Feng.ctxPath + "/article/content/" + param.articleId, function (data) {
            Article.bulletinDetailContent("内容", data.content);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };
    Article.imgDetail= function (param) {
        var ajax = new $ax(Feng.ctxPath + "/article/content/" + param.articleId, function (data) {
            Article.bulletinDetailImg("图片", data.mainImg);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };


    Article.bulletinDetailContent = function (title, info) {
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
            area: ['1200px', '750px'],
            // content: escape2Html(info)
            content: info
        });
    };
    Article.bulletinDetailImg = function (title, info) {
        console.log("info:"+JSON.stringify(info));
        var resultImg='<div></div>'

        var el="<div><img src='"+info+"'/></div>"
        resultImg+=el;
        top.layer.open({
            title: title,
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['1200px', '750px'], //宽高
            // content: escape2Html(info)
            content: resultImg
        });
    };
    // 工具条点击事件
    table.on('tool(' + Article.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Article.onEditArticle(data);
        } else if (layEvent === 'delete') {
            Article.onDeleteArticle(data);
        }else if(layEvent==='detail')
            Article.bulletinDetail(data);
    });
});
