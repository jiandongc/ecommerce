$(document).on('click', '.filter-header', function(e){
    var $this = $(this);
	if(!$this.hasClass('collapsed')) {
		$this.find('i').removeClass('glyphicon glyphicon-plus').addClass('glyphicon glyphicon-minus');
	} else {
		$this.find('i').removeClass('glyphicon glyphicon-minus').addClass('glyphicon glyphicon-plus');
	}
});


$(document).on('click', '.nav-pills a', function (e) {
	$(this).parent().addClass('active').siblings().removeClass('active');
});
