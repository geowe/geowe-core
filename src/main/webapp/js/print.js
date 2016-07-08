function exportPDF() {
	
	var transform = $(".gm-style>div:first>div").css("transform");
	var comp = transform.split(","); // split up the transform matrix
	var mapleft = parseFloat(comp[4]); // get left value
	var maptop = parseFloat(comp[5]); // get top value
	$(".gm-style>div:first>div").css({ // get the map container. not sure if
										// stable
		"transform" : "none",
		"left" : mapleft,
		"top" : maptop,
	});

	html2canvas(document.body, {
		useCORS : true,
		onrendered : function(canvas) {			
			mapImg = canvas.toDataURL("image/jpeg", 1.0);

			// reset the map to original styling
			$(".gm-style>div:first>div").css({
				left : 0,
				top : 0,
				"transform" : transform
			});			

			 var specialElementHandlers = function() {
			        return;
			    }
			var img = new Image();
			img.src = mapImg;
			img.onload = function() {				
				var pdf = new jsPDF();
				var anchoDeseado = 190;
				var height = getHeight(img.width,img.height, anchoDeseado);
				pdf.addImage(img, 'JPEG', 10, 10, anchoDeseado, height);
				pdf.setFontSize(8);
				pdf.setFontType("italic");
				pdf.text(10,(height + 13), "Powered by GeoWE - map.geowe.org");
				//doc.fromHTML('<a href="http://www.geowe.org">geowe.org</a>', 20,(height + 30), {'width': 50, 'elementHandlers': specialElementHandlers});
				pdf.save('map-geowe.pdf');
			};			
		}
	});	
}



function exportPNG() {
	//http://stackoverflow.com/questions/24046778/html2canvas-does-not-work-with-google-maps-pan
	
	
	var transform = $(".gm-style>div:first>div").css("transform");
	var comp = transform.split(","); // split up the transform matrix
	var mapleft = parseFloat(comp[4]); // get left value
	var maptop = parseFloat(comp[5]); // get top value
	$(".gm-style>div:first>div").css({ // get the map container. not sure if
										// stable
		"transform" : "none",
		"left" : mapleft,
		"top" : maptop,
	});

	html2canvas(document.body, {	
		
		useCORS: true,
		  onrendered: function(canvas)
		  {
			  $(".gm-style>div:first>div").css({
			      left:0,
			      top:0,
			      "transform":transform
			    });
			  
			var dataUrl= canvas.toDataURL('image/png');
			//window.open(dataUrl, '_blank');
				
			$(".gm-style>div:first>div").css({
			      left:0,
			      top:0,
			      "transform":transform
			    })
			
			window.location.href=dataUrl;   
			//window.location.href=dataUrl;		    
		  }
	});
	
	
}

function exportJPG() {
	
	var transform = $(".gm-style>div:first>div").css("transform");
	var comp = transform.split(","); // split up the transform matrix
	var mapleft = parseFloat(comp[4]); // get left value
	var maptop = parseFloat(comp[5]); // get top value
	$(".gm-style>div:first>div").css({ // get the map container. not sure if
										// stable
		"transform" : "none",
		"left" : mapleft,
		"top" : maptop,
	});

	html2canvas(document.body, {
		useCORS : true,
		onrendered : function(canvas) {
			
			// reset the map to original styling
			$(".gm-style>div:first>div").css({
				left : 0,
				top : 0,
				"transform" : transform
			});			
			
			var myImage = canvas.toDataURL("image/jpeg", 1.0).replace("image/jpeg",
			"image/octet-stream");

			var link = document.createElement('a');
			if (link.download !== undefined) {
				link.download = "map-geowe.jpg";
				link.href = myImage;
				document.body.appendChild(link);
				window.jQuery(link).css("display", "none");
				link.click();
				document.body.removeChild(link);
			}					
		}
	});
}

function getHeight(anchoOri, altoOri, anchoDeseado) {	
	return (anchoDeseado * altoOri) / anchoOri;
}