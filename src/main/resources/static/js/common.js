
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


let table = {
	config: {},
	// 当前实例配置
	options: {},
	// // 设置实例配置
	// set: function(id) {
	// 	if($.common.getLength(table.config) > 1) {
	// 		let tableId = $.common.isEmpty(id) ? $(event.currentTarget).parents(".bootstrap-table").find("table.table").attr("id") : id;
	// 		if ($.common.isNotEmpty(tableId)) {
	// 			table.options = table.get(tableId);
	// 		}
	// 	}
	// },
	// // 获取实例配置
	// get: function(id) {
	// 	return table.config[id];
	// },
	// 记住选择实例组
	rememberSelecteds: {},
	// 记住选择ID组
	rememberSelectedIds: {}
};



(function ($) {
	$.extend({
		table: {
			init: function (options) {
				let defaults = {
					id: "bootstrap-table",
					type: 0, // 0 代表bootstrapTable 1代表bootstrapTreeTable
					method: 'get',
					height: undefined,
					sidePagination: "server",
					sortName: "",
					sortOrder: "asc",
					pagination: true,
					paginationLoop: false,
					pageSize: 10,
					pageNumber: 1,
					pageList: [10, 25, 50],
					toolbar: "toolbar",
					loadingFontSize: 13,
					striped: false,
					escape: false,
					firstLoad: true,
					showFooter: false,
					search: false,
					showSearch: true,
					showPageGo: false,
					showRefresh: true,
					showColumns: true,
					showToggle: true,
					showExport: false,
					clickToSelect: false,
					singleSelect: false,
					mobileResponsive: true,
					maintainSelected: false,
					rememberSelected: false,
					fixedColumns: false,
					fixedNumber: 0,
					fixedRightNumber: 0,
					queryParams: $.table.queryParams,
					rowStyle: {},
				};
				options = $.extend(defaults, options);
				table.options = options;
				table.config[options.id] = options;


				// $.table.initEvent();

				$('#' + options.id).bootstrapTable({
					id: options.id,
					url: options.url,                                   // 请求后台的URL（*）
					contentType: "application/x-www-form-urlencoded",   // 编码类型
					method: options.method,                             // 请求方式（*）
					cache: false,                                       // 是否使用缓存
					height: options.height,                             // 表格的高度
					striped: options.striped,                           // 是否显示行间隔色
					sortable: true,                                     // 是否启用排序
					sortStable: true,                                   // 设置为 true 将获得稳定的排序
					sortName: options.sortName,                         // 排序列名称
					sortOrder: options.sortOrder,                       // 排序方式  asc 或者 desc
					pagination: options.pagination,                     // 是否显示分页（*）
					paginationLoop: options.paginationLoop,             // 是否启用分页条无限循环的功能
					pageNumber: 1,                                      // 初始化加载第一页，默认第一页
					pageSize: options.pageSize,                         // 每页的记录行数（*）
					pageList: options.pageList,                         // 可供选择的每页的行数（*）
					firstLoad: options.firstLoad,                       // 是否首次请求加载数据，对于数据较大可以配置false
					escape: options.escape,                             // 转义HTML字符串
					showFooter: options.showFooter,                     // 是否显示表尾
					iconSize: 'outline',                                // 图标大小：undefined默认的按钮尺寸 xs超小按钮sm小按钮lg大按钮
					toolbar: '#' + options.toolbar,                     // 指定工作栏
					loadingFontSize: options.loadingFontSize,           // 自定义加载文本的字体大小
					sidePagination: options.sidePagination,             // server启用服务端分页client客户端分页
					search: options.search,                             // 是否显示搜索框功能
					searchText: options.searchText,                     // 搜索框初始显示的内容，默认为空
					showSearch: options.showSearch,                     // 是否显示检索信息
					showPageGo: options.showPageGo,               		// 是否显示跳转页
					showRefresh: options.showRefresh,                   // 是否显示刷新按钮
					showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
					showToggle: options.showToggle,                     // 是否显示详细视图和列表视图的切换按钮
					showExport: options.showExport,                     // 是否支持导出文件
					showHeader: options.showHeader,                     // 是否显示表头
					showFullscreen: options.showFullscreen,             // 是否显示全屏按钮
					uniqueId: options.uniqueId,                         // 唯 一的标识符
					clickToSelect: options.clickToSelect,				// 是否启用点击选中行
					singleSelect: options.singleSelect,                 // 是否单选checkbox
					mobileResponsive: options.mobileResponsive,         // 是否支持移动端适配
					cardView: options.cardView,                         // 是否启用显示卡片视图
					detailView: options.detailView,                     // 是否启用显示细节视图
					onClickRow: options.onClickRow,                     // 点击某行触发的事件
					onDblClickRow: options.onDblClickRow,               // 双击某行触发的事件
					onClickCell: options.onClickCell,                   // 单击某格触发的事件
					onDblClickCell: options.onDblClickCell,             // 双击某格触发的事件
					onEditableSave: options.onEditableSave,             // 行内编辑保存的事件
					onExpandRow: options.onExpandRow,                   // 点击详细视图的事件
					maintainSelected: options.maintainSelected,         // 前端翻页时保留所选行
					rememberSelected: options.rememberSelected,         // 启用翻页记住前面的选择
					fixedColumns: options.fixedColumns,                 // 是否启用冻结列（左侧）
					fixedNumber: options.fixedNumber,                   // 列冻结的个数（左侧）
					fixedRightNumber: options.fixedRightNumber,         // 列冻结的个数（右侧）
					onReorderRow: options.onReorderRow,                 // 当拖拽结束后处理函数
					queryParams: options.queryParams,                   // 传递参数（*）
					rowStyle: options.rowStyle,                         // 通过自定义函数设置行样式
					footerStyle: options.footerStyle,                   // 通过自定义函数设置页脚样式
					columns: options.columns,                           // 显示列信息（*）
					data: options.data,                                 // 被加载的数据
					responseHandler: $.table.responseHandler,           // 在加载服务器发送来的数据之前处理函数
					onLoadSuccess: $.table.onLoadSuccess,               // 当所有数据被加载时触发处理函数
					exportOptions: options.exportOptions,               // 前端导出忽略列索引
					detailFormatter: options.detailFormatter,           // 在行下面展示其他数据列表
				});





			},
			// 查询条件
			queryParams: function(params) {
				let curParams = {
					// 传递参数查询参数
					size: params.limit,
					page: params.offset / params.limit,
					sort: params.sort + ',' + params.order,
					searchValue: params.search,
				};
				var currentId = $.common.isEmpty(table.options.formId) ? $('form').attr('id') : table.options.formId;
				return $.extend(curParams, $.common.formToJSON(currentId));

				// var curParams = {
				// 	// 传递参数查询参数
				// 	pageSize:       params.limit,
				// 	pageNum:        params.offset / params.limit + 1,
				// 	searchValue:    params.search,
				// 	orderByColumn:  params.sort,
				// 	isAsc:          params.order
				// };
				// var currentId = $.common.isEmpty(table.options.formId) ? $('form').attr('id') : table.options.formId;
				// return $.extend(curParams, $.common.formToJSON(currentId));
			},
			// 请求获取数据后处理回调函数
			responseHandler: function(result) {


				return {rows: result.data.content, total: result.data.totalElements};


				// if (typeof table.get(this.id).responseHandler == "function") {
				// 	table.get(this.id).responseHandler(res);
				// }
				// if (res.code == 0) {
				// 	if ($.common.isNotEmpty(table.options.sidePagination) && table.options.sidePagination == 'client') {
				// 		return res.rows;
				// 	} else {
				// 		if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
				// 			var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
				// 			$.each(res.rows, function(i, row) {
				// 				row.state = $.inArray(row[column], table.rememberSelectedIds[table.options.id]) !== -1;
				// 			})
				// 		}
				// 		return { rows: res.rows, total: res.total };
				// 	}
				// } else {
				// 	$.modal.alertWarning(res.msg);
				// 	return { rows: [], total: 0 };
				// }
			},

			// 搜索-默认第一个form
			search: function(formId, tableId) {

				// table.set(tableId);
				// table.options.formId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
				let params = $.common.isEmpty(tableId) ? $("#" + table.options.id).bootstrapTable('getOptions') : $("#" + tableId).bootstrapTable('getOptions');
				console.log(params);
				if($.common.isNotEmpty(tableId)){
					$("#" + tableId).bootstrapTable('refresh', params);
				} else{
					$("#" + table.options.id).bootstrapTable('refresh', params);
				}
			},


		},
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


			// 打开遮罩层
			loading: function (message) {
				layer.msg(message, {icon: 16, shade: 0.5, time: 0});
			},

			// 关闭遮罩层
			closeLoading: function () {
				layer.closeAll('dialog');
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

			//    liukai
			submit: function(url, type, dataType, data, callback) {
				let config = {
					url: url,
					type: type,
					dataType: dataType,
					data: data,
					beforeSend: function () {
						$.modal.loading("正在处理中，请稍后...");
					},
					success: function (result) {
						if (typeof callback == "function") {
							callback(result);
						}
						// $.operate.ajaxSuccess(result);
					}
				};
				$.ajax(config)
			},

			// post请求传输   liukai
			post: function (url, data, callback) {
				$.operate.submit(url, "post", "json", data, callback);
			},
			// get请求传输    liukai
			get: function(url, callback) {
				$.operate.submit(url, "get", "json", "", callback);
			},
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
