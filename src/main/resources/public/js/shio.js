var menuFloat = '<div class="menufloat">' + "Content"
'</div>';
$('.sh-region').wrap('<div class="sh-edit"></div>');
$('.sh-edit').append(menuFloat);
$('.sh-edit').on("mouseout", function() {
	$(this).children(".menufloat").css("visibility", "hidden");
});
$('.sh-edit').on("mouseover", function() {
	$(this).children(".menufloat").css("visibility", "visible");
});
