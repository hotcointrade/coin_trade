layui.use(['table', 'admin', 'ax', 'ztree', 'laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;
/**
 * 充币记录管理初始化
 */
var Recharge = {
    tableId: "rechargeTable",	//表格id
    condition: {
        rechargeId: ""
        ,txHash: ""
        ,type: ""
        ,timeLimit: ""

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
Recharge.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'rechargeId', hide: true, sort: true, title: 'id'},
         {field: 'orderNo',title: '单号', minWidth: 180},
         {field: 'memberIdValue',title: '用户'},
         {field: 'price', sort: true, title: '金额'},
         {field: 'actualPrice', sort: true, title: '实际金额'},
         {field: 'txHash', sort: true, title: '区块链交易ID',minWidth: 200},
        {field: 'remark',title: '充值凭证(点击放大)',minWidth: 200,templet:'#showScreenhost'},
         {field: 'type', sort: true, title: '币种', minWidth: 130},
         {field: 'statusValue', sort: true, title: '状态'},
         {field: 'createTime', sort: true, title: '创建时间', minWidth: 180},
        // {fixed: 'right',align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    Recharge.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['rechargeId'] = Recharge.condition.rechargeId;
        queryData['txHash'] = $("#txHash").val();
        queryData['type'] = $("#type").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(Recharge.tableId, {where: queryData});
        total();
    };

    $(function () {
        total();
    })
    function total(){
        $.ajax({
            url:Feng.ctxPath+'/recharge/total',
            data:{
                "condition":$("#condition").val()
                , "txHash":$("#txHash").val()
                , "type":$("#type").val()
                , "timeLimit":$("#timeLimit").val()
            },
            success:function (data) {
                if(data!=null)
                {
                    $("#totalRecharge").text(data.totalRecharge);
                    $("#totalGas").text(data.totalGas);
                }
            }
        })
    }


    /**
     * 导出excel按钮
     */
    Recharge.exportExcel = function () {
        var checkRows = table.checkStatus(Recharge.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

 // 渲染表格
    var tableResult = table.render({
        elem: '#' + Recharge.tableId,
        url: Feng.ctxPath + '/recharge/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Recharge.initColumn(),
        done: function (res, curr, count) {
            // 该方法用于解决,使用fixed固定列后,行高和其他列不一致的问题
            $(".layui-table-main tr").each(function (index ,val) {
                $($(".layui-table-fixed .layui-table-body tbody tr")[index]).height($(val).height());
            });
        }
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Recharge.search();
    });


    // 导出excel
    $('#btnExp').click(function () {
        Recharge.exportExcel();
    });
    /**
     * 审核 Pass =
     *
     */
    Recharge.onCheckPass = function (data) {
        layer.prompt({
            formType: 2,
            title: '是否通过?',
            area:["800px",'auto'],
            btn: ['确定','取消'], //按钮，
            btnAlign: 'c',
            content: `<div><label class="layui-form-label">实付金额<span style="color: red;">*</span></label>
                <textarea id="content" placeholder="请输入实付金额" class="layui-textarea"></textarea>
                </div>`,
            yes:function (index,value) {
                var content =  $("#content").val();
                if (content){
                    var ajax = new $ax(Feng.ctxPath + "/recharge/pass", function (data) {
                        if (data.success){
                            Feng.success("操作成功!");
                            location.reload();
                        }else{
                            Feng.error(data.message);
                        }
                    }, function (data) {
                        Feng.error("操作失败!" + data.responseJSON.message + "!");
                    });
                    var dataStr = {'id':data.rechargeId,"actualPrice":content}
                    ajax.setData(dataStr);
                    ajax.start();
                    layer.close(index);
                }else{
                    Feng.error(data.message);
                }
            }
        });
    };

    /**
     * 审核 Fail =
     *
     */
    Recharge.onCheckFail = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/recharge/fail", function () {
                Feng.success("成功!");
                table.reload(Recharge.tableId);
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
            });
            ajax.set("id", data.rechargeId);
            ajax.start();
        };
        Feng.confirm("是否拒绝 ?", operation);
    };

    // 工具条点击事件
    table.on('tool(' + Recharge.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
         if (layEvent === 'checkPass') {
            Recharge.onCheckPass(data);
        }else if (layEvent === 'checkFail') {
            Recharge.onCheckFail(data);
        }
    });
});
//显示大图片
function show_img(t) {
    var t = $(t).find("img");
    //页面层
    layer.open({
        type: 1,
        skin: 'layui-layer-rim', //加上边框
        area: ['80%', '80%'], //宽高
        shadeClose: true, //开启遮罩关闭
        end: function (index, layero) {
            return false;
        },
        content: '<div style="text-align:center;margin-top: 30px"><img style="width: 50%" src="' + $(t).attr('src') + '" /></div>'
    });
}
