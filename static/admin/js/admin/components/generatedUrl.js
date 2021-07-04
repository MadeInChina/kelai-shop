BLCAdmin.generatedUrl.convertToUrlFragment = function convertToUrlFragment(val, options) {
    var valPostFix = "";
    if (val.toString().indexOf('.') != -1) {
        var valFragments = val.split('.');
        valPostFix = valFragments[valFragments.length - 1];
        val = val.substring(0,val.length - valPostFix.length)
        if(valPostFix){
            valPostFix="."+valPostFix
        }
    }
    val =  pinyin.getFullChars(val);
    if (options != null && options.allowSlash) {
        return val.replace(/ /g, BLC.systemProperty.urlFragmentSeparator).replace(/[^\w\s-_\/]/gi, '') + valPostFix;
    } else {
        return val.replace(/ /g, BLC.systemProperty.urlFragmentSeparator).replace(/[^\w\s-_]/gi, '') + valPostFix;
    }
};