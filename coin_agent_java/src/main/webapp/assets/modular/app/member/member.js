layui.use(['table', 'admin', 'ax', 'ztree','form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 用户信息管理初始化
 */
var Member = {
    tableId: "memberTable",	//表格id
    condition: {
        memberId: ""
    }
};

    Member.onClickDept = function (e, treeId, treeNode) {
        Member.condition.refereeId = treeNode.id;
        Member.search();
    };

    //初始化左侧关系链树
    var ztree = new $ZTree("leftTree", "/member/tree");
    ztree.bindOnClick(Member.onClickDept);
    ztree.init();

/**
 * 初始化表格的列
 */
Member.initColumn = function () {
    return [[
        {type: 'checkbox'},
         {field: 'memberId', hide: true, sort: true, title: 'id'},
         {field: 'account',width:125, title: '用户账号(首次注册)',width:153},
         {field: 'phone',width:125, title: '手机号码'},
         {field: 'email',width:125, title: '邮箱'},
         // {field: 'uid',  title: 'uid'},
         {field: 'type', hide: true, sort: true, title: '用户类型'},
         // {field: 'typeValue', title: '用户类型'},
         {field: 'inviteCode',width:120,  title: '邀请码'},
         // {field: 'refereeId',hide: true, sort: true, title: '推荐人'},
         {field: 'refereeIdValue',width:125,title: '推荐人'},
        {field: 'eth',hide: true,width:125,title: 'ETH'},
        {field: 'md',hide: true,width:125,title: 'MGE'},
        {field: 'usdt',hide: true,width:125,title: 'USDT'},
        // {field: 'rt',width:125,title: 'rt'},

         // {field: 'small',sort: true, title: '小BOSS'},
         // {field: 'big',width:125, sort: true, title: '大BOSS'},
         {field: 'nickName',width:170, sort: true, title: '法币昵称'},
        {field: 'otc',width:170, sort: true, title: '是否承兑商',templet: function (data) {
                if (data.otc ==1){
                    return '是';
                }
                return '否';
            }},
        {field: 'statusValue', title: '用户状态'},
        {field: 'registerTime',width:180, sort: true, title: '注册时间'},
        // {field: 'remark',width:180, sort: true, title: '测试账号标记'},
        // {field: 'unlockc',width:180, sort: true, title: '锁仓赎回币种'},
        // {field: 'status',fixed: 'right', templet: '#statusTpl', title: '用户状态'},
        {align: 'center',fixed: 'right', toolbar: '#tableBar', title: '操作', minWidth: 300}
    ]];
};
// 渲染表格
    var tableResult = table.render({
        elem: '#' + Member.tableId,
        url: Feng.ctxPath + '/member/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: Member.initColumn(),
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




    /**
     * 点击查询按钮
     */
    Member.search = function () {
        var queryData = {};
        queryData['refereeId'] = Member.condition.refereeId;
        queryData['condition'] = $("#condition").val();
        queryData['memberId'] = Member.condition.memberId;
        table.reload(Member.tableId, {where: queryData});
    };



/**
 * 打开查看用户信息详情
 */
Member.openMemberDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户信息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/member/member_edit/' + Member.seItem.id
        });
        this.layerIndex = index;
    }
};





    /**
     * 导出excel按钮
     */
    Member.exportExcel = function () {
        var checkRows = table.checkStatus(Member.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击编辑用户信息
     *
     */
    Member.onEditMember = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改用户信息',
            area:["800px","420px"],
            content: Feng.ctxPath + '/member/member_edit?memberId=' + data.memberId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Member.tableId);
            }
        });
    };
    /**
     * 重置资金密码
     *
     * @param data 点击按钮时候的行数据
     */
    Member.resetPayPassword = function (data) {
        Feng.confirm("是否重置资金密码为123456 ?", function () {
            var ajax = new $ax(Feng.ctxPath + "/member/updatePay/"+data.memberId, function (data) {
                Feng.success("重置资金密码成功!");
            }, function (data) {
                Feng.error("重置资金密码失败!");
            });
            //ajax.set("memberId", data.memberId);
            ajax.start();
        });
    };
    /**
     * 点击编辑用户信息
     *
     */
    Member.queryMyTeam = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            maxmin: true,
            title: '用户团队信息',
            area:["1200px","990px"],
            content: Feng.ctxPath + '/member/member_team_info?memberId=' + data.memberId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Member.tableId);
            }
        });
    };



 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Member.search();
    });



    // 导出excel
    $('#btnExp').click(function () {
        Member.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Member.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'queryMyTeam') {
            Member.queryMyTeam(data);
        }
    });
});
