//metoda koja kreira tabelu certifikata
function RenderHtmlOnSuccess() {
    
    $.get({
		
		url:'api/certificates',
		contentType: 'application/json',
		success: function(certifikati)
		{	
			//certifikati
			var data = certifikati;
			
			//pocetak kreiranja html-a
			var html = '<table id="tableCert" class="display" ><thead><tr><th>Common Name</th><th>Given name</th><th>Surname</th><th>Organization</th><th>Organizational Unit</th><th>Country code</th><th>E-mail</th><th>Serial number</th><th>Status</th></thead><tbody>';
			
			data.forEach((item)=>{
 			
			
				  //ubacivanje redova u tabelu
				  html+='<tr>';
				  
				  html+='<td>';
				  html+=item.commonName;
				  html+='</td>';
		
				  html+='<td>';
				  html+=item.givenname;
				  html+='</td>'; 
				  
				  html+='<td>';
				  html+=item.surname;
				  html+='</td>';
				  
				  html+='<td>';
				  html+=item.organization;
				  html+='</td>';
				  
				  html+='<td>';
				  html+=item.organizationalUnit;
				  html+='</td>';
				  
				  html+='<td>';
				  html+=item.countryCode;
				  html+='</td>';
				  
				  html+='<td>';
				  html+=item.email;
				  html+='</td>';
				  
				  html+='<td>';
				  html+=item.serialNumber;
				  html+='</td>';
				  
				  html+='<td>';
				  html+=item.status;
				  html+='</td>';
				  
				  html+='</tr>';
			
			})
			
		    html += '</tbody></table>';

			//postavljanje tabele u div na html-u
		    $(html).appendTo('#divTabela');

		    //konvertovanje tabele u dataTable
		    var table = $('#tableCert').dataTable({
		        "pagingType": "full_numbers",
		        select: false
		    });
		    
		    $('.dataTables_length').addClass('bs-select');
		    
		},
		error: function()
		{
			//ako nema pregleda prikazi praznu tabelu
			alert('Nema certifikata u tabeli');
			var html = '<table id="tableCert" class="display" ><thead><tr><th>Common Name</th><th>Given name</th><th>Surname</th><th>Organization</th><th>Organizational Unit</th><th>Country code</th><th>E-mail</th><th>Serial number</th><th>Status</th></thead><tbody>';
			html += '</tbody></table>';
			//postavljanje tabele u div na html-u
		    $(html).appendTo('#divTabela');

		    //konvertovanje tabele u dataTable
		    var table = $('#tableCert').dataTable({
		        "pagingType": "full_numbers",
		        select: false
		    });
		}
		
	});
    
}







$(document).ready(function() { 
	$('.header').load("navbar.html");
	RenderHtmlOnSuccess();
});