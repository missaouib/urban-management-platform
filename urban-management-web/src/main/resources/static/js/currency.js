/*
* 附件回显 超出隐藏  鼠标移入 显示title
* */
$(".banners").on("mouseover",".roleSize",function () {
    var problemTitle = $(this).text()
    $(".roleSize").attr("title",problemTitle);
})