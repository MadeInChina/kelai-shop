function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return decodeURI(r[2]); return null; //返回参数值
}
window.onbeforeunload  = function () {
    $.cookie('FIRST_LOAD_RECORDS', null);
};
function ajaxModalContentLoadCompleteIntercept(modalTitle,func){
    $.ajaxSetup({
        complete:function (XMLHttpRequest, textStatus) {
            if(XMLHttpRequest.responseText && XMLHttpRequest.responseText.indexOf(modalTitle) > 0){
                var ele = $(XMLHttpRequest.responseText);
                var myModalLabel = ele.find('#myModalLabel');
                if (myModalLabel.length === 1){
                    if($.trim(myModalLabel.text()) === modalTitle ){
                       if (func){
                           func();
                       }
                    }
                }
            }
        }
    });
}