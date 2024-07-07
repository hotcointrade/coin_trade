layui.use(['table', 'admin', 'ax', 'ztree','laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 用户押金管理初始化
     */
    var Obj = {
        tableId: "depositTable",	//表格id
        condition: {
            depositId: ""
            ,timeLimit:""
            ,coin:""
            ,status:""
            ,remark:""
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
    Obj.initColumn = function () {
        return [[
            // {type: 'checkbox'},
             {field: 'depositId', hide: true, sort: true, title: 'id'},
             {field: 'memberId', hide: true, sort: true, title: '用户'},
             {field: 'memberIdValue', sort: true, title: '用户'},
             {field: 'nickName', sort: true, title: '昵称'},
             {field: 'coin', sort: true, title: '币种'},
             {field: 'number', sort: true, title: '押金数量'},
             // {field: 'deposit', sort: true, title: '需缴纳押金总量'},
             {field: 'createTime', sort: true, title: '创建时间'},
             {field: 'statusValue', sort: true, title: '状态'},
             {field: 'remarkValue', sort: true, title: '承兑商申请状态'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


        /**
         * 点击查询按钮
         */
        Obj.search = function () {
            var queryData = {};
            queryData['condition'] = $("#condition").val();
            queryData['depositId'] = Obj.condition.depositId;
            queryData['timeLimit'] = $("#timeLimit").val();
            queryData['coin'] = $("#coin").val();
            queryData['status'] = $("#status").val();
            queryData['remark'] = $("#remark").val();
            table.reload(Obj.tableId, {where: queryData});
        };



    /**
     * 打开查看用户押金详情
     */
    Obj.openDepositDetail = function () {
        if (this.check()) {
            var index = layer.open({
                type: 2,
                maxmin:true,
                title: '用户押金详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/deposit/deposit_edit/' + Obj.seItem.id
            });
            this.layerIndex = index;
        }
    };


        /**
         * 删除用户押金
         *
         */
        Obj.onDeleteDeposit = function (data) {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/deposit/delete", function () {
                    Feng.success("删除成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.message + "!");
                });
                ajax.set("depositId", data.depositId);
                ajax.start();
            };
            Feng.confirm("是否删除用户押金 ?", operation);
        };

        /**
         * 弹出添加用户押金
         */
        Obj.openAddDeposit = function () {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '添加用户押金',
                area:["800px","420px"],
                content: Feng.ctxPath + '/deposit/deposit_add',
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
         * 点击编辑用户押金
         *
         */
         Obj.onEditDeposit = function (data) {
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                maxmin:true,
                title: '修改用户押金',
                area:["800px","420px"],
                content: Feng.ctxPath + '/deposit/deposit_edit?depositId=' + data.depositId,
                end: function () {
                    admin.getTempData('formOk') && table.reload(Obj.tableId);
                }
            });
        };
     // 渲染表格
        var tableResult = table.render({
            elem: '#' + Obj.tableId,
            url: Feng.ctxPath + '/deposit/list',
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

        $(function () {
            $.ajax({
                url: Feng.ctxPath + '/deposit/total',
                success:function (data) {
                    // console.table(data);
                    if(data[0]!=null)
                        $("#p0").text(data[0].coin+"总押金:"+data[0].total);
                     if(data[1]!=null)
                        $("#p1").text(data[1].coin+"总押金:"+data[1].total);

                }
            })
        })


    /**
     * 审核 Pass =
     *
     */
    var passApi="/deposit/pass" ;//
    var failApi="/deposit/fail";//
    Obj.onCheckPass = function (data) {
        var num = 0;
        var operation = function () {
            num += 1;
            if (num == 1) {
                var ajax = new $ax(Feng.ctxPath + passApi, function () {
                    Feng.success("成功!");
                    table.reload(Obj.tableId);
                }, function (data) {
                    Feng.error("失败!" + data.message + "!");
                });
                ajax.set("id", data.depositId);//
                ajax.start();
            }
        };
        Feng.confirm("是否通过?", operation);
    };

    /**
     * 审核 Fail =
     *
     */
    Obj.onCheckFail = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath +failApi, function () {
                Feng.success("成功!");
                table.reload(Obj.tableId);
            }, function (data) {
                Feng.error("失败!" + data.message + "!");
            });
            ajax.set("id", data.depositId);//
            ajax.start();
        };
        Feng.confirm("是否拒绝 ?", operation);
    };

        // 添加按钮点击事件
        $('#btnAdd').click(function () {
            Obj.openAddDeposit();
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
                Obj.onEditDeposit(data);
            } else if (layEvent === 'delete') {
                Obj.onDeleteDeposit(data);
            }else if (layEvent === 'checkPass') {
                Obj.onCheckPass(data);
            }
            else if (layEvent === 'checkFail') {
                Obj.onCheckFail(data);
            }
        });
});