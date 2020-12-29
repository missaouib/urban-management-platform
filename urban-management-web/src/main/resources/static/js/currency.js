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