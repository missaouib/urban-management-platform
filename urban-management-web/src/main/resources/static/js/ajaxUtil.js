(function ($) {
    $.extend({
        ajaxUtil: {
            get(url, callback) {
                let config = {
                    url: url,
                    type: 'get',
                    dataType: 'json',
                    success: function (result) {
                        if (result.code === web_status.SUCCESS) {
                            callback(result);
                        } else {
                            $.modal.alertError(result.message);
                        }
                    }
                };
                $.ajax(config)
            }
        }
    });


})(jQuery);