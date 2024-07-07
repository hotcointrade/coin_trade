layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

    /**
     * 系统管理--参数管理
     */
    var Records = {
        tableId: "recordsTable",
        condition: {
            recordsId: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Records.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, sort: true, title: 'id'},
            {field: 'taskNo', sort: true, title: '任务编号'},
            {field: 'timeKeyValue', sort: true, title: '执行时间格式值'},
            {field: 'executeTime', hide: true,sort: true, title: '执行时间'},
            {field: 'executeTimeValue', sort: true, title: '执行时间'},
            {field: 'times', sort: true, title: '执行时长(毫秒)'},
            {field: 'taskStatus', hide: true,sort: true, title: '执行状态'},
            {field: 'statusName', sort: true, title: '执行状态'},
            {field: 'failcount', sort: true, title: '失败数'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Records.search = function () {
        var queryData = {};
        queryData['condition'] = $("#name").val();
        queryData['id'] = Records.condition.id;
        table.reload(Records.tableId, {where: queryData});
    };

    /**
     * 选择参数时
     */
    Records.onClickRecords = function (e, treeId, treeNode) {
        Records.condition.recordsId = treeNode.id;
        Records.search();
    };



    /**
     * 导出excel按钮
     */
    Records.exportExcel = function () {
        var checkRows = table.checkStatus(Records.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.records.id, checkRows.data, 'xls');
        }
    };


    /**
     * 错误详情
     * @param data
     */
    Records.onDetailRecords=function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '错误详情',
            area:["450px","400px"],
            content: Feng.ctxPath + '/task/record/records_detail?id=' + data.id,
            end: function () {
                admin.getTempData('formOk') && table.reload(Records.tableId);
            }
        });
    }
    
    
    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Records.tableId,
        url: Feng.ctxPath + '/task/record/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Records.initColumn()
    });
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Records.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Records.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Records.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'detail') {
            Records.onDetailRecords(data);
        }
    });
});
