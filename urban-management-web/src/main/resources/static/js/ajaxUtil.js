(function ($) {
    $.extend({
        ajaxUtil: {
            get(url, callback) {
                let index;
                let config = {
                    url: url,
                    type: 'get',
                    dataType: 'json',
                    beforeSend: function () {
                        index = layer.load(0, {shade: [0.3,'#fff'] /*0.1透明度的白色背景*/});
                    },
                    success: function (result) {
                        layer.close(index);
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