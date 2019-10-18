
    // 弹窗显示
    function showImportModel() {
        $('#importModel').modal('show');
    }
    function showAddModel() {
        if (titleConfig == null) {
            alert('请先操作导入')
            return;
        }
        $('#addModel').modal('show');
        var inputdiv = "";
        for (var i=0;i<titleConfig.length;i++) {
            inputdiv = inputdiv + "<div class='form-group col-md-6'>";
            inputdiv  = inputdiv + "<label>"+titleConfig[i]+"</label><input class='form-control'/>";
            inputdiv = inputdiv + "</div>";
        }
        $('#formRow').append(inputdiv);
    }
    //定义按钮事件
    function excelImport(voteId) {
        var fileObj = document.getElementById("file").files[0]; // js 获取文件对象
        var url = window.location.origin + "/item/import"; // 接收上传文件的后台地址
        var form = new FormData(); // FormData 对象
        if (form) {
            form.append("file", fileObj); // 文件对象
        }
        xhr = new XMLHttpRequest(); // XMLHttpRequest 对象
        xhr.open("post", url, false); //post方式，url为服务器请求地址，true 该参数规定请求是否异步处理。
        xhr.onload = uploadComplete; //请求完成
        xhr.onerror = uploadFailed; //请求失败
        xhr.upload.onprogress = progressFunction; //【上传进度调用方法实现】
        xhr.upload.onloadstart = function () { //上传开始执行方法
            ot = new Date().getTime(); //设置上传开始时间
            oloaded = 0; //设置上传开始时，以上传的文件大小为0
        };
        xhr.setRequestHeader("itemId", voteId);
        xhr.send(form); //开始上传，发送form数据
    }

function showAddModel() {
    $('#addModel').modal('show');
}

//定义按钮事件
function excelImport(voteId) {
    var fileObj = document.getElementById("file").files[0]; // js 获取文件对象
    var url = window.location.origin + "/item/import"; // 接收上传文件的后台地址
    var form = new FormData(); // FormData 对象
    if (form) {
        form.append("file", fileObj); // 文件对象
    }
    xhr = new XMLHttpRequest(); // XMLHttpRequest 对象
    xhr.open("post", url, false); //post方式，url为服务器请求地址，true 该参数规定请求是否异步处理。
    xhr.onload = uploadComplete; //请求完成
    xhr.onerror = uploadFailed; //请求失败
    xhr.upload.onprogress = progressFunction; //【上传进度调用方法实现】
    xhr.upload.onloadstart = function () { //上传开始执行方法
        ot = new Date().getTime(); //设置上传开始时间
        oloaded = 0; //设置上传开始时，以上传的文件大小为0
    };
    xhr.setRequestHeader("itemId", voteId);
    xhr.send(form); //开始上传，发送form数据
}

//上传成功响应
function uploadComplete(evt) {
    if (evt.target.status) {
        window.location.reload();
    } else {
        alert("上传失败！");
    }

}

//上传失败
function uploadFailed(evt) {
    alert("上传失败！");
}

//上传进度实现方法，上传过程中会频繁调用该方法
function progressFunction(evt) {
    var progressBar = document.getElementById("progressBar");
    var percentageDiv = document.getElementById("percentage");
    // event.total是需要传输的总字节，event.loaded是已经传输的字节。如果event.lengthComputable不为真，则event.total等于0
    if (evt.lengthComputable) { //
        progressBar.max = evt.total;
        progressBar.value = evt.loaded;
        percentageDiv.innerHTML = Math.round(evt.loaded / evt.total * 100) + "%";
    }
    var time = document.getElementById("time");
    var nt = new Date().getTime(); //获取当前时间
    var pertime = (nt - ot) / 1000; //计算出上次调用该方法时到现在的时间差，单位为s
    ot = new Date().getTime(); //重新赋值时间，用于下次计算
    var perload = evt.loaded - oloaded; //计算该分段上传的文件大小，单位b
    oloaded = evt.loaded; //重新赋值已上传文件大小，用以下次计算
    //上传速度计算
    var speed = perload / pertime; //单位b/s
    var bspeed = speed;
    var units = 'b/s'; //单位名称
    if (speed / 1024 > 1) {
        speed = speed / 1024;
        units = 'k/s';
    }
    if (speed / 1024 > 1) {
        speed = speed / 1024;
        units = 'M/s';
    }
    speed = speed.toFixed(1);
    //剩余时间
    var resttime = ((evt.total - evt.loaded) / bspeed).toFixed(1);
    time.innerHTML = '，速度：' + speed + units + '，剩余时间：' + resttime + 's';
    if (bspeed == 0) time.innerHTML = '上传已取消';
}

// 表格方法
function getIdSelections() {
    return $.map($table.bootstrapTable('getSelections'), function (row) {
        return row.id
    })
}

function showModel(id) {
    $(id).modal('show');
}

function columnConfig(columnsArr, rules) {
    // 格式 column option
    let columnsOption = Object.keys(columnsArr || [])
        .map(function (item, index) {

                return {
                    title: rules.titleConfig[index],
                    field: rules.isRead ?  'voteItem.' + item :item,
                    align: 'center',
                    valign: 'middle'
                }
            }
        )
        .filter(function (item) {
            if (item.title && item.title.indexOf(rules.hideKeys.join('|')) !== -1
            ) {
                return false
            }
            return item
        })
    // ## 是否是编辑页面
    if (window.location.href.indexOf('editItem') === -1) {

        switch (rules.rules) {
            case '1':
                $._voteArr = []

                columnsOption.push({
                    title: '投票',
                    field: rules.isRead ? "agreeFlag" : "total",
                    align: 'center',
                    valign: 'middle',
                    events: window.operateEvents,
                    formatter: function (value, row, index) {

                        return [
                            `<a class="${rules.isRead ? '' :'castVote'}" href="javascript:void(0)" title="vote">`,
                            rules.isRead && row.agreeFlag === '1'? '已投' :'投票',
                            '</a>'
                        ].join('')
                    }
                })
                break;
            case '2':
                columnsOption.push({
                    title: '排序',
                    field: "SerialNumber",
                    align: 'center',
                    valign: 'middle',
                    events: window.operateEvents,
                    formatter: function (value, row, index) {
                        if(rules.isRead) {
                            return '<a>' + row.order +'</a>'
                        }
                        return [
                            '<select class="form-control selectpicker selectVote" data-live-search="true" name="orgid" >',
                            '<option value="">请选择</option>',
                            '</select>'
                        ].join('')
                    }
                })
                break;
            default:
                $._fraction = []
                columnsOption.push({

                    title: '分数',
                    field: "SerialNumber",
                    align: 'center',
                    valign: 'middle',
                    events: window.operateEvents,
                    formatter: function (value, row, index) {
                        if(rules.isRead) {
                            return [
                                `<input class="form-control voteInput" readonly value="${row.score}" data-live-search="true" name="orgid" >`,
                            ].join('')
                        }
                        return [
                            '<input class="form-control voteInput" data-live-search="true" name="orgid" >',
                        ].join('')
                    }
                })
                ;
        }
    }

    // columns 添加序号表头
    columnsOption.unshift({
        title: '序号',
        field: "voteItemId",
        align: 'center',
        valign: 'middle'
    })
    columnsOption.unshift({checkbox: rules.checkbox})
    return columnsOption
}

/**
 * 初始化表格
 */
function initTable(options) {
    //hasData
    $table.bootstrapTable('destroy').bootstrapTable({
        height: 550, // 初始高度
        clickToSelect: true,
        minimumCountColumns: 2,
        idField: 'id',
        sidePagination: 'server',
        // 列操作栏
        columns: columnConfig(options.data[0], {
            isRead: options.hasData && options.hasData.length > 0 ? true : false,
            rules: options.rules,
            hideKeys: ['item'],
            titleConfig: options.titleConfig,
            checkbox: options.checkbox
        }),
        data: options.hasData && options.hasData.length > 0 ? options.hasData : options.data
    })
}

/**
 * 格式化 data 数据
 */
function resetData(data, ruleOption) {

    return data.map(function (item) {
        var obj = {}
        for (let key in item
            ) {
            obj[key] = item[key]
        }
        if (ruleOption.rules === '1') {
            obj['votes'] = ''
        } else {
            obj['SerialNumber'] = ''
        }
        return obj
    })
}

/**
 * delSelect
 * @param data { Object } 表格所有数据
 * @param val { number | string } 当前选中排序值
 * @param allOrder { array<number> } 所有剩余排序值
 * @param delOrder { array<number> } 已使用的排序值
 * @param preValue { number | string } change 之前下拉框的selected值
 * @description 排序下拉操作    uservote.html 92
 */
function columnOperation(data, val, delOrder, allOrder, preValue) {
    delOrder = data
        .filter(function (item, index, arr) {
            if (item.SerialNumber) {
                return item;
            }
        })
        .map(function (item, index, arr) {
            return item.SerialNumber
        })

        if (val == '') {
            allOrder.push(Number(preValue))
            return
        } else {
            if(allOrder.indexOf(Number(preValue)) === -1 && Number(preValue) !== 0) {
                allOrder.push(Number(preValue))
            }
            allOrder.splice(allOrder.indexOf(Number(val)), 1)
            return;
        }

        allOrder.splice(allOrder.indexOf(Number(val)), 1)
}

function initDataItem(item, type) {
    let obj = {}
    if(type === 'castVote') {
        obj.agreeFlag = '1'
    }
    console.log(item)
    obj.score = item.score ? item.score : ''
    obj.order = item.SerialNumber
    obj.voteItem = {}
    for (let key in item) {
        obj.voteItem[key] = item[key]
    }
    obj.item = item.item
    obj.order = item.SerialNumber
    return obj
}

function request(obj) {

    $.ajax({
        url: obj.url, //     //请求的url地址
        headers: {
            'Content-Type': 'application/json;charset=utf8'
        },
        // dataType:"json",   //返回格式为json
        async: true,//请求是否异步，默认为异步，这也是ajax重要特性
        data: obj.data,    //参数值
        type: "POST",   //请求方式
        beforeSend: function () {
            //请求前的处理
            // xhr.setRequestHeader("content-Type:'1333333333'");
        },
        success: function (req) {
            //请求成功时处理

        },
        complete: function (success) {
            if (success.responseText === 'success') {
                // window.location.href = '/vote/2'
                alert('投票成功')
            }
            //请求完成的处理
            console.log(success)
        },
        error: function (error) {
            //请求出错处理
            console.log(error)
        }
    });


}