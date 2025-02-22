/**
 * 角色详情对话框
 */
var configInfoDlg = {
    data: {
        pid: "",
        pName: ""
    }
};

layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    //获取参数信息
    var ajax = new $ax(Feng.ctxPath + "/domain_config/detail/" + Feng.getUrlParam("configId"));
    var result = ajax.start();
    form.val('configForm', result);



    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/domain_config/edit", function (data) {
           if (data.success){
               Feng.success("修改成功！");
               //传给上个页面，刷新table用
               admin.putTempData('formOk', true);
               //关掉对话框
               admin.closeThisDialog();
           }else{
               Feng.error("修改失败！" + data.message);
           }
        }, function (data) {
            Feng.error("修改失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
    });
});