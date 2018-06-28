/** http://www.bootply.com/90936 */

/*$('#sidebar_lang').affix({
      offset: {
        top: 245
      }
});*/ //No longer supported in Bootstrap 4.. :(

var $body   = $(document.body);
var navHeight = $('.navbar').outerHeight(true) + 10;

$body.scrollspy({
	target: '#rightCol',
	offset: navHeight
});