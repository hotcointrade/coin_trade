layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 上币申请管理初始化
     */
    var Obj = {
        tableId: "coinApplyTable",	//表格id
        condition: {
            coinApplyId: ""
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
             {type: 'checkbox'},
             {field: 'coinApplyId', hide: true, sort: true, title: 'id'},
             {field: 'memberId', sort: true,hide: true, title: '用户id'},
             {field: 'account', sort: true, title: '账户'},
             {field: 'currencyZhName', sort: true, title: '币种全称'},
             {field: 'currencyEnName', sort: true, title: '英文全称'},
             {field: 'currencyAbbrName', sort: true, title: '货币英文缩写'},
             {field: 'marketFloat', sort: true, title: '市场流通量'},
             {field: 'officialHoldings', sort: true, title: '官方持有量'},
             {field: 'officialAddress', sort: true, title: '官方地址'},
             {field: 'blockBrowser', sort: true, title: '区块浏览器'},
             {field: 'movementArea', sort: true, title: '活动区域'},
             {field: 'whitePaperAddress', sort: true, title: '白皮书地址'},
             {field: 'listedExchange', sort: true, title: '已上线交易所'},
             {field: 'projectAreas', sort: true, title: '项目领域'},
             {field: 'functionalInterpretation', sort: true, title: '功能解释'},
             {field: 'projectAddress', sort: true, title: '程序地址'},
             {field: 'addressWay', sort: true, title: '地址方式'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'status', sort: true, title: '状态',templet: function (data) {
                    if (data.status =='Y'){
                        return '已处理';
                    }
                    return '未处理';
                }},
            {fixed: 'right',align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['condition'] = $("#condition").val();
            queryData['coinApplyId'] = Obj.condition.coinApplyId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看上币申请详情
     */
    Obj.openCoinApplyDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '上币申请详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/coinApply/coinApply_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除上币申请
         *
         */
        Obj.onDeleteCoinApply = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/coinApply/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("coinApplyId", data.coinApplyId);
                ajax.start();
            };
            Feng.confirm("是否删除上币申请 ?", operation);
        };

        /**
         * 弹出添加上币申请
         */
        Obj.openAddCoinApply = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加上币申请',
                area:["800px","420px"],
                content: Feng.ctxPath + '/coinApply/coinApply_add',
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
         * 点击编辑上币申请
         *
         */
         Obj.onEditCoinApply = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改上币申请',
                area:["800px","420px"],
                content: Feng.ctxPath + '/coinApply/coinApply_edit?coinApplyId=' + data.coinApplyId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/coinApply/list',
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
            Obj.openAddCoinApply();
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
                Obj.onEditCoinApply(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteCoinApply(data);
            }
        });
});
