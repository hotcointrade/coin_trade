layui.use(['table', 'admin', 'ax', 'ztree','laydate','form'], function () {
    var form = layui.form;
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 现货交易对管理初始化
     */
    var Obj = {
        tableId: "spotTable",	//表格id
        condition: {
            spotId: ""
            ,timeLimit:""
        }
    };
    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
     });
    var typeMap={1:'自由浮动',2:'要控制涨',3:'要控制跌'}
    /**
     * 初始化表格的列
     */
    Obj.initColumn = function () {
        return [[
            // {type: 'checkbox'},
             {field: 'id', hide: true, sort: true, title: 'id'},
            {field: 'p8LossRatio', sort: true, title: '交易模式',minWidth: 60,templet:function (data) {
                    return data.p8LossRatio>=1?"永续合约":"币币交易";
                }},
            {field: 'symbol', sort: true, title: '交易对',minWidth: 60},
            {field: 'p1LossRatio', sort: true, title: '行情模式',minWidth: 60,templet:function (data) {
                    return typeMap[data.p1LossRatio];
                }},
            {field: 'p7LossRatio', sort: true, title: '历史数据',minWidth: 60,templet:function (data) {
                    return data.p7LossRatio>1?"是":"否";
                }},
            {field: 'fistOpenPrice', sort: true, title: '初始价格',minWidth: 60},
            {field: 'p2LossRatio', sort: true, title: '高',minWidth: 60},
            {field: 'p3LossRatio', sort: true, title: '开',minWidth: 60},
            {field: 'p4LossRatio', sort: true, title: '低',minWidth: 60},
            {field: 'p5LossRatio', sort: true, title: '收',minWidth: 60},
            {field: 'maxValue', sort: true, title: '涨',minWidth: 60},
            {field: 'minValue', sort: true, title: '跌',minWidth: 60},
            {field: 'p6LossRatio', sort: true, title: '涨跌浮动',minWidth: 60,templet:function (data) {
                    return data.p6LossRatio+"%";
                }},
            {field: 'lossRatio', sort: true, title: '自由浮动',minWidth: 60,templet:function (data) {
                    return data.lossRatio+"%";
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
            queryData['spotId'] = Obj.condition.spotId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看现货交易对详情
     */
    Obj.openSpotDetail = function (data) {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '交易对详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/symbolsSetting/symbols_setting_edit/' + data.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除现货交易对
         *
         */
        Obj.onDeleteSpot = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/symbolsSetting/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("id", data.id);
                ajax.start();
            };
            Feng.confirm("是否删除 ?", operation);
        };

        /**
         * 弹出添加现货交易对
         */
        Obj.openAddSpot = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加平台币机器人',
                area:["800px","420px"],
                content: Feng.ctxPath + '/symbolsSetting/symbols_setting_add',
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
         * 点击编辑现货交易对
         *
         */
         Obj.onEditSpot = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改',
                area:["800px","420px"],
                content: Feng.ctxPath + '/symbolsSetting/symbols_setting_edit?id=' + data.id,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/symbolsSetting/list',
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
            Obj.openAddSpot();
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
                Obj.onEditSpot(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteSpot(data);
            }
        });
});
