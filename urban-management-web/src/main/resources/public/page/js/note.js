
$(function(){
    table();
    nav();
    change();
});
function nav(){
    $(".news_nav li").each(function(index){
        $(this).click(function(){
            $(".news_nav li").removeClass("nav_active");
            $(this).addClass("nav_active");
            $(".news_table>li").eq(index).show().siblings("li").stop().hide();
        })

    })
}
function change(){
    $(".dropdown-menu>li").click(function(){
        $(".change").html($(this).html())
    })
}
function table() {
    $('#table').bootstrapTable({
        method: "get",
        striped: true,
        singleSelect: false,
        url: "json/note.json",
        dataType: "json",
        pagination: true, //分页
        pageSize: 8,
        pageNumber: 1,
        search: false, //显示搜索框
        contentType: "application/x-www-form-urlencoded",
        queryParams: null,
        columns: [
            {
                checkbox: "true",
                field: 'check',
                align: 'center',
                valign: 'middle'
            }
            ,
            {
                title: "编号",
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '标题',
                field: 'title',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '类型',
                field: 'type',
                align: 'center',
                valign: 'middle'
            },

            {
                title: '来源',
                field: 'person',
                align: 'center'
            },
            {
                title: '通知时间',
                field: 'time',
                align: 'center'
            },
            {
                title: '内容',
                field: 'con',
                align: 'center'
            },
            {
                title: '操作',
                field: 'opear',
                align: 'center',
                formatter: function (value, row) {
                    var c = '<a  href="javascript:void(0)" title=""  style="color: #337ab7;font-weight: normal"  onclick="openlayer(\'' + row.id + '\')">编辑</a> ';
                    var d = '<a  href="javascript:void(0)" title=""  style="color: #337ab7;font-weight: normal"  onclick="del(\'' + row.id + '\')">删除</a> ';
                    return c+d;                }
            }
        ]
    });


}
// 弹出框
function openlayer() {
    layer.open({
        type: 2,
        title: '预警信息',
        shade: 0.5,
        skin: 'layui-layer-rim',
        area: ['920px', '580px'],
        shadeClose: true,
        closeBtn: 1,
        content: 'addNotice.html'
    });

}
function senetmes() {
    layer.open({
        type: 2,
        title: '人员选择',
        shade: 0.5,
        skin: 'layui-layer-rim',
        area: ['70%', '50%'],
        shadeClose: true,
        closeBtn: 1,
        content: 'user.html'
    });

}
// 添加新闻
function addNes() {
    openlayer();

}
// 编辑新闻
function editNes() {
    openlayer();

}
function setbox() {
    layer.open({
        type: 2,
        title: '预警信息',
        shade: 0.5,
        skin: 'layui-layer-rim',
        area: ['70%', '50%'],
        shadeClose: true,
        closeBtn: 1,
        content: 'set.html'
    });

}
