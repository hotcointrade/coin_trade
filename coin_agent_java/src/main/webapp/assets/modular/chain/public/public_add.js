
layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    // admin.iframeAuto();

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/public/add", function (data) {
            Feng.success("提币成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            var info= data.responseText;
            console.log(info)
            if(info!=null)
            {
                var msg=info
                data.message=msg;
            }
            Feng.error("提币失败！" + data.message)
        });
        ajax.set(data.field);
        ajax.start();
    });
});
