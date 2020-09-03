
//重写alert
window.alert = function(msg, callback){
	parent.layer.alert(msg, function(index){
		parent.layer.close(index);
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
};

//重写confirm式样框
window.confirm = function(msg, callback){
	parent.layer.confirm(msg, {btn: ['确定','取消']},
	function(){//确定事件
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
};

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length > 1){
    	alert("只能选择一条记录");
    	return ;
    }
    
    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    return grid.getGridParam("selarrrow");
}



(function ($) {

	// $.extend($.fn.dataTable.defaults, {
	// 	method: 'GET',
	// 	searching: false,
	// 	ordering: false,
	// 	processing: true,
	// 	lengthChange: false,
	// 	serverSide: true,
	// 	language: language,
	// });

	$.extend({
		modal: {
			// 显示图标
			icon: function (type) {
				let icon = "";
				if (type === modal_status.WARNING) {
					icon = 0;
				} else if (type === modal_status.SUCCESS) {
					icon = 1;
				} else if (type === modal_status.FAIL) {
					icon = 2;
				} else {
					icon = 3;
				}
				return icon;
			},
			alert: function (message, callback, type) {
				layer.alert(message, {
					icon: $.modal.icon(type),
					title: "系统提示",
					btn: ['确认'],
					closeBtn: 0
				}, function (index) {
					//点击确定按钮 关闭窗口
					layer.close(index);
					if (callback !== undefined) {
						callback(true);
					}
				});
			},
			// 成功提示
			alertSuccess: function (message, callback) {
				$.modal.alert(message, callback, modal_status.SUCCESS);
			},
			// 错误提示
			alertError: function (message, callback) {
				$.modal.alert(message, callback, modal_status.FAIL);
			},
			// 警告提示
			alertWarning: function (message, callback) {
				$.modal.alert(message, callback, modal_status.WARNING);
			},
			confirm: function (message, callback) {
				layer.confirm(message, {
					icon: 3,
					title: "系统提示",
					closeBtn: 0,
					btn: ['确认', '取消']
				}, function (index) {
					layer.close(index);
					if (callback !== undefined) {
						callback(true);
					}
				});
			},
		},
		common: {
			// 判断字符串是否为空
			isEmpty: function (value) {
				return value == null || value.trim === "";

			},
			// 判断一个字符串是否为非空串
			isNotEmpty: function (value) {
				return !$.common.isEmpty(value);
			},
			// 获取form下所有的字段并转换为json对象
			formToJSON: function (formId) {
				return $('#' + formId).serializeJSON();
			},
			// 获取searchForm表单转换成字符串
			searchForm: function (formId) {
				return $('#' + formId).serialize(formId);
			}

		},
		operate: {
			// add: function () {
			// 	console.log('enter operate add function');
			// 	window.location.href = 'http://baidu.com';
				// table.set();
				// $.modal.open("添加" + table.options.modalName, $.operate.addUrl(id));
			// }

			// 添加信息，以tab页展现
			addTab: function () {
				// table.set();
				// $.modal.openTab("添加" + table.options.modalName, $.operate.addUrl(id));


				window.parent.$.learuntab.myAddTab('新增用户','/user/add','');
			},
		}

	});


})(jQuery);


/** 弹窗状态码 */
modal_status = {
	SUCCESS: "success",
	FAIL: "error",
	WARNING: "warning"
};
