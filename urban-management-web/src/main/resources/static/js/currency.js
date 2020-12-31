/*
* 附件回显 超出隐藏  鼠标移入 显示title
* */
$(".banners").on("mouseover",".roleSize",function () {
    var problemTitle = $(this).text()
    $(".roleSize").attr("title",problemTitle);
})

$(document).keydown(function(event){
    let codeKey = event.keyCode;
    if (codeKey == 17 || codeKey == 91){
        $(".tips_sele").addClass('roleBox')
    }
});

$(document).keyup(function(event){
    let codeKey = event.keyCode;
    if (codeKey == 17 || codeKey == 91) {
        $(".tips_sele").removeClass('roleBox')
    }
});

/**
 * 搜索地图数据 打开地点
 */
//点击搜索 打开搜索情况菜单
$(".map_button").click(function () {
    //输入的查询名字
    console.log($("#sole-input").val())
    $(".place_map").slideDown(200);
})
//点击菜单 收起搜索情况菜单
$(document).on('click','.place_li',function () {
    $(".place_map").slideUp(200);
    setCenterAndZoom(595677.038671875,5242300.472485352,17);
})

/*
* 回车与搜索条件挂钩
* */
$(document).keydown(function (e) {
    if (e.keyCode === 13){
        if ($(".selectPoint").css("display")=="block"){
            $.table.search()
        }
    }

})