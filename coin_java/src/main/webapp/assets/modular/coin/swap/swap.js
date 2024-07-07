layui.use(['table', 'admin', 'ax', 'ztree','laydate','form'], function () {
    var form = layui.form;
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 合约交易对管理初始化
     */
    var Obj = {
        tableId: "swapTable",	//表格id
        condition: {
            swapId: ""
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
             {field: 'swapId', hide: true, sort: true, title: 'id'},
            {field: 'img',  title: '图标',templet: "#imgs" ,minWidth:40},
             {field: 'symbol', sort: true, title: '交易对'},
            {field: 'type',hide: true, sort: true, title: '币种类型'},
            {field: 'typeValue',  title: '币种类型'},
            {field: 'handNumber', sort: true, title: '每手价值数量'},
            {field: 'openFeePrice', sort: true, title: '建仓手续费率',templet:function (data) {
                    return data.openFeePrice +'%';
                }},
            {field: 'closeFeePrice', sort: true, title: '平仓手续费率',templet:function (data) {
                    return data.closeFeePrice +'%';
                }},
            {field: 'number', sort: true, title: '保留小数点位'},
             {field: 'type',  hide: true,sort: true, title: '币种类型'},
/*             {field: 'spread', sort: true, title: '点差'},*/
             {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'status', templet: '#statusTpl', title: '状态'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['condition'] = $("#condition").val();
            queryData['swapId'] = Obj.condition.swapId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };

    var apiPrefix="swap";//api
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
// 状态监听
    form.on('switch(status)', function (obj) {
        var id = obj.elem.value;
        var checked = obj.elem.checked ? true : false;
        Obj.changeStatus(id, checked);
    });
    /**
     * 打开查看合约交易对详情
     */
    Obj.openSwapDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '合约交易对详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/swap/swap_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除合约交易对
         *
         */
        Obj.onDeleteSwap = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/swap/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("swapId", data.swapId);
                ajax.start();
            };
            Feng.confirm("是否删除合约交易对 ?", operation);
        };

        /**
         * 弹出添加合约交易对
         */
        Obj.openAddSwap = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加合约交易对',
                area:["800px","420px"],
                content: Feng.ctxPath + '/swap/swap_add',
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
         * 点击编辑合约交易对
         *
         */
         Obj.onEditSwap = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改合约交易对',
                area:["800px","420px"],
                content: Feng.ctxPath + '/swap/swap_edit?swapId=' + data.swapId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/swap/list',
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
            Obj.openAddSwap();
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
                Obj.onEditSwap(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteSwap(data);
            }
        });
});
