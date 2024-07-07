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
var teamDetail = {
    tableId: "teamDetailTable",	//表格id
    condition: {
        teamDetailId: ""
    }
};
var teamDetail2 = {
    tableId: "teamDetailTable2",	//表格id
    condition2: {
        teamDetailId: ""
    }
};
/**
 * 初始化表格的列
 */
//第二框
teamDetail.initColumn = function () {
    return [[
        {field: 'account',width:125, title: '用户账号'},

        {field: 'class', sort: true, title: '等级',templet: function (data) {
                return data.class+'级';
            }},
        {field: 'refereeIdValue',width:125,title: '推荐人'},
        {field: 'statusValue', title: '用户状态'},
        {field: 'phone',width:125, title: '手机号码'},
        {field: 'email',width:125, title: '邮箱'},

        {field: 'registerTime',width:160, sort: true, title: '注册时间'},
        {field: 'status',fixed: 'right', width:95, templet: '#statusTpl1', title: '用户状态'},
    ]];
};
//第一框
    teamDetail2.initColumn = function () {
        return [[
            {field: 'account',width:125, title: '用户账号'},
            {field: 'inviteCode',width:120,  title: '邀请码'},
            // {field: 'refereeIdValue',width:125,title: '推荐人'},
            {field: 'registerTime',width:160, sort: true, title: '注册时间'},
            {field: 'phone',width:125, title: '手机号码'},

            {field: 'email',width:125, title: '邮箱'},
            {field: 'status',fixed: 'right',width:95, templet: '#statusTpl2', title: '用户状态'},
        ]];
    };
// 渲染表格
    table.render({
        elem: '#' + teamDetail.tableId,
        url: Feng.ctxPath + '/teamDetail/tree',
        page: true,
        height: "full-158",
        cellMinWidth: 50,
        limit:100,
        limits: [100,200,300,400,500],
        cols: teamDetail.initColumn()
    });
    table.render({
        elem: '#' + teamDetail2.tableId,
        url: Feng.ctxPath + '/teamDetail/list',
        page: true,
        height: "full-158",
        cellMinWidth: 50,
        limit:100,
        limits: [100,200,300,400,500],
        cols: teamDetail2.initColumn(),
    });
    //监听行单击事件
    var seaId = null
    table.on('rowDouble(teamDetailTable2)', function(obj){
        console.log(obj.tr) //得到当前行元素对象
        console.log(obj.data) //得到当前行数据
        seaId = obj.data.memberId
        console.log(seaId)
        $('#account').html('当前顶部账户：'+obj.data.account);
        teamDetail.search()
        //obj.del(); //删除当前行
        //obj.update(fields) //修改当前行数据
    });

    /**
     * 点击查询按钮
     */
    teamDetail.search = function () {
        var queryData = {};
        queryData['refereeId'] = seaId;
        queryData['condition'] = $("#condition").val();
        queryData['teamDetailId'] = teamDetail.condition.teamDetailId;
        table.reload(teamDetail.tableId, {where: queryData});
    };
    teamDetail2.search = function () {
        var queryData = {};
        queryData['refereeId'] = teamDetail2.condition2.refereeId;
        queryData['lever'] = $("#tableSchema").val();
        queryData['condition'] = $("#condition2").val();
        queryData['teamDetailId'] = teamDetail2.condition2.teamDetailId;
        table.reload(teamDetail2.tableId, {where: queryData});
    };
    /**
     * 修改状态
     *
     * @param contactId id
     * @param checked 是否选中（true,false），选中就是启用，未选中就是禁用
     */
    teamDetail.changeStatus = function (id, checked) {
        if (checked) {
            var ajax = new $ax(Feng.ctxPath + "/member/status/Y", function (data) {
                Feng.success("已启用!");
            }, function (data) {
                Feng.error("失败!");
                table.reload(member.tableId);
            });
            ajax.set("memberId", id);
            ajax.start();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/member/status/N", function (data) {
                Feng.success("已禁用!");
            }, function (data) {
                Feng.error("失败!" + data.responseJSON.message + "!");
                table.reload(Contact.tableId);
            });
            ajax.set("memberId", id);
            ajax.start();
        }
    };
    // 修改user状态
    form.on('switch(status)', function (obj) {

        var id = obj.elem.value;
        var checked = obj.elem.checked ? true : false;

        teamDetail.changeStatus(id, checked);
    });
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        teamDetail.search();
    });
    // 搜索按钮点击事件
    $('#btnSearch2').click(function () {
        teamDetail2.search();
    });

});
