$(document).ready(function() { 
	$('.header').load("navbar.html");
	
	$( "#forma1" ).submit(function( event ) {
		event.preventDefault();
		
		var checkNumber = $('#serNumCheck').val();
		
		$.get({
			
			url:'api/certificates/check/' + checkNumber,
			contentType: 'application/json',
			success: function(valid)
			{	
				$('#valid').text("Certificate is: " + valid)
			},
			error: function()
			{
				alert("Error while checking validity");
			}
			
		});
		  
	});
	
	$( "#forma2" ).submit(function( event ) {
		event.preventDefault();
		
		var revokeNumber = $('#serNumRevoke').val();
		
		$.post({
			
			url:'api/certificates/revoke/' + revokeNumber,
			contentType: 'application/json',
			success: function()
			{	
				alert("Certificate succesfully revoked.");
			},
			error: function()
			{
				alert("Error - Certificate could not be revoked.");
			}
			
		});
		  
	});
	
	
});