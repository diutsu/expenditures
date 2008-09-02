	function calculate(maxElements, writeTo, maxValue, outOfLabel) {
				sum = parseFloat('0');
				for(i=0; i<maxElements;i++) {
					element = document.getElementById('tr' + i);
					if (element != null) {
						checked = element.cells[0].getElementsByTagName("input")[0].checked;
						value = element.cells[2].getElementsByTagName("input")[0].value;
						if (checked && value.match('\\d+')) {
							sum += parseFloat(value);
						}
					}	
				}
				sumValue ="";
				maxValueFloat = parseFloat(maxValue); 
				if (sum > maxValueFloat) {
					sumValue = '<span class="invalid">' + sum + '</span>';
				}
				else if (sum == maxValueFloat) {
					sumValue = '<span class="valid">' + sum + '</span>';
				}
				else {
					sumValue = sum;
				}
							document.getElementById(writeTo).innerHTML=sumValue + ' (' + outOfLabel + ' ' + maxValue + ')';
			}