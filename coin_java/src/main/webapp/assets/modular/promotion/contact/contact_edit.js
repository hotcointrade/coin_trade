layui.use(['layer', 'form', 'admin', 'ax','laydate'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var laydate=layui.laydate;

    // 让当前iframe弹层高度适应
    // admin.iframeAuto();

    //获取信息
    var ajax = new $ax(Feng.ctxPath + "/contact/detail/" + Feng.getUrlParam("contactId"));
    var result = ajax.start();
    form.val('contactForm', result);
 // //限定可选时间
 //    laydate.render({
 //        elem: '#test-limit3'
 //        ,type: 'time'
 //        ,format: 'HH:mm:ss'
 //        ,btns: ['clear', 'confirm']
 //
 //    });
 //    laydate.render({
 //        elem: '#test-limit4'
 //        ,type: 'time'
 //        ,format: 'HH:mm:ss'
 //        ,btns: ['clear', 'confirm']
 //    });


    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/contact/edit", function (data) {
            Feng.success("修改成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("修改失败！" + data.responseJSON.message)
        });
        var a=data.field;
        // var start=a.startTime;
        // var end =a.endTime;
        // if(start!=null&&end!=null&&parseInt(start.substring(0,2))>parseInt(end.substring(0,2)))
        // {
        //     Feng.error("上班时间不能大于或等于下班时间");
        // }
        // else if(start!=null&&end!=null&&parseInt(start.substring(0,2))==parseInt(end.substring(0,2))&&parseInt(start.substring(3,5))>=parseInt(end.substring(3,5)))
        // {
        //     Feng.error("上班时间不能大于或等于下班时间");
        // }
        // else{
        //     ajax.set(data.field);
        //     ajax.start();
        // }
        ajax.set(data.field);
        ajax.start();
    });
});