
layui.use(['layer', 'form', 'admin', 'ax','upload'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var upload=layui.upload;

    // 让当前iframe弹层高度适应
    // admin.iframeAuto();

    layui.use('layedit', function () {
        var layedit = layui.layedit;
        layedit.set({
            uploadImage: {
                url: '/upload/uploadFile', //接口url
                // url: '/mgr/upload', //接口url
                type: 'post' //默认post
            }
        });
        var content_index = layedit.build('content', {
            height: 500,
            tool: [
                'strong' //加粗
                ,'italic' //斜体
                ,'underline' //下划线
                ,'del' //删除线
                ,'|' //分割线
                ,'left' //左对齐
                ,'center' //居中对齐
                ,'right' //右对齐
                ,'link' //超链接
                ,'unlink' //清除链接
                ,'face' //表情
                ,'image' //插入图片

            ]
        }); //建立编辑器
        //验证富文本内容
        form.verify({
            content_desc: function () {
                layedit.sync(content_index);
            }
        })
    });
    //普通图片上传
    var uploadInst = upload.render({
        elem: '#test1'
        , url: '/upload/uploadFile'
        , before: function (obj) {
            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                $('#demo1').attr('src', result); //图片链接（base64）
            });
        }
        , done: function (res) {
            //如果上传失败
            if (res.code > 0) {
                return layer.msg('上传失败');
            }
            //上传成功
            $('#demo1').attr("url_link",res.data.src);
            console.log('upload:'+JSON.stringify(res));
        }
        , error: function () {
            //演示失败状态，并实现重传
            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function () {
                uploadInst.upload();
            });
        }
    });


    //获取参数信息
    var ajax = new $ax(Feng.ctxPath + "/business/detail/" + Feng.getUrlParam("businessId"));
    var result = ajax.start();
    $('#demo1').attr('src', result.mainImg); //图片链接（base64）
    form.val('businessForm', result);

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/business/edit", function (data) {
            Feng.success("修改成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        var a=data.field
        a.mainImg=$("#demo1").attr("url_link");
        ajax.set(a);
        ajax.start();
    });
});
