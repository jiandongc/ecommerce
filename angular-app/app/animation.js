$(document).on('click', '.filter-header', function(e){
    var $this = $(this);
	if(!$this.hasClass('collapsed')) {
		$this.find('i').removeClass('glyphicon glyphicon-plus').addClass('glyphicon glyphicon-minus');
	} else {
		$this.find('i').removeClass('glyphicon glyphicon-minus').addClass('glyphicon glyphicon-plus');
	}
});

$('ul.nav li.dropdown').hover(function() {
	$(this).find('.dropdown-menu').stop(true, true).delay(200).fadeIn(500);
}, function() {
 	$(this).find('.dropdown-menu').stop(true, true).delay(200).fadeOut(500);
});