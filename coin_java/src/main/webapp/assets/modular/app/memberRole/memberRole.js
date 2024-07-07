layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

/**
 * 用户角色关系管理初始化
 */
var MemberRole = {
    tableId: "memberRoleTable",	//表格id
    condition: {
        memberRoleId: ""
    }
};

/**
 * 初始化表格的列
 */
MemberRole.initColumn = function () {
    return [[
        // {type: 'checkbox'},
         {field: 'memberRoleId', hide: true, sort: true, title: 'id'},
         {field: 'memberId', sort: true, title: '用户id'},
         {field: 'roleId', sort: true, title: '角色id'},
         {field: 'createTime', sort: true, title: '创建时间'},
        {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
    ]];
};


    /**
     * 点击查询按钮
     */
    MemberRole.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['memberRoleId'] = MemberRole.condition.memberRoleId;
        table.reload(MemberRole.tableId, {where: queryData});
    };



/**
 * 打开查看用户角色关系详情
 */
MemberRole.openMemberRoleDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户角色关系详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberRole/memberRole_edit/' + MemberRole.seItem.id
        });
        this.layerIndex = index;
    }
};


    /**
     * 删除用户角色关系
     *
     */
    MemberRole.onDeleteMemberRole = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/memberRole/delete", function () {
                Feng.success("删除成功!");
                table.reload(MemberRole.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("memberRoleId", data.memberRoleId);
            ajax.start();
        };
        Feng.confirm("是否删除用户角色关系 ?", operation);
    };

    /**
     * 弹出添加用户角色关系
     */
    MemberRole.openAddMemberRole = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加用户角色关系',
            area:["800px","420px"],
            content: Feng.ctxPath + '/memberRole/memberRole_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(MemberRole.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    MemberRole.exportExcel = function () {
        var checkRows = table.checkStatus(MemberRole.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.memberRole.id, checkRows.data, 'xls');
        }
    };
   /**
     * 点击编辑用户角色关系
     *
     */
     MemberRole.onEditMemberRole = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改用户角色关系',
            area:["800px","420px"],
            content: Feng.ctxPath + '/memberRole/memberRole_edit?memberRoleId=' + data.memberRoleId,
            end: function () {
                admin.getTempData('formOk') && table.reload(MemberRole.tableId);
            }
        });
    };
 // 渲染表格
    var tableResult = table.render({
        elem: '#' + MemberRole.tableId,
        url: Feng.ctxPath + '/memberRole/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        limit:100,
        limits: [100,200,300,400,500],
        cols: MemberRole.initColumn()
    });
 // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        MemberRole.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        MemberRole.openAddMemberRole();
    });

    // 导出excel
    $('#btnExp').click(function () {
        MemberRole.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + MemberRole.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            MemberRole.onEditMemberRole(data);
        } else if (layEvent === 'delete') {
            MemberRole.onDeleteMemberRole(data);
        }
    });
});