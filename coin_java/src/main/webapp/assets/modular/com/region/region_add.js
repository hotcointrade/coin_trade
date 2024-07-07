
layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    // admin.iframeAuto();

    $.ajax({
        type: "POST",
        url: "/region/getTypeList",
        datatype: "json",
        success: function (data) {
            $.each(data, function (index, item) {
                $('#parentId').append(new Option(item.value, item.code));// 下拉菜单里添加元素
            });
            layui.form.render('select');
        }, error: function () {
            Feng.error("查询等级失败");
        }
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/region/add", function (data) {
            Feng.success("添加成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
    });
});
