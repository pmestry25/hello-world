function mandatoryFieldsValidation() {

	var w_form = document.getElementsByTagName('form');
	var w_formElem = w_form[0];
	var w_temp;
	var w_alertContent = "";
	var w_blankFieldCount = 0;
	var w_requiredFieldCount = 0;
	var w_tag = w_formElem.getElementsByTagName('input');
	for ( var i = 0; i < w_tag.length; i++) {
		w_temp = w_tag[i];
		if (w_temp.required) {
			w_requiredFieldCount++;
			if (w_temp.value.length == 0) {
				w_alertContent = w_alertContent + w_temp.name + ",  ";
				w_blankFieldCount++;
			}
		}
	}

	if ((w_blankFieldCount > 0) && (w_blankFieldCount < w_requiredFieldCount)) {
		alert(w_alertContent + "cannot be left blank!!!");
		return false;
	} else if (w_blankFieldCount == w_requiredFieldCount) {
		alert("Mandatory fields cannot be left blank!!!");
		return false;
	}

	return true;
}

function createUserValidation(w_id) {
	
	document.getElementById("Invalid" + w_id.id).innerHTML="";
	if ((w_id.name == "firstName" || w_id.name == "lastName")&& w_id.value.length > 100) {
		document.getElementById("Invalid" + w_id.id).innerHTML='Maximum length of characters should not exceed 100 characters!';
		w_id.focus();
	} else if (w_id.name == "loginid") {
		var w_atpos = w_id.value.indexOf("@");
		var w_dotpos = w_id.value.lastIndexOf(".");
		if ((w_id.value.length > 20)|| (w_atpos < 1 || w_dotpos < w_atpos + 2 || w_dotpos + 2 >= w_id.value.length)) {
			document.getElementById("Invalid" + w_id.id).innerHTML='Not a valid login-id. It should be a valid email address of maximum 20 chars';
			w_id.focus();
		}else {
			$.ajax({
				type : "POST",
				url : "CommonController",
				success : function(a_resp) {

					if(a_resp == "notUnique"){
						document.getElementById("Invalid" + w_id.id).innerHTML="Login Id already exists. Please try another";
					}
					
				},
				error : function() {
					window.location.replace("SQLError.html");
				},
				data : {
					action : 'uniqueLoginId',
					loginId: w_id.value
				}
			});

		}
	} else if (w_id.name == "password") {
		var w_passwordContent = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{4,20}$/;
		if (!w_id.value.match(w_passwordContent)) {
			
			document.getElementById("Invalid" + w_id.id).innerHTML='Not a valid Password. Password should contain atleast 1 lowercase, 1 uppercase, 1 number, & 1 special character. Max size is 20 chars';
			w_id.focus();
		}
	}

}

function createDefectValidation(w_ele) {
	
	var w_element = w_ele;
	if(w_ele == "name") {
		document.getElementById("Invalid" + w_ele).innerHTML="";
	} else {
		document.getElementById("Invalid" + w_ele.id).innerHTML="";
	}
	
	if (w_element == "name" && document.getElementById(""+w_element).value.length > 100) {
		document.getElementById("Invalid" + w_element).innerHTML= "Make sure the defect name is less than 100 characters.";
	} else if ((w_element.name == "description" || w_element.name == "resolution")
			&& w_element.value.length > 2000) {
		document.getElementById("Invalid" + w_element.id).innerHTML= "Defect " + w_element.name + " cannot be more than 2000 characters!";
	} else if (w_ele.name == "dateidentified") {
		
		var w_currentDate = getSystemDate();
		
		if (w_ele.value > w_currentDate) {
			document.getElementById("Invalid" + w_ele.id).innerHTML= "Defect date identified should not be a future date.";
		}
	}
}
