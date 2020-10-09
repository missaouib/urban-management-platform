$(function() {
    $(".circle").click(function() {
        $(this).addClass("active").siblings(".circle").removeClass("active");
        $(".test").eq($(this).index(".circle")).addClass("testActive").siblings(".test").removeClass("testActive");
        $(".work").eq($(this).index(".circle")).addClass("workActive").siblings(".work").removeClass("workActive");
        $(this).children(".other").addClass("otherActive").parent(".circle").siblings('.circle').children(".other").removeClass("otherActive");

    })
    // 退出点击事件
    $(".quit").click(function () {
        window.location.href = logoutUrl;
    })
})