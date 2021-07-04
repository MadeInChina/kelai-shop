
function checkMobile(){
    var mobile = $.trim($('input[id="customer.username"]').val());
    var exp = /^1\d{10}$/;
    return exp.test(mobile);
}
$(function () {
    if($.cookie("captcha") && $.cookie("captcha") > 0){
        var count = $.cookie("captcha");
        var btn = $('#btn-send-verification-code');
        btn.text(count+'s').attr('disabled',true).css('cursor','not-allowed');
        var resend = setInterval(function(){
            count--;
            if (count > 0){
                btn.text(count+'s').attr('disabled',true).css('cursor','not-allowed');
                $.cookie("captcha", count, {path: '/', expires: (1/86400)*count});
            }else {
                clearInterval(resend);
                btn.text("发送验证码").removeClass('disabled').removeAttr('disabled');
                btn.css('cursor','pointer');
            }
        }, 1000);
    }
    $('#btn-send-verification-code').bind('click',function () {
        var btn = $(this);
        if(btn.text() != '发送验证码' || $.cookie("captcha") > 0){
            return;
        }
        if(!checkMobile()){
            toastr.error('请输入有效的手机号','提示');
            return false;
        }

        var count = 60;
        var mobile = $.trim($('input[id="customer.username"]').val());
        //var csrfToken = $('input[name="csrfToken"]').val();
        $.ajax({
            url : '/sms/send-code',
            type:'POST',
            data:{
                mobile : mobile,
                csrfToken : params.csrfToken
            },
            success:function (res) {
                if (res.status === 'SUCCESS'){
                    toastr.success('验证码发送成功','提示');
                    btn.attr('disabled',true).css('cursor','not-allowed');
                    btn.text(count+"s");
                    var resend = setInterval(function(){
                        count--;
                        if (count > 0){
                            btn.text(count+"s");
                            $.cookie("captcha", count, {path: '/', expires: (1/86400)*count});
                        }else {
                            clearInterval(resend);
                            btn.text("发送验证码").removeAttr('disabled');
                            btn.css('cursor','pointer');
                        }
                    }, 1000);
                }else {
                    toastr.error('验证码发送失败','提示');
                }

            },
            error:function () {
                toastr.error('验证码发送失败','提示');
            }
        })
    });
});