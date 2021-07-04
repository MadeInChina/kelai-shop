function initAdministrativeRegionSelect(){
    var provinceSelect = $('#province-select');
    provinceSelect.empty();
    provinceSelect.append('<option value=""></option>');
    for(var i=0;i<provinces.length;i++){
        provinceSelect.append('<option value="' + provinces[i].code + '">' + provinces[i].name + '</option>');
    }
    $('#province-select').on('change', function () {
        $('#city-select').empty();
        $('#region-select').empty();
        $('input[name="fields[\'city\'].value"]').val('');
        $('input[name="fields[\'cityCode\'].value"]').val('');
        $('input[name="fields[\'region\'].value"]').val('');
        $('input[name="fields[\'regionCode\'].value"]').val('');

        var that = $(this);
        console.log(that.find("option:selected").text());
        $('input[name="fields[\'province\'].value"]').val(that.find("option:selected").text());
        $('input[name="fields[\'provinceCode\'].value"]').val(that.find("option:selected").val());

        $('#city-select').append('<option value=""></option>');
        $.ajax({
            url :'/admin/administrativeRegion/cities?provinceCode=' + that.val(),
            type:'GET',
            success:function (cities) {
                for (var i = 0; i < cities.length; i++) {
                    $('#city-select').append('<option value="' + cities[i].code + '">' + cities[i].name + '</option>');
                }
            }
        });

    });
    $('#city-select').on('change', function () {

        $('#region-select').empty();
        $('input[name="fields[\'region\'].value"]').val('');
        $('input[name="fields[\'regionCode\'].value"]').val('');

        var that = $(this);
        $('input[name="fields[\'city\'].value"]').val(that.find("option:selected").text());
        $('input[name="fields[\'cityCode\'].value"]').val(that.find("option:selected").val());

        $('#region-select').append('<option value=""></option>');
        $.ajax({
            url :'/admin/administrativeRegion/regions?cityCode=' + that.val(),
            type:'GET',
            success:function (regions) {
                for (var i = 0; i < regions.length; i++) {

                    $('#region-select').append('<option value="' + regions[i].code + '">' + regions[i].name + '</option>');

                }
            }
        });

    });
    $('#region-select').on('change', function () {

        var that = $(this);
        $('input[name="fields[\'region\'].value"]').val(that.find("option:selected").text());
        $('input[name="fields[\'regionCode\'].value"]').val(that.find("option:selected").val());

    });
    $('.selectize-control').hide();
    $('#province-select').show();
    $('#city-select').show();
    $('#region-select').show();
    $('#field-provinceCode').hide();
    $('#field-cityCode').hide();
    $('#field-regionCode').hide();

    var provinceCode = $('input[name="fields[\'provinceCode\'].value"]').val();
    var cityCode = $('input[name="fields[\'cityCode\'].value"]').val();
    var regionCode = $('input[name="fields[\'regionCode\'].value"]').val();
    if (provinceCode !== '' && cityCode !== '' && regionCode !== ''){
        $('#province-select').val(provinceCode);
        //$('#city-select').val(cityCode).change();
        //$('#region-select').val(regionCode).change();
        $('#city-select').empty();
        $('#city-select').append('<option value=""></option>');
        $.ajax({
            url :'/admin/administrativeRegion/cities?provinceCode=' + provinceCode,
            type:'GET',
            success:function (cities) {
                for (var i = 0; i < cities.length; i++) {
                    $('#city-select').append('<option value="' + cities[i].code + '">' + cities[i].name + '</option>');
                }
                $('#city-select').val(cityCode);
            }
        });
        $('#region-select').empty();
        $('#region-select').append('<option value=""></option>');
        $.ajax({
            url :'/admin/administrativeRegion/regions?cityCode=' + cityCode,
            type:'GET',
            success:function (regions) {
                for (var i = 0; i < regions.length; i++) {

                    $('#region-select').append('<option value="' + regions[i].code + '">' + regions[i].name + '</option>');

                }
                $('#region-select').val(regionCode);
            }
        });
    }

}