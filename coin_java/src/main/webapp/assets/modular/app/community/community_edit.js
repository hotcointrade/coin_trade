
layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    //admin.iframeAuto();


    //获取参数信息
    var ajax = new $ax(Feng.ctxPath + "/community/detail/" + Feng.getUrlParam("communityId"));
    var result = ajax.start();
    form.val('communityForm', result);
//使用百度富文本
    var ue = UE.getEditor('content',{ zIndex: 100});
    setTimeout(function () {
        ue.setContent(result.content);
    },500);
    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/community/edit", function (data) {
            Feng.success("修改成功！");
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
