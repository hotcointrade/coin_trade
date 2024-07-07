
layui.use(['layer', 'form', 'admin', 'ax','upload'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var upload=layui.upload;
    // 让当前iframe弹层高度适应
    admin.iframeAuto();


    //获取参数信息
    var type = Feng.getUrlParam("type");// 设置类型 1一口价,  2价格涨跌,  3比例涨跌
    var ajax = new $ax(Feng.ctxPath + "/market/detail?swapId=" + Feng.getUrlParam("swapId") + "&type=" + type) ;
    var result = ajax.start();
    form.val('marketForm', result);
    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/market/edit", function (data) {
            Feng.success("修改成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        var a=data.field;
        // data.field.content=htmlEncode(a.content);
        a.img=$("#demo1").attr("url_link");
        ajax.set(a);
        ajax.start();
    });
});
