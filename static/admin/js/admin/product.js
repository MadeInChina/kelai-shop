BLCAdmin.product.refreshSkusGrid = function ($container, listGridUrl) {

    BLC.ajax({
        url : listGridUrl,
        type : "GET"
    }, function(data) {
        BLCAdmin.listGrid.replaceRelatedCollection($(data));
    });

    BLC.ajax({
        url : listGridUrl.substr(0,listGridUrl.indexOf('/additionalSkus')) + '/colorOptionValueMedia',
        type : "GET"
    }, function(data) {
        BLCAdmin.listGrid.replaceRelatedCollection($(data));
    });

};