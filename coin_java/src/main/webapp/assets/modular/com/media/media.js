layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 视频和音频管理初始化
 */
var Media = {
    tableId: "mediaTable",	//表格id
    condition: {
        mediaId: ""
    }
};

/**
 * 初始化表格的列
 */
Media.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'mediaId', hide: true, sort: true, title: 'id'},
         {field: 'title', sort: true, title: '标题'},
         // {field: 'content', sort: true, title: '内容'},
         {field: 'address', sort: true,hide: true, title: '链接地址'},
         // {field: 'img', sort: true, title: '图片'},
         {field: 'type', hide:true,sort: true, title: '类型(0:视频 1:语音）'},
         {field: 'typeValue', sort: true, title: '类型'},
         // {field: 'sort', sort: true, title: '排序'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Media.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['mediaId'] = Media.condition.mediaId;
        table.reload(Media.tableId, {where: queryData});
    };



/**
 * 打开查看视频和音频详情
 */
Media.openMediaDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '视频和音频详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/media/media_edit/' + Media.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除视频和音频
     *
     */
    Media.onDeleteMedia = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/media/delete", function () {
                Feng.success("删除成功!");
                table.reload(Media.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("mediaId", data.mediaId);
            ajax.start();
        };
        Feng.confirm("是否删除视频和音频 ?", operation);
    };
 /**
     *播放视频和音频
     *
     */
    Media.play = function (data) {
        // $.window.attr("href",data.address);
        window.open(data.address);
        // var operation = function () {
        //
        //     ajax.set("mediaId", data.mediaId);
        //     ajax.start();
        // };
        // Feng.confirm("是否删除视频和音频 ?", operation);
    };

    /**
     * 弹出添加视频和音频
     */
    Media.openAddMedia = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加视频和音频',
            area:["800px","420px"],
            content: Feng.ctxPath + '/media/media_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Media.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Media.exportExcel = function () {
        var checkRows = table.checkStatus(Media.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.media.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑视频和音频
     *
     */
     Media.onEditMedia = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改视频和音频',
            area:["800px","420px"],
            content: Feng.ctxPath + '/media/media_edit?mediaId=' + data.mediaId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Media.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Media.tableId,
        url: Feng.ctxPath + '/media/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Media.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Media.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Media.openAddMedia();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Media.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Media.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Media.onEditMedia(data);
        } else if (layEvent === 'delete') {
            Media.onDeleteMedia(data);
        } else if (layEvent === 'play') {
            Media.play(data);
        }
    });
});