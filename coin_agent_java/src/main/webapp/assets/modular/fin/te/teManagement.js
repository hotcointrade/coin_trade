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
        tableId: "teManagementTable",	//表格id
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
            {field: 'img',  title: '图标',templet: "#imgs",minWidth:40 },
							{field: 'title', sort: true, title: '标题',minWidth: 90},
							{field: 'describes', sort: true, title: '描述',minWidth: 90},
							{field: 'issuanceCount', sort: true, title: '发行总量',minWidth: 90},
							// {field: 'issuancePrice', sort: true, title: '发行价格',minWidth: 90},
							{field: 'currency', sort: true, title: '支付币种',minWidth: 90},

							{field: 'minPrice', sort: true, title: '最低认购金额',minWidth: 90},
							{field: 'drawsTotal', sort: true, title: '抽签总数',minWidth: 90},
							{field: 'oddsWinning', sort: true, title: '中签率(%)',minWidth: 90,templet:function (data) {
                                    return data.oddsWinning+'%';
                                }},
							{field: 'startTime', sort: true, title: '开始时间',minWidth: 90},
							{field: 'endTime', sort: true, title: '结束时间',minWidth: 90},
							{field: 'comLink', sort: true, title: '官方链接',minWidth: 90},
							{field: 'whiteBookLink', sort: true, title: '白皮书链接',minWidth: 90},
							{field: 'status', sort: true, title: '状态',minWidth: 90 ,templet:function (data) {
                                    return data.status==1?"进行中":"已结束";
                                }},
            /*{field: 'status', sort: true, title: '状态:1=进行中,2=已结束',minWidth: 90 ,templet: '#statusTpl', },*/
							{field: 'introduce', sort: true, title: '介绍',minWidth: 90},
							{field: 'conditions', sort: true, title: '条件',minWidth: 90},
			            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    var apiPrefix="teManagement";//api
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
     * 打开查看理财认购币详情
     */
    Obj.openDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '理财认购币',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + "/"+apiPrefix+'/teManagement_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除理财认购币
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
            Feng.confirm("是否删除理财认购币 ?", operation);
        };

        /**
         * 弹出添加现货交易对
         */
        Obj.openAdd = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加理财认购币账户',
                area:["800px","420px"],
                content: Feng.ctxPath + "/"+apiPrefix+'/teManagement_add',
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };


       /**
         * 点击编辑理财认购币
         *
         */
         Obj.onEdit = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改理财认购币',
                area:["800px","420px"],
                content: Feng.ctxPath + "/"+apiPrefix+'/teManagement_edit?id=' + data.id,
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
            cellMinWidth: 150,
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