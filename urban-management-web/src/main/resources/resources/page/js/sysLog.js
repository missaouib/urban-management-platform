/**
 * Created by Administrator on 2017/6/12.
 */
var currentID;
function tableLoad(){
    $('#table').bootstrapTable({
        method: "get",
        url: "json/sysLog.json",
        striped: true,
        singleSelect: false,
        dataType: "json",
        pagination: true, //分页
        pageSize: 10,
        pageNumber: 1,
        search: false, //显示搜索框
        contentType: "application/x-www-form-urlencoded",
        queryParams: queryParams,
        columns: [

            {
                checkbox: "true",
                field: 'check',
                align: 'center',
                valign: 'middle'
            }
            ,
            {
                title: '序号',
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '日志名称',
                field: 'serviceName',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '日志描述',
                field: 'note',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '发生时间',
                field: 'visitTime',
                align: 'center',
                valign: 'middle'
            }


        ]
    });
}

function queryParams(params) {
    return {
        page: params.pageNumber,
        rows: params.limit,//页码大小
        order: params.order,
        sort: params.sort,
        SendPeople: $("#person").val(),
        Title: $("#tit").val(),
        BeginSendTime: $("#beginSendTime").val(),
        EndSendTime: $("#endSendTime").val()
    };
}

//数据的查询
function checkPersonData() {
    $("#table").bootstrapTable('refresh');
}



