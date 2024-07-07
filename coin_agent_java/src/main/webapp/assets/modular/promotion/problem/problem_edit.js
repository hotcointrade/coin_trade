
layui.use(['layer', 'form', 'admin', 'ax', 'layedit'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var layedit = layui.layedit;
    // 让当前iframe弹层高度适应
    // admin.iframeAuto();

    //获取参数信息
    var ajax = new $ax(Feng.ctxPath + "/problem/detail/" + Feng.getUrlParam("problemId"));
    var result = ajax.start();
    form.val('problemForm', result);
    // 重写文件上传路径
    // UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    // UE.Editor.prototype.getActionUrl = function(action) {
    //     if (action == 'uploadimage' || action == 'uploadfile') {
    //         return '/ueditor/action';
    //     } else {
    //         return this._bkGetActionUrl.call(this, action);
    //     }
    // }
    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/problem/edit", function (data) {
            Feng.success("修改成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("修改失败！" + data.message)
        });
        ajax.set(data.field);
        ajax.start();
    });
    var ue = UE.getEditor('content',{ zIndex: 100});
    setTimeout(function () {
        ue.setContent(result.content);
    },500);
    //var ue = UE.getEditor('container',window.UEDITOR_CONFIG);
// 重写文件上传路径


    // var content_index = layedit.build('content', {
    //     height: 400,
    //     tool: [
    //         'strong' //加粗
    //         , 'italic' //斜体
    //         , 'underline' //下划线
    //         , 'del' //删除线
    //         , '|' //分割线
    //         , 'left' //左对齐
    //         , 'center' //居中对齐
    //         , 'right' //右对齐
    //     ]
    // }); //建立编辑器
    // //验证富文本内容
    // form.verify({
    //     content_desc: function () {
    //         layedit.sync(content_index);
    //     }
    // })





});
