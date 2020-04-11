function VratiSelfSigned(callback)
{
	$.get
	({
		url: '/api',
		contentType: 'application/json',
		success: function(certs)
		{
			
			callback(certs);
			
		},
		error: function()
		{
			alert("Error while getting Self signed certificates");
		}	
	});
}


function VratiCA()
{
	$.get
	({
		url: 'api/certificates/allCAcertificates',
		contentType: 'application/json',
		success: function(certs)
		{
			
			var html = '<select id="caissuer" class="form-control" required><option value="-1">Choose Issuer</option>';
			
			certs.forEach((item)=>{
				html+='<option value=' + item.serialNumber + '>' + 'SN:' + item.serialNumber + ' - ' + item.commonName + '</option>';
			});
						
			html+= '</select>';
								
			$('#select').html(html);
			
		},
		error: function()
		{
			alert("Error while getting CA certificates");
		}	
	});
}

function KreirajCert(){
	
	var url1;
	
	if($('#self').is(':checked')) 
	{ 
		
		url1 = "api/certificates/selfsigned";
	}
	else if($('#ca').is(':checked'))
	{
		
		if($("#caissuer").val() == -1)
		{
			alert("You must choose issuer");
		}
		else
		{
			
			url1 = "api/certificates/intermediate/" + $("#caissuer :selected").val();
		}
	}
	else if($('#end').is(':checked'))
	{
		
		if($("#caissuer").val() == -1)
		{
			alert("You must choose issuer");
		}
		else
		{
			
			url1 = "api/certificates/end/" + $("#caissuer :selected").val();
		}
	}
	else
	{
		alert("Radio button is not selected");
	}
	
	var commonName = $('#common').val();
	var givenname = $('#given').val();
	var surname = $('#surname').val();
	var organization = $('#org').val();
	var organizationalUnit = $('#orgUnitName').val();
	var countryCode = $('#cc').val();
	var email = $('#email').val();
	var parts = $('#dateED').val().split("-");
	var expirationDate = parts[2] + "-" + parts[1]+ "-" + parts[0];
	
	$.post
	({
		url: url1,
		data: JSON.stringify({commonName,givenname,surname,organization,organizationalUnit,countryCode,email,expirationDate}),
		contentType: 'application/json',
		success: function()
		{
			alert("Certificate succesfully posted");
		},
		error: function()
		{
			alert("Error - cerificate didnt post properly");
		}	
	});	
	
}

$(document).ready(function() { 
	$('.header').load("navbar.html");
	$('#issuerDiv').hide();
	
	
	//podesavanje da ne moze kraj odsustva da bude pre pocetka i druga podesavnja za datepicker
	$("#dateED").datepicker({
		  changeMonth: true,
		  changeYear: true,
		  dateFormat : "dd-mm-yy",
		  minDate : 0,
		  });
	
	
	
	var radios = document.querySelectorAll('input[type=radio][name="type"]');

	function changeHandler()
	{
	   if ( this.value === 'self' ) 
	   {
		   $('#issuerDiv').hide();
		   $('#h4').text("Self Signed Certificate");
	   } 
	   else if ( this.value === 'ca' ) 
	   {
		   $('#issuerDiv').show();
		   $('#h4').text("CA Certificate");
		   VratiCA();
	   } 
	   else if ( this.value === 'end') 
	   {
		   $('#h4').text("End Certificate");
		   $('#issuerDiv').show();
		   VratiCA();	    
		   
	   } 
	}

	Array.prototype.forEach.call(radios, function(radio) {
	   radio.addEventListener('change', changeHandler);
	});
	
	//submit forme
	$( "#forma" ).submit(function( event ) {
		event.preventDefault();
		
		KreirajCert();
		  
	});;
	
	
});
