$(function() {
    $(".circle").click(function() {
        $(this).addClass("active").siblings(".circle").removeClass("active");
        $(".test").eq($(this).index(".circle")).addClass("testActive").siblings(".test").removeClass("testActive");
        $(".work").eq($(this).index(".circle")).addClass("workActive").siblings(".work").removeClass("workActive");

        // $(".circle .other").eq($(this).index(".circle")).addClass("otherActive").siblings(".circle .other").removeClass("otherActive");
        $(this).children(".other").addClass("otherActive").parent(".circle").siblings('.circle').children(".other").removeClass("otherActive");
    })
})