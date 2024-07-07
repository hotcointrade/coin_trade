
layui.use(['table', 'layer', 'form', 'admin', 'ax', 'element'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var table = layui.table;
    var admin = layui.admin;
    // var layer = layui.layer;
    var element=layui.element;

    // 让当前iframe弹层高度适应
    //admin.iframeAuto();
    //一些事件触发
    element.on('tab(tab-dome)', function(data){
        var layId = $(this).attr("lay-id");
        console.log(layId)
        if(layId == "11"){
            table.reload(Member1.tableId);
        }
        if(layId == "22"){
            table.reload(Member2.tableId);
        }if(layId == "33"){
            table.reload(Member3.tableId);
        }
        if(layId == "44"){
            table.reload(Member4.tableId);
        }
        // console.log(this); //当前Tab标题所在的原始DOM元素
        // console.log(data.index); //得到当前Tab的所在下标
        // console.log(data.elem); //得到当前的Tab大容器
    });
    /**
     * 初始化表格的列
     */
    var colum = [[
        // {type: 'checkbox'},
        {field: 'memberId', hide: true, sort: true, title: 'id'},
        {field: 'account',width:180, title: '用户账号(首次注册)'},
        {field: 'todayRecharge',width:200, title: '今日充值'},
        {field: 'todayWithdraw',width:200, title: '今日提现'},
        {field: 'allRecharge',width:350,  title: '总充值'},
        {field: 'allWithdraw',width:350,title: '总提现'},
        {field: 'refereeIdValue',width:125, title: '上级代理'},
        {field: 'userNoPrice',width:320,title: '冻结资产'},
        {field: 'teamNumbers',width:125, sort: true, title: '邀请人数'},
        {field: 'statusValue',width:125, title: '用户状态'},
        {field: 'registerTime',width:180, sort: true, title: '注册时间'},
    ]]
    /**
     * 用户信息管理初始化
     */
    var Member1 = {
        tableId: "memberTable1",	//表格id
        condition: {
            memberId: ""
        }
    };
    Member1.initColumn = function () {
        return colum;
    };
    /**
     * 用户信息管理初始化
     */
    var Member2 = {
        tableId: "memberTable2",	//表格id
        condition: {
            memberId: ""
        }
    };
    Member2.initColumn = function () {
        return colum;
    };
    /**
     * 用户信息管理初始化
     */
    var Member3 = {
        tableId: "memberTable3",	//表格id
        condition: {
            memberId: ""
        }
    };
    Member3.initColumn = function () {
        return colum;
    };
    /**
     * 用户信息管理初始化
     */
    var Member4 = {
        tableId: "memberTable4",	//表格id
        condition: {
            memberId: ""
        }
    };
    Member4.initColumn = function () {
        return colum;
    };

    var tableResult1 = table.render({
        elem: '#' + Member1.tableId,
        url: Feng.ctxPath + '/member/list2?level=1&refereeId='+Feng.getUrlParam("memberId"),
        page: true,
        height: "full-258",
        cellMinWidth: 100,
        limit:2,
        limits: [3,4,5,6,7],
        cols: Member1.initColumn(),
        done: function (res, curr, count) {
            // $("[data-field='small']").children().each(function () {
            //     if ($(this).text() == '0') {
            //         $(this).text("否")
            //     } else if ($(this).text() == '1') {
            //         $(this).text("是")
            //     }
            // });
            // $("[data-field='big']").children().each(function () {
            //     if ($(this).text() == '0') {
            //         $(this).text("否")
            //     } else if ($(this).text() == '1') {
            //         $(this).text("是")
            //     }
            // });

        }
    });
    var tableResult2 = table.render({
        elem: '#' + Member2.tableId,
        url: Feng.ctxPath + '/member/list2?level=2&refereeId='+Feng.getUrlParam("memberId"),
        page: true,
        height: "full-258",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Member2.initColumn(),
        done: function (res, curr, count) {
            $("[data-field='small']").children().each(function () {
                if ($(this).text() == '0') {
                    $(this).text("否")
                } else if ($(this).text() == '1') {
                    $(this).text("是")
                }
            });
            $("[data-field='big']").children().each(function () {
                if ($(this).text() == '0') {
                    $(this).text("否")
                } else if ($(this).text() == '1') {
                    $(this).text("是")
                }
            });

        }
    });
    var tableResult3 = table.render({
        elem: '#' + Member3.tableId,
        url: Feng.ctxPath + '/member/list2?level=3&refereeId='+Feng.getUrlParam("memberId"),
        page: true,
        height: "full-258",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Member3.initColumn(),
        done: function (res, curr, count) {
            $("[data-field='small']").children().each(function () {
                if ($(this).text() == '0') {
                    $(this).text("否")
                } else if ($(this).text() == '1') {
                    $(this).text("是")
                }
            });
            $("[data-field='big']").children().each(function () {
                if ($(this).text() == '0') {
                    $(this).text("否")
                } else if ($(this).text() == '1') {
                    $(this).text("是")
                }
            });

        }
    });
    var tableResult4 = table.render({
        elem: '#' + Member4.tableId,
        url: Feng.ctxPath + '/member/list2?level=4&refereeId='+Feng.getUrlParam("memberId"),
        page: true,
        height: "full-258",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Member4.initColumn(),
        done: function (res, curr, count) {
            $("[data-field='small']").children().each(function () {
                if ($(this).text() == '0') {
                    $(this).text("否")
                } else if ($(this).text() == '1') {
                    $(this).text("是")
                }
            });
            $("[data-field='big']").children().each(function () {
                if ($(this).text() == '0') {
                    $(this).text("否")
                } else if ($(this).text() == '1') {
                    $(this).text("是")
                }
            });

        }
    });
    //获取参数信息
    var ajax = new $ax(Feng.ctxPath + "/member/memberTeamInfo/" + Feng.getUrlParam("memberId"));
    var result = ajax.start();
    $("#account").text(result.account)
    $("#teamCount").text(result.teamCount)
    $("#buyMatch").text(result.buyMatch)
    $("#sellMatch").text(result.sellMatch)
    $("#buyCompact").text(result.buyCompact)

    $("#sellCompact").text(result.sellCompact)
    $("#otcBuy").text(result.otcBuy)
    $("#otcSell").text(result.otcSell)
    $("#optionZ").text(result.optionZ)
    $("#optionD").text(result.optionD)

    $("#rechargeTotal").text(result.rechargeTotal)
    $("#withdrawalTotal").text(result.withdrawalTotal)
    console.log(result)
    // form.val('memberForm', result);
    // $.ajax({
    //     type: "POST",
    //     url: "/member/getTypeList",
    //     datatype: "json",
    //     success: function (data) {
    //         $.each(data, function (index, item) {
    //             // $('#guessCode').append(new Option(item.value, item.code,true));// 下拉菜单里添加元素
    //             if(item.code===result.type)
    //             {
    //                 $('#OpSelect').append(new Option(item.value, item.code,true,true));
    //             }
    //             else{
    //                 $('#OpSelect').append(new Option(item.value, item.code));
    //             }
    //         });
    //
    //         layui.form.render('select');
    //     }, error: function () {
    //         Feng.error("查询列表失败");
    //     }
    // });



    // 表单提交事件
    // form.on('submit(btnSubmit)', function (data) {
    //     var ajax = new $ax(Feng.ctxPath + "/member/edit", function (data) {
    //         Feng.success("修改成功！");
    //         //传给上个页面，刷新table用
    //         admin.putTempData('formOk', true);
    //         //关掉对话框
    //         admin.closeThisDialog();
    //     }, function (data) {
    //         Feng.error("添加失败！" + data.responseJSON.message)
    //     });
    //     ajax.set(data.field);
    //     ajax.start();
    // });
});
