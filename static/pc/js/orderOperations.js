(function(Checkout, $, undefined) {
    Order.confirmReceipt = function (orderId) {
        if (confirm('确定已收到货？')){
            $.ajax({
                url :'/account/orders/confirmReceipt?orderId=' + orderId + '&csrfToken=' + params.csrfToken,
                type : 'POST',
                success : function (res) {
                    if (res.status === 'SUCCESS'){
                        toastr.success('确认收货成功');
                        $('#js-order-confirm-receipt-' + orderId ).remove();
                    }else {
                        toastr.error(res.message);
                    }
                },
                error:function () {
                    toastr.error('确认收货失败');
                }
            })
        }
    }
})(window.Order = window.Order || {}, jQuery);