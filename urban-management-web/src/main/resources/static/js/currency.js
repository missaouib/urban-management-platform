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
    localGeocoding();
})
//点击菜单 收起搜索情况菜单
// $(document).on('click','.place_li',function () {
//     $(".place_map").slideUp(200);
//     setCenterAndZoom(595677.038671875,5242300.472485352,17);
// })

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

/*
* 清空 查询的选项 搜索条件内 选择部件之后
* */
$(".addBtn").click(function () {
    $(".selectPoint #grid1 option:first").prop("selected",'selected');
    $(".selectPoint #eventSource option:first").prop("selected",'selected');
    $(".selectPoint #eventTerm option:first").prop("selected",'selected');
    $(".selectPoint #eventCondition option:first").prop("selected",'selected');
    $(".selectPoint #timeType option:first").prop("selected",'selected');
    $(".selectPoint .datepicker").val("")
})


//全屏展示  大屏处
$(".fullScreen_index").click(function () {
    $.learuntab.requestFullScreen();
})

/*
获取本周
*/
function getWeek() {
    this.nowTime = new Date();
    this.init = function() {
        this.dayInWeek = this.nowTime.getDay();
        this.dayInWeek == 0 && (this.dayInWeek = 7);
        this.thsiWeekFirstDay = this.nowTime.getTime() - (this.dayInWeek - 1) * 86400000;
        this.thisWeekLastDay = this.nowTime.getTime() + (7 - this.dayInWeek) * 86400000;
        return this;
    };
    this.getWeekType = function(type) {
        type = ~~type;
        var firstDay = this.thsiWeekFirstDay + type * 7 * 86400000;
        var lastDay = this.thisWeekLastDay + type * 7 * 86400000;
        return this.getWeekHtml(firstDay, lastDay);
    }
    this.formateDate = function(time) {
        var newTime = new Date(time)
        var year = newTime.getFullYear();
        var month = newTime.getMonth() + 1;
        var day = newTime.getDate();
        return year + "-" + (month >= 10 ? month : "0" + month) + "-" + (day >= 10 ? day : "0" + day);
    };
    this.getWeekHtml = function(f, l) {
        return this.formateDate(f) + "," + this.formateDate(l);
    };
}

/*
* 本季度
* */
function getQuarterMonth(){
    var now = new Date();
    var nowMonth = now.getMonth();
    var year = now.getFullYear();
    var starQuarte,endQyarte;
    if(nowMonth>= 0 && nowMonth<= 2){
        starQuarte = year + '-01-01 00:00:00';
        endQyarte = year + '-03-31 23:59:59'
    }else if(nowMonth>= 3 && nowMonth<= 5){
        starQuarte = year + '-04-01 00:00:00';
        endQyarte = year + '-06-30 23:59:59'
    }else if(nowMonth>= 6 && nowMonth<= 8){
        starQuarte = year + '-07-01 00:00:00';
        endQyarte = year + '-09-30 23:59:59'
    }else {
        starQuarte = year + '-10-01 00:00:00';
        endQyarte = year + '-12-31 23:59:59'
    }
    $("#begin").val(starQuarte)
    $("#end").val(endQyarte)
}