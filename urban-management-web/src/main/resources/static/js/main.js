$(function() {
    $(".circle").click(function() {
        $(this).addClass("active").siblings(".circle").removeClass("active");
        $(".test").eq($(this).index(".circle")).addClass("testActive").siblings(".test").removeClass("testActive");
        $(".work").eq($(this).index(".circle")).addClass("workActive").siblings(".work").removeClass("workActive");
        $(this).children(".other").addClass("otherActive").parent(".circle").siblings('.circle').children(".other").removeClass("otherActive");
        console.log($(this).children(".identification").val())
        if ($(this).children(".identification").val() == 1){
            $(".noFlat").css("display","block")
            $(".test").eq($(this).index(".circle")).html(`暂无此权限`).siblings(".test").removeClass("testActive");
            $(".test").eq($(this).index(".circle")).css({"line-height":"99px"})
        }else {
            $(".noFlat").css("display","none")
        }
    })
    // 退出点击事件
    $(".quit").click(function () {
        window.location.href = logoutUrl;
    })
    window.onload = function () {
        if ($(".identification").val() == 1){
            $(".noFlat").css("display","block")
            $(".test:first").html(`暂无此权限`)
            $(".test").css({"line-height":"99px"})
        }else {
            $(".noFlat").css("display","none")
        }
    }

})