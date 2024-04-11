$(function() {
    setTimeout(function() {
        $('div#banner').animate({width: '10rem', top: '14rem', right: '-15rem'}, 500);
    }, 5000);

    $( "div#banner_menu" ).click(function() {
      $( this ).animate({right: '-3rem', cursor: 'default'}, 500);
    });

    $( "div#banner_menu" ).mouseleave(function() {
      $( this ).animate({right: '-22rem'}, 500);
    });
});