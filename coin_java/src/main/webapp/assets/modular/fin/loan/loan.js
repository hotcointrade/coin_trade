layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 申请管理初始化
     */
    var Obj = {
        tableId: "loanTable",	//表格id
        condition: {
            loanId: ""
            ,timeLimit:""
        }
    };
    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
     });
    /**
     * 初始化表格的列
     */
    var statusMap={'N':'未审核','NoPass':'审核不通过','PASS':'审核通过'}
    Obj.initColumn = function () {
        return [[
            // {type: 'checkbox'},
             {field: 'loanId', hide: true, sort: true, title: 'id'},
            {field: 'memberIdValue', sort: true, title: '用户账户'},
             {field: 'phone', sort: true, title: '联系电话'},
             {field: 'coin', sort: true, title: '币种'},
             {field: 'address', sort: true, title: '收款钱包地址'},
             {field: 'idCard', sort: true, title: '身份证'},
             {field: 'idCard2', sort: true, title: '护照'},
             {field: 'price', sort: true, title: '金额'},
            {field: 'status', sort: true, title: '状态',templet:function (data) {
                    return statusMap[data.status];
                }},
            {field: 'createTime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['condition'] = $("#condition").val();
            queryData['loanId'] = Obj.condition.loanId;
            queryData['timeLimit'] = $("#timeLimit").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看申请详情
     */
    Obj.openLoanDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '申请详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/loan/loan_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };
    /**
     * 打开查看申请详情
     */
    Obj.onPass = function (data) {

            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '申请详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/loan/loan_pass?loanId=' + data.loanId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
            this.layerIndex = index;

    };


        /**
         * 删除申请
         *
         */
        Obj.onDeleteLoan = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/loan/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("loanId", data.loanId);
                ajax.start();
            };
            Feng.confirm("是否删除申请 ?", operation);
        };

        /**
         * 弹出添加申请
         */
        Obj.openAddLoan = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加申请',
                area:["800px","420px"],
                content: Feng.ctxPath + '/loan/loan_add',
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };

        /**
         * 导出excel按钮
         */
        Obj.exportExcel = function () {
            var checkRows = table.checkStatus(Obj.tableId);
            if (checkRows.data.length === 0) {
                Feng.error("请选择要导出的数据");
            } else {
                table.exportFile(tableResult.config.id, checkRows.data, 'xls');
            }
        };
       /**
         * 点击编辑申请
         *
         */
         Obj.onEditLoan = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改申请',
                area:["800px","420px"],
                content: Feng.ctxPath + '/loan/loan_edit?loanId=' + data.loanId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/loan/list',
            page: true,
            height: "full-158",
            cellMinWidth: 100,
            //limit:100,
            //limits: [100,200,300,400,500],
            cols: Obj.initColumn()
        });
     // 搜索按钮点击事件
        $('#btnSearch').click(function () {
            Obj.search();
        });

        // 添加按钮点击事件
        $('#btnAdd').click(function () {
            Obj.openAddLoan();
        });

        // 导出excel
        $('#btnExp').click(function () {
            Obj.exportExcel();
        });

        // 工具条点击事件
        table.on('tool(' + Obj.tableId + ')', function (obj) {
            var data = obj.data;
            var layEvent = obj.event;
            if (layEvent === 'edit') {
                Obj.onEditLoan(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteLoan(data);
            }
            else if (layEvent === 'pass') {
                Obj.onPass(data);
            }
        });
});
