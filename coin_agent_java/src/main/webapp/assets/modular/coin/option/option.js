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
    //0投注中 1开奖中  2开奖结束 3撤销
    var statusList=["投注中","开奖中","开奖结束","撤销"]
    //0待开奖 1涨  2跌  3平 4撤销
    var resultList=["待开奖","涨","跌","平","撤销"]
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
            {field: 'symbol', sort: true, title: '交易对',minWidth: 90},
            {field: 'optionNo', sort: true, title: '期数',minWidth: 150,templet:function (data) {
                    return "第"+data.optionNo+"期";
             }},
            {field: 'totalBuyCount', sort: true, title: '买涨奖池/人数',minWidth: 50},
            {field: 'totalSellCount', sort: true, title: '买跌奖池/人数',minWidth: 50},
            {field: 'openTime', sort: true, title: '开盘时间',templet:function (data) {
                    return layui.util.toDateString(data.openTime/1, 'yyyy-MM-dd HH:mm:ss')
                }},
            {field: 'openPrice',  title: '开盘价格',minWidth: 30},
            {field: 'closeTime',  title: '收盘时间',minWidth: 120,templet:function (data) {
                    return layui.util.toDateString(data.closeTime/1, 'yyyy-MM-dd HH:mm:ss')
                }},
            {field: 'closePrice',  title: '收盘价格'},
            {field: 'totalPl',  title: '盈利',templet:function (data) {
                    return data.totalPl;
                }},
            {field: 'status',  title: '状态',templet:function (data) {
                    return statusList[data.status];
                }},
            {field: 'result',  title: '开奖结果',minWidth: 150,templet:function (data) {
                    return resultList[data.result];
                }}
        ]];
    };

    var apiPrefix="contractOption";//api
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
            queryData['optionNo'] = $("#optionNo").val();

            queryData['symbol'] = $("#symbol").val()
            queryData['totalBuyCount'] = $("#totalBuyCount").val()
            queryData['totalSellCount'] = $("#totalSellCount").val()
            queryData['totalPl'] = $("#totalPl").val()
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
    Obj.cleanLog2 = function () {
        Feng.confirm("是否清空期权数据?", function () {
            var ajax = new $ax(Feng.ctxPath + "/"+apiPrefix+'/clean', function (data) {
                Feng.success("清空期权数据成功!");
                Obj.search();
            }, function (data) {
                Feng.error("清空期权数据失败!");
            });
            ajax.start();
        });
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

    // 搜索按钮点击事件
    $('#btnClean2').click(function () {
        Obj.cleanLog2();
    });

        // 添加按钮点击事件
        $('#btnAdd').click(function () {
            Obj.openAddSpot();
        });

        // 导出excel
        $('#btnExp').click(function () {
            Obj.exportExcel();
        });
    function getTime(second,getDateType) {
        var date =new Date(second);
        if(getDateType==0){
            return date.getFullYear();
        }else if(getDateType==1){
            if((date.getMonth()+1)<=9){
                return "0"+(date.getMonth()+1);
            }else {
                return date.getMonth()+1;
            }
        }else if(getDateType==2){
            if(date.getDate()<=9){
                return "0"+date.getDate();
            }else {
                return date.getDate();
            }
        }else if(getDateType==3){
            if(date.getHours()<=9){
                return "0"+date.getHours();
            }else {
                return date.getHours();
            }
        }else if(getDateType==4){
            if(date.getMinutes()<=9){
                return "0"+date.getMinutes();
            }else {
                return date.getMinutes();
            }
        }else if(getDateType==5){
            return date.getSeconds ();
        }else {
            alert("输入时间格式有误!");
            return;
        }
    }

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