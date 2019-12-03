'use strict';
var submitcount = 0;// 提交次数
var ot;
var oloaded;
var unpassMap = new Map();
var templateOption = {
    haveVoted: [
        '<div class="have-voted">',
        '<span>',
        '<span class="badge badge-info mr15">已投</span>',
        '</span>',
        '<span data-action="cancel">取消</span>',
        '</div>'
    ].join(''),
    haveVotedRead: [
        '<span class="badge badge-success">已投</span>',
        '<span class="badge badge-secondary">未投</span>'
    ]
};

// 弹窗显示
function showImportModel() {
    // 添加约束：如果使用上一轮投票项，限制导入新的投票项
    // 判断依据：如果有结果列即为使用上一轮投票项
    var tableData = $table.bootstrapTable('getData');
    var disImport = tableData.some(function (item) {
        if (item.parentStatisticsNum != null || item.parentStatisticsToalScore != null || item.parentStatisticsOrderScore != null) {
            return true;
        }
    })
    if (disImport) {
        $('#importExcel').attr('disabled', 'disabled');
        showInfo('提醒：由于已使用上一轮投票项，不能导入新的投票项');
        return;
    }
    $('#importModel').modal('show');
    // 由于未刷新页面，需要对上一次上次内容初始化
    document.getElementById("time").innerHTML = '<div></div>';
    document.getElementById("percentage").innerHTML = '<div></div>';
    document.getElementById("progressBar").value = 0;
}

// 导入提示
function showAddModel() {
    if (titleConfig == null) {
        $('body').message({
            message: '请先操作导入',
            type: 'warning'
        })
        return;
    }
    $('#addModel').modal('show');
    $('#formRow').empty();
    var inputdiv = "";
    for (var i = 0; i < titleConfig.length; i++) {
        inputdiv = inputdiv + "<div class='form-group col-md-6'>";
        inputdiv = inputdiv + "<label>" + titleConfig[i].title + "</label><input class='form-control-sm'/>";
        inputdiv = inputdiv + "</div>";
    }
    $('#formRow').append(inputdiv);
}

//定义按钮事件
function excelImport(voteId, fn) {
    ot = new Date().getTime(); //设置上传开始时间
    oloaded = 0; //设置上传开始时，以上传的文件大小为0
    var fileObj = document.getElementById("file").files[0]; // js 获取文件对象
    if (!fileObj) {
        alert('请选择一个Excel文件')
        return;
    }
    $('#copyCode').attr('disabled', 'disabled');
    $('#copyCode').find('.hide').removeClass('hide')
    var url = window.location.origin + "/item/import"; // 接收上传文件的后台地址
    var form = new FormData(); // FormData 对象
    if (form) {
        form.append("file", fileObj); // 文件对象
    }
    var xhr = new XMLHttpRequest(); // XMLHttpRequest 对象
    xhr.open("post", url, true); //post方式，url为服务器请求地址，true 该参数规定请求是否异步处理。
    xhr.onload = fn; //请求完成
    xhr.onerror = uploadFailed; //请求失败
    xhr.upload.onprogress = progressFunction; //【上传进度调用方法实现】
    xhr.upload.onloadstart = function () { //上传开始执行方法
        ot = new Date().getTime(); //设置上传开始时间
        oloaded = 0; //设置上传开始时，以上传的文件大小为0
    };
    xhr.setRequestHeader("itemId", voteId);
    xhr.send(form); //开始上传，发送form数据
}

// //上传成功响应
// function uploadComplete(evt) {
//     if (evt.target.status) {
//         setInterval(function () {
//             window.location.reload();
//         }, 1000)
//
//     } else {
//         $('body').message({
//             message: '上传失败',
//             type: 'danger'
//         })
//     }
//
// }

//上传失败
function uploadFailed(evt) {
    $('body').message({
        message: '上传失败',
        type: 'danger'
    })
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

/**
 * showModel
 * @param id
 */
function showModel(id) {
    $(id).modal('show');
}


/**
 * columnConfig
 * @param rules { Object } 格式化后的excelHeader
 * @returns {*}
 */
function columnConfig(rules, callback) {
    // 根据 excelHeader 修改头部格式
    var columnsOption = rules.titleConfig
        .map(function (item, index) {
            return Object.assign(
                {},
                item,
                {
                    // field: rules.isRead ? 'voteItem.' + item.field : item.field,
                    align: 'center',
                    valign: 'middle',
                    cellStyle: cellStyle,
                    formatter: function (value, row, index) {
                        //格式化条件
                        if (row.fraction) {
                            return ''
                        }
                        return value
                    }
                }
            )

        })
        .filter(function (item, index) {
            if (index >= 6) {
                return false
            }
            return item
        })
    // 添加 序号 表头与 voteItemId 关联
    columnsOption.unshift({
        title: '序号',
        field: "voteItemId",
        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index) {
            if (value.contentTitle.indexOf("(序号") != -1) {
                return index
            }
            return index + 1

            // if (value) {
            //     return value
            // }
            // return ''
        },
        cellStyle: cellStyle
    })
    callback(rules, columnsOption, rules.resultName, rules.fraction)
    return columnsOption
}

/**
 * 配置table column参数
 * @param rules
 * @param columnsOption
 */
function configOperation(rules, columnsOption, resultName, fraction) {
    // 是否是编辑页面
    if (window.location.href.indexOf('userVote') !== -1) {
        switch (rules.rules) {
            case '1':
                $._voteArr = []
                columnsOption.push({
                    title: '投票',
                    field: rules.isRead ? "agreeFlag" : "total",
                    width: 150,
                    align: 'center',
                    valign: 'middle',
                    events: window.operateEvents,
                    formatter: function (value, row, index) {
                        var className = rules.isRead ? (row.agreeFlag == '1' ? '' : 'normal') : 'castVote'
                        return [
                            '<a class="btn btn-link btn-sm ' + className + '" href="javascript:void(0)" data-action="vote" title="vote">',
                            rules.isRead ? (row.agreeFlag == '1' ? templateOption.haveVotedRead[0] : templateOption.haveVotedRead[1]) : '投票',
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
                    cellStyle: cellStyle,
                    formatter: function (value, row, index) {
                        var attribute = ''
                        if (rules.isRead) {
                            return '<a>' + row.order + '</a>'
                        }
                        if (Object.keys(fraction).length > 0) {
                            attribute = 'data-select="' + fraction[row[resultName]] + '"'
                        }
                        return [

                            '<select ' + attribute + ' class="form-control-sm selectpicker selectVote" data-live-search="true" name="orgid" >',
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
                    cellStyle: cellStyle,
                    events: window.operateEvents,
                    formatter: function (value, row, index) {
                        if (rules.isRead) {
                            return [
                                '<input type="number" class="form-control input-group-sm voteInput" readonly value="' + row.score + '" data-live-search="true" name="orgid" >',
                            ].join('')
                        }
                        return [
                            '<input type="number" class="form-control input-group-sm voteInput" data-live-search="true" name="orgid" onKeypress="return (/[\\d]/.test(String.fromCharCode(event.keyCode)))">',
                        ].join('')
                    }
                })
                ;
        }
    }
    if (rules.operateCol) {
        columnsOption.push({
            title: '操作',
            cellStyle: cellStyle,

            field: "Button",
            align: 'center',
            valign: 'middle',
            events: window.operateEvents,
            formatter: rules.operateColFormat
        })
    }

}

/**
 * 初始化table
 * @param options { Object }
 */
function initTable(options) {
    var bootStrapDataOption = options.hasData && options.hasData.length > 0
        ?
        options.hasData.map(function (item) {
            return Object.assign({}, item, item.voteItem, {order: item.order, item: item.item})
        }) :
        options.data.map(function (item, index) {
            return Object.assign({}, item, {item: options.data[0].item, index: index})
        });

    // 根据分数做分类
    var fraction = {}
    $(bootStrapDataOption).each(function (index, item) {
        // 不存在序列，此值不存在内容
        if (!item[options.resultName]) {
            return
        }
        // 存在则赋值
        if (fraction[item[options.resultName]]) {
            fraction[item[options.resultName]] = fraction[item[options.resultName]].concat(index * 1 + 1)
            return
        }
        fraction[item[options.resultName]] = [].concat(index * 1 + 1)
    })
    bootStrapDataOption = orderDataTest(bootStrapDataOption, Object.keys(fraction), options.resultName, Object.keys(fraction).length)
    // 根据“通过系数”判断，不能通过的行，背景色显示红色
    if (options.rules === '1') {
        bootStrapDataOption.forEach(function (item) {
            if (item.agreeRulePassFlag == undefined) {
                unpassMap.set(item.voteItemId, 1)
            }
        })
    }
    // parentStatisticsToalScore
    $table.bootstrapTable('destroy').bootstrapTable({
        url: options.url,
        clickToSelect: options.clickToSelect,
        minimumCountColumns: 2,
        idField: 'id',
        sidePagination: 'server',
        // 列操作栏
        columns: columnConfig({
            isRead: options.hasData && options.hasData.length > 0 ? true : false,
            rules: options.rules,
            titleConfig: options.titleConfig,
            operateCol: options.operateCol,
            operateColFormat: options.operateColFormat,
            resultName: options.resultName,
            fraction: fraction
        }, configOperation),
        rowStyle: options.rowStyle,
        data: bootStrapDataOption,
        rowAttributes: function (row, index) {
            // 条件
            // if (row.fraction) {
            //     return {
            //         class: 'cell'
            //     }
            // }
        },
        detailView: true,
        detailViewIcon: true,
        detailFilter: function (index, row, element) {
            if (row.fraction) {
                return false
            }
            return true
        },
        detailFormatter: function (index, row, element) {

            var html = [
                '<div class="card">',
                '<div class="card-body">',
            ]
            $.each(row, function (key, value) {
                if (!value) {
                    return;
                }
                var title = options.titleConfig.filter(function (item, index) {
                    return item.field === key
                })[0]
                if (title) {
                    if (value.indexOf && value.indexOf(',') === -1) {
                        html.push('<p><b style="display: inline-block; margin-right: 10px;">' + title.title + ':</b> ' + value.split(',')[0] + '</p>')
                    } else {
                        if (typeof value === 'number') {
                            html.push(
                                [
                                    '<b style="display: inline-block; margin-right: 10px;">',
                                    '结果',
                                    ':</b> ',
                                    value,
                                    '</p>'
                                ].join('')
                            )
                        } else {
                            html.push(
                                [
                                    '<p>',
                                    '<b style="display: inline-block; margin-right: 10px;">',
                                    title.title,
                                    ':</b> ',
                                    value.split(',')[0],
                                    '</p>',
                                    '<p>',
                                    '<b style="display: inline-block; margin-right: 10px;">',
                                    '其他',
                                    ':</b> ',
                                    value.split(',').slice(1).join(' '),
                                    '</p>'
                                ].join('')
                            )
                        }

                    }
                }
            })
            html.concat([
                '</div>',
                '</div>'
            ])
            return html.join('')
        },
        onLoadSuccess: function () {
            console.log('success')
        }
    })

    $(bootStrapDataOption).each(function (index, item) {
        if (item.fraction) {
            $table.bootstrapTable('mergeCells', {index: index, colspan: 9})

            $table.find('tbody').find('tr').eq(index).find('td').eq(0).text(item.contentTitle).css({
                textAlign: 'center',
                verticalAlign: 'middle',
                // fontWeight: 'bold',
                fontSize: '18px'
            })
        }
    })

    $(bootStrapDataOption).each(function (index, item) {
        if (item.fraction) {
            $table.bootstrapTable('mergeCells', {index: index, colspan: 9})

            $table.find('tbody').find('tr').eq(index).find('td').eq(0).text(item.contentTitle).css({
                textAlign: 'center',
                verticalAlign: 'middle',
                // fontWeight: 'bold',
                fontSize: '18px'
            })
        }
    })

}

/**
 * 格式化 data 数据
 * @param data
 * @param ruleOption
 * @returns {*}
 */
function resetData(data, ruleOption) {

    return data.map(function (item) {
        var obj = {}
        for (var key in item
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
        if (allOrder.indexOf(Number(preValue)) === -1 && Number(preValue) !== 0) {
            allOrder.push(Number(preValue))
        }
        allOrder.splice(allOrder.indexOf(Number(val)), 1)
        return;
    }

    allOrder.splice(allOrder.indexOf(Number(val)), 1)
}

/**
 * 格式化data 的 item 值
 * @param item
 * @param type
 */
function initDataItem(item, type) {
    var obj = {}
    if (type === 'castVote') {
        obj.agreeFlag = '1'
    }
    obj.score = item.score ? item.score : ''
    obj.order = item.SerialNumber
    obj.voteItem = {}
    for (var key in item) {
        obj.voteItem[key] = item[key]
    }
    obj.item = item.item
    obj.order = item.SerialNumber
    return obj
}

/**
 * 请求
 * @param obj
 */
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
            // disabled="disabled"
            $('button[type=submit]').attr('disabled', 'disabled');
            $('button[type=submit]').find('.spinner-border').removeClass('hide')
            $('#loading').modal('show');
        },
        success: function (req) {
            // $('button[type=submit]').removeAttr('disabled');
            // $('button[type=submit]').find('.hide').addClass('hide')

            //请求成功时处理
            if (req.status == 200) {
                $('body').message({
                    message: '提交成功！正在跳转，请稍等',
                    type: 'success'
                })
                reloadPage()
                return
            }
            $('body').message({
                message: req.message || JSON.stringify(req),
                type: 'danger'
            })
        },
        complete: function (success) {
            //请求完成的处理
            $('button[type=submit]').removeAttr('disabled');
            $('button[type=submit]').find('.spinner-border').addClass('hide')
            $('#loading').modal('hide');
        },
        error: function (error) {
            //请求出错处理
            if (error) {
                $('body').message({
                    message: '错误,响应状态码: ' + error.status,
                    type: 'danger'
                })
                // reloadPage()
            }
        }
    });
}

function reloadPage() {

    var timer = setTimeout(function () {
        timer = null
        window.location.reload()
    }, 500)
}

/**
 * 数组排序
 * @param arr
 * @param callback
 * @param callbackFun
 * @returns {*}
 */
function sorts(arr, callback, callbackFun) {
    if (callback) {
        arr = [].concat(callback(arr))
    }
    for (var i = 0; i < arr.length; i++) {
        for (var j = 0; j < arr.length; j++) {
            if (arr[i] < arr[j]) {
                var temp = arr[j]
                arr[j] = arr[i]
                arr[i] = temp
            }
        }
    }

    if (callbackFun) {
        return callbackFun(arr)
    }
    return arr
}

/**
 * 设置table标题
 * @param titleConfig
 * @returns {*}
 */
function initTitleConfig(titleConfig) {
    var titleObj = JSON.parse(titleConfig)
    titleConfig = sorts(
        Object.keys(titleObj),
        function (arr) {
            return arr.map(function (item) {
                return item.replace('attr', '') * 1
            })
        },
        function (arr) {
            return arr.map(function (item) {
                return 'attr' + item
            })
        }
    )
        .map(function (item) {
            return {
                title: titleObj[item],
                field: item
            }
        })

    return titleConfig
}

/**
 * 是否满足提交条件
 * @param val
 * @param min
 * @param max
 * @returns {boolean}
 */
function isPass(val, min, max) {
    // 判断input数字与整数
    if (isNaN(val * 1) || val % 1 !== 0) {
        $('body').message({
            message: '请输入正确格式数字.',
            type: 'danger'
        })
        return
    }
    if (max * 1 >= val * 1 && val * 1 >= min * 1) {
        return true
    }
    $('body').message({
        message: '分数必须在 ' + min + ' 与 ' + max + ' 之间。',
        type: 'danger'
    })

    return false
}

/**
 * 过滤已存在的对象
 * @param data
 * @param hasItemIndex
 * @param keys
 * @returns {any[]}
 */
function delHasItem(data, hasItemIndex, keys) {
    var arr = [].concat(data)
    var hasArr = []
    for (var i = 0; i < hasItemIndex.length; i++) {
        arr = arr.filter(function (item) {
            return item.voteItemId !== hasItemIndex[i]
        })
    }
    return arr.map(function (el) {
        var obj = {}
        obj[keys] = '0'
        if (keys == 'score') {
            return Object.assign({}, initDataItem(el), obj)
        } else {
            return Object.assign({}, initDataItem(el, 'castVote'), obj)
        }
        // return initDataItem(el, 'castVote', '0')
    })
}

/**
 * 弹窗
 * @param object
 */
$.fn.message = function (options) {
    options = options || {};
    if (typeof options === 'string') {
        options = {
            message: options,
            type: options.type || 'success'
        };
    }
    var template = [
        '<div class="alert alert-dismissible fade show alert alert-' + (options.type || 'success') + ' position_alert position_message"  data-message="message" role="alert">',
        '<span class="mr30">',
        options.message,
        '</span>',
        '<button type="button" class="close closeButton" data-dismiss="alert" aria-label="Close">',
        '<span aria-hidden="true">&times;</span>',
        '</button>',
        '</div>',
    ].join('')
    if ($('.position_message').length > 0 && $('.position_message').data('message') === "message") {
        $('.position_message').replaceWith($(template))
        return
    }
    var userOnClose = options.onClose;
    $(template).appendTo('body')
    // $('<div data-message="message" class="alert alert-' + (options.type || 'success') + ' position_alert position_message" aria-label="Close">' + options.message + '  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>').appendTo($('body'))

}

function PassRules(option, minOrMax) {
    if (minOrMax === 'max') {
        // 最大票数已经用完
        if (option.max * 1 - (option.currentVotes * 1 + 1) <= -1) {
            return false
        }
    }

    if (minOrMax === 'min') {
        // 未达到最小票数
        if (option.currentVotes - option.min < 0) {
            $('body').message({
                message: ['最少投 ', $._voteArr[0].item.agreeMin, ' 票.'].join(''),
                type: 'danger'
            })
            return false
        }

        return true
    }
    return 'maxPass'

}

function unique(arr) {
    for (var i = 0; i < arr.length; i++) {
        for (var j = i + 1; j < arr.length; j++) {
            if (arr[i] == arr[j]) {         //第一个等同于第二个，splice方法删除第二个
                arr.splice(j, 1);
                j--;
            }
        }
    }
    return arr;
}

function tips(el, childName, requestBody, urlOption) {
    el
        .show()
        .find(childName)
        .html('确定后将无法修改数据，确定提交吗？')
    btnAction(requestBody, urlOption);
}

function btnAction(requestBody, urlOption) {
    var url = window.location.href
    var arr = url.split('/');
    $('.submit-fraction')
        .off('click')
        .on('click', function () {
            $(this).parents('.modal').hide()
            // 打分存在不符合范围的分数
            var noPass = requestBody.filter(function (item) {
                return item.ispass === false
            })
            if (noPass.length > 0) {
                $('body').message({
                    type: 'danger',
                    message: '填写内容不符合规则，请正确填写.'
                })
                return
            }
            request({
                url: window.location.origin + "/userVote/add/" + urlOption,
                data: JSON.stringify(requestBody)
            })
        })
    $('.close-btn')
        .off('click')
        .on('click', function () {
            $('.modal').hide();
        })
}

var colorMap = new Map();

/**
 * 对重复投票结果做标记
 */
function handleRepeatData() {
    var randowColor = ['#2789ff', '#52b1ff', '#7ac8ff', '#a3dcff', '#e6f6ff'];
    // 第一次循环，结果计数 (key:score,value:出现次数)
    var countMap = new Map();
    for (var i = 0; data.length > i; i++) {
        var score = data[i][resultName];
        var count = countMap.get(score)
        if (count > 0) {
            count = count + 1;
            countMap.set(score, count);
        } else {
            countMap.set(score, 1);
        }
    }
    // 第二次循环，出现次数超过一次且分数不为0的
    var i = 0;
    for (var [key, value] of countMap) {
        /*<![CDATA[*/
        if (value > 1 && key != 0 && key != undefined) {
            colorMap.set(key, randowColor[i]);
            i++;
        }
        /*]]>*/
    }
}

/**
 * 序列分类
 * @param data
 * @param orderData
 * @param keyName
 * @param sequence
 * @returns {*}
 */
function orderDataTest(data, orderData, keyName, sequence) {

    for (var i = 0; i < orderData.length; i++) {
        for (var j = 0; j < data.length; j++) {
            if (orderData[i] == data[j][keyName]) {
                data.splice(j, 0, {
                    fraction: orderData[i],
                    contentTitle: '(第' + sequence + '序列)'
                })
                orderData.splice(i, 1)

                return orderDataTest(data, orderData, keyName, --sequence)

            }
        }
    }
    return data
}

// 设置相同结果的行样式
function rowStyle(row, index) {
    // 根据“通过系数”判断，不能通过的行，背景色显示红色
    if (unpassMap.get(row.voteItemId) === 1) {
        return {css: {'background-color': '#FF4040'}}
    }
    if (colorMap.get(row[resultName])) {
        return {css: {'background-color': colorMap.get(row[resultName])}}
    }
    return {}
}

/**
 * 表格行样式
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function cellStyle(value, row, index) {
    if (row.fraction) {
        return {
            classes: '',
            css: {
                background: '#fff'

            }
        }
    }
    return false
}

/**
 * 分别为各个序列去掉已存在的序号
 * @param dataArr
 * @param selectArr
 * @returns {string[]}
 */
function removal(dataArr, selectArr) {
    selectArr = selectArr.split(',')
    $.each(dataArr, function (index, item) {
        if (selectArr.indexOf(item.SerialNumber) !== -1) {
            selectArr.splice(selectArr.indexOf(item.SerialNumber), 1)
        }
    })
    return selectArr
}
