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
        tableId: "contractGoldCoinTable",	//表格id
        condition: {
            id: ""
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
            {field: 'id', hide: true, sort: true, title: 'id'},
            {field: 'symbol', sort: true, title: '交易对',minWidth: 90},
            {field: 'maxOptionNo', sort: true, title: '当前期数',minWidth: 120,templet:function (data) {
                    return "第"+data.maxOptionNo+"期";
                }},
            {field: 'enable', sort: true, title: '启用',minWidth: 50,templet:function (data) {
                    return data.enable==1?"启用":"禁用";
                }},
            {field: 'visible', sort: true, title: '显示',minWidth: 50,templet:function (data) {
                    return data.visible==1?"显示":"不显示";
                }},
            {field: 'enableBuy', sort: true, title: '看涨',minWidth: 50,templet:function (data) {
                    return data.enableBuy==1?"允许":"不允许";
                }},
            {field: 'enableSell',  title: '看跌',minWidth: 50,templet:function (data) {
                    return data.enableSell==1?"允许":"不允许";
                }},
            {field: 'tiedType',  title: '平局处理',minWidth: 80,templet:function (data) {
                    return data.tiedType==1?"退回资金":"平台通吃";
                }},
            {field: 'amount',  title: '允许投注金额'},
            {field: 'feePercent',  title: '开仓手续费',templet:function (data) {
                    return data.feePercent+"‰";
                }},
            {field: 'winFeePercent',  title: '赢家抽水',templet:function (data) {
                    return data.winFeePercent+"‰";
                }},
            {field: 'initBuyReward',  title: '初始买涨奖池',minWidth: 110},
            {field: 'initSellReward', sort: true, title: '初始买跌奖池',minWidth: 110},
            {field: 'totalProfit',  title: '盈利',minWidth: 100},
            {field: 'openTimeGap', title: '下注时间',minWidth: 150},
            {field: 'closeTimeGap', sort: true, title: '开奖时间'},
            {fixed: 'right',align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    var apiPrefix="contractGoldCoin";//api
    /**
     * 修改状态
     *
     * @param contactId id
     * @param checked 是否选中（true,false），选中就是启用，未选中就是禁用
     */
    //Obj.changeStatus = function (id, checked) {
    //    if (checked) {
    //        var ajax = new $ax(Feng.ctxPath + "/"+apiPrefix+"/status/Y", function (data) {
    //            Feng.success("已启用!");
    //        }, function (data) {
    //            Feng.error("失败!");
    //            table.reload(Obj.tableId);
    //        });
    //        ajax.set("id", id);
    //        ajax.start();
    //    } else {
    //        var ajax = new $ax(Feng.ctxPath + "/"+apiPrefix+"/status/N", function (data) {
    //            Feng.success("已禁用!");
    //        }, function (data) {
    //            Feng.error("失败!" + data.message + "!");
    //            table.reload(Obj.tableId);
    //        });
    //        ajax.set("id", id);
    //        ajax.start();
    //    }
    //};

// 状态监听
    //form.on('switch(status)', function (obj) {
    //    var id = obj.elem.value;
    //    var checked = obj.elem.checked ? true : false;
    //    Obj.changeStatus(id, checked);
    //});
        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['condition'] = $("#condition").val()
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看黄金交易对详情
     */
    Obj.openDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '黄金交易对',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + "/"+apiPrefix+'/contractGoldCoin_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除黄金交易对
         *
         */
        Obj.onDelete = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/"+apiPrefix+"/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("id", data.id);
                ajax.start();
            };
            Feng.confirm("是否删除黄金交易对 ?", operation);
        };

        /**
         * 弹出添加现货交易对
         */
        Obj.openAdd = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加黄金交易对账户',
                area:["800px","420px"],
                content: Feng.ctxPath + "/"+apiPrefix+'/contractGoldCoin_add',
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };

        
       /**
         * 点击编辑黄金交易对
         *
         */
         Obj.onEdit = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改黄金交易对',
                area:["800px","420px"],
                content: Feng.ctxPath + "/"+apiPrefix+'/contractGoldCoin_edit?id=' + data.id,
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
            Obj.openAdd();
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
                Obj.onEdit(data);
            } else if (layEvent === 'delete') {
                Obj.onDelete(data);
            }
        });
});