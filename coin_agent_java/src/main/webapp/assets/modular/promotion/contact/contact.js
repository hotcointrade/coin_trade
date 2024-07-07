layui.use(['table', 'form','admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var form = layui.form;
    var $ZTree = layui.ztree;

    /**
     * 推广管理--客服管理
     */
    var Contact = {
        tableId: "contactTable",
        condition: {
            contactId: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Contact.initColumn = function () {
        return [[
            // {type: 'checkbox'},
            {field: 'contactId', hide: true, sort: true, title: 'id'},
            {field: 'contactName', sort: true, title: '外链接地址'},
            // {field: 'phone', sort: true, title: '热线'},
            // {field: 'startTime', sort: true,  title: '上班时间'},
            // {field: 'endTime', sort: true,  title: '下班时间'},
            // {field: 'remark', sort: true, title: '备注'},
            {field: 'createTime', sort: true,title: '创建时间'},
            {field: 'updateTime', sort: true,title: '更新时间'},
            // {field: 'status', sort: true, templet: '#statusTpl', title: '状态'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Contact.search = function () {
        var queryData = {};
        queryData['condition'] = $("#name").val();
        queryData['contactId'] = Contact.condition.contactId;
        table.reload(Contact.tableId, {where: queryData});
    };

    /**
     * 选择部门时
     */
    Contact.onClickContact = function (e, treeId, treeNode) {
        Contact.condition.contactId = treeNode.id;
        Contact.search();
    };

    /**
     * 弹出添加
     */
    Contact.openAddContact = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加联系人',
            content: Feng.ctxPath + '/contact/contact_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Contact.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Contact.exportExcel = function () {
        var checkRows = table.checkStatus(Contact.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.contact.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击编辑
     *
     * @param data 点击按钮时候的行数据
     */
    Contact.onEditContact = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改',
            area: ['600px', '600px'], //宽高
            content: Feng.ctxPath + '/contact/contact_edit?contactId=' + data.contactId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Contact.tableId);
            }
        });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Contact.onDeleteContact = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/contact/delete", function () {
                Feng.success("删除成功!");
                table.reload(Contact.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("contactId", data.contactId);
            ajax.start();
        };
        Feng.confirm("是否删除联系人 " + data.simpleName + "?", operation);
    };


    /**
     * 修改状态
     *
     * @param contactId 客服id
     * @param checked 是否选中（true,false），选中就是启用，未选中就是禁用
     */
    Contact.changeStatus = function (contactId, checked) {
        if (checked) {
            var ajax = new $ax(Feng.ctxPath + "/contact/status/ENABLE", function (data) {
                Feng.success("已启用!");
            }, function (data) {
                Feng.error("失败!");
                table.reload(Contact.tableId);
            });
            ajax.set("contactId", contactId);
            ajax.start();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/contact/status/DISABLE", function (data) {
                Feng.success("已禁用!");
            }, function (data) {
                Feng.error("失败!" + data.responseJSON.message + "!");
                table.reload(Contact.tableId);
            });
            ajax.set("contactId", contactId);
            ajax.start();
        }
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Contact.tableId,
        url: Feng.ctxPath + '/contact/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Contact.initColumn()
    });



    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Contact.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Contact.openAddContact();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Contact.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Contact.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Contact.onEditContact(data);
        } else if (layEvent === 'delete') {
            Contact.onDeleteContact(data);
        }
    });

    // 修改user状态
    form.on('switch(status)', function (obj) {

        var contactId = obj.elem.value;
        var checked = obj.elem.checked ? true : false;

        Contact.changeStatus(contactId, checked);
    });

});
