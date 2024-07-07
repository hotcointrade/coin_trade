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
    //0待开始  1胜利  2失败  3平局
    var resultList =["等待","胜局","负局","平局"];
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
            {field: 'id', hide: true, sort: true, title: 'id'},
            {field: 'memberId', hide: true, sort: true, title: '用户ID'},
            {field: 'memberIdValue',  sort: true, title: '用户'},
            {field: 'symbol', sort: true, title: '交易对',minWidth: 60},
            {field: 'optionNo', sort: true, title: '期数',minWidth: 80,templet:function (data) {
                    return "第"+data.optionNo+"期";
             }},
            {field: 'direction', sort: true, title: '方向',minWidth: 50,templet:function (data) {
                    return data.direction=='0'?'涨':'跌';
                }},
            {field: 'result', sort: true, title: '开奖结果',minWidth: 50,templet:function (data) {
                    return resultList[data.result];
                }},
            {field: 'betAmount', sort: true, title: '投注额',minWidth: 50},
            {field: 'fee', sort: true, title: '开仓手续费',minWidth: 50},
            {field: 'winFee',  title: '奖金抽水',minWidth: 50},
            {field: 'rewardAmount',  title: '获奖金额',minWidth: 80},
            {field: 'status',  title: '状态',templet:function(data){
                return data.status=='0'?'参与中':'已开奖'
                }}
        ]];
    };

    var apiPrefix="contractOptionOrder";//api

        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['symbol'] = $("#symbol").val();
            queryData['memberId'] = $("#memberId").val();
            queryData['betAmount'] = $("#betAmount").val();
            queryData['rewardAmount'] = $("#rewardAmount").val();

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
                title: '期权秒合约账户',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + "/"+apiPrefix+'/option_edit/' + Obj.seItem.id
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
                var ajax = new $ax(Feng.ctxPath + "/"+apiPrefix+"/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("spotId", data.spotId);
                ajax.start();
            };
            Feng.confirm("是否删除期权秒合约账户 ?", operation);
        };

        /**
         * 弹出添加现货交易对
         */
        Obj.openAddSpot = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加期权秒合约账户',
                area:["800px","420px"],
                content: Feng.ctxPath + "/"+apiPrefix+'/option_add',
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
                title: '修改期权秒合约账户',
                area:["800px","420px"],
                content: Feng.ctxPath + "/"+apiPrefix+'/option_edit?spotId=' + data.spotId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + "/"+apiPrefix+'/list',
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
