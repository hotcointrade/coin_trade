layui.use(['layer', 'form', 'admin','laydate', 'ax','upload'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var upload=layui.upload;


    // 让当前iframe弹层高度适应
    admin.iframeAuto();
//渲染时间选择框
    laydate.render({
        elem:'#endTime'
        ,type: 'datetime'
    });
    //渲染时间选择框
    laydate.render({
        elem:'#startTime'
        ,type: 'datetime'
    });

    //获取参数信息
    var ajax = new $ax(Feng.ctxPath + "/teManagement/detail/" + Feng.getUrlParam("id"));
    var result = ajax.start();
    form.val('teManagementForm', result);
    $('#demo1').attr('src', result.img); //图片链接（base64）

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
    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/teManagement/edit", function (data) {
            Feng.success("修改成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        var a = data.field;
        a.img=$("#demo1").attr("url_link");
        ajax.set(a);
        ajax.start();
    });
});
