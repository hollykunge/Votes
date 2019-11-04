'use strict';
var unpassMap = new Map();
/**
 * columnConfig
 * @param rules { Object } 格式化后的excelHeader
 * @returns {*}
 */
function columnConfig(rules, callback) {
    // 根据 excelHeader 修改头部格式
    var columnsOption = rules.titleConfig
        .map(function (item, index) {
            if (rules.isRead) {
                // ...
            }
            return Object.assign(
                {},
                item,
                {
                    align: 'center',
                    valign: 'middle'
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
            return index + 1;
        }
    })
    callback(rules, columnsOption)
    return columnsOption
}

/**
 * 配置table column参数
 * @param rules
 * @param columnsOption
 */
function configOperation(rules, columnsOption) {
}

/**
 * 初始化table
 * @param options { Object }
 */
function initTable(options) {
    // 根据“通过系数”判断，不能通过的行，背景色显示红色
    if (options.rules === '1') {
        options.data.forEach(function(item){
            if (item.agreeRulePassFlag == undefined) {
                unpassMap.set(item.voteItemId,1)
            }
        })
    }
    $table.bootstrapTable('destroy').bootstrapTable({
        url: options.url,
        clickToSelect: options.clickToSelect,
        minimumCountColumns: 2,
        idField: 'id',
        sidePagination: 'server',
        rowStyle: options.rowStyle,
        // 列操作栏
        columns: columnConfig({
            isRead: options.hasData && options.hasData.length > 0 ? true : false,
            rules: options.rules,
            titleConfig: options.titleConfig,
            operateCol: options.operateCol,
            operateColFormat: options.operateColFormat
        }, configOperation),
        data: options.hasData && options.hasData.length > 0 ? options.hasData.map(function (item) {
            return Object.assign({}, item, item.voteItem, {order: item.order, item: item.item})
        }) : options.data.map(function (item) {
            return Object.assign({}, item, {item: options.data[0].item})
        }),
        detailView: true,
        detailViewIcon: true,
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
        }
    })
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
var colorMap = new Map();
/**
 * 对重复投票结果做标记
 */
function handleRepeatData() {
    var randowColor = ['#2789ff' ,'#52b1ff', '#7ac8ff', '#a3dcff', '#e6f6ff'];
    // 第一次循环，结果计数 (key:score,value:出现次数)
    var countMap = new Map();
    for(var i=0; data.length>i; i++){
        var score = data[i][resultName];
        var count = countMap.get(score)
        if (count > 0) {
            count = count + 1;
            countMap.set(score,count);
        } else {
            countMap.set(score,1);
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
// 设置相同结果的行样式
function rowStyle(row, index) {
    // 根据“通过系数”判断，不能通过的行，背景色显示红色
    if (unpassMap.get(row.voteItemId) === 1) {
        return {css:{'background-color': '#FF4040'}}
    }
    if (colorMap.get(row[resultName])){
        return {css:{'background-color':colorMap.get(row[resultName])}}
    }
    return {}
}
