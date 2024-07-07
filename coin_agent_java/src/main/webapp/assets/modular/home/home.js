layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax'], function () {
    var $ = layui.$;
    var layer = layui.Node;
    var form = layui.form;
    var table = layui.table;
    var $ZTree = layui.ztree;
    var $ax = layui.ax;
    var laydate = layui.laydate;
    var admin = layui.admin;


    var dayList = [];
    var withdrawalList = [];
    var rechargeList = [];


    $.ajax({
            type:"post",
            url: "/member/getSummary",
            async:true,
            dataType:'json',
            success:function(data){
                $("#totalPower").text(data.totalPower);
                $("#totalBtc").text(data.totalBtc);
                $("#totalWithdraw").text(data.totalWithdraw);
                $("#todayPower").text(data.todayPower);
                $("#todayBtc").text(data.todayBtc);
                $("#todayWithdraw").text(data.todayWithdraw);
            }
        });






});