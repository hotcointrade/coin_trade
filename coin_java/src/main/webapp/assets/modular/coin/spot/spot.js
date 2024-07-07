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
    /**
     * 初始化表格的列
     */
    Obj.initColumn = function () {
        return [[
            // {type: 'checkbox'},
             {field: 'spotId', hide: true, sort: true, title: 'id'},
            {field: 'img',  title: '图标',templet: "#imgs" ,minWidth:40},
             {field: 'symbol', sort: true, title: '交易对',minWidth: 180},
              {field: 'number', sort: true, title: '保留小数点位',minWidth: 150},
             {field: 'type',hide: true, sort: true, title: '币种类型'},
             {field: 'typeValue',  title: '币种类型'},
             {field: 'minWithdraw',  title: '最小提币量',minWidth: 150},
             {field: 'minWithdrawValue', title: '提币最小区间值',minWidth: 150},
             {field: 'maxWithdrawValue',title: '提币最大区间值',minWidth: 150},
             {field: 'withdrawFee',  title: '提币手续费率',minWidth: 200,templet:function (data) {
                    return data.withdrawFee+"%";
                 }},
             {field: 'spotFee',hide: true, sort: true, title: '币币买入手续费比例',minWidth: 150},
            {field: 'minBuyNumber',  title: '币币最小委托量',minWidth: 150},
             {field: 'spotFeeValue', title: '币币买入手续费比例',minWidth: 150},
            {field: 'usdtSpotFee',  title: '币币卖出usdt手续费比例',minWidth: 180,templet:function (data) {
                    return data.usdtSpotFee+"%";
                }},
             {field: 'createTime', sort: true, title: '创建时间'},
             {field: 'updateTime', sort: true, title: '更新时间'},
            {fixed: 'right',field: 'status', templet: '#statusTpl', title: '状态'},
            {fixed: 'right',align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    var apiPrefix="spot";//api
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
    Obj.openSpotDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '现货交易对详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/spot/spot_edit/' + Obj.seItem.id
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
                var ajax = new $ax(Feng.ctxPath + "/spot/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("spotId", data.spotId);
                ajax.start();
            };
            Feng.confirm("是否删除现货交易对 ?", operation);
        };

        /**
         * 弹出添加现货交易对
         */
        Obj.openAddSpot = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加现货交易对',
                area:["800px","420px"],
                content: Feng.ctxPath + '/spot/spot_add',
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
                title: '修改现货交易对',
                area:["800px","420px"],
                content: Feng.ctxPath + '/spot/spot_edit?spotId=' + data.spotId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/spot/list',
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
