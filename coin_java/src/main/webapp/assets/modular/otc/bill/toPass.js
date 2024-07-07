
layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();
    var ajax = new $ax(Feng.ctxPath + "/bill/detail/" + Feng.getUrlParam("billId"));
    var result = ajax.start();
    form.val('billForm', result);
    $('#uploadImg').attr('src', result.uploadImg); //图片链接（base64）
    //$("uploadImg").src(result.uploadImg)

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        if(result.status==='WAIT_COIN'){
            var ajax = new $ax(Feng.ctxPath + "/bill/pass2?billId="+data.field.billId, function (data) {
                Feng.success("操作成功！");
                //传给上个页面，刷新table用
                admin.putTempData('formOk', true);
                //关掉对话框
                admin.closeThisDialog();
            }, function (data) {
                Feng.error("添加失败！" + data.message)
            });
            ajax.set(data.field);
            ajax.start();
        }else{
            Feng.error("该订单未处于卖家待放币状态")
        }
    });
});
