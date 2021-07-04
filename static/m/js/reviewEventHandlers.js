/**
 * Reveal the create Product review modal, leveraging the jQuery Star Rating Plugin v4.11.
 */
$('body').on('click', 'a.js-createReview', function() {
    $('#review-form').slideToggle();
    $(this).slideUp();
});

/**
 * Handle the submission of a Product review
 */
$('body').on('click', '.js-submitReview', function() {
    var $form = $(this).closest("form");
    if($form.find('.star-rating-on').length <= 0)
    {
        $('.review-status-bottom').text('您忘记打分咯!');
        $('.review-status-bottom').show();
        return;
    }
    if ($.trim($('textarea[name="reviewText"]').val()) === ''){
        $('.review-status-bottom').text('请填写品论内容!');
        $('.review-status-bottom').show();
        return;
    }
    BLC.ajax({
        url: $form.data('action'),
        type: "POST",
        data: $form.serialize()
    }, function(responseData) {
        if ($(responseData).data('status') === 'error'){
            $('.review-status-bottom').text($(responseData).text());
            $('.review-status-bottom').show();
            return;
        }
        $('.review-status-top').html(responseData);
        $('.js-createReview').hide();
        $('#review-form').slideUp();
    });
});

$(function() {
    Review.initialize();
});