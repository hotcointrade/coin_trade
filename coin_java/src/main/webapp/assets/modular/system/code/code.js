/**
 * 代码生成管理初始化
 */
layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;

    $.ajax({
        type: "POST",
        url: "/code/getTypeList",
        datatype: "json",
        success: function (data) {
            $.each(data, function (index, item) {
                $('#OpSelect').append(new Option(item.value, item.code));// 下拉菜单里添加元素
            });
            layui.form.render('select');
        }, error: function () {
            Feng.error("查询等级失败");
        }
    });

    $.ajax({
        type: "POST",
        url: "/code/showDateBase",
        datatype: "json",
        success: function (data) {
            $.each(data, function (index, item) {
                $('#tableSchema').append(new Option(item.value, item.code));// 下拉菜单里添加元素
            });
            layui.form.render('select');
        }, error: function () {
            Feng.error("查询等级失败");
        }
    });
    form.on('select(tableSchema)', function (data) {
        $.ajax({
            type: "POST",
            url: "/code/getTableList",
            datatype: "json",
            data: {
                tableSchema: $("#tableSchema").val()
            },
            success: function (data) {
                $('#bizTableName').empty();
                $.each(data, function (index, item) {
                    $('#bizTableName').append(new Option(item.value, item.code));// 下拉菜单里添加元素
                });
                layui.form.render('select');
            }, error: function () {
                Feng.error("查询失败");
            }
        });
    })


    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/code/generate", function (data) {
            Feng.success("添加成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            // admin.closeThisDialog();
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
    });


});
