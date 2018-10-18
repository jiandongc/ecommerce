$(document).on('click', '.filter-header', function(e){
    var $this = $(this);
	if(!$this.hasClass('collapsed')) {
		$this.find('i').removeClass('glyphicon glyphicon-plus').addClass('glyphicon glyphicon-minus');
	} else {
		$this.find('i').removeClass('glyphicon glyphicon-minus').addClass('glyphicon glyphicon-plus');
	}
});