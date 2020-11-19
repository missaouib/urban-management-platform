/*
* 时限判断
* */
function setTimeLimit(row,index) {
	var date=new Date();
	var year=date.getFullYear();
	var month=date.getMonth()+1;
	var day=date.getDate();
	var hh=date.getHours();
	var mm=date.getMinutes();
	var ss=date.getSeconds();
	if (mm < 10){
		var MM = "0"+mm;
	}else {
		MM = mm
	}
	if (hh < 10){
		var HH = "0"+hh;
	}else {
		HH = hh
	}
	if (ss < 10){
		var SS = "0"+ss;
	}else {
		SS = ss
	}
	var nowTime=year+"-"+month+"-"+day+" "+HH+":"+MM+":"+SS;
	var nowTimes= new Date(nowTime);
	var staTime = new Date(index.startTime)
	var lessNum = nowTimes - staTime;
	if (index.timeType = "分钟"){
		var num = lessNum/60000
	}else if(index.timeType = "天"){

	}
	if (num > index.timeLimit){
		var demo = "<div style='width: 25px;height: 25px;border-radius: 50%;overflow: hidden;margin: 0 auto' class='timeSet'>" +
			"<img src='../img/0.png' style='width: 100%;height: 100%'></div>";
	}else if(num < (index.timeLimit)/0.8) {
		var demo ="<div style='width: 25px;height: 25px;border-radius: 50%;overflow: hidden;margin: 0 auto' class='timeSet'>" +
			"<img src='../img/1.png' style='width: 100%;height: 100%'></div>";
	}else {
		var demo= "<div style='width: 25px;height: 25px;border-radius: 50%;overflow: hidden;margin: 0 auto' class='timeSet'>" +
			"<img src='../img/2.png' style='width: 100%;height: 100%'></div>";
	}
	return demo;
}
//*重写alert
// window.alert = function(msg, callback){
// 	parent.layer.alert(msg, function(index){
// 		parent.layer.close(index);
// 		if(typeof(callback) === "function"){
// 			callback("ok");
// 		}
// 	});
// };
// function success_jsonpCallback(res) {
// 	var features = geojsonFormat.readFeatures(res);
// 	console.log('点击查询返回的结果如下：');
// 	console.log("features", features);
// 	let mongodbId = features[0].H.mongodb_id;
// 	console.log("features", mongodbId);
// 	/*$.ajaxUtil.get(getGridByCheckLayerUrl + "?mongodbId=" + mongodbId, function (e) {
//         console.log("e", e);
//     });*/
// }


/**
 * 清空时间框里的值
 */
function cleanSearchInput() {
	$("#search_clean").click(function () {
		$("#begin").val("");
		$("#end").val("")
	})
}



/**
 * 步骤进行到哪一步
 * @param bgmNum
 */

function setConduct(bgmNum) {
	var alist = document.querySelectorAll(".alist")
	$(".alist").removeClass("dieselActive")
	$(".alist span").removeClass("spanlist")
	alist[bgmNum].classList.remove("dieselActive");
	alist[bgmNum].childNodes[1].classList.add("spanlist");
	alist[bgmNum].childNodes[3].classList.add("spanlist");
	alist[bgmNum].childNodes[5].classList.add("spanlist");
	alist[bgmNum].childNodes[7].classList.add("spanlist");
}

//全屏 body
function fullScreen() {
	// var element = document.documentElement;
	// if (element.requestFullscreen) {
	// 	element.requestFullscreen();
	// } else if (element.msRequestFullscreen) {
	// 	element.msRequestFullscreen();
	// } else if (element.mozRequestFullScreen) {
	// 	element.mozRequestFullScreen();
	// } else if (element.webkitRequestFullscreen) {
	// 	element.webkitRequestFullscreen();
	// }
	var element = document.getElementById("content-wrapper");
	if (element.requestFullscreen) {
		element.requestFullscreen();
	} else if (element.msRequestFullscreen) {
		element.msRequestFullscreen();
	} else if (element.mozRequestFullScreen) {
		element.mozRequestFullScreen();
	} else if (element.webkitRequestFullscreen) {
		element.webkitRequestFullscreen();
	}
}

//退出全屏
function exitFullscreen() {
	var element = document.getElementById("content-wrapper");
	if (document.exitFullscreen) {
		document.exitFullscreen();
	} else if (document.msExitFullscreen) {
		document.msExitFullscreen();
	} else if (document.mozCancelFullScreen) {
		document.mozCancelFullScreen();
	} else if (document.webkitExitFullscreen) {
		document.webkitExitFullscreen();
	}
}
//重写confirm式样框
// window.confirm = function(msg, callback){
// 	parent.layer.confirm(msg, {btn: ['确定','取消']},
// 	function(){//确定事件
// 		if(typeof(callback) === "function"){
// 			callback("ok");
// 		}
// 	});
// };

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


var table = {
	config: {},
	// 当前实例配置
	options: {},
	// 设置实例配置
	set: function(id) {
		if($.common.getLength(table.config) > 1) {
			let tableId = $.common.isEmpty(id) ? $(event.currentTarget).parents(".bootstrap-table").find("table.table").attr("id") : id;
			if ($.common.isNotEmpty(tableId)) {
				table.options = table.get(tableId);
			}
		}
	},
	// 获取实例配置
	get: function(id) {
		return table.config[id];
	},
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
					treeEnable: true,
					parentIdField: 'parentId',
					id: "bootstrap-table",
					type: 0, // 0 代表bootstrapTable 1代表bootstrapTreeTable
					method: 'get',
					height: undefined,
					sidePagination: "server",
					sortName: "",
					sortOrder: "asc",
					pagination: true,
					paginationLoop: true,
					pageSize: 10,
					pageNumber: 1,
					pageList: [10],
					// pageList: [10, 25, 50],
					toolbar: "toolbar",
					loadingFontSize: 13,
					striped: true,
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


				$.table.initEvent();

				$('#' + options.id).bootstrapTable({
					id: options.id,
					// treeEnable: options.treeEnable,                     // 是否为tree table
					// treeShowField: 'name',                              //
					// parentIdField: 'parentId',                          //

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
					// rowStyle: options.rowStyle,                         // 通过自定义函数设置行样式
					footerStyle: options.footerStyle,                   // 通过自定义函数设置页脚样式
					columns: options.columns,                           // 显示列信息（*）
					data: options.data,                                 // 被加载的数据
					responseHandler: $.table.responseHandler,           // 在加载服务器发送来的数据之前处理函数
					onLoadSuccess: $.table.onLoadSuccess,               // 当所有数据被加载时触发处理函数
					exportOptions: options.exportOptions,               // 前端导出忽略列索引
					detailFormatter: options.detailFormatter,           // 在行下面展示其他数据列表
					// onPostBody: function (data) {
					// 	$($.table.getOptionsIds()).treegrid({
					// 		treeColumn: 1,
					// 		onChange: function() {
					// 			$($.table.getOptionsIds()).bootstrapTable('resetView')
					// 		}
					// 	})
					// }





				});





			},
			// 初始化事件
			initEvent: function() {
				// 实例ID信息
				var optionsIds = $.table.getOptionsIds();
				// 监听事件处理
				// $(optionsIds).on(TABLE_EVENTS, function () {
				// 	table.set($(this).attr("id"));
				// });
				// 选中、取消、全部选中、全部取消（事件）
				$(optionsIds).on("check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table", function (e, rowsAfter, rowsBefore) {

					console.log('选中、取消、全部选中、全部取消（事件）');

					// 复选框分页保留保存选中数组
					var rows = $.common.equals("uncheck-all", e.type) ? rowsBefore : rowsAfter;
					var rowIds = $.table.affectedRowIds(rows);
					console.log(rows);
					console.log(rowIds);
					if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
						func = $.inArray(e.type, ['check', 'check-all']) > -1 ? 'union' : 'difference';
						var selectedIds = table.rememberSelectedIds[table.options.id];
						if($.common.isNotEmpty(selectedIds)) {
							table.rememberSelectedIds[table.options.id] = _[func](selectedIds, rowIds);
						} else {
							table.rememberSelectedIds[table.options.id] = _[func]([], rowIds);
						}
						var selectedRows = table.rememberSelecteds[table.options.id];
						if($.common.isNotEmpty(selectedRows)) {
							table.rememberSelecteds[table.options.id] = _[func](selectedRows, rows);
						} else {
							table.rememberSelecteds[table.options.id] = _[func]([], rows);
						}
					}
				});
				// 加载成功、选中、取消、全部选中、全部取消（事件）
				$(optionsIds).on("check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table load-success.bs.table", function () {
					var toolbar = table.options.toolbar;
					var uniqueId = table.options.uniqueId;
					// 工具栏按钮控制
					var rows = $.common.isEmpty(uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(uniqueId);
					// 非多个禁用
					$('#' + toolbar + ' .multiple').toggleClass('disabled', !rows.length);
					// 非单个禁用
					$('#' + toolbar + ' .single').toggleClass('disabled', rows.length!=1);
				});
				// 图片预览事件
				// $(optionsIds).off("click").on("click", '.img-circle', function() {
				// 	var src = $(this).attr('src');
				// 	var target = $(this).data('target');
				// 	if($.common.equals("self", target)) {
				// 		var height = $(this).data('height');
				// 		var width = $(this).data('width');
				// 		// 如果是移动端，就使用自适应大小弹窗
				// 		if ($.common.isMobile()) {
				// 			width = 'auto';
				// 			height = 'auto';
				// 		}
				// 		layer.open({
				// 			title: false,
				// 			type: 1,
				// 			closeBtn: true,
				// 			shadeClose: true,
				// 			area: ['auto', 'auto'],
				// 			content: "<img src='" + src + "' height='" + height + "' width='" + width + "'/>"
				// 		});
				// 	} else if ($.common.equals("blank", target)) {
				// 		window.open(src);
				// 	}
				// });
				// 单击tooltip事件
				// $(optionsIds).on("click", '.tooltip-show', function() {
				// 	var target = $(this).data('target');
				// 	var input = $(this).prev();
				// 	if ($.common.equals("copy", target)) {
				// 		input.select();
				// 		document.execCommand("copy");
				// 	} else if ($.common.equals("open", target)) {
				// 		parent.layer.alert(input.val(), {
				// 			title: "信息内容",
				// 			shadeClose: true,
				// 			btn: ['确认'],
				// 			btnclass: ['btn btn-primary'],
				// 		});
				// 	}
				// });
			},

			// 获取实例ID，如存在多个返回#id1,#id2 delimeter分隔符
			getOptionsIds: function(separator) {
				var _separator = $.common.isEmpty(separator) ? "," : separator;
				var optionsIds = "";
				$.each(table.config, function(key, value){
					optionsIds += "#" + key + _separator;
				});
				return optionsIds.substring(0, optionsIds.length - 1);
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
				if (typeof table.get(this.id).responseHandler == "function") {
					table.get(this.id).responseHandler(result);
				}
				if (result.code === web_status.SUCCESS) {
					if ($.common.isNotEmpty(table.options.sidePagination) && table.options.sidePagination === 'client') {
						return result.data.content;
					} else {
						if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
							var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
							$.each(result.data.content, function(i, row) {
								row.state = $.inArray(row[column], table.rememberSelectedIds[table.options.id]) !== -1;
							})
						}
						return {rows: result.data.content, total: result.data.totalElements};
					}
				} else {
					$.modal.alertWarning(result.message);
					return { rows: [], total: 0 };
				}
			},

			// 搜索-默认第一个form
			search: function(formId, tableId) {
				table.set(tableId);
				table.options.formId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
				let params = $.common.isEmpty(tableId) ? $("#" + table.options.id).bootstrapTable('getOptions') : $("#" + tableId).bootstrapTable('getOptions');
				if($.common.isNotEmpty(tableId)){
					$("#" + tableId).bootstrapTable('refresh', params);
				} else{
					$("#" + table.options.id).bootstrapTable('refresh', params);
				}
			},
			// 刷新表格
			refresh: function (tableId) {
				var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
				$("#" + currentId).bootstrapTable('refresh', {
					silent: false
				});
			},
			// 查询表格指定列值
			selectColumns: function(column) {
				var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
					return $.common.getItemField(row, column);
				});
				if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
					var selectedRows = table.rememberSelecteds[table.options.id];
					if($.common.isNotEmpty(selectedRows)) {
						rows = $.map(table.rememberSelecteds[table.options.id], function (row) {
							return $.common.getItemField(row, column);
						});
					}
				}
				return $.common.uniqueFn(rows);
			},
			// 查询表格首列值
			selectFirstColumns: function() {
				var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
					return $.common.getItemField(row, table.options.columns[1].field);
				});
				if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
					var selectedRows = table.rememberSelecteds[table.options.id];
					if($.common.isNotEmpty(selectedRows)) {
						rows = $.map(selectedRows, function (row) {
							return $.common.getItemField(row, table.options.columns[1].field);
						});
					}
				}
				return $.common.uniqueFn(rows);
			},
			// 获取当前页选中或者取消的行ID
			affectedRowIds: function (rows) {
				var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
				var rowIds;
				if ($.isArray(rows)) {
					rowIds = $.map(rows, function (row) {
						return $.common.getItemField(row, column);
					});
				} else {
					rowIds = [rows[column]];
				}
				return rowIds;
			},

		},
		form: {
			// 表单重置
			reset: function(formId, tableId) {
				table.set(tableId);
				var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
				$("#" + currentId)[0].reset();
				if (table.options.type == table_type.bootstrapTable) {
					if($.common.isEmpty(tableId)){
						$("#" + table.options.id).bootstrapTable('refresh');
					} else{
						$("#" + tableId).bootstrapTable('refresh');
					}
				} else if (table.options.type == table_type.bootstrapTreeTable) {
					if($.common.isEmpty(tableId)){
						$("#" + table.options.id).bootstrapTreeTable('refresh', []);
					} else{
						$("#" + tableId).bootstrapTreeTable('refresh', []);
					}
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
			// 消息提示
			msg: function(content, type) {
				if (type != undefined) {
					layer.msg(content, { icon: $.modal.icon(type), time: 1000, shift: 5 });
				} else {
					layer.msg(content);
				}
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

			// 成功消息
			msgSuccess: function(content) {
				$.modal.msg(content, modal_status.SUCCESS);
			},



			// 选卡页方式打开
			openTab: function (title, url) {
				createMenuItem(title, url);
			},

			// 关闭选项卡
			closeTab: function (dataId) {
				closeItem(dataId);
			},

			// 弹出层指定宽度
			open: function (title, url, width, height, callback) {
				//如果是移动端，就使用自适应大小弹窗
				if ($.common.isMobile()) {
					width = 'auto';
					height = 'auto';
				}
				if ($.common.isEmpty(title)) {
					title = false;
				}
				if ($.common.isEmpty(url)) {
					url = "/404.html";
				}
				if ($.common.isEmpty(width)) {
					width = 800;
				}
				if ($.common.isEmpty(height)) {
					height = ($(window).height() - 50);
				}
				// if ($.common.isEmpty(callback)) {
				// 	callback = function(index, layero) {
				// 		var iframeWin = layero.find('iframe')[0];
				// 		iframeWin.contentWindow.submitHandler(index, layero);
				// 	}
				// }
				layer.open({
					type: 2,
					area: [width + 'px', height + 'px'],
					fix: false,
					//不固定
					maxmin: true,
					shade: 0.3,
					title: title,
					content: url,
					btn: ['确定', '关闭'],
					// 弹层外区域关闭
					shadeClose: true,
					yes: callback,
					cancel: function(index) {
						return true;
					}
				});
			},
			openOptions: function (options) {
				var _url = $.common.isEmpty(options.url) ? "/404.html" : options.url;
				var _title = $.common.isEmpty(options.title) ? "系统窗口" : options.title;
				var _width = $.common.isEmpty(options.width) ? "800" : options.width;
				var _height = $.common.isEmpty(options.height) ? ($(window).height() - 150) : options.height;
				var _btn = ['<i class="fa fa-check"></i> 确认', '<i class="fa fa-close"></i> 关闭'];
				if ($.common.isEmpty(options.yes)) {
					options.yes = function(index, layero) {
						options.callBack(index, layero);
					}
				}
				var btnCallback = {};
				if(options.btn instanceof Array){
					for (var i = 1, len = options.btn.length; i < len; i++) {
						var btn = options["btn" + (i + 1)];
						if (btn) {
							btnCallback["btn" + (i + 1)] = btn;
						}
					}
				}
				var index = layer.open($.extend({
					type: 2,
					maxmin: $.common.isEmpty(options.maxmin) ? true : options.maxmin,
					shade: 0.3,
					title: _title,
					fix: false,
					area: [_width + 'px', _height + 'px'],
					content: _url,
					shadeClose: $.common.isEmpty(options.shadeClose) ? true : options.shadeClose,
					skin: options.skin,
					btn: $.common.isEmpty(options.btn) ? _btn : options.btn,
					yes: options.yes,
					cancel: function () {
						return true;
					}
				}, btnCallback));
				if ($.common.isNotEmpty(options.full) && options.full === true) {
					layer.full(index);
				}
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

			// 比较两个字符串（大小写敏感）
			equals: function (str, that) {
				return str == that;
			},
			// 获取form下所有的字段并转换为json对象
			formToJSON: function (formId) {
				return $('#' + formId).serializeJSON();
			},
			// 获取searchForm表单转换成字符串
			searchForm: function (formId) {
				return $('#' + formId).serialize(formId);
			},

			// 数组去重
			uniqueFn: function(array) {
				var result = [];
				var hashObj = {};
				for (var i = 0; i < array.length; i++) {
					if (!hashObj[array[i]]) {
						hashObj[array[i]] = true;
						result.push(array[i]);
					}
				}
				return result;
			},

			// 获取节点数据，支持多层级访问
			getItemField: function (item, field) {
				var value = item;
				if (typeof field !== 'string' || item.hasOwnProperty(field)) {
					return item[field];
				}
				var props = field.split('.');
				for (var p in props) {
					value = value && value[props[p]];
				}
				return value;
			},



			// 获取obj对象长度
			getLength: function(obj) {
				var count = 0;
				for (var i in obj) {
					if (obj.hasOwnProperty(i)) {
						count++;
					}
				}
				return count;
			},
			// 判断移动端
			isMobile: function () {
				return navigator.userAgent.match(/(Android|iPhone|SymbianOS|Windows Phone|iPad|iPod)/i);
			},
			// 空格截取
			trim: function (value) {
				if (value == null) {
					return "";
				}
				return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
			},



		},
		operate: {

			// 保存提交当前表单数据 然后刷新当前选项卡页面
			saveAndReload(url, data) {
				let config = {
					url: url,
					type: 'post',
					dataType: 'json',
					data: data,
					beforeSend: function () {
						$.modal.loading("正在处理中，请稍后...");
					},
					success: function (result) {
						if (result.code === web_status.SUCCESS) {
							$.modal.msgSuccess(result.message);
							setTimeout("window.location.reload()", 2000);
							return;
						} else if (result.code === web_status.SUCCESS && table.options.type === table_type.bootstrapTreeTable) {
							$.modal.msgSuccess(result.message);
						} else if (result.code === web_status.SUCCESS && $.common.isEmpty(table.options.type)) {
							$.modal.msgSuccess(result.message)
						} else if (result.code === web_status.WARNING) {
							$.modal.alertWarning(result.message)
						} else {
							$.modal.alertError(result.message);
							return;
						}
						$.modal.closeLoading();
					}
				};
				$.ajax(config)

			},

			// 保存选项卡信息
			saveTab: function(url, data, callback) {
				var config = {
					url: url,
					type: "post",
					dataType: "json",
					data: data,
					beforeSend: function () {
						$.modal.loading("正在处理中，请稍后...");
					},
					success: function(result) {
						if (typeof callback == "function") {
							callback(result);
						}
						$.operate.successTabCallback(result);
					}
				};
				$.ajax(config)
			},
			// 选项卡成功回调执行事件（父窗体静默更新）
			successTabCallback: function(result) {
				if (result.code === web_status.SUCCESS) {
					var topWindow = $(window.parent.document);
					var currentId = $('.page-tabs-content', topWindow).find('.active').attr('parent-panel');
					var $contentWindow = $('.LRADMS_iframe[data-id="' + currentId + '"]', topWindow)[0].contentWindow;
					// $.modal.close();
					$contentWindow.$.modal.msgSuccess(result.message);
					$contentWindow.$(".layui-layer-padding").removeAttr("style");

					if ($contentWindow.table.options.type === table_type.bootstrapTable) {
						$contentWindow.$.table.refresh();
					} else if ($contentWindow.table.options.type === table_type.bootstrapTreeTable) {
						// $contentWindow.$.treeTable.refresh();
					}

					$.modal.closeTab();
				} else if (result.code === web_status.WARNING) {
					$.modal.alertWarning(result.message)
				} else {
					$.modal.alertError(result.message);
				}
				// $.modal.closeLoading();
			},
			// 添加访问地址
			addUrl: function(id) {
				var url = $.common.isEmpty(id) ? table.options.createUrl.replace("{id}", "") : table.options.createUrl.replace("{id}", id);
				return url;
			},
			// 修改访问地址
			editUrl: function(id) {
				var url = "/404.html";
				if ($.common.isNotEmpty(id)) {
					url = table.options.updateUrl.replace("{id}", id);
				} else {
					var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
					if (id.length == 0) {
						$.modal.alertWarning("请至少选择一条记录");
						return;
					}
					url = table.options.updateUrl.replace("{id}", id);
				}
				return url;
			},
			submit: function (url, type, dataType, data, callback) {
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
						$.operate.ajaxSuccess(result);
					}
				};
				$.ajax(config)
			},
			// 保存结果弹出msg刷新table表格
			ajaxSuccess: function (result) {
				if (result.code === web_status.SUCCESS && table.options.type === table_type.bootstrapTable) {
					$.modal.msgSuccess(result.message);
					$.table.refresh();
					return;
				} else if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTreeTable) {
					$.modal.msgSuccess(result.message);
					// $.treeTable.refresh();
				} else if (result.code == web_status.SUCCESS && $.common.isEmpty(table.options.type)) {
					$.modal.msgSuccess(result.message)
				}  else if (result.code === web_status.WARNING) {
					$.modal.alertWarning(result.message)
				}  else {
					$.modal.alertError(result.message);
					return;
				}
				$.modal.closeLoading();
			},
			// 删除信息
			remove: function(id) {
				table.set();
				$.modal.confirm("确定删除该条" + table.options.modalName + "信息吗？", function() {
					var url = $.common.isEmpty(id) ? table.options.removeUrl : table.options.removeUrl.replace("{id}", id);
					if(table.options.type == table_type.bootstrapTreeTable) {
						$.operate.get(url);
					} else {
						var data = { "ids": id };
						$.operate.submit(url, "post", "json", data);
					}
				});

			},
			//上报案件
			report: function(id) {
				table.set();
				$.modal.confirm("确定上报该条信息吗？", function() {
					var url = $.common.isNotEmpty(id) ? table.options.reportUrl : table.options.removeUrl.replace("{id}", id);
					if(table.options.type == table_type.bootstrapTreeTable) {
						$.operate.get(url);
					} else {
						var data = { "id": id };
						$.operate.submit(url, "post", "json", data);
					}
				});

			},
			// 批量删除信息
			removeBatch: function() {
				table.set();
				var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
				if (rows.length == 0) {
					$.modal.alertWarning("请至少选择一条记录");
					return;
				}
				$.modal.confirm("确认要删除选中的 " + rows.length + " 条数据吗?", function() {
					var url = table.options.removeUrl;
					var data = { "ids": rows.join() };
					$.operate.submit(url, "post", "json", data);
				});
			},
			// post请求传输
			post: function (url, data, callback) {
				$.operate.submit(url, "post", "json", data, callback);
			},
			// get请求传输
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
			addTab: function (id) {
				table.set();
				$.modal.openTab("添加" + table.options.modalName, $.operate.addUrl(id));
			},

			// 修改信息，以tab页展现
			editTab: function(id) {
				table.set();
				$.modal.openTab("修改" + table.options.modalName, $.operate.editUrl(id));
			},
		},

		// //校验
		// validate: {
		// 	// 判断返回标识是否唯一 false 不存在 true 存在
		// 	unique: function (value) {
		// 		if (value == "0") {
		// 			return true;
		// 		}
		// 		return false;
		// 	},
		// 	// 表单验证
		// 	form: function (formId) {
		// 		var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
		// 		return $("#" + currentId).validate().form();
		// 	},
		// 	// 重置表单验证（清除提示信息）
		// 	reset: function (formId) {
		// 		var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
		// 		return $("#" + currentId).validate().resetForm();
		// 	}
		// },

	});


})(jQuery);
//点击行变色
$('#bootstrap-table').on('click-row.bs.table', function (e,row,$element) {
	$('.changeColor').removeClass('changeColor');
	$($element).addClass('changeColor');

});
/** 弹窗状态码 */
modal_status = {
	SUCCESS: "success",
	FAIL: "error",
	WARNING: "warning"
};
/** 消息状态码 */
web_status = {
	SUCCESS: "0",
	FAIL: 500,
	WARNING: 301
};

/** 表格类型 */
table_type = {
	bootstrapTable: 0,
	bootstrapTreeTable: 1
};


function createMenuItem(title, url) {
	var panelUrl = window.frameElement.getAttribute('data-id');
	var topWindow = $(window.parent.document);
	$(".navbar-custom-menu>ul>li.open").removeClass("open");
	var dataId = url;
	var dataUrl = url;
	var menuName = title;
	var flag = true;
	if (dataUrl == undefined || $.trim(dataUrl).length == 0) {
		return false;
	}
	$('.menuTab', topWindow).each(function () {
		if ($(this).data('id') === dataUrl) {
			if (!$(this).hasClass('active')) {
				$(this).addClass('active').siblings('.menuTab').removeClass('active');
				window.parent.$.learuntab.scrollToTab(this);
				$('.mainContent .LRADMS_iframe', topWindow).each(function () {
					if ($(this).data('id') === dataUrl) {
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

		var str = '<a href="javascript:;" class="active menuTab" data-id="' + dataUrl + '" parent-panel="' + panelUrl + '">' + menuName + ' <i class="fa fa-remove"></i></a>';
		$('.menuTab', topWindow).removeClass('active');

		var str1 = '<iframe class="LRADMS_iframe" id="' + dataId + '" name="iframe' + dataId + '"  width="100%" height="100%" src="' + dataUrl + '" parent-panel="' + panelUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless style="display: inline"></iframe>';
		$('.mainContent', topWindow).find('iframe.LRADMS_iframe').hide();
		$('.mainContent', topWindow).append(str1);
		//$.loading(true);
		// $('.mainContent iframe:visible').load(function () {
			//$.loading(false);
		// });
		//if(count>1){
		//    $('.menuTabs .page-tabs-content').pop().append(str);
		//    $.learuntab.scrollToTab($('.menuTab.active'));
		//}
		//else {
		$('.menuTabs .page-tabs-content', topWindow).append(str);
		window.parent.$.learuntab.scrollToTab($('.menuTab.active'));
		//}

	}
	return false;
}

function closeItem(dataId) {
	var topWindow = $(window.parent.document);
	if ($.common.isNotEmpty(dataId)) {
		window.parent.$.modal.closeLoading();
		// 根据dataId关闭指定选项卡
		$('.menuTab[data-id="' + dataId + '"]', topWindow).remove();
		// 移除相应tab对应的内容区
		$('.mainContent .RuoYi_iframe[data-id="' + dataId + '"]', topWindow).remove();
		return;
	}
	var panelUrl = window.frameElement.getAttribute('parent-panel');
	$('.page-tabs-content .active i', topWindow).click();
	if ($.common.isNotEmpty(panelUrl)) {
		$('.menuTab[data-id="' + panelUrl + '"]', topWindow).addClass('active').siblings('.menuTab').removeClass('active');
		$('.mainContent .LRADMS_iframe', topWindow).each(function () {
			if ($(this).data('id') == panelUrl) {
				$(this).show().siblings('.LRADMS_iframe').hide();
				return false;
			}
		});
	}
}