/* Operations that deal with checkout */
(function(Checkout, $, undefined) {

    // Public properties

    /**
     * These options define the checkout modal view
     */
    Checkout.modalCheckoutOptions = {
        maxWidth    : 720,
        maxHeight   : 560,
        minHeight   : 360,
        position    : ['30px']
    };

    // Public Functions

    /**
     * Hides/shows fields on the checkout page
     */
    Checkout.initialize = function() {
        Checkout.showDynamicAddressForms();
        Checkout.togglePromoCreditOptions();
        Checkout.hideAllPaymentOptionDescriptions();

        if (!savedPaymentContainerIsHidden()) {
            selectActiveSavedPayment();
        }

        // Make checkboxes update the value of their "value holder"
        $(':checkbox').on('change', function() {
            var valueHolderName = $(this).attr('name').replace('-check', '');
            $("[name='" + valueHolderName + "']").val($(this).is(':checked'));
        });
        var positiveRegExp = /^[1-9]\d*$/;
        $('#provinceCode').on('change',function () {
            var that = $(this);
            if (!positiveRegExp.test(that.val())){
                return;
            }
            $('#cityCode').empty();
            $('#regionCode').empty();
            $('#streetCode').empty();
            var that = $(this);
            $('input[name="address.stateProvinceRegion"]').val(that.find("option:selected").text());
            $('#address.city').val('');
            $('#address.region').val('');
            $('#address.street').val('');
            //alert(that.find("option:selected").text());
            $('#cityCode').append('<option value=""></option>');
            $.ajax({
                url :'/administrativeRegion/cities?provinceCode=' + that.val(),
                type:'GET',
                success:function (cities) {
                    for(var i=0;i<cities.length;i++){
                        $('#cityCode').append('<option value="' + cities[i].code + '">' + cities[i].name + '</option>');
                    }
                }
            });

        });
        $('#cityCode').on('change',function () {
            var that = $(this);
            if (!positiveRegExp.test(that.val())){
                return;
            }
            $('#regionCode').empty();
            $('#streetCode').empty();
            $('input[name="address.city"]').val(that.find("option:selected").text());
            $('#address.region').val('');
            $('#address.street').val('');
            $('#regionCode').append('<option value=""></option>');
            $.ajax({
                url :'/administrativeRegion/regions?cityCode=' + that.val(),
                type:'GET',
                success:function (regions) {
                    for(var i=0;i<regions.length;i++){
                        $('#regionCode').append('<option value="' + regions[i].code + '">' + regions[i].name + '</option>');
                    }
                }
            });

        });
        $('#regionCode').on('change',function () {
            var that = $(this);
            if (!positiveRegExp.test(that.val())){
                return;
            }
            $('#streetCode').empty();

            $('input[name="address.region"]').val(that.find("option:selected").text());
            $('#address.street').val('');
            $('#streetCode').append('<option value=""></option>');
            $.ajax({
                url :'/administrativeRegion/streets?regionCode=' + that.val(),
                type:'GET',
                success:function (streets) {
                    for(var i=0;i<streets.length;i++){
                        //if (streets[i].regionCode == that.val()){
                        $('#streetCode').append('<option value="' + streets[i].code + '">' + streets[i].name + '</option>');
                        //}
                    }
                }
            });

        });
        $('#streetCode').on('change',function () {
            $('input[name="address.streetCommunity"]').val($(this).find("option:selected").text());
        });
    };

    /**
     * Handles the submission of checkout stage forms & updates the view
     *  with either validation errors or the next stage of the checkout flow
     * @param {element} $checkoutStageAction
     * @param {array} formData
     */
    Checkout.handleCheckoutStageSubmission = function($checkoutStageAction, formData) {
        var $checkoutStage = $checkoutStageAction.closest('.js-checkoutStage');
        var $form = $checkoutStage.find('form');

        if (formData === undefined) {
            formData = $form.serialize();
        }

        BLC.ajax({
            method: 'POST',
            url: $form.attr('action'),
            data: formData
        }, function(data) {
            /*if($form.attr('action') == '/checkout/singleship' && $(data).find('.text-danger').length == 0) {
                //Checkout.handlePaymentMethodCheck();
                checkoutReload(null);
            }else {*/
            clearWindowStateHistory();

            Checkout.updateCartPricingSummary();
            replaceCheckoutStages(data);
            Checkout.handlePaymentMethodCheck();
            //}

        });
    };

    Checkout.handlePaymentMethodCheck = function(){
        $('.check-payment').bind('click',function (e) {
            e.stopPropagation();
            $('.center-pill').removeClass('active');
            $('li[data-value="' + $(this).val() + '"]').addClass('active');
        });

        $('.payment_methods_ul').on('click','li',function (e) {
            e.stopPropagation();
            $('.center-pill').removeClass('active');
            $(this).addClass('active');
            $('#check' + $(this).data('value')).click();
        });
    };
    /**
     * Handles the submission of the payment checkout stage & updates the view
     *  with the next stage of the checkout flow
     *
     * @param {element} $checkoutStageAction
     */
    Checkout.handlePaymentCheckoutStageSubmission = function($checkoutStageAction) {
        var $paymentStage = $checkoutStageAction.closest('.js-checkoutStage');

        var paymentMethod = getPaymentMethod();
        if ('CreditCard' === paymentMethod) {
            if (shouldSaveNewPayment() || shouldUseCustomerPayment()) {
                savePaymentInfo($paymentStage)
            } else {
                // If we are not saving the payment method, then the payment data will be converted into a nonce.
                // Therefore, we want to avoid tokenizing the credit card data until the checkout is submitted
                // in the review stage. In the meantime, we can save the billing address.
                saveBillingAddressOnly($paymentStage)
            }
        } else if ('COD' === paymentMethod) {
            advanceFromPaymentToReviewStage();
            showReadOnlyPaymentMethod(paymentMethod);
        } else if ('PayPal' === paymentMethod) {
            if (isPayPalConfigComplete()) {
                collectPaymentInfoViaPayPal();
            }
        }else if('Alipay' === paymentMethod){
            goAlipay();
        }else if('WxPay' === paymentMethod){
            loadWxPayQrcode();
        }else {
            toastr.info('请选择支付方式','提示');
        }
    };
    Checkout.submitOrder = function () {
        $.ajax({
            url : '/checkout/submitOrder?csrfToken=' + params.csrfToken,
            type : 'POST',
            success : function (res) {
                if (res.status === 'SUCCESS'){
                    window.location.href = '/checkout/payment?orderId=' + res.data
                }else {
                    toastr.error(res.message);
                }
            },
            error:function () {
                toastr.error('订单提交失败');
            }
        });
    };
    function loadWxPayQrcode() {
        $('#wxpayQrcodeImg').attr('src','/wxpay/qrcode?orderId=' + $('#orderId').val());
        $('#wxpayQrcodeImg').imagesLoaded()
            .always(function (instance) { // always事件，在所有图片都加载完成（成功与否不论）时触发
                console.log('all images loaded');
            })
            .done(function (instance) { // done事件，在所有图片都加载成功时触发
                console.log('all images successfully loaded');
                $('#wxpayModal').modal('show');
                if (!wxpayCheckOrderStatusTimer){
                    wxpayCheckOrderStatusTimer = setInterval(checkOrderStatus,3000);
                }
            })
            .fail(function (instance) { // fail事件，在所有图片都加载完成，并且至少有一张图片加载失败时触发
                console.log('all images loaded, at least one is broken');
                toastr.error('微信支付下单失败', '提示');
            })
            .progress(function (instance, image) { // progress事件，在每一张图片加载完成时都触发一次
                var result = image.isLoaded ? 'loaded' : 'broken'; // 判断当前图片加载成功与否
                //image.img.src = image.isLoaded ? image.img.src : "../img/img.jpg";
                console.log('image is ' + result + ' for ' + image.img.src);
            });
    };
    function goAlipay(){
        $('.js-alipayPaymentMethodAction')[0].click();
    }
    var wxpayCheckOrderStatusTimer = null;
    function checkOrderStatus() {
        $.ajax({
            url :'/wxpay/checkOrderStatus',
            type:'GET',
            success:function (response) {
                if (response.orderStatus === 'PAID') {
                    clearInterval(wxpayCheckOrderStatusTimer);
                    window.location.href = '/wxpay/return?out_trade_no=' + response.unifiedOrderId;
                }
            },
            error:function () {
                
            }
        });
    }
    function wxPayRequest(appid,timeStamp,nonceStr,package,paySign,orderId){
        WeixinJSBridge.invoke('getBrandWCPayRequest',{
            "appId" : appid,
            "timeStamp" : timeStamp,
            "nonceStr" : nonceStr,
            "package" : package,
            "signType" : "MD5",
            "paySign" : paySign
        },function(res){
            WeixinJSBridge.log(res.err_msg);
            if(res.err_msg == "get_brand_wcpay_request:ok"){
                window.location.href = '/wxpay/return?orderId=' + orderId;
            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                /*layer.open({
                    content: '您已取消支付'
                    ,skin: 'msg'
                    ,time: 5 //2秒后自动关闭
                });*/
                toastr.info('您已取消支付', '提示');
            }else{
                /* layer.open({
                     content: '支付失败'
                     ,skin: 'msg'
                     ,time: 5 //2秒后自动关闭
                 });*/
                toastr.error('支付失败', '提示');
            }
        })
    }
    function wxpayUnifiedOrder() {
        $.ajax({
            url:'/wxpay/unifiedOrder?orderId=' + $('#orderId').val(),
            type:'GET',
            contentType:'application/json',
          /*  data:{
                csrfToken : params.csrfToken
            },*/
           /* beforeSend: function(request) {
                request.setRequestHeader("X-CSRF-TOKEN",params.csrfToken);
            },*/
            success:function (response) {
                if(response.status === 'success'){
                    wxPayRequest(response.data.appId,response.data.timeStamp,response.data.nonceStr,response.data.package,response.data.paySign,response.orderId);
                    //$('#wechatPay').data('url', '/pay/wxpay/pay.html?orderNO=$request.getParameter("orderNO")&appid=' + response.appId + '&timeStamp=' + response.timeStamp + '&nonceStr=' + response.nonceStr + '&package=' + response.paypackage + '&paySign=' + response.sign);
                }else {
                    /*layer.open({
                        content: '支付失败'
                        ,skin: 'msg'
                        ,time: 5 //2秒后自动关闭
                    });*/
                    toastr.error('支付失败', '提示');
                }
            },
            error:function () {
                /*layer.open({
                    content: '支付失败'
                    ,skin: 'msg'
                    ,time: 5 //2秒后自动关闭
                });*/
                toastr.error('支付失败', '提示');
            }
        });
    }
    /**
     * Handles the submission of the review checkout stage
     */
    Checkout.performCheckout = function() {
        var paymentMethod = getPaymentMethod();
        var $reviewStageContent = $('.js-reviewStageContent');
        var $form = $reviewStageContent.find('#' + paymentMethod + 'CheckoutSubmissionForm');

        if ('CreditCard' === paymentMethod) {
            if (!shouldUseCustomerPayment() && !shouldSaveNewPayment()) {
                var $paymentStageContent = $('.js-paymentStageContent');
                var $creditCardData = $paymentStageContent.find('.js-creditCardData');
                var nonce = SamplePaymentService.tokenizeCard($creditCardData);

                $form.find('#payment_method_nonce').val(nonce);
            }
        }

        $form.submit();
    };

    /**
     * Handles the navigation from one checkout stage to another by swapping
     *  out the section specified by `js-checkoutStages`.
     * @param {String} requestedCheckoutStage
     */
    Checkout.navigateToCheckoutStage = function(requestedCheckoutStage) {
        var url = '/checkout/' + requestedCheckoutStage;

        BLC.ajax({
            method: 'GET',
            url: url
        }, function(data) {
            updateWindowStateHistory(requestedCheckoutStage);

            Checkout.updateCartPricingSummary();
            replaceCheckoutStages(data);
        });
    };

    /**
     * Handles the navigation from one checkout stage to another by swapping
     *  out the section specified by `js-checkoutStages`.
     * @param {$element} $editCheckoutStageAction
     */
    Checkout.updateCartPricingSummary = function() {
        BLC.ajax({
            url: '/cart/summary?isCheckoutContext=true',
            method: 'GET'
        }, function(data) {
            $('.js-cart-summary').replaceWith(data);
        });
    };

    /**
     * Copies all 'js-cloneable' fields from the Shipping Form to the Billing Form
     */
    Checkout.clearFormInputs = function($form) {
        $form.find('input:not(:radio,[name=csrfToken],[name=emailAddress],[name=address\\.isoCountryAlpha2]), select:not(.js-chooseAddress)')
            .each(function(i, el) {
                $(el).val('').trigger('change');
            });
    };

    /**
     * Copies all 'js-cloneable' fields from the Shipping Form to the Billing Form
     */
    Checkout.copyShippingForm = function() {
        $('.js-cloneable').each(function() {
            var $billingInfo = $(".js-billingInfo");
            $billingInfo.find("input[name='" + $(this).attr('name') + "']").val($(this).val()).attr('disabled', 'disabled');
            $billingInfo.find("select[name='" + $(this).attr('name') + "']").val($(this).val()).attr('disabled', 'disabled');
        });
    };

    /**
     * Copies all 'js-cloneable' fields from the Billing Form to the Shipping Form
     */
    Checkout.copyBillingForm = function() {
        $('.js-cloneable').each(function() {
            var $shippingInfo = $(".js-shippingInfo");
            $shippingInfo.find("input[name='" + $(this).attr('name') + "']").val($(this).val()).attr('disabled', 'disabled');
            $shippingInfo.find("select[name='" + $(this).attr('name') + "']").val($(this).val()).attr('disabled', 'disabled');
        });
    };

    /**
     * Reveals the Multi-Ship add address form in the current modal
     */
    Checkout.showAddAddress = function() {
        var $form = $('.js-multishipAddressForm');
        BLC.ajax({
            url: $form.attr('action'),
            type: "POST",
            data: $form.serialize()
        }, function(data, extraData) {
            var showAddAddressUrl = $('a.addAddressLink').attr('href');
            BLC.ajax({url: showAddAddressUrl}, function(data, extraData) {
                $('.js-multishipProducts').hide();
                $('.simplemodal-wrap').append(data);
            });
        });
        return false;
    };

    /**
     * Based on the provided dropdown, reload the dynamic form body to show the correct fields.
     * @param {element} dropdown - contains selected option that should drive the dynamic form display
     * @param {element} formDiv - the form whose contents should be replaced
     * @param {function} copyForm - an additional copy method that can be run after the dynamic form is replaced
     */
    Checkout.reloadDynamicForm = function(dropdown, formDiv, copyForm) {
        var $selectedOption = $(dropdown).children(':selected');

        BLC.ajax({
            url: $selectedOption.data('href'),
            type: "GET",
            cache: false
        }, function(data) {
            $(formDiv).html(data);
            if (typeof copyForm == "function") {
                copyForm()
            }
        });
    };

    /**
     * Reveals the dynamic address forms and hides all non-js elements
     */
    Checkout.showDynamicAddressForms = function() {
        $('.js-dynamicAddressCountry').show();
        $('.js-billingInfoCountryNonJs').hide();
        $('.js-shippingInfoCountryNonJs').hide();
    };

    /**
     * Hides all PaymentOption description elements
     */
    Checkout.hideAllPaymentOptionDescriptions = function() {
        $('.js-paymentOptions > div').hide();
    };

    /**
     * Reveals description element related to the selected Payment Option
     */
    Checkout.showSelectedPaymentOptionDetails = function() {
        var paymentMethod = getPaymentMethod();

        $('#' + paymentMethod + 'Options').show();
    };

    /**
     * Reveals description element related to the provided Payment Option
     * @param {element} $paymentOption - the payment option whose details should be revealed
     */
    Checkout.showPaymentOptionDetails = function($paymentOption) {
        var targetOptions = '#' + $paymentOption.val() + 'Options';
        $(targetOptions).show();
    };

    /**
     * Toggles description element related to the provided Payment Option
     * @param {element} $paymentOption - the payment option whose details should be toggled
     */
    Checkout.togglePaymentOptionDetails = function($paymentOption) {
        var targetOptions = $paymentOption.val() + 'Options';
        $(targetOptions).toggle();
    };

    /**
     * Hides/shows the promo credit options based on the presence or absence of errors
     */
    Checkout.togglePromoCreditOptions = function() {
        $('.js-promoCreditOptions').children('dd').each(function(){
            var isChecked = $(this).prev().find('input[type=checkbox]').prop('checked');
            var hasErrors = $(this).find('span.error').length != 0;

            if (!isChecked && !hasErrors) {
                $(this).hide();
            } else {
                $(this).prev().find('input[type=checkbox]').prop('checked', true);
                $(this).show();
            }
        })
    };

    /// Private Methods

    /**
     * Replaces the checkout stages partial with an updated version
     * @param {html} newCheckoutStagesPartial
     */
    function replaceCheckoutStages(newCheckoutStagesPartial) {
        var $previousPaymentOptionForms = $('.js-checkoutStages').find('.js-paymentInfoForm');

        $('.js-checkoutStages').html(newCheckoutStagesPartial);

        // Initialize any select pickers
        $(newCheckoutStagesPartial).find('.selectpicker').selectpicker({ container: 'body' });

        // Run initialization logic
        Checkout.initialize();

        copyPaymentStageDataToNewPartial($previousPaymentOptionForms);

       /* if (paymentStageIsReadOnly()) {
            var $paymentStageContent = $('.js-paymentStageContent');
            var $readOnlyPaymentStageContent = $('.js-readOnlyPaymentStageContent');
            populateReadOnlyCreditCardView($paymentStageContent, $readOnlyPaymentStageContent);
        }*/

        Checkout.hideAllPaymentOptionDescriptions();
        Checkout.showSelectedPaymentOptionDetails();
    }

    function savedPaymentContainerIsHidden() {
        var $savedPaymentsContainer = $('.js-savedPaymentsContainer');

        return $savedPaymentsContainer.hasClass('is-hidden');
    };

    function selectActiveSavedPayment() {
        var $savedPaymentsContainer = $('.js-savedPaymentsContainer');
        var $defaultSavedPayment = $savedPaymentsContainer.find('.js-chooseSavedPayment.active');

        $defaultSavedPayment.click();
    };

    /**
     * If the customer is not anonymous, they will have the opportunity to save their credit card payment method
     *
     * @return
     */
    function shouldSaveNewPayment() {
        var $saveNewPaymentCheckbox = $("[name='shouldSaveNewPayment']");
        var shouldSaveNewPayment = $saveNewPaymentCheckbox.length > 0 && $saveNewPaymentCheckbox.val() === "true";

        return shouldSaveNewPayment;
    };

    /**
     * If the customer is not anonymous, they will have the opportunity to select one of their saved payments
     *
     * @return
     */
    function shouldUseCustomerPayment() {
        var $useCustomerPaymentCheckbox = $("[name='shouldUseCustomerPayment']");
        var shouldUseCustomerPayment = $useCustomerPaymentCheckbox.length > 0 && $useCustomerPaymentCheckbox.val() === "true";

        return shouldUseCustomerPayment;
    };

    /**
     * Submits the payment info form & advances to the next stage of the checkout flow.
     *
     * If a customer's saved payment is not being used, then we tokenize the provided credit card data
     *  and send the token along in the form. This assumes that we are given a multi-use token from
     *  the SamplePaymentService.
     */
    function savePaymentInfo($paymentStage) {
        var $paymentInfoForm = $paymentStage.find('.js-creditCardPaymentForm');

        if (!shouldUseCustomerPayment()) {
            var $paymentStageContent = $('.js-paymentStageContent');
            var $creditCardData = $paymentStageContent.find('.js-creditCardData');

            var paymentToken = SamplePaymentService.tokenizeCard($creditCardData);
            $paymentInfoForm.find('.js-paymentToken').val(paymentToken);
        }

        BLC.ajax({
            url: $paymentInfoForm.attr('action'),
            type: "POST",
            data: $paymentInfoForm.serialize()
        }, function(data) {
            clearWindowStateHistory();

            replaceCheckoutStages(data);
            showHiddenPerformCheckoutActions();
        });
    };

    /**
     * Submits the payment info form & advances to the next stage of the checkout flow.
     *
     * If the customer is anonymous or elects not to save their provided credit card, then this method should be used
     *  to simply submit the billing address data. This assumes that we would NOT be given a multi-use token from
     *  the SamplePaymentService, therefore, tokenizing the credit card data should be delayed until the final
     *  submission of the checkout flow.
     */
    function saveBillingAddressOnly($paymentStage) {
        var $paymentInfoForm = $paymentStage.find('.js-creditCardPaymentForm');

        BLC.ajax({
            url: $paymentInfoForm.attr('action') + '/billing',
            type: "POST",
            data: $paymentInfoForm.serialize()
        }, function(data) {
            clearWindowStateHistory();

            replaceCheckoutStages(data);
            showHiddenPerformCheckoutActions();
        });
    };

    function isPayPalConfigComplete() {
        return $('.js-payPalConfigLink').length === 0;
    }

    /**
     * Clicks a link that leads to a redirect into PayPal's ExpressCheckout using Broadleaf's PayPal integration module.
     *
     * Note: The link will not be present if there is already a third party payment associated with the order. If you wish
     *  to change this behavior, you'll need to override the `payPalPaymentMethodForm.html` template partial.
     */
    function collectPaymentInfoViaPayPal() {
        var $payPalPaymentMethodAction = $('.js-payPalPaymentMethodAction');

        if ($payPalPaymentMethodAction.length) {
            $payPalPaymentMethodAction[0].click();
        } else {
            Checkout.navigateToCheckoutStage('REVIEW');
        }
    };

    /**
     * Replaces the checkout stages partial with an updated version
     * @param {Element} $previousPaymentOptionForms
     */
    function copyPaymentStageDataToNewPartial($previousPaymentOptionForms) {
        var $newPaymentOptionForms = $('.js-checkoutStages').find('.js-paymentInfoForm');

        $newPaymentOptionForms.find('input:not([name=customerPaymentId], [name=useCustomerPayment]), select').each(function(i, el) {
            var value;
            if ($(el).is('select')) {
                value = $previousPaymentOptionForms.find("select[name='" + $(el).attr('name') + "']").val();
            } else {
                value = $previousPaymentOptionForms.find("input[name='" + $(el).attr('name') + "']").val();
            }
            $(el).val(value);

            if ($(el).hasClass('selectpicker')) {
                $('.selectpicker').selectpicker('refresh');
            }
        });

        var $newCustomerPaymentId = $newPaymentOptionForms.find('input[name=customerPaymentId]');

        if ($newCustomerPaymentId.val() === undefined || $newCustomerPaymentId.val().length == 0) {
            var previousValue = $previousPaymentOptionForms.find("input[name=customerPaymentId]").val();
            $newCustomerPaymentId.val(previousValue)
        }
    }

    /**
     * Gathers the specified payment method from the payment stage
     */
    function getPaymentMethod() {
        var $paymentStageContent = $('.js-paymentStageContent');

        return $paymentStageContent.find('.js-paymentMethodSelectors').find('li.active').data('value');
    }

    function paymentStageIsReadOnly() {
        var $readOnlyPaymentStageContent = $('.js-readOnlyPaymentStageContent');
        var $checkoutStage = $readOnlyPaymentStageContent.closest('.js-checkoutStage');

        return !$checkoutStage.hasClass('is-hidden') && !$readOnlyPaymentStageContent.hasClass('is-hidden');
    }

    function populateReadOnlyCreditCardView($paymentStageContent, $readOnlyPaymentStageContent) {
        var $creditCardForm = $paymentStageContent.find('.js-creditCardPaymentFormContainer');
        var creditCardNumber = $creditCardForm.find('#cardNumber').val();
        var cardType = SamplePaymentService.cardType(creditCardNumber);
        var lastFour = SamplePaymentService.lastFour(creditCardNumber);
        var expirationDate = SamplePaymentService.expirationDate($creditCardForm);

        $readOnlyPaymentStageContent.find('#' + cardType).removeClass('is-hidden');
        $readOnlyPaymentStageContent.find('.js-creditCardLastFour').text(lastFour);
        $readOnlyPaymentStageContent.find('.js-creditCardNumber').removeClass('is-hidden');
        $readOnlyPaymentStageContent.find('.js-creditCardExpDate').text(expirationDate);
        $readOnlyPaymentStageContent.find('.js-creditCardExpDate').removeClass('is-hidden');
    }

    function advanceFromPaymentToReviewStage() {
        $('.js-paymentStageContent').addClass('is-hidden');
        $('.js-readOnlyPaymentStageContent').removeClass('is-hidden');

        var $paymentStageCard = $('.js-paymentStageCard');
        var $checkoutStageCompleteIcon = $paymentStageCard.find('.js-checkoutStageCompleteIcon');
        var $editPaymentStageAction = $paymentStageCard.find('.js-editCheckoutStage');
        $checkoutStageCompleteIcon.removeClass('is-hidden');
        $editPaymentStageAction.removeClass('is-hidden');

        var $reviewStageContent = $('.js-reviewStageContent');
        $reviewStageContent.removeClass('is-hidden');

        showHiddenPerformCheckoutActions();
    }

    function showHiddenPerformCheckoutActions() {
        var $hiddenPerformCheckoutActions = $('.js-hiddenPerformCheckoutActions');
        $hiddenPerformCheckoutActions.removeClass('is-hidden');
    }

    function showReadOnlyPaymentMethod(paymentMethod) {
        $('.js-readOnlyPaymentMethod:not(.is-hidden)').addClass('is-hidden');
        $(".js-readOnlyPaymentMethod[data-type='" + paymentMethod + "']").removeClass('is-hidden');
    }

    function clearWindowStateHistory() {
        updateWindowStateHistory(null);
    }

    /**
     * Replaces the active checkout stage request param
     * @param {String} checkoutStage
     */
    function updateWindowStateHistory(checkoutStage) {
        var params = BLC.getUrlParameters();
        if (checkoutStage === undefined || checkoutStage ===null || checkoutStage.length === 0) {
            delete params['activeStage'];
        } else {
            params['activeStage'] = checkoutStage;
        }

        var urlWithParams = BLC.buildUrlWithParams('/checkout', params);
        window.history.replaceState(null, null, urlWithParams);
    }
    function checkoutReload(checkoutStage) {
        var params = BLC.getUrlParameters();
        if (checkoutStage === undefined || checkoutStage ===null || checkoutStage.length === 0) {
            delete params['activeStage'];
        } else {
            params['activeStage'] = checkoutStage;
        }

        var urlWithParams = BLC.buildUrlWithParams('/checkout', params);
        window.location.href = urlWithParams;
        //window.history.replaceState(null, null, urlWithParams);
    }
})(window.Checkout = window.Checkout || {}, jQuery);
