/**
 * @author banxian
 * @date: 2021/1/15 11:35
 */
var fileArray = [];
var satellitepathway = {
    data: function () {
        $('#table').datagrid({
            url: '/api/satellitepathway/list',
            fitColumns: true,
            rownumbers: true,
            pagination: true,//数据网格（datagrid）底部显示分页工具栏
            collapsible: true,
            sortOrder: 'desc',
            loadFilter: function (data) {
                console.log("当前卫星轨道报查询结果page:");
                console.log(data);
                return {
                    rows: data.records,//数据对象json
                    total: data.total,//数据总个数
                }
            },
            columns: [[
                {field: 'cx', checkbox: 'true'},
                {field: 'satelliteAcceptsite', title: '接收站点', width: 50, sortable: true},
                {field: 'satelliteName', title: '卫星名称', width: 50, align: 'right', sortable: true},
                {field: 'enterTime', title: '入境时间', width: 100, align: 'right', sortable: true},
                {field: 'outTime', title: '出境时间', width: 100, align: 'right', sortable: true},
                {field: 'scanArea', title: '扫描范围', width: 100, align: 'right', sortable: true},
                {
                    field: 'whetherIncludeHunan', title: '是否包含湖南', width: 100, align: 'right', sortable: true,
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return "包含湖南";
                        } else {
                            return "不包含湖南";
                        }
                    }
                },
                {field: 'tabulationPerson', title: '制表人', width: 100, align: 'right', sortable: true},
                {field: 'approvePerson', title: '审批人', width: 100, align: 'right', sortable: true},
                {field: 'addTime', title: '添加时间', width: 100, align: 'right', sortable: true}
            ]]
        });
    },

    add: function () {
        $("#add_dialog").dialog({
            title: '新增轨道报',
            top: '10%',
            left: '20%',
            width: 900,
            height: 600,
            modal: true,
            content: "<iframe scrolling='auto' frameborder='0' src='/page/satellitepathway/add' style='width:100%; height:100%; display:block;'></iframe>"
        });
        $("#add_dialog").dialog("open"); // 打开dialog
    },

    delete: function () {
        //console.log("查看删除列表");
        var rows = $('#table').datagrid('getSelections');
        console.log(rows);
        if (rows.length == 0) {
            $.messager.alert('提示', "请选择至少一行");
        } else {
            var idArray = [];
            for (x in rows) {
                idArray.push(rows[x].satellitePathwayId);
            }
            console.log('下一行为要删除的项目idlist');
            console.log(idArray);
            $.messager.confirm('提示', '你确定删除这条记录嘛？', function (r) {
                if (r) {
                    $.ajax({
                        url: '/api/satellitepathway/delete',
                        traditional: true,
                        type: 'post',
                        dataType: 'json',
                        data: {ids: idArray},
                        // success:function (e) {
                        //     console.log("删除成功");
                        //     $('#filetable').datagrid('reload');	// reload the user data
                        // },
                        error: function (e) {
                            //console.log(e);
                            if (e.responseText == "Y") {
                                $.messager.show({	// show error message
                                    title: '提示',
                                    msg: "删除成功"
                                });
                                $('#table').datagrid('reload');	// reload the user data
                            } else {
                                $.messager.show({	// show error message
                                    title: 'Error',
                                    msg: "删除失败"
                                });
                            }
                        }
                    })
                }
            });
        }

    },

    upload: function () {
        $("#upload_dialog").dialog({
            title: '导入轨道报',
            top: '10%',
            left: '25%',
            width: 700,
            height: 450,
            modal: true,
            content: "<iframe scrolling='auto' frameborder='0' src='/page/satellitepathway/upload' style='width:100%; height:100%; display:block;'></iframe>"
        });
        $("#upload_dialog").dialog("open"); // 打开dialog
    },

    down: function () {
        var params = $("#reportFind").serializeArray();
        var columns = $("#table").datagrid("options").columns;
        var url = "/forestFire/SatelLitePathWay/downSate";
        doExport(url, columns, params);
    },

    search: function () {
        var formdata = $("#serachform").serializeArray();
        console.log(formdata);
        // var SatellitaPathWay = {
        //     enterTime: formdata[0].value,
        //     outTime: formdata[1].value,
        //     satelliteName: formdata[2].value,
        //     whetherIncludeHunan: formdata[3].value,
        // }
        var user =
        $('#table').datagrid({
            // url: '/api/satellitepathway/list',
            url: '/api/satellitepathway/es/list',
            queryParams: SatellitaPathWay
        });
    },

    reset: function () {
        $('#serachform').form('clear');
    },

    init: function () {
        $("#search").on('click', function () {
            satellitepathway.search();
        });
        $("#refresh").on('click', function () {
            satellitepathway.refresh();
        });
        $("#resetsearch").on('click', function () {
            satellitepathway.reset();
        })
        $('#satelliteName').combobox({
            url: '/api/satellitepathway/namelist',
            // valueField:'satelliteName',
            textField: 'satelliteName'
        });
    },

}

//选择文件
function importDoc() {
    $('#fileShhd')[0].value = "";//清空file
    $('#fileShhd').click();
}

//把选择的文件放入div中
function docViewTable() {

    $('#shhdViewDiv').empty();//删除div
    var fileList = $('#uploadForm')[0][0].files;//需要上传的文件
    console.log("下一行为上传的文件集合:");
    console.log(fileList);
    for (var i = 0; i < fileList.length; i++) {
        fileArray.push(fileList[i]);
    }
    if (fileArray.length == 0) {
        return
    }
    var divStr = "";//div内容
    for (var i = 0; i < fileArray.length; i++) {
        var docZt = "等待上传";
        divStr = divStr + '<span class="div_txt">\n' +
            '                        <span class="lf sp_txt">' + fileArray[i].name + '</span>\n' +
            '                        <span class="lf sp_kb">' + (fileArray[i].size) / 1000 + "KB" + '</span>\n' +
            '                        <span class="lf sp_file">' + docZt + '</span>\n' +
            '                        <span class="sp_close rt"></span>\n' +
            '                    </span></br>';
    }
    $('#shhdViewDiv').append(divStr);
    $(".sp_close").on("click", function () {
        var i = $(this).parent().index();
        fileArray.splice(i, i + 1);
        $(this).parent()._remove();
    });
}

//开始上传
function uploadFileDoc() {//提交上传表单
    var url = "/api/satellitepathway/add/list";
    var formData = new FormData();
    for (var i = 0; i < fileArray.length; i++) {
        formData.append("fileShhd[]", fileArray[i]);
    }
    uploadFiles(url, formData);

    $('#fileShhd')[0].value = "";//清空file
}

function uploadFiles(url, formData) {
    $.ajax({
        url: url,
        type: "POST",
        data: formData,
        async: false,
        cache: false,
        processData: false,  // 不处理数据
        contentType: false,  // 不设置内容类型
    }).always(function (data) {
        uploadSuccessTxt(data);
    });
}

function uploadSuccessTxt(data) {//上传文件回调函数
    var docZt = $('#shhdViewDiv').find('.sp_file');//更改doc状态
    var docZtVal = "";
    console.log("上传结果（data>0成功，0失败）:" + data);
    if (parseInt(data) > 0) {
        $.messager.alert('提示', '成功上传' + data + '条轨道报');
        parent.$('#table').datagrid('reload');
        parent.$("#upload_dialog").dialog("close");
    } else {
        $.messager.alert('提示', '数据上传错误');
    }

    // document.getElementById("input_topbanner").className = "hide";
}

//清屏
function closeDoc() {
    $('#fileShhd')[0].value = "";//清空file
    $('#shhdViewDiv').empty();//删除div内容
    fileArray = [];
}


