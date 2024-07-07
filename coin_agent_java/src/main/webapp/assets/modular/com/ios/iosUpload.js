layui.use(['layer', 'form', 'admin', 'ax','upload'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var upload = layui.upload;

    // 让当前iframe弹层高度适应
    // admin.iframeAuto();

//普通图片上传
    var uploadInst = upload.render({
        //指定允许上传的文件类型
        elem: '#testApk'
        ,url: '/api/upload/apkUpload'
        ,accept: 'file' //普通文件
        ,method: 'post'
        ,done: function(res){
        console.log(res)
    }
    });

    var uploadInst = upload.render({
        //指定允许上传的文件类型
        elem: '#testIpa'
        ,url: '/api/upload/iosUpload'
        ,accept: 'file' //普通文件
        ,method: 'post'
        ,done: function(res){
        console.log(res)
    }
    });



});
