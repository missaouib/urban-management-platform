
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
		// table: {
		// 	init: function (options) {
		// 		let defaults = {
		// 			id: 'dataTables',
		// 			searching: false,
		// 			ordering: false,
		// 			serverSide: true,
		// 			processing: true,
		// 			method: 'GET'
		// 		}
		// 		options = $.extend(defaults, options);
		//
		// 		//添加序号列
		// 		options.columns.unshift({
		// 			title: '序号',
		// 			render: function () {
		// 				return '';
		// 			}
		// 		});
		//
		// 		tables = $('#' + options.id).DataTable({
		// 			searching: options.searching,
		// 			ordering: options.ordering,
		// 			serverSide: options.serverSide,
		// 			columns: options.columns,
		//
		// 			createdRow: function (row, data, index) {
		// 				let i = tables.page() * tables.page.len() + index + 1;
		// 				$('td:eq(0)', row).html("&nbsp;" + i);
		// 			},
		// 			ajax: function (data, callback, settings) {
		// 				//封装请求参数
		//
		//
		// 				if (jQuery.isEmptyObject(param) && options.param !== undefined) {
		// 					param = options.param;
		// 				}
		// 				param.size = data.length;
		// 				param.start = data.start;
		// 				param.page = (data.start / data.length);
		//
		// 				$.ajax({
		// 					type: options.method,
		// 					url: options.url,
		// 					cache: false,
		// 					data: param,
		// 					success: function (result) {
		// 						let returnData = {};
		// 						returnData.recordsTotal = result.data.totalElements;           //返回数据全部记录
		// 						returnData.recordsFiltered = result.data.totalElements;        //后台不实现过滤功能，每次查询均视作全部结果
		// 						returnData.data = result.data.content;                         //返回的数据列表
		// 						callback(returnData);
		// 					}
		// 				});
		// 			}
		// 		})
		// 	},
		// 	search: function (parameter) {
		// 		param = parameter;
		// 		tables.draw();
		// 	},
		// 	reset: function (formId, tableId) {
		// 		var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
		// 		$("#" + currentId)[0].reset();
		// 		$("#" + table.options.id).bootstrapTable('refresh', []);
		// 	},
		// 	// 查询条件
		// 	queryParams: function(params) {
		// 		var curParams = {
		// 			// 传递参数查询参数
		// 			size:           params.limit,
		// 			page:           params.offset / params.limit
		// 			// searchValue:    params.search,
		// 			// orderByColumn:  params.sort,
		// 			// isAsc:          params.order
		// 		};
		// 		var currentId = $.common.isEmpty(table.options.formId) ? $('form').attr('id') : table.options.formId;
		// 		return $.extend(curParams, $.common.formToJSON(currentId));
		// 	},
		// 	// 请求获取数据后处理回调函数
		// 	responseHandler: function(res) {
		// 		if (typeof table.options.responseHandler == "function") {
		// 			table.options.responseHandler(res);
		// 		}
		// 		if (res.code === "0") {
		// 			// 判断是否为服务器分页
		// 			if ($.common.isNotEmpty(table.options.sidePagination) && table.options.sidePagination === 'client') {
		// 				return res.rows;
		// 			} else {
		// 				// if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
		// 				//     var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
		// 				//     $.each(res.rows, function (i, row) {
		// 				//         row.state = $.inArray(row[column], table.rememberSelectedIds[table.options.id]) !== -1;
		// 				//     })
		// 				// }
		// 				return {rows: res.data.content, total: res.data.totalElements};
		// 			}
		// 		} else {
		// 			$.modal.alertWarning(res.msg);
		// 			return { rows: [], total: 0 };
		// 		}
		// 	},
		// 	search2: function (formId, tableId, data) {
		// 		// table.set(tableId);
		// 		var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
		// 		var params = $.common.isEmpty(tableId) ? $("#" + table.options.id).bootstrapTable('getOptions') : $("#" + tableId).bootstrapTable('getOptions');
		// 		// params.queryParams = function (params) {
		// 		//     var search = $.common.formToJSON(currentId);
		// 		//     if ($.common.isNotEmpty(data)) {
		// 		//         $.each(data, function (key) {
		// 		//             search[key] = data[key];
		// 		//         });
		// 		//     }
		// 		//     search.pageSize = params.limit;
		// 		//     search.pageNum = params.offset / params.limit + 1;
		// 		//     search.searchValue = params.search;
		// 		//     search.orderByColumn = params.sort;
		// 		//     search.isAsc = params.order;
		// 		//     return search;
		// 		// }
		// 		if ($.common.isNotEmpty(tableId)) {
		// 			$("#" + tableId).bootstrapTable('refresh', params);
		// 		} else {
		// 			$("#" + table.options.id).bootstrapTable('refresh', params);
		// 		}
		// 	}
		// },
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
		}

	});


})(jQuery);


/** 弹窗状态码 */
modal_status = {
	SUCCESS: "success",
	FAIL: "error",
	WARNING: "warning"
};
