layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;
/**
 * 公账提币管理初始化
 */
var Public = {
    tableId: "publicTable",	//表格id
    condition: {
        publicId: ""
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
Public.initColumn = function () {
    return [[
        {type: 'checkbox'},
         {field: 'publicId', hide: true, sort: true, title: 'id'},
         {field: 'orderNo', sort: true, title: '提币单号'},
         {field: 'toAddress', sort: true, title: '提币地址'},
         {field: 'coin', sort: true, title: '提币币种'},
         // {field: 'type', sort: true, title: '提币币种类型'},
         {field: 'price', sort: true, title: '提币数量'},
         {field: 'txHash', sort: true, title: 'hash值'},
        {field: 'remark', sort: true, title: '矿工费'},
         {field: 'createTime', sort: true, title: '创建时间'},
        // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};

    $(".accountBalance").click(function(){
        var type = $(this).val();
        $.ajax({
            type:"post",
            url: "/public/accountBalance",
            async:true,
            data:{
                type:type
            },
            dataType:'json',
            success:function(data){
                if(data.code!='202'){
                    console.log(data);
                    layer.alert("余额："+data.data);
                    // layer.msg(data.data);
                }else{
                    layer.msg(data.data);
                }
            }
        })
    })

    $(".findMainAccount").click(function(){
        var type = $(this).val();
        $.ajax({
            type:"post",
            url: "/public/findMainAccount",
            async:true,
            data:{
                type:type
            },
            dataType:'json',
            success:function(data){
                if(data.code!='202'){
                    console.log(data);
                    alert(data.data);
                }else{
                    layer.msg(data.data);
                }
            }
        })
    })



    /**
     * 点击查询按钮
     */
    Public.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['publicId'] = Public.condition.publicId;
        queryData['toAddress'] = $("#toAddress").val();
        queryData['txHash'] = $("#txHash").val();
        queryData['type'] = $("#type").val();
        queryData['status'] = $("#status").val();
        table.reload(Public.tableId, {where: queryData});
        total();
    };

    $(function () {
        total();
    })
    function total(){
        $.ajax({
            url:Feng.ctxPath+'/public/total',
            data:{
                "condition":$("#condition").val()
                , "toAddress":$("#toAddress").val()
                , "txHash":$("#txHash").val()
                , "type":$("#type").val()
                , "status":$("#status").val()
                , "timeLimit":$("#timeLimit").val()
            },
            success:function (data) {
                if(data!=null)
                {
                    // $("#usdt").text(data.usdt);
                    // $("#eth").text(data.eth);
                    // $("#mge").text(data.mge);
                    // $("#mgm").text(data.mgm);
                    $("#totalWithdraw").text(data.totalWithdraw);
                    $("#totalGas").text(data.totalGas);
                    // $("#totalFee").text(data.totalFee);
                    // $("#totalActual").text(data.totalActual);
                }
            }
        })
    }


    /**
 * 打开查看公账提币详情
 */
Public.openPublicDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '公账提币详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/public/public_edit/' + Public.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除公账提币
     *
     */
    Public.onDeletePublic = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/public/delete", function () {
                Feng.success("删除成功!");
                table.reload(Public.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("publicId", data.publicId);
            ajax.start();
        };
        Feng.confirm("是否删除公账提币 ?", operation);
    };

    /**
     * 弹出添加公账提币
     */
    Public.openAddPublic = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加公账提币',
            area:["800px","420px"],
            content: Feng.ctxPath + '/public/public_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Public.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Public.exportExcel = function () {
        var checkRows = table.checkStatus(Public.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑公账提币
     *
     */
     Public.onEditPublic = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改公账提币',
            area:["800px","420px"],
            content: Feng.ctxPath + '/public/public_edit?publicId=' + data.publicId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Public.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Public.tableId,
        url: Feng.ctxPath + '/public/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Public.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Public.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Public.openAddPublic();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Public.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Public.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Public.onEditPublic(data);
        } else if (layEvent === 'delete') {
            Public.onDeletePublic(data);
        }
    });
});