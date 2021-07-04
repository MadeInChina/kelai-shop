//发动邮件验证码
$(function () {
    if($.cookie("mailcaptcha") && $.cookie("mailcaptcha") > 0){
        var count = $.cookie("mailcaptcha");
        var btn = $('#sendMailCode');
        btn.text(count+'s').attr('disabled',true).css('cursor','not-allowed');
        var resend = setInterval(function(){
            count--;
            if (count > 0){
                btn.text(count+'s').attr('disabled',true).css('cursor','not-allowed');
                $.cookie("mailcaptcha", count, {path: '/', expires: (1/86400)*count});
            }else {
                clearInterval(resend);
                btn.text("发送验证码").removeClass('disabled').removeAttr('disabled');
                btn.css('cursor','pointer');
            }
        }, 1000);
    }
   $('#sendMailCode').bind('click',function () {
       var btn = $(this);
       if(btn.text() != '发送验证码' || $.cookie("mailcaptcha") > 0){
           return;
       }
       var count = 60;
       var email = $('#emailAddress').val();
       var reg = /^([a-z0-9A-Z]+[-|\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\.)+[a-zA-Z]{2,}$/;
       if (!reg.test(email)){
           toastr.error('请输入有效的邮件地址','提示');
           return;
       }else {
           $.ajax({
               url : '/mailcode/send',
               data:{
                   email : email,
                   csrfToken : params.csrfToken
               },
               type:'POST',
               success:function (response) {
                   if (response.status === 'SUCCESS') {
                       toastr.success('邮件验证码发送成功','提示');
                       btn.attr('disabled',true).css('cursor','not-allowed');
                       btn.text(count+"s");
                       var resend = setInterval(function(){
                           count--;
                           if (count > 0){
                               btn.text(count+"s");
                               $.cookie("mailcaptcha", count, {path: '/', expires: (1/86400)*count});
                           }else {
                               clearInterval(resend);
                               btn.text("发送验证码").removeAttr('disabled');
                               btn.css('cursor','pointer');
                           }
                       }, 1000);
                   }else {
                       toastr.error('邮件验证码发送失败','提示');
                   }
               },
               error:function () {
                   toastr.error('邮件验证码发送失败','提示');
               }
           });
       }

   }) ;
});