
$('.service-category').mouseover(function () {
    //$('.service-category-panel').show();
    var panel = $('#service-category-2-panel');
    panel.addClass('show').removeClass('hidden');
}).mouseout(function () {
    //$('.service-category-panel').hide();
    var panel = $('#service-category-2-panel');
    panel.removeClass('show').addClass('hidden');
}).click(function () {
    var panel = $('#service-category-2-panel');
    panel.addClass('show').removeClass('hidden');
});
