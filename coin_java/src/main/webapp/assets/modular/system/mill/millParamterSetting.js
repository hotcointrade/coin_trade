layui.use(['layer', 'form', 'table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    
    /**
     * 系统管理--消息管理
     */
    var MillParamterSetting = {
        tableId: "mpsTable"    //表格id
    };


    /**
     * 弹出添加参数设置
     */
    MillParamterSetting.openAddMillParamterSetting = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '参数设置添加',
            content: Feng.ctxPath + '/millPSMgr/mps_add',
            end: function () {
                //admin.getTempData('formOk') && table.reload(MillParamterSetting.tableId);
            }
        });
    };


    // 渲染表格
//    var tableResult = table.render({
//        elem: '#' + MillParamterSetting.tableId,
//        url: Feng.ctxPath + '/millPSMgr/list',
//        page: true,
//        height: "full-158",
//        cellMinWidth: 100,
//        cols: MillParamterSetting.initColumn()
//    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
    	MillParamterSetting.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
    	MillParamterSetting.openAddMillParamterSetting();
    });

});
