layui.use(['layer', 'table', 'ax', 'laydate'], function () {
    var $ = layui.$;
    var $ax = layui.ax;
    var layer = layui.layer;
    var admin = layui.admin;
    var table = layui.table;
    var laydate = layui.laydate;

    /**
     * 轮播管理--
     */
    var bulletin = {
        tableId: "bulletinTable"   //表格id
    };

    /**
     * 初始化表格的列
     */
    bulletin.initColumn = function () {
        return [[
            // {type: 'checkbox'},
            {field: 'carouselId', hide: true, sort: true, title: 'id'},
            {field: 'carouselId',  sort: true, title: '轮播图id'},
            {field: 'link', sort: true, title: '链接'},
            // {field: 'title', sort: true, title: '标题'},
            {field: 'type',hide: true,  sort: true, title: '类型'},
            {field: 'typeValue', sort: true, title: '类型'},
            {field: 'showType', sort: true, title: '展示端',templet: function (data) {
                    if (data.showType =='PC'){
                        return 'PC端';
                    }
                    return '移动端';
                }},
            {field: 'img', sort: true, title: '图片',templet: "#imgs" },
            {field: 'createTime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 100}
        ]];
    };

    /**
     * 点击查询按钮
     */
    bulletin.search = function () {
        var queryData = {};
        queryData['title'] = $("#title").val();
        table.reload(bulletin.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    bulletin.exportExcel = function () {
        var checkRows = table.checkStatus(bulletin.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 轮播内容详情，局限内容
     */
    bulletin.bulletinDetail = function (param) {
        var ajax = new $ax(Feng.ctxPath + "/bulletin/content/" + param.carouselId, function (data) {
            Feng.bulletinDetail("详情", data.content);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };
    bulletin.imgDetail= function (param) {
        var ajax = new $ax(Feng.ctxPath + "/bulletin/content/" + param.carouselId, function (data) {
            Feng.bulletinDetailImg("详情", data.img);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    };


       /**
     *  轮播关闭
     */
    bulletin.onCloseBul=function(data)
    {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bulletin/close", function () {
                table.reload(bulletin.tableId);
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
    bulletin.deleteC=function(data)
    {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bulletin/delete", function () {
                table.reload(bulletin.tableId);
                Feng.success("已删除轮播!");
            }, function (data) {
                Feng.error("错误！失败!" + data.responseJSON.message + "!");
            });
            ajax.set("carouselId", data.carouselId);
            ajax.start();
        };
        Feng.confirm("是否删除轮播：" + data.carouselId + "?", operation);
    }


    /**
     * 点击编辑
     *
     * @param data 点击按钮时候的行数据
     */
    bulletin.onEditBul = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            area:["800px","800px"],
            title: '修改轮播',
            content: Feng.ctxPath + '/bulletin/bul_edit?carouselId=' + data.carouselId,
            end: function () {
                admin.getTempData('formOk') && table.reload(bulletin.tableId);
            }
        });
    };


    /**
     * 弹出添加
     */
    bulletin.openAddBul=function(){
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            area:["1000px","800px"],
            title: '添加轮播',
            content: Feng.ctxPath + '/bulletin/bul_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(bulletin.tableId);
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
        elem: '#' + bulletin.tableId,
        url: Feng.ctxPath + '/bulletin/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: bulletin.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        bulletin.search();
    });

    // 搜索按钮点击事件
    $('#btnClean').click(function () {
        bulletin.cleanLog();
    });
    $('#btnAdd').click(function () {
       bulletin.openAddBul();
    });
    // 导出excel
    $('#btnExp').click(function () {
        bulletin.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + bulletin.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'detail') {
            console.log("dfs")
            bulletin.bulletinDetail(data);
        } else if (layEvent === 'close') {
            bulletin.onCloseBul(data);
        }else if(layEvent==='edit'){
            bulletin.onEditBul(data);
        }else if(layEvent==='detailImg')
        {
            bulletin.imgDetail(data);
        }else if(layEvent==='delete')
        {
            bulletin.deleteC(data);
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
    Feng.bulletinDetail = function (title, info) {
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
            area: ['950px', '600px'], //宽高
            // content: escape2Html(info)
            content: info
        });
    };
    Feng.bulletinDetailImg = function (title, info) {
        var el="<div><img src='"+info+"'/></div>"
        top.layer.open({
            title: title,
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['950px', '600px'], //宽高
            // content: escape2Html(info)
            content: el
        });
    };

});
