layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    debugger;
    //获取参数信息
    var ajax = new $ax(Feng.ctxPath + "/task/record/detail/" + Feng.getUrlParam("id"));
    var result = ajax.start();
    form.val('recordsForm', result);
});