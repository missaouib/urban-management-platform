(function ($) {


    $.learuntab = {
        requestFullScreen: function () {
            var de = document.documentElement;
            if (de.requestFullscreen) {
                de.requestFullscreen();
            } else if (de.mozRequestFullScreen) {
                de.mozRequestFullScreen();
            } else if (de.webkitRequestFullScreen) {
                de.webkitRequestFullScreen();
            }
        },
        exitFullscreen: function () {
            var de = document;
            if (de.exitFullscreen) {
                de.exitFullscreen();
            } else if (de.mozCancelFullScreen) {
                de.mozCancelFullScreen();
            } else if (de.webkitCancelFullScreen) {
                de.webkitCancelFullScreen();
            }
        },
        refreshTab: function () {
            var currentId = $('.page-tabs-content').find('.active').attr('data-id');
            var target = $('.LRADMS_iframe[data-id="' + currentId + '"]');
            var url = target.attr('src');
            //$.loading(true);
            target.attr('src', url).load(function () {
                //$.loading(false);
            });
        },
        activeTab: function () {
            var currentId = $(this).data('id');
            if (!$(this).hasClass('active')) {
                $('.mainContent .LRADMS_iframe').each(function () {
                    if ($(this).data('id') == currentId) {
                        $(this).show().siblings('.LRADMS_iframe').hide();
                        return false;
                    }
                });
                $(this).addClass('active').siblings('.menuTab').removeClass('active');
                $.learuntab.scrollToTab(this);
            }
        },
        closeOtherTabs: function () {
            $('.page-tabs-content').children("[data-id]").find('.fa-remove').parents('a').not(".active").each(function () {
                $('.LRADMS_iframe[data-id="' + $(this).data('id') + '"]').remove();
                $(this).remove();
            });
            $('.page-tabs-content').css("margin-left", "0");
        },
        closeTab: function () {
            var closeTabId = $(this).parents('.menuTab').data('id');
            var currentWidth = $(this).parents('.menuTab').width();
            if ($(this).parents('.menuTab').hasClass('active')) {
                if ($(this).parents('.menuTab').next('.menuTab').size()) {
                    var activeId = $(this).parents('.menuTab').next('.menuTab:eq(0)').data('id');
                    $(this).parents('.menuTab').next('.menuTab:eq(0)').addClass('active');

                    $('.mainContent .LRADMS_iframe').each(function () {
                        if ($(this).data('id') == activeId) {
                            $(this).show().siblings('.LRADMS_iframe').hide();
                            return false;
                        }
                    });
                    var marginLeftVal = parseInt($('.page-tabs-content').css('margin-left'));
                    if (marginLeftVal < 0) {
                        $('.page-tabs-content').animate({
                            marginLeft: (marginLeftVal + currentWidth) + 'px'
                        }, "fast");
                    }
                    $(this).parents('.menuTab').remove();
                    $('.mainContent .LRADMS_iframe').each(function () {
                        if ($(this).data('id') == closeTabId) {
                            $(this).remove();
                            return false;
                        }
                    });
                }
                if ($(this).parents('.menuTab').prev('.menuTab').size()) {
                    var activeId = $(this).parents('.menuTab').prev('.menuTab:last').data('id');
                    $(this).parents('.menuTab').prev('.menuTab:last').addClass('active');
                    $('.mainContent .LRADMS_iframe').each(function () {
                        if ($(this).data('id') == activeId) {
                            $(this).show().siblings('.LRADMS_iframe').hide();
                            return false;
                        }
                    });
                    $(this).parents('.menuTab').remove();
                    $('.mainContent .LRADMS_iframe').each(function () {
                        if ($(this).data('id') == closeTabId) {
                            $(this).remove();
                            return false;
                        }
                    });
                }
            } else {
                $(this).parents('.menuTab').remove();
                $('.mainContent .LRADMS_iframe').each(function () {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
                $.learuntab.scrollToTab($('.menuTab.active'));
            }
            return false;
        },
        addTab: function () {
            $(".navbar-custom-menu>ul>li.open").removeClass("open");
            var dataId = $(this).attr('data-id');
            if (dataId != "") {
                //top.$.cookie('nfine_currentmoduleid', dataId, { path: "/" });
            }
            var dataUrl = $(this).attr('data-href');

            var menuName = $.trim($(this).text());
            var flag = true;
            if (dataUrl == undefined || $.trim(dataUrl).length == 0) {
                return false;
            }
            $('.menuTab').each(function () {
                if ($(this).data('id') == dataUrl) {
                    if (!$(this).hasClass('active')) {
                        $(this).addClass('active').siblings('.menuTab').removeClass('active');
                        $.learuntab.scrollToTab(this);
                        $('.mainContent .LRADMS_iframe').each(function () {
                            if ($(this).data('id') == dataUrl) {
                                $(this).show().siblings('.LRADMS_iframe').hide();
                                return false;
                            }
                        });
                    }

                    flag = false;

                    // 点击菜单 如果菜单中有table 也要跟着刷新
                    var $contentWindow = $('.LRADMS_iframe[data-id="' + dataUrl + '"]')[0].contentWindow;
                    if ($contentWindow.table.options.type === table_type.bootstrapTable) {
                        $contentWindow.$.table.refresh();
                    } else if ($contentWindow.table.options.type === table_type.bootstrapTreeTable) {
                        // $contentWindow.$.treeTable.refresh();
                    }




                    return false;
                }
            });
            if (flag) { // 如果选项卡不存在
                var str = '<a href="javascript:;" class="active menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-remove"></i></a>';
                $('.menuTab').removeClass('active');
                var str1 = '<iframe class="LRADMS_iframe" id="iframe' + dataId + '" name="iframe' + dataId + '"  width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
                $('.mainContent').find('iframe.LRADMS_iframe').hide();
                $('.mainContent').append(str1);
                //$.loading(true);
                $('.mainContent iframe:visible').load(function () {
                    //$.loading(false);
                });
                $('.menuTabs .page-tabs-content').append(str);
                $.learuntab.scrollToTab($('.menuTab.active'));
            }
            return false;
        },
        myAddTab: function (Name, Href, data_id) {

            $(".navbar-custom-menu>ul>li.open").removeClass("open");
            var dataId = data_id;
            var dataUrl = Href;
            var menuName = Name;
            var flag = true;
            if (dataUrl == undefined || $.trim(dataUrl).length == 0) {
                return false;
            }
            $('.menuTab').each(function () {
                if ($(this).data('id') == dataUrl) {
                    if (!$(this).hasClass('active')) {
                        $(this).addClass('active').siblings('.menuTab').removeClass('active');
                        $.learuntab.scrollToTab(this);
                        $('.mainContent .LRADMS_iframe').each(function () {
                            if ($(this).data('id') == dataUrl) {
                                $(this).show().siblings('.LRADMS_iframe').hide();
                                return false;
                            }
                        });
                    }
                    flag = false;
                    return false;
                }
            });
            if (flag) {

                var str = '<a href="javascript:;" class="active menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-remove"></i></a>';
                $('.menuTab').removeClass('active');

                var str1 = '<iframe class="LRADMS_iframe" id="iframe' + dataId + '" name="iframe' + dataId + '"  width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
                $('.mainContent').find('iframe.LRADMS_iframe').hide();
                $('.mainContent').append(str1);
                //$.loading(true);
                $('.mainContent iframe:visible').load(function () {
                    //$.loading(false);
                });
                //if(count>1){
                //    $('.menuTabs .page-tabs-content').pop().append(str);
                //    $.learuntab.scrollToTab($('.menuTab.active'));
                //}
                //else {
                $('.menuTabs .page-tabs-content').append(str);
                $.learuntab.scrollToTab($('.menuTab.active'));
                //}

            }
            return false;
        },
        scrollTabRight: function () {
            var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
            var tabOuterWidth = $.learuntab.calSumWidth($(".content-tabs").children().not(".menuTabs"));
            var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
            var scrollVal = 0;
            if ($(".page-tabs-content").width() < visibleWidth) {
                return false;
            } else {
                var tabElement = $(".menuTab:first");
                var offsetVal = 0;
                while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
                    offsetVal += $(tabElement).outerWidth(true);
                    tabElement = $(tabElement).next();
                }
                offsetVal = 0;
                while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                    offsetVal += $(tabElement).outerWidth(true);
                    tabElement = $(tabElement).next();
                }
                scrollVal = $.learuntab.calSumWidth($(tabElement).prevAll());
                if (scrollVal > 0) {
                    $('.page-tabs-content').animate({
                        marginLeft: 0 - scrollVal + 'px'
                    }, "fast");
                }
            }
        },
        scrollTabLeft: function () {
            var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
            var tabOuterWidth = $.learuntab.calSumWidth($(".content-tabs").children().not(".menuTabs"));
            var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
            var scrollVal = 0;
            if ($(".page-tabs-content").width() < visibleWidth) {
                return false;
            } else {
                var tabElement = $(".menuTab:first");
                var offsetVal = 0;
                while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
                    offsetVal += $(tabElement).outerWidth(true);
                    tabElement = $(tabElement).next();
                }
                offsetVal = 0;
                if ($.learuntab.calSumWidth($(tabElement).prevAll()) > visibleWidth) {
                    while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                        offsetVal += $(tabElement).outerWidth(true);
                        tabElement = $(tabElement).prev();
                    }
                    scrollVal = $.learuntab.calSumWidth($(tabElement).prevAll());
                }
            }
            $('.page-tabs-content').animate({marginLeft: 0 - scrollVal + 'px'}, "fast");
        },
        scrollToTab: function (element) {
            var marginLeftVal = $.learuntab.calSumWidth($(element).prevAll()),
                marginRightVal = $.learuntab.calSumWidth($(element).nextAll());
            var tabOuterWidth = $.learuntab.calSumWidth($(".content-tabs").children().not(".menuTabs"));
            var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
            var scrollVal = 0;
            if ($(".page-tabs-content").outerWidth() < visibleWidth) {
                scrollVal = 0;
            } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
                if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
                    scrollVal = marginLeftVal;
                    var tabElement = element;
                    while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content").outerWidth() - visibleWidth)) {
                        scrollVal -= $(tabElement).prev().outerWidth();
                        tabElement = $(tabElement).prev();
                    }
                }
            } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
                scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
            }
            $('.page-tabs-content').animate({
                marginLeft: 0 - scrollVal + 'px'
            }, "fast");
        },
        calSumWidth: function (element) {
            var width = 0;
            $(element).each(function () {
                width += $(this).outerWidth(true);
            });
            return width;
        },
        init: function () {
            $('.menuItem').on('click', $.learuntab.addTab);
            $('.menuTabs').on('click', '.menuTab i', $.learuntab.closeTab);
            $('.menuTabs').on('click', '.menuTab', $.learuntab.activeTab);
            $('.tabLeft').on('click', $.learuntab.scrollTabLeft);
            $('.tabRight').on('click', $.learuntab.scrollTabRight);
            $('.tabReload').on('click', $.learuntab.refreshTab);
            $('.tabCloseCurrent').on('click', function () {
                $('.page-tabs-content').find('.active i').trigger("click");
            });
            $('.tabCloseAll').on('click', function () {
                $('.page-tabs-content').children("[data-id]").find('.fa-remove').each(function () {
                    $('.LRADMS_iframe[data-id="' + $(this).data('id') + '"]').remove();
                    $(this).parents('a').remove();
                });
                $('.page-tabs-content').children("[data-id]:first").each(function () {
                    $('.LRADMS_iframe[data-id="' + $(this).data('id') + '"]').show();
                    $(this).addClass("active");
                });
                $('.page-tabs-content').css("margin-left", "0");
            });
            $('.tabCloseOther').on('click', $.learuntab.closeOtherTabs);
            $('.fullscreen').on('click', function () {
                if (!$(this).attr('fullscreen')) {
                    $(this).attr('fullscreen', 'true');
                    $.learuntab.requestFullScreen();
                } else {
                    $(this).removeAttr('fullscreen')
                    $.learuntab.exitFullscreen();
                }
            });
        }
    };
    $.learunindex = {
        load: function () {
            $("body").removeClass("hold-transition")
            $("#content-wrapper").find('.mainContent').height($(window).height() - 130);
            $(window).resize(function (e) {
                $("#content-wrapper").find('.mainContent').height($(window).height() - 130);
            });
            $(".sidebar-toggle").click(function () {
                if (!$("body").hasClass("sidebar-collapse")) {
                    $("body").addClass("sidebar-collapse");
                } else {
                    $("body").removeClass("sidebar-collapse");
                }
            });
            $(window).load(function () {
                window.setTimeout(function () {
                    $('#ajax-loader').fadeOut();
                }, 300);
            });
        },
        jsonWhere: function (data, action) {
            if (action == null) return;
            var reval = new Array();
            $(data).each(function (i, v) {
                if (action(v)) {
                    reval.push(v);
                }
            });
            return reval;
        },
        loadMenu: function (id) {
            var data;

            data = [
                // {
                //     "F_ModuleId": "001",
                //     "F_ParentId": "0",
                //     "F_EnCode": "SysManage",
                //     "F_FullName": "实例演示",
                //     "F_Icon": "fa fa-wrench",
                //     "F_UrlAddress": "/default",
                //     "F_Target": "expand",
                //     "F_IsMenu": 0,
                //     "F_AllowExpand": 1,
                //     "F_IsPublic": 0,
                //     "F_AllowEdit": null,
                //     "F_AllowDelete": null,
                //     "F_SortCode": 1,
                //     "F_DeleteMark": 0,
                //     "F_EnabledMark": 1,
                //     "F_Description": null,
                //     "F_CreateDate": null,
                //     "F_CreateUserId": null,
                //     "F_CreateUserName": null,
                //     "F_ModifyDate": "2015-11-17 11:22:46",
                //     "F_ModifyUserId": "System",
                //     "F_ModifyUserName": "超级管理员"
                // },
                // {
                //     "F_ModuleId": "002",
                //     "F_ParentId": "001",
                //     "F_EnCode": "SysManage",
                //     "F_FullName": "弹窗",
                //     "F_Icon": "fa fa-wrench",
                //     "F_UrlAddress": "/default",
                //     "F_Target": "expand",
                //     "F_IsMenu": 0,
                //     "F_AllowExpand": 1,
                //     "F_IsPublic": 0,
                //     "F_AllowEdit": null,
                //     "F_AllowDelete": null,
                //     "F_SortCode": 1,
                //     "F_DeleteMark": 0,
                //     "F_EnabledMark": 1,
                //     "F_Description": null,
                //     "F_CreateDate": null,
                //     "F_CreateUserId": null,
                //     "F_CreateUserName": null,
                //     "F_ModifyDate": "2015-11-17 11:22:46",
                //     "F_ModifyUserId": "System",
                //     "F_ModifyUserName": "超级管理员"
                // },
                // {
                //     "F_ModuleId": "003",
                //     "F_ParentId": "002",
                //     "F_EnCode": "SysManage",
                //     "F_FullName": "弹窗组件",
                //     "F_Icon": "fa fa-wrench",
                //     "F_UrlAddress": "/demo/modal/layer",
                //     "F_Target": "iframe",
                //     "F_IsMenu": 0,
                //     "F_AllowExpand": 1,
                //     "F_IsPublic": 0,
                //     "F_AllowEdit": null,
                //     "F_AllowDelete": null,
                //     "F_SortCode": 1,
                //     "F_DeleteMark": 0,
                //     "F_EnabledMark": 1,
                //     "F_Description": null,
                //     "F_CreateDate": null,
                //     "F_CreateUserId": null,
                //     "F_CreateUserName": null,
                //     "F_ModifyDate": "2015-11-17 11:22:46",
                //     "F_ModifyUserId": "System",
                //     "F_ModifyUserName": "超级管理员"
                // },
                // {
                //     "F_ModuleId": "004",
                //     "F_ParentId": "002",
                //     "F_EnCode": "SysManage",
                //     "F_FullName": "模态窗口",
                //     "F_Icon": "fa fa-wrench",
                //     "F_UrlAddress": "/demo/modal/modals",
                //     "F_Target": "iframe",
                //     "F_IsMenu": 0,
                //     "F_AllowExpand": 1,
                //     "F_IsPublic": 0,
                //     "F_AllowEdit": null,
                //     "F_AllowDelete": null,
                //     "F_SortCode": 1,
                //     "F_DeleteMark": 0,
                //     "F_EnabledMark": 1,
                //     "F_Description": null,
                //     "F_CreateDate": null,
                //     "F_CreateUserId": null,
                //     "F_CreateUserName": null,
                //     "F_ModifyDate": "2015-11-17 11:22:46",
                //     "F_ModifyUserId": "System",
                //     "F_ModifyUserName": "超级管理员"
                // },
                // {
                //     "F_ModuleId": "0502",
                //     "F_ParentId": "001",
                //     "F_EnCode": "SysManage",
                //     "F_FullName": "文件上传",
                //     "F_Icon": "fa fa-wrench",
                //     "F_UrlAddress": "/default",
                //     "F_Target": "expand",
                //     "F_IsMenu": 0,
                //     "F_AllowExpand": 1,
                //     "F_IsPublic": 0,
                //     "F_AllowEdit": null,
                //     "F_AllowDelete": null,
                //     "F_SortCode": 1,
                //     "F_DeleteMark": 0,
                //     "F_EnabledMark": 1,
                //     "F_Description": null,
                //     "F_CreateDate": null,
                //     "F_CreateUserId": null,
                //     "F_CreateUserName": null,
                //     "F_ModifyDate": "2015-11-17 11:22:46",
                //     "F_ModifyUserId": "System",
                //     "F_ModifyUserName": "超级管理员"
                // },
                // {
                //     "F_ModuleId": "0502",
                //     "F_ParentId": "001",
                //     "F_EnCode": "SysManage",
                //     "F_FullName": "文件上传",
                //     "F_Icon": "fa fa-wrench",
                //     "F_UrlAddress": "/default",
                //     "F_Target": "expand",
                //     "F_IsMenu": 0,
                //     "F_AllowExpand": 1,
                //     "F_IsPublic": 0,
                //     "F_AllowEdit": null,
                //     "F_AllowDelete": null,
                //     "F_SortCode": 1,
                //     "F_DeleteMark": 0,
                //     "F_EnabledMark": 1,
                //     "F_Description": null,
                //     "F_CreateDate": null,
                //     "F_CreateUserId": null,
                //     "F_CreateUserName": null,
                //     "F_ModifyDate": "2015-11-17 11:22:46",
                //     "F_ModifyUserId": "System",
                //     "F_ModifyUserName": "超级管理员"
                // },


                {
                    "F_ModuleId": "1000",
                    "F_ParentId": "0",
                    "F_EnCode": "SysManage",
                    "F_FullName": "应用维护子系统",
                    "F_Icon": "fa fa-wrench",
                    "F_UrlAddress": "/default",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "1011111100",
                    "F_ParentId": "1000",
                    "F_EnCode": "SysManage",
                    "F_FullName": "bpmn.js",
                    "F_Icon": "fa fa-wrench",
                    "F_UrlAddress": "/bpmn/dist/bpmn.html",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "1100041123",
                    "F_ParentId": "1000",
                    "F_EnCode": "SysManage",
                    "F_FullName": "用户管理",
                    "F_Icon": "fa fa-user",
                    "F_UrlAddress": "/user",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 2,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "11110004331123",
                    "F_ParentId": "1000",
                    "F_EnCode": "SysManage",
                    "F_FullName": "角色管理",
                    "F_Icon": "fa fa-user",
                    "F_UrlAddress": "/role",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 2,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "1100041123123123",
                    "F_ParentId": "1000",
                    "F_EnCode": "SysManage",
                    "F_FullName": "菜单管理",
                    "F_Icon": "fa fa-list",
                    "F_UrlAddress": "/menu",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 2,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                // {
                //     "F_ModuleId": "11000",
                //     "F_ParentId": "1000",
                //     "F_EnCode": "SysManage",
                //     "F_FullName": "登录日志",
                //     "F_Icon": "fa fa-user",
                //     "F_UrlAddress": "/logininfo",
                //     "F_Target": "expand",
                //     "F_IsMenu": 0,
                //     "F_AllowExpand": 1,
                //     "F_IsPublic": 0,
                //     "F_AllowEdit": null,
                //     "F_AllowDelete": null,
                //     "F_SortCode": 3,
                //     "F_DeleteMark": 0,
                //     "F_EnabledMark": 1,
                //     "F_Description": null,
                //     "F_CreateDate": null,
                //     "F_CreateUserId": null,
                //     "F_CreateUserName": null,
                //     "F_ModifyDate": "2015-11-17 11:22:46",
                //     "F_ModifyUserId": "System",
                //     "F_ModifyUserName": "超级管理员"
                // },
                {
                    "F_ModuleId": "1100065",
                    "F_ParentId": "1000",
                    "F_EnCode": "SysManage",
                    "F_FullName": "热线上报",
                    "F_Icon": "fa fa-file-text-o",
                    "F_UrlAddress": "/hotline",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 3,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                // {
                //     "F_ModuleId": "11000654",
                //     "F_ParentId": "1000",
                //     "F_EnCode": "SysManage",
                //     "F_FullName": "数据字段",
                //     "F_Icon": "fa fa-database",
                //     "F_UrlAddress": "/dictdata",
                //     "F_Target": "expand",
                //     "F_IsMenu": 0,
                //     "F_AllowExpand": 1,
                //     "F_IsPublic": 0,
                //     "F_AllowEdit": null,
                //     "F_AllowDelete": null,
                //     "F_SortCode": 3,
                //     "F_DeleteMark": 0,
                //     "F_EnabledMark": 1,
                //     "F_Description": null,
                //     "F_CreateDate": null,
                //     "F_CreateUserId": null,
                //     "F_CreateUserName": null,
                //     "F_ModifyDate": "2015-11-17 11:22:46",
                //     "F_ModifyUserId": "System",
                //     "F_ModifyUserName": "超级管理员"
                // },
                {
                    "F_ModuleId": "11000155",
                    "F_ParentId": "1000",
                    "F_EnCode": "SysManage",
                    "F_FullName": "流程定义管理",
                    "F_Icon": "fa fa-cog",
                    "F_UrlAddress": "/processdef",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 3,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                // {
                //     "F_ModuleId": "1100015335",
                //     "F_ParentId": "1000",
                //     "F_EnCode": "SysManage",
                //     "F_FullName": "事件详情",
                //     "F_Icon": "fa fa-user",
                //     "F_UrlAddress": "/detail",
                //     "F_Target": "expand",
                //     "F_IsMenu": 0,
                //     "F_AllowExpand": 1,
                //     "F_IsPublic": 0,
                //     "F_AllowEdit": null,
                //     "F_AllowDelete": null,
                //     "F_SortCode": 3,
                //     "F_DeleteMark": 0,
                //     "F_EnabledMark": 1,
                //     "F_Description": null,
                //     "F_CreateDate": null,
                //     "F_CreateUserId": null,
                //     "F_CreateUserName": null,
                //     "F_ModifyDate": "2015-11-17 11:22:46",
                //     "F_ModifyUserId": "System",
                //     "F_ModifyUserName": "超级管理员"
                // },
                // {
                //     "F_ModuleId": "1100015331235",
                //     "F_ParentId": "1000",
                //     "F_EnCode": "SysManage",
                //     "F_FullName": "流程图绘制",
                //     "F_Icon": "fa fa-user",
                //     "F_UrlAddress": "/activiti/create",
                //     "F_Target": "expand",
                //     "F_IsMenu": 0,
                //     "F_AllowExpand": 1,
                //     "F_IsPublic": 0,
                //     "F_AllowEdit": null,
                //     "F_AllowDelete": null,
                //     "F_SortCode": 3,
                //     "F_DeleteMark": 0,
                //     "F_EnabledMark": 1,
                //     "F_Description": null,
                //     "F_CreateDate": null,
                //     "F_CreateUserId": null,
                //     "F_CreateUserName": null,
                //     "F_ModifyDate": "2015-11-17 11:22:46",
                //     "F_ModifyUserId": "System",
                //     "F_ModifyUserName": "超级管理员"
                // },
                {
                    "F_ModuleId": "1006555550",
                    "F_ParentId": "0",
                    "F_EnCode": "SysManage",
                    "F_FullName": "基础数据资源管理子系统",
                    "F_Icon": "fa fa-list-alt",
                    "F_UrlAddress": "/default",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                /*{
                    "F_ModuleId": "1006555550123",
                    "F_ParentId": "1006555550",
                    "F_EnCode": "SysManage",
                    "F_FullName": "GIS数据管理",
                    "F_Icon": "fa fa-globe",
                    "F_UrlAddress": "/default",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },*/
                {
                    "F_ModuleId": "1006555512350123",
                    "F_ParentId": "1006555550",
                    "F_EnCode": "SysManage",
                    "F_FullName": "网格管理",
                    "F_Icon": "fa fa-table",
                    "F_UrlAddress": "/grid/grid",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031232",
                    "F_ParentId": "1006555550",
                    "F_EnCode": "SysManage",
                    "F_FullName": "部件导入",
                    "F_Icon": "fa fa-upload",
                    "F_UrlAddress": "/cImport",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031232",
                    "F_ParentId": "1006555550",
                    "F_EnCode": "SysManage",
                    "F_FullName": "网格导入",
                    "F_Icon": "fa fa-upload",
                    "F_UrlAddress": "/gImport",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031232",
                    "F_ParentId": "1006555550",
                    "F_EnCode": "SysManage",
                    "F_FullName": "部件管理",
                    "F_Icon": "fa fa-gears",
                    "F_UrlAddress": "/component",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031232",
                    "F_ParentId": "1006555550",
                    "F_EnCode": "SysManage",
                    "F_FullName": "部件编辑",
                    "F_Icon": "fa fa-pencil",
                    "F_UrlAddress": "/component/import",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031233",
                    "F_ParentId": "1006555550",
                    "F_EnCode": "SysManage",
                    "F_FullName": "单元网格编辑",
                    "F_Icon": "fa fa-pencil",
                    "F_UrlAddress": "/grid/toGridSave",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031234",
                    "F_ParentId": "1006555550",
                    "F_EnCode": "SysManage",
                    "F_FullName": "地图发布",
                    "F_Icon": "fa fa-map",
                    "F_UrlAddress": "/publish/toPublishList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "1006555550124",
                    "F_ParentId": "0",
                    "F_EnCode": "SysManage",
                    "F_FullName": "无线采集子系统",
                    "F_Icon": "fa fa-signal",
                    "F_UrlAddress": "/default",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031235",
                    "F_ParentId": "1006555550124",
                    "F_EnCode": "SysManage",
                    "F_FullName": "案件采集",
                    "F_Icon": "fa fa-folder-open",
                    "F_UrlAddress": "/event/toWirelessAcquisitionList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "1001248655551235031235",
                    "F_ParentId": "1006555550124",
                    "F_EnCode": "SysManage",
                    "F_FullName": "案件核实",
                    "F_Icon": "fa fa-stethoscope",
                    "F_UrlAddress": "/event/toCaseVerifyList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100121248655551235031235",
                    "F_ParentId": "1006555550124",
                    "F_EnCode": "SysManage",
                    "F_FullName": "案件核查",
                    "F_Icon": "fa fa-eye",
                    "F_UrlAddress": "/event/toCaseInspectList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "10033121248655551235031235",
                    "F_ParentId": "1006555550124",
                    "F_EnCode": "SysManage",
                    "F_FullName": "无效案件",
                    "F_Icon": "fa fa-trash",
                    "F_UrlAddress": "/event/toCaseInvalidList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "1110033121248655551235031235",
                    "F_ParentId": "1006555550124",
                    "F_EnCode": "SysManage",
                    "F_FullName": "案件历史",
                    "F_Icon": "fa fa-history",
                    "F_UrlAddress": "/event/toCaseHistoryList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "1006555550125",
                    "F_ParentId": "0",
                    "F_EnCode": "SysManage",
                    "F_FullName": "监督受理子系统",
                    "F_Icon": "fa fa-video-camera",
                    "F_UrlAddress": "/default",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031236",
                    "F_ParentId": "1006555550125",
                    "F_EnCode": "SysManage",
                    "F_FullName": "案件受理",
                    "F_Icon": "fa fa-play",
                    "F_UrlAddress": "/event/toSupervisionAcceptanceSave",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031240",
                    "F_ParentId": "1006555550125",
                    "F_EnCode": "SysManage",
                    "F_FullName": "自处理案件列表",
                    "F_Icon": "fa fa-wrench",
                    "F_UrlAddress": "/event/toSupervisionAcceptanceList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031237",
                    "F_ParentId": "1006555550125",
                    "F_EnCode": "SysManage",
                    "F_FullName": "待办案件",
                    "F_Icon": "fa fa-spinner",
                    "F_UrlAddress": "/event/toSelfProcessingList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                /*{
                    "F_ModuleId": "100655551235031238",
                    "F_ParentId": "1006555550125",
                    "F_EnCode": "SysManage",
                    "F_FullName": "待办案件列表",
                    "F_Icon": "fa fa-newspaper-o",
                    "F_UrlAddress": "/event/toSendVerificationList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031239",
                    "F_ParentId": "1006555550125",
                    "F_EnCode": "SysManage",
                    "F_FullName": "待核查列列表",
                    "F_Icon": "fa fa-newspaper-o",
                    "F_UrlAddress": "/event/toSendCheckList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },*/
                {
                    "F_ModuleId": "1006555550126",
                    "F_ParentId": "0",
                    "F_EnCode": "SysManage",
                    "F_FullName": "协同工作子系统",
                    "F_Icon": "fa fa-users",
                    "F_UrlAddress": "/default",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031237",
                    "F_ParentId": "1006555550126",
                    "F_EnCode": "SysManage",
                    "F_FullName": "案件处理",
                    "F_Icon": "fa fa-code",
                    "F_UrlAddress": "/event/toCooperativeWorkList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031238",
                    "F_ParentId": "1006555550126",
                    "F_EnCode": "SysManage",
                    "F_FullName": "督办案件",
                    "F_Icon": "fa fa-yelp",
                    "F_UrlAddress": "/event/toSuperviseCasesList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031239",
                    "F_ParentId": "1006555550126",
                    "F_EnCode": "SysManage",
                    "F_FullName": "已办案件",
                    "F_Icon": "fa fa-thumbs-o-up",
                    "F_UrlAddress": "/event/toCasesHandledList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031240",
                    "F_ParentId": "1006555550126",
                    "F_EnCode": "SysManage",
                    "F_FullName": "已结案件",
                    "F_Icon": "fa fa-thumbs-up",
                    "F_UrlAddress": "/event/toClosedCasesList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031240",
                    "F_ParentId": "1006555550126",
                    "F_EnCode": "SysManage",
                    "F_FullName": "已挂账列表",
                    "F_Icon": "fa fa-rmb",
                    "F_UrlAddress": "/event/toOnAccountList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031240",
                    "F_ParentId": "1006555550126",
                    "F_EnCode": "SysManage",
                    "F_FullName": "已作废列表",
                    "F_Icon": "fa fa-times-circle",
                    "F_UrlAddress": "/event/toCancelList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031240",
                    "F_ParentId": "1006555550126",
                    "F_EnCode": "SysManage",
                    "F_FullName": "授权列表",
                    "F_Icon": "fa fa-plus",
                    "F_UrlAddress": "/event/toAuthorizationList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031241",
                    "F_ParentId": "1006555550126",
                    "F_EnCode": "SysManage",
                    "F_FullName": "案件查询",
                    "F_Icon": "fa fa-search",
                    "F_UrlAddress": "/event/toCaseInquiryList",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "1006555550127",
                    "F_ParentId": "0",
                    "F_EnCode": "SysManage",
                    "F_FullName": "综合评价子系统",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/default",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031242",
                    "F_ParentId": "1006555550127",
                    "F_EnCode": "SysManage",
                    "F_FullName": "区域评价",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/default",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031246",
                    "F_ParentId": "100655551235031242",
                    "F_EnCode": "SysManage",
                    "F_FullName": "一类区域",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/evaluate/toCellGridRegionOne",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031247",
                    "F_ParentId": "100655551235031242",
                    "F_EnCode": "SysManage",
                    "F_FullName": "二类区域",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/evaluate/toCellGridRegionTwo",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031243",
                    "F_ParentId": "1006555550127",
                    "F_EnCode": "SysManage",
                    "F_FullName": "专业部门评价",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/evaluate/toProfessionalDepartments",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031244",
                    "F_ParentId": "1006555550127",
                    "F_EnCode": "SysManage",
                    "F_FullName": "岗位评价",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/evaluate/toPosition",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031245",
                    "F_ParentId": "1006555550127",
                    "F_EnCode": "SysManage",
                    "F_FullName": "评价等级",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/evaluate/toEvaluationLevel",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "1006555550128",
                    "F_ParentId": "0",
                    "F_EnCode": "SysManage",
                    "F_FullName": "监督指挥子系统",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/default",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031248",
                    "F_ParentId": "1006555550128",
                    "F_EnCode": "SysManage",
                    "F_FullName": "首页",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/commandSubsystem/index",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031249",
                    "F_ParentId": "1006555550128",
                    "F_EnCode": "SysManage",
                    "F_FullName": "网格人员",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/commandSubsystem/gridOwner",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031250",
                    "F_ParentId": "1006555550128",
                    "F_EnCode": "SysManage",
                    "F_FullName": "案件分析",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/commandSubsystem/caseAnalysis",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031251",
                    "F_ParentId": "1006555550128",
                    "F_EnCode": "SysManage",
                    "F_FullName": "综合评价",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/commandSubsystem/comprehensiveEvaluation",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "100655551235031251",
                    "F_ParentId": "1006555550128",
                    "F_EnCode": "SysManage",
                    "F_FullName": "网格信息",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "/commandSubsystem/gridInformation",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "1006542340",
                    "F_ParentId": "0",
                    "F_EnCode": "SysManage",
                    "F_FullName": "3+1平台",
                    "F_Icon": "fa fa-wrench",
                    "F_UrlAddress": "/default",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "10065551111151235031251",
                    "F_ParentId": "1006542340",
                    "F_EnCode": "SysManage",
                    "F_FullName": "市容环卫",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "http://115.233.226.243:8095/OnlineSupervise",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "10065551111151235031251",
                    "F_ParentId": "1006542340",
                    "F_EnCode": "SysManage",
                    "F_FullName": "市政公用",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "http://218.205.125.157:8094/#/home/IntelligentMonitoring",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "10065551111151235031251",
                    "F_ParentId": "1006542340",
                    "F_EnCode": "SysManage",
                    "F_FullName": "综合执法",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "http://61.164.53.62:9137/#/dashboard",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
                {
                    "F_ModuleId": "10065551111151235031251",
                    "F_ParentId": "1006542340",
                    "F_EnCode": "SysManage",
                    "F_FullName": "园林绿化",
                    "F_Icon": "fa fa-magic",
                    "F_UrlAddress": "http://101.251.144.62:8002/tree-admin/#/greenmanage",
                    "F_Target": "expand",
                    "F_IsMenu": 0,
                    "F_AllowExpand": 1,
                    "F_IsPublic": 0,
                    "F_AllowEdit": null,
                    "F_AllowDelete": null,
                    "F_SortCode": 1,
                    "F_DeleteMark": 0,
                    "F_EnabledMark": 1,
                    "F_Description": null,
                    "F_CreateDate": null,
                    "F_CreateUserId": null,
                    "F_CreateUserName": null,
                    "F_ModifyDate": "2015-11-17 11:22:46",
                    "F_ModifyUserId": "System",
                    "F_ModifyUserName": "超级管理员"
                },
            ];

            var _html = "";
            $.each(data, function (i) {
                var row = data[i];
                if (row.F_ParentId == "0") {
                    if (i == 0) {
                        // _html += '<li class="treeview active">';
                        _html += '<li class="treeview">';
                    } else {
                        _html += '<li class="treeview">';
                    }
                    _html += '<a href="#">'
                    _html += '<i class="' + row.F_Icon + '"></i><span>' + row.F_FullName + '</span><i class="fa fa-angle-left pull-right"></i>'
                    _html += '</a>'
                    var childNodes = $.learunindex.jsonWhere(data, function (v) {
                        return v.F_ParentId == row.F_ModuleId
                    });
                    if (childNodes.length > 0) {
                        _html += '<ul class="treeview-menu">';
                        $.each(childNodes, function (i) {
                            var subrow = childNodes[i];
                            var subchildNodes = $.learunindex.jsonWhere(data, function (v) {
                                return v.F_ParentId == subrow.F_ModuleId
                            });
                            _html += '<li>';
                            if (subchildNodes.length > 0) {
                                _html += '<a href="#"><i class="' + subrow.F_Icon + '"></i>' + subrow.F_FullName + '';
                                _html += '<i class="fa fa-angle-left pull-right"></i></a>';
                                _html += '<ul class="treeview-menu">';
                                $.each(subchildNodes, function (i) {
                                    var subchildNodesrow = subchildNodes[i];
                                    // _html += '<li><a class="menuItem" data-id="' + subrow.F_ModuleId + '" href="' + subrow.F_UrlAddress + '"><i class="' + subchildNodesrow.F_Icon + '"></i>' + subchildNodesrow.F_FullName + '</a></li>';
                                    //把subrow修改为subchildNodesrow 不知道是不是源代码写错了
                                    _html += '<li><a class="menuItem" data-id="' + subchildNodesrow.F_ModuleId + '" href="javascript:void(0)" data-href="' + subchildNodesrow.F_UrlAddress + '"><i class="' + subchildNodesrow.F_Icon + '"></i>' + subchildNodesrow.F_FullName + '</a></li>';
                                });
                                _html += '</ul>';

                            } else {
                                _html += '<a class="menuItem" data-id="' + subrow.F_ModuleId + '" href="javascript:void(0)" data-href="' + subrow.F_UrlAddress + '"><i class="' + subrow.F_Icon + '"></i>' + subrow.F_FullName + '</a>';
                            }
                            _html += '</li>';
                        });
                        _html += '</ul>';
                    }
                    _html += '</li>'
                }
            });
            $("#sidebar-menu").append(_html);
            $("#sidebar-menu li a").click(function () {


                var d = $(this), e = d.next();
                if (e.is(".treeview-menu") && e.is(":visible")) {
                    e.slideUp(500, function () {
                        e.removeClass("menu-open")
                    });
                    e.parent("li").removeClass("active")
                } else if (e.is(".treeview-menu") && !e.is(":visible")) {
                    var f = d.parents("ul").first();
                    g = f.find("ul:visible").slideUp(500);
                    g.removeClass("menu-open");
                    var h = d.parent("li");
                    e.slideDown(500, function () {
                        e.addClass("menu-open");
                        f.find("li.active").removeClass("active");
                        h.addClass("active");

                        var _height1 = $(window).height() - $("#sidebar-menu >li.active").position().top - 41;
                        var _height2 = $("#sidebar-menu li > ul.menu-open").height() + 10;
                        if (_height2 > _height1) {
                            $("#sidebar-menu >li > ul.menu-open").css({
                                overflow: "auto",
                                height: _height1
                            })
                        }
                    })
                }
                e.is(".treeview-menu");







            });
        }
    };
    $(function () {
        var url = window.location.href;
        var urlNum = url.split("=")[1];

        $.learunindex.load();
        $.learunindex.loadMenu(urlNum);
        $.learuntab.init();
        // 右键菜单实现
        $.contextMenu({
            selector: ".menuTab",
            trigger: 'right',
            autoHide: true,
            items: {
                "close_current": {
                    name: "关闭当前",
                    icon: "fa-close",
                    callback: function (key, opt) {
                        $('.page-tabs-content').find('.active i').trigger("click");
                    }
                },
                "close_other": {
                    name: "除此之外全部关闭",
                    icon: "paste",
                    callback: function (key, opt) {
                        $('.page-tabs-content').children("[data-id]").find('.fa-remove').parents('a').not(".active").each(function () {
                            $('.LRADMS_iframe[data-id="' + $(this).data('id') + '"]').remove();
                            $(this).remove();
                        });
                        $('.page-tabs-content').css("margin-left", "0");
                    }
                },
                "close_all": {
                    name: "全部关闭",
                    icon: "cut",
                    callback: function (key, opt) {
                        $('.page-tabs-content').children("[data-id]").find('.fa-remove').each(function () {
                            $('.LRADMS_iframe[data-id="' + $(this).data('id') + '"]').remove();
                            $(this).parents('a').remove();
                        });
                        $('.page-tabs-content').children("[data-id]:first").each(function () {
                            $('.LRADMS_iframe[data-id="' + $(this).data('id') + '"]').show();
                            $(this).addClass("active");
                        });
                        $('.page-tabs-content').css("margin-left", "0");
                    }
                },
                "refresh": {
                    name: "刷新页面",
                    icon: "fa-refresh",
                    callback: function (key, opt) {
                        var currentId = $('.page-tabs-content').find('.active').attr('data-id');
                        var target = $('.LRADMS_iframe[data-id="' + currentId + '"]');
                        var url = target.attr('src');
                        //$.loading(true);
                        target.attr('src', url).load(function () {
                            //$.loading(false);
                        });
                    }
                },
                // "step": "---------",
                "full": {
                    name: "全屏显示",
                    icon: "fa-arrows-alt",
                    callback: function (key, opt) {
                        if (!$(this).attr('fullscreen')) {
                            $(this).attr('fullscreen', 'true');
                            $.learuntab.requestFullScreen();
                        } else {
                            $(this).removeAttr('fullscreen')
                            $.learuntab.exitFullscreen();
                        }
                    }
                },
                // "close_left": {
                //     name: "关闭左侧",
                //     icon: "fa-reply",
                //     callback: function(key, opt) {
                //         setActiveTab(this);
                //         this.prevAll('.menuTab').not(":last").each(function() {
                //             if ($(this).hasClass('active')) {
                //                 setActiveTab(this);
                //             }
                //             $('.RuoYi_iframe[data-id="' + $(this).data('id') + '"]').remove();
                //             $(this).remove();
                //         });
                //         $('.page-tabs-content').css("margin-left", "0");
                //     }
                // },
                // "close_right": {
                //     name: "关闭右侧",
                //     icon: "fa-share",
                //     callback: function(key, opt) {
                //         setActiveTab(this);
                //         this.nextAll('.menuTab').each(function() {
                //             $('.menuTab[data-id="' + $(this).data('id') + '"]').remove();
                //             $(this).remove();
                //         });
                //     }
                // },
                // "open": {
                //     name: "新窗口打开",
                //     icon: "fa-link",
                //     callback: function(key, opt) {
                //         var target = $('.RuoYi_iframe[data-id="' + this.data('id') + '"]');
                //         window.open(target.attr('src'));
                //     }
                // },
            }
        })

    });


})(jQuery);

