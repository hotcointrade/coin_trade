layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 官方社区管理初始化
     */
    var Obj = {
        tableId: "communityTable",	//表格id
        condition: {
            communityId: ""
            ,timeLimit:""
        }
    };
    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
     });
    /**
     * 初始化表格的列
     */
    Obj.initColumn = function () {
        return [[
            // {type: 'checkbox'},
             {field: 'communityId', hide: true, sort: true, title: 'id'},
            {align: 'center', toolbar: '#tableBarDetail', title: '详情', minWidth: 200},
            {field: 'language', sort: true, title: '语言',width:200,templet: function (data) {
                    return Feng.LanguageMap[data.language]
                }},
             {field: 'createTime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['condition'] = $("#condition").val();
            queryData['communityId'] = Obj.condition.communityId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };
    Obj.contentDetail= function (title, info) {
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
     * 打开查看官方社区详情
     */
    Obj.openCommunityDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '官方社区详情',
                area:["1200px","850px"],
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/community/community_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除官方社区
         *
         */
        Obj.onDeleteCommunity = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/community/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("communityId", data.communityId);
                ajax.start();
            };
            Feng.confirm("是否删除官方社区 ?", operation);
        };
    /**
     * 内容详情，局限内容
     */
    Obj.bulletinDetail = function (param) {
        Obj.contentDetail("详情", param.content);

    };

        /**
         * 弹出添加官方社区
         */
        Obj.openAddCommunity = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加官方社区',
                area:["1200px","850px"],
                content: Feng.ctxPath + '/community/community_add',
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };

        /**
         * 导出excel按钮
         */
        Obj.exportExcel = function () {
            var checkRows = table.checkStatus(Obj.tableId);
            if (checkRows.data.length === 0) {
                Feng.error("请选择要导出的数据");
            } else {
                table.exportFile(tableResult.config.id, checkRows.data, 'xls');
            }
        };
       /**
         * 点击编辑官方社区
         *
         */
         Obj.onEditCommunity = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改官方社区',
                area:["1200px","850px"],
                content: Feng.ctxPath + '/community/community_edit?communityId=' + data.communityId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/community/list',
            page: true,
            height: "full-158",
            cellMinWidth: 100,
            //limit:100,
            //limits: [100,200,300,400,500],
            cols: Obj.initColumn()
        });
     // 搜索按钮点击事件
        $('#btnSearch').click(function () {
            Obj.search();
        });

        // 添加按钮点击事件
        $('#btnAdd').click(function () {
            Obj.openAddCommunity();
        });

        // 导出excel
        $('#btnExp').click(function () {
            Obj.exportExcel();
        });

        // 工具条点击事件
        table.on('tool(' + Obj.tableId + ')', function (obj) {
            var data = obj.data;
            var layEvent = obj.event;
            if (layEvent === 'edit') {
                Obj.onEditCommunity(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteCommunity(data);
            } else if (layEvent === 'contentDetail') {
                //内容
                Obj.bulletinDetail(data);
            }
        });
});
