
/**
 * 简介文章详情对话框
 */
layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;

    //使用百度富文本
    var ue = UE.getEditor('content');
    setTimeout(function () {
        ue.setContent(result.content);
    },500);
    //注释原来的layui自带富文本
    // 让当前iframe弹层高度适应
    // admin.iframeAuto();
    // layui.use('layedit', function () {
    //     var layedit = layui.layedit;
    //     layedit.set({
    //         uploadImage: {
    //             url: '/upload/uploadFile', //接口url
    //             // url: '/mgr/upload', //接口url
    //             type: 'post' //默认post
    //         }
    //     });
    //     var content_index = layedit.build('content', {
    //         height: 500,
    //         tool: [
    //             'strong' //加粗
    //             ,'italic' //斜体
    //             ,'underline' //下划线
    //             ,'del' //删除线
    //             ,'|' //分割线
    //             ,'left' //左对齐
    //             ,'center' //居中对齐
    //             ,'right' //右对齐
    //             ,'link' //超链接
    //             ,'unlink' //清除链接
    //             ,'face' //表情
    //             ,'image' //插入图片
    //
    //         ]
    //     }); //建立编辑器
    //     //验证富文本内容
    //     form.verify({
    //         content_desc: function () {
    //             layedit.sync(content_index);
    //         }
    //     })
    // });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/article/add", function (data) {
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
