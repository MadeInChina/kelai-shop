//<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=您的密钥"></script>
//接口文档地址：http://lbsyun.baidu.com/jsdemo.htm#c2_5
//获取定位名称
function isWxEnv(){
    //window.navigator.userAgent属性包含了浏览器类型、版本、操作系统类型、浏览器引擎类型等信息，这个属性可以用来判断浏览器类型
    var ua = window.navigator.userAgent.toLowerCase();
    //通过正则表达式匹配ua中是否含有MicroMessenger字符串
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        return true;
    }else{
        return false;
    }
}
function storeLocation () {
    $('.city-stores-wrapper').remove();
    $('#storeModal').modal('show');
    $('#loading').show();
    //如果是微信环境，使用微信定位
    if (isWxEnv()){
        getWxConfig({
           success : locationByWxJsApi,
           error : locationByCity
        });
       
    } else {
        //非微信环境，使用浏览器定位功能
        if (navigator.geolocation) {
            //浏览器支持geolocation
            navigator.geolocation.getCurrentPosition(onSuccess, onError);
        } else {
            //浏览器不支持geolocation，使用ip定位
            //alert('您的浏览器不支持地理位置定位');
            locationByCity();
        }
    }
}
function getWxConfig(options) {
    var url = encodeURIComponent(window.location.href);
    $.ajax({
        url :'/wx/config?url=' + url,
        type:'GET',
        success:function (wxInfo) {
            options.success(wxInfo);
        },
        error:function () {
            options.error();
        }
    });
}
function locationByCity() {

    function myFun(result){
        $.ajax({
            url :'/store/nearbyStoresByCurrentUserCity?city=' + result.name,
            type:'GET',
            success:function (response) {
                renderStores(response);
            },
            error:function () {
                console.log('根据ip获取附近门店失败');
            },
            complete :function () {
                $('#loading').hide();
            }
        });
    }
    var myCity = new BMap.LocalCity();
    myCity.get(myFun);
}
function locationByLatLng(latitude,longitude) {
    function myFun(result){
        var cityName = result.name;
        $.ajax({
            url :'/store/nearbyStoresByCurrentUserLngLat?longitude=' + longitude + '&latitude=' + latitude + '&city=' + cityName ,
            type:'GET',
            success:function (response) {
                renderStores(response);
            },
            error:function () {
                console.log('根据经纬度获取门店失败');
            },
            complete : function () {
                $('#loading').hide();
            }
        });
    }
    var myCity = new BMap.LocalCity();
    myCity.get(myFun);
}
function onSuccess(position){
    //返回用户位置
    //经度
    var longitude =position.coords.longitude;
    //纬度
    var latitude = position.coords.latitude;
    //alert('经度'+longitude+'，纬度'+latitude);
    locationByLatLng(latitude,longitude);

}
//失败时
function onError(error){
    locationByCity();
}
function locationByWxJsApi(wxInfo) {
    wx.config({
        debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: wxInfo.appId, // 必填，公众号的唯一标识
        timestamp:  wxInfo.timestamp, // 必填，生成签名的时间戳
        nonceStr: wxInfo.nonceStr, // 必填，生成签名的随机串
        signature: wxInfo.signature,// 必填，签名，见附录1
        jsApiList: [ 'checkJsApi', 'openLocation', 'getLocation'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    wx.checkJsApi({
        jsApiList: ['getLocation'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
        success: function(res) {
            if (res.checkResult.getLocation == false) {
                //alert('你的微信版本太低，不支持微信JS接口，请升级到最新的微信版本！');
                //return;
            }
        }
    });
    wx.error(function(res){
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        //alert("微信JS-SDK验证出错");
    });
    wx.ready(function(){
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
        wx.getLocation({
            type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
            success: function (res) {
                var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                locationByLatLng(latitude,longitude);
            },
            fail:function () {
                locationByCity();
            },
            complete:function () {
                
            }
        });
    });
    
}
function renderStores(stores) {
    console.log(stores);
    var html = template('tpl-store', {
        allCityStores : stores
    });
    $('#storeModalBody').append(html);
    for (var i=0;i<stores.length;i++){
        if (stores[i].currentUserCity){
            $('#cityKeyword').val(stores[i].city).trigger('keyup');
            break;
        }
    }
}
function filterStore() {
    var keyword = $.trim($('#cityKeyword').val());
    if (keyword == ''){
        $('.city-stores').show();
    }else {
        $('.city-stores').each(function () {
            var that = $(this);
            if (that.data('city').indexOf(keyword) >= 0 ||
                that.data('city-quanpin').indexOf(keyword) >= 0 ||
                that.data('city-jianpin').indexOf(keyword) >= 0){
                that.show();
            }else {
                that.hide();
            }
        });
    }
}
