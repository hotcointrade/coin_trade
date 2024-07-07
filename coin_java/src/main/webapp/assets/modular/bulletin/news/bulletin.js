layui.use(['layer', 'table', 'ax', 'laydate',"form"], function () {
    var $ = layui.$;
    var $ax = layui.ax;
    var layer = layui.layer;
    var admin = layui.admin;
    var table = layui.table;
    var laydate = layui.laydate;
    var form = layui.form;

    /**
     * 轮播管理--
     */
    var news = {
        tableId: "newsTable"   //表格id
    };

    /**
     * 初始化表格的列
     */
    news.initColumn = function () {
        return [[
            // {type: 'checkbox'},
            {field: 'carouselId', hide: true, sort: true, title: 'id'},
            {field: 'title', sort: true, title: '公告标题',width:200},
            {field: 'createTime', sort: true, title: '发布时间',width:230},
            {field: 'language', sort: true, title: '语言',width:200,templet: function (data) {
                    return Feng.LanguageMap[data.language]
            }},

            // {field: 'content', sort: true, title: '内容',width:330},
            {field: 'status', sort: true,templet: '#statusTpl', title: '是否首页'},
            {align: 'center', toolbar: '#tableBar', title: '操作', width: 200}
        ]];
    };


    var Obj=news;
    var apiPrefix="news"
    /**
     * 修改状态
     *
     * @param contactId id
     * @param checked 是否选中（true,false），选中就是启用，未选中就是禁用
     */
    Obj.changeStatus = function (id, checked) {
        if (checked) {
            var ajax = new $ax(Feng.ctxPath + "/"+apiPrefix+"/status/Y", function (data) {
                Feng.success("已启用!");
            }, function (data) {
                Feng.error("失败!");
                table.reload(Obj.tableId);
            });
            ajax.set("id", id);
            ajax.start();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/"+apiPrefix+"/status/N", function (data) {
                Feng.success("已禁用!");
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
                table.reload(Obj.tableId);
            });
            ajax.set("id", id);
            ajax.start();
        }
    };

// 修改user状态
    form.on('switch(status)', function (obj) {

        var id = obj.elem.value;
        var checked = obj.elem.checked ? true : false;

        Obj.changeStatus(id, checked);
    });



    /**
     * 点击查询按钮
     */
    news.search = function () {
        var queryData = {};
        queryData['title'] = $("#title").val();
        table.reload(news.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    news.exportExcel = function () {
        var checkRows = table.checkStatus(news.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 轮播内容详情，局限内容
     */
    news.newsDetail = function (param) {
        var ajax = new $ax(Feng.ctxPath + "/news/content/" + param.carouselId, function (data) {
            Feng.newsDetail("详情", data.content);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };
    news.imgDetail= function (param) {
        var ajax = new $ax(Feng.ctxPath + "/news/content/" + param.carouselId, function (data) {
            Feng.newsDetailImg("详情", data.img);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };


       /**
     *  轮播关闭
     */
    news.onCloseBul=function(data)
    {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/news/close", function () {
                table.reload(news.tableId);
                Feng.success("已关闭轮播!");
            }, function (data) {
                Feng.error("错误！失败!" + data.responseJSON.message + "!");
            });
            ajax.set("carouselId", data.carouselId);
            ajax.start();
        };
        Feng.confirm("是否关闭轮播：" + data.title + "?", operation);
    }

    /**
     *  轮播删除
     */
    news.deleteC=function(data)
    {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/news/delete", function () {
                table.reload(news.tableId);
                Feng.success("已删除该公告!");
            }, function (data) {
                Feng.error("错误！失败!" + data.responseJSON.message + "!");
            });
            ajax.set("carouselId", data.carouselId);
            ajax.start();
        };
        Feng.confirm("是否删除该公告：" + data.title + "?", operation);
    }


    /**
     * 点击编辑
     *
     * @param data 点击按钮时候的行数据
     */
    news.onEditBul = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            area:["1200px","850px"],
            title: '修改公告',
            content: Feng.ctxPath + '/news/bul_edit?carouselId=' + data.carouselId,
            end: function () {
                admin.getTempData('formOk') && table.reload(news.tableId);
            }
        });
    };


    /**
     * 弹出添加
     */
    news.openAddBul=function(){
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            area:["1200px","850px"],
            title: '添加公告',
            content: Feng.ctxPath + '/news/bul_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(news.tableId);
            }
        });
    }


    //渲染时间选择框
    laydate.render({
        elem: '#beginTime'
    });

    //渲染时间选择框
    laydate.render({
        elem: '#endTime'
    });

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + news.tableId,
        url: Feng.ctxPath + '/news/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: news.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        news.search();
    });

    // 搜索按钮点击事件
    $('#btnClean').click(function () {
        news.cleanLog();
    });
    $('#btnAdd').click(function () {
       news.openAddBul();
    });
    // 导出excel
    $('#btnExp').click(function () {
        news.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + news.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'detail') {
            news.newsDetail(data);
        } else if (layEvent === 'close') {
            news.onCloseBul(data);
        }else if(layEvent==='edit'){
            news.onEditBul(data);
        }else if(layEvent==='detailImg')
        {
            news.imgDetail(data);
        }else if(layEvent==='delete')
        {
            news.deleteC(data);
        }
    });

    function htmlEncode(value){
        return $('<div/>').text(value).html();
    }
    //转义符换成普通字符
    function escape2Html(str) {
        var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
        return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
    }
    Feng.newsDetail = function (title, info) {
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
    Feng.newsDetailImg = function (title, info) {
        var el="<div><img src='"+info+"'/></div>"
        top.layer.open({
            title: title,
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['1200px', '850px'], //宽高
            // content: escape2Html(info)
            content: el
        });
    };

});
