function init() {

	var idleTime = 0;
	$(document).ready(function() {
		// Increment the idle time counter every minute.
		var idleInterval = setInterval(timerIncrement, 60000); // 1 minute
		// Zero the idle timer on mouse movement.
		$(this).mousemove(function(e) {
			idleTime = 0;
		});
		$(this).keypress(function(e) {
			idleTime = 0;
		});
	});

	function timerIncrement() {
		idleTime = idleTime + 1;
		if (idleTime > 20) { // 20 minutes
			var value = confirm("Your session will expire now! Do you wish to extend your session?");
			if (value) {
				alert("Your session has been extended!");
			} else {
				$.ajax({
					type : "POST",
					url : "CommonController",
					dataType : 'json',
					success : function() {

						window.location.replace("Login.html");
					},
					error : function() {
						window.location.replace("SQLError.html");
					},
					data : {
						action : 'logout',

					}
				});
			}
		}
	}

	getUser();
}

function getUser() {
	
	$.ajax({
		type : "POST",
		url : "CommonController",
		dataType : 'json',
		success : function(a_resp) {
			if (a_resp['pageName'] == "Login") {
				window.location.replace("Login.html");
			} else {
				header(a_resp);			
			}
		},
		error : function() {
			window.location.replace("SQLError.html");
		},
		data : {
			action : 'loginUser',
		}
	});
}

function header(w_loggedInUserDetails) {

	var w_content = document.getElementById('content');

	var w_headerDiv = document.createElement('div');
	w_headerDiv.id = "hdiv";
	w_headerDiv.setAttribute('class', 'hdiv');

	var w_logo = document.createElement('img');
	w_logo.setAttribute('src', 'media/logo.png');
	w_logo.id = "logo";
	w_logo.setAttribute('class', 'logo');
	w_headerDiv.appendChild(w_logo);

	var w_userNameDiv = document.createElement('div');
	w_userNameDiv.id = "usernamediv";
	w_userNameDiv.setAttribute('class', 'usernamediv');

	var w_roleLogo = document.createElement('img');
	if (w_loggedInUserDetails['role'] == "Admin") {
		w_roleLogo.setAttribute('src', 'media/adminuser.png');
	} else {
		w_roleLogo.setAttribute('src', 'media/dev_test.png');
	}
	w_roleLogo.setAttribute('title', w_loggedInUserDetails['role']);
	w_roleLogo.id = "rolelogo";
	w_roleLogo.setAttribute('class', 'rolelogo');

	w_userNameDiv.appendChild(w_roleLogo);

	var w_username = document.createTextNode("     Hello " + w_loggedInUserDetails['name']);
	w_userNameDiv.appendChild(w_username);
	w_headerDiv.appendChild(w_userNameDiv);
	
	var w_searchIconDiv = document.createElement('div');
	w_searchIconDiv.id = "searchicondiv";
	w_searchIconDiv.setAttribute('class', 'searchicondiv');
	var searchBar = document.createElement('input');
	searchBar.id = "searchbar";
	searchBar.setAttribute('class', 'searchbar');
	searchBar.setAttribute('name', 'searchbar');
	searchBar.setAttribute('onkeyup','myFunction()');
	searchBar.setAttribute('placeholder', 'Search...');
	w_searchIconDiv.appendChild(searchBar);
	w_headerDiv.appendChild(w_searchIconDiv);
	
	var w_loginTimeDiv = document.createElement('div');
	w_loginTimeDiv.id = "logintimediv";
	w_loginTimeDiv.setAttribute('class', 'logintimediv');
	var w_time = document.createTextNode("     Logged in Time: " + w_loggedInUserDetails['loggedInTime']);
	w_loginTimeDiv.appendChild(w_time);
	w_headerDiv.appendChild(w_loginTimeDiv);

	var w_logoutDiv = document.createElement('div');
	w_logoutDiv.id = "logoutdiv";
	w_logoutDiv.setAttribute('class', 'logoutdiv');

	var w_logoutBtn = document.createElement('button');
	w_logoutBtn.id = "logoutbtnn";
	w_logoutBtn.setAttribute('class', 'logoutbtnn');
	var w_btnname = document.createTextNode("Logout");
	w_logoutBtn.appendChild(w_btnname);
	w_logoutDiv.appendChild(w_logoutBtn);

	w_headerDiv.appendChild(w_logoutDiv);

	$(w_logoutBtn).click(function(a_e) {

		var w_logoutValue = confirm("Do you really want to Logout?");
		if (w_logoutValue) {
			$.ajax({
				type : "POST",
				url : "CommonController",
				dataType : 'json',
				success : function() {
					window.location.replace("Login.html");
				},
				error : function() {
					window.location.replace("SQLError.html");
				},
				data : {
					action : 'logout',
				}
			});
		}
	});

	w_content.appendChild(w_headerDiv);
	rolewiseButton(w_loggedInUserDetails['role']);
}

function rolewiseButton(w_role) {

	var w_content = document.getElementById('content');

	var w_buttonDiv = document.createElement('div');
	w_buttonDiv.id = "btndiv";
	w_buttonDiv.setAttribute('class', 'btndiv');

	var w_br = document.createElement('br');
	w_buttonDiv.appendChild(w_br);

	if (w_role == "Admin") {
		var w_createUserBtn = document.createElement('img');
		w_createUserBtn.id = "headerbtnn";
		w_createUserBtn.setAttribute('class', 'headerbtnn');
		w_createUserBtn.setAttribute('src', 'media/createUser.jpg');
		w_createUserBtn.setAttribute('title', 'Create User');
		var w_btnname = document.createTextNode("Create User");
		w_createUserBtn.appendChild(w_btnname);
		w_buttonDiv.appendChild(w_createUserBtn);
	}

	if (w_role == "Tester" || w_role == "Developer") {
		var w_allDefectsBtn = document.createElement('img');
		w_allDefectsBtn.id = "headerbtnn";
		w_allDefectsBtn.setAttribute('class', 'headerbtnn');
		w_allDefectsBtn.setAttribute('src', 'media/allDefects.png');
		w_allDefectsBtn.setAttribute('title', 'All Defects');
		var w_btnname = document.createTextNode("All Defects");
		w_allDefectsBtn.appendChild(w_btnname);
		w_buttonDiv.appendChild(w_allDefectsBtn);

		if (w_role == "Tester") {
			var w_createDefectBtn = document.createElement('img');
			w_createDefectBtn.id = "createdefect";
			w_createDefectBtn.setAttribute('class', 'headerbtnn');
			w_createDefectBtn.setAttribute('src', 'media/createDefect.bmp');
			w_createDefectBtn.setAttribute('title', 'Create Defect');
			var w_btnname = document.createTextNode("Create Defect");
			w_createDefectBtn.appendChild(w_btnname);
			w_buttonDiv.appendChild(w_createDefectBtn);
		}
	}

	var w_pageTitleDiv = document.createElement('div');
	w_pageTitleDiv.id = "pagetitlediv";
	w_pageTitleDiv.setAttribute('class', 'pagetitlediv');
	var w_titleContent = document.createTextNode("Inbox");
	w_pageTitleDiv.appendChild(w_titleContent);
	
	
	var w_sortDiv = document.createElement('div');
	w_sortDiv.id = "sortdiv";
	w_sortDiv.setAttribute('class', 'sortdiv');
	w_buttonDiv.appendChild(w_sortDiv);
	
	var w_sortDiv = document.createElement('div');
	w_sortDiv.id = "filterdiv";
	w_sortDiv.setAttribute('class', 'filterdiv');
	w_buttonDiv.appendChild(w_sortDiv);
	
	w_buttonDiv.appendChild(w_pageTitleDiv);

	var w_br2 = document.createElement('br');
	w_buttonDiv.appendChild(w_br2);

	w_content.appendChild(w_buttonDiv);

	$(w_createUserBtn).click(function(a_e) {
		$.ajax({
			type : "POST",
			url : "CommonController",
			dataType : 'json',
			success : function(a_resp) {
				createForm(a_resp, "create");
			},
			error : function() {
				window.location.replace("SQLError.html");
			},
			data : {
				action : 'createUser',
			}
		});

	});

	$(w_createDefectBtn).click(function(a_e) {
		$.ajax({
			type : "POST",
			url : "CommonController",
			dataType : 'json',
			success : function(a_resp) {
				createForm(a_resp, "create");
			},
			error : function(a_resp) {
				window.location.replace("SQLError.html");
			},
			data : {
				action : 'createDefect',
				mode : 'create'
			}
		});

	});

	$(w_allDefectsBtn).click(function(a_e) {
		$.ajax({
			type : "POST",
			url : "CommonController",
			dataType : 'json',
			success : function(a_resp) {
				commondiv.parentElement.removeChild(commondiv);
				renderList(a_resp, "All Defects");
			},
			error : function() {
				window.location.replace("SQLError.html");
			},
			data : {
				action : 'allDefects',
			}
		});

	});
	getList();
}

function getList() {

	$.ajax({
		type : "POST",
		url : "CommonController",
		dataType : 'json',
		success : function(a_resp) {
			renderList(a_resp, "Inbox");
		},
		error : function() {
			window.location.replace("SQLError.html");
		},
		data : {
			action : 'render',
		}
	});

}

function renderList(w_object, w_pageTitle, w_fromSort) {

	document.getElementById("pagetitlediv").innerHTML = w_pageTitle;

	var w_content = document.getElementById('content');

	var w_list = document.createElement('div');
	w_list.id = "commondiv";
	w_list.setAttribute('class', 'ldiv');

	var w_divWrapTable = document.createElement('div');
	w_divWrapTable.setAttribute("class", "wrap");

	var w_table = document.createElement('table');
	w_table.setAttribute("border", "1px");
	w_table.setAttribute("id", "tableborder");
	var w_row = document.createElement('tr');

	for ( var j = 0; j < w_object.config.length; j++) {

		var w_th = document.createElement('th');
		var w_header = document.createTextNode(w_object.config[j]['name']);
		w_th.appendChild(w_header);
		w_row.appendChild(w_th);

	}

	w_table.appendChild(w_row);
	w_divWrapTable.appendChild(w_table);
	w_list.appendChild(w_divWrapTable);

	var w_divListTableDiv = document.createElement('div');
	w_divListTableDiv.setAttribute("class", "wrap");
	w_divListTableDiv.setAttribute("id", "inner_table");

	var w_listTable = document.createElement('table');
	w_listTable.setAttribute("border", "1px");
	w_listTable.setAttribute("id", "myTable");

	var w_dataRow;
	var w_value;
	if (w_object.data.length > 0) {
		for ( var j = 0; j < w_object.data.length; j++) {
			var w_userId = w_object.data[j]['order'];
			w_dataRow = document.createElement('tr');
			w_dataRow.setAttribute("id", "tablerow");
			w_dataRow.setAttribute("class", "tablerow");
			for ( var k = 0; k < w_object.config.length; k++) {
				if (w_object.config[k]['order'] == k + 1) {
					var w_newtd = document.createElement('td');
					
					if(k==0){
						w_value = document.createTextNode(j+1);
						w_newtd.appendChild(w_value);
					}else{
						if(w_object.config[k]['name'] == "Priority"){
							var w_priorityDiv = createPriorityDiv(w_object.data[j][w_object.config[k]['name']]);
							w_newtd.appendChild(w_priorityDiv);
						}else{
							w_value = document.createTextNode(w_object.data[j][w_object.config[k]['name']]);
							w_newtd.appendChild(w_value);
						}
						
					}
					w_dataRow.appendChild(w_newtd);
				}
			}
			w_listTable.appendChild(w_dataRow);

			w_dataRow.onclick = function(a_e) {
				var w_Id = a_e.currentTarget.cells[1].innerText;
				var w_mode = "edit";
				$.ajax({
					type : "POST",
					url : "CommonController",
					dataType : 'json',
					success : function(a_resp) {
						if (a_resp['editUser'] == "editUser") {
							alert("You have clicked on Login Id: " + w_Id);
						} else {
							createForm(a_resp, w_mode, w_pageTitle);
						}
					},
					error : function(a_resp) {
						window.location.replace("SQLError.html");
					},
					data : {
						action : 'createDefect',
						defectId : w_Id,
						mode : 'edit',
					}
				});
			}
		}
	} else {	
		w_dataRow = document.createElement('tr');
		var w_newtd = document.createElement('td');
		w_newtd.setAttribute('colspan', '7');
		var w_value = document.createTextNode("No defects to show...");
		w_newtd.appendChild(w_value);
		w_dataRow.appendChild(w_newtd);
		w_listTable.appendChild(w_dataRow);
	}

	w_divListTableDiv.appendChild(w_listTable);
	w_list.appendChild(w_divListTableDiv);

	if (w_pageTitle == "All Defects") {
		var w_backBtn = backBtn();
		var w_backButtonDiv = document.createElement('div');
		w_backButtonDiv.id = "formbuttondiv";
		w_backButtonDiv.setAttribute('class', 'btnn');
		w_backButtonDiv.appendChild(w_backBtn);
		w_list.appendChild(w_backButtonDiv);
	}

	w_content.appendChild(w_list);

	var w_footerDiv = document.createElement('div');
	w_footerDiv.id = "fdiv";
	w_footerDiv.setAttribute('class', 'fdiv');
	var w_copyright = document.createTextNode('\u00A9' + " Copyright, 2016");
	w_footerDiv.appendChild(w_copyright);
	w_content.appendChild(w_footerDiv);
	
	if(w_fromSort != "fromSortFunction"){
		sortList(w_object,w_pageTitle);
	}
	
	if(w_fromSort != "fromFilterFunction"){
		filterList(w_object,w_pageTitle);
	}
	
}

function createPriorityDiv(w_priorityType){
	var w_priorityDiv = document.createElement('span');
	w_priorityDiv.setAttribute('class', 'prioritydiv');
	w_priorityDiv.setAttribute('title', w_priorityType);
	if(w_priorityType == "Critical"){
		w_priorityDiv.id = "priorityCritical";
	}else if(w_priorityType == "High"){
		w_priorityDiv.id = "priorityHigh";
	}else if(w_priorityType == "Medium"){
		w_priorityDiv.id = "priorityMedium";
	}else{
		w_priorityDiv.id = "priorityLow";
	}
	var w_optionvalue = document.createTextNode("aaa");
	w_priorityDiv.appendChild(w_optionvalue);
	return w_priorityDiv;
}

function filterList(w_listJsonObject,w_pageTitle){
	document.getElementById("filterdiv").innerHTML = "";
	var w_filterOptionDiv = document.getElementById("filterdiv");
	
	var w_select = document.createElement('select');
	w_select.setAttribute("class", "filterSelect");
	w_select.setAttribute("id", "filterSelect");
	
	var w_option = document.createElement('option');
	w_option.setAttribute("value", "#");
	var w_optionvalue = document.createTextNode("-select Filter Type-");
	w_option.appendChild(w_optionvalue);
	w_select.appendChild(w_option);
	
	for ( var j = 0; j < w_listJsonObject.config.length; j++) {
		
		if(w_listJsonObject.config[j]['name'] != "SrNo."){
		var w_option = document.createElement('option');
		w_option.setAttribute("value", w_listJsonObject.config[j]['name']);
		var w_optionvalue = document.createTextNode(w_listJsonObject.config[j]['name']);
		w_option.appendChild(w_optionvalue);
		w_select.appendChild(w_option);
		}
	}
	
	/*var w_option = createSelectOption("-select Filter Type-","#");
	w_select.appendChild(w_option);
	
	var w_option = createSelectOption("Critical","Critical");
	w_select.appendChild(w_option);
	
	var w_option = createSelectOption("High","High");
	w_select.appendChild(w_option);
	
	var w_option = createSelectOption("Medium","Medium");
	w_select.appendChild(w_option);
	
	var w_option = createSelectOption("Low","Low");
	w_select.appendChild(w_option);*/
	
	w_filterOptionDiv.appendChild(w_select);
	
	$('#filterSelect').change(function() {	
		$(this).selected = true;
		alert($(this).val());
		var w_priorityType= $(this).val();
		var w_filteredData = w_listJsonObject.data.filter(function (el) {
			  return el.Priority == w_priorityType;
			});
		
		var w_filteredJson = {"data" :  w_filteredData,
				"config" : w_listJsonObject.config};
		
		commondiv.parentElement.removeChild(commondiv);
		renderList(w_filteredJson, w_pageTitle,"fromFilterFunction"); 		
	});	
}

/*function createSelectOption(w_optionName, w_optionValue){
	var w_option = document.createElement('option');
	w_option.setAttribute("value", w_optionValue);
	var w_optionvalue = document.createTextNode(w_optionName);
	w_option.appendChild(w_optionvalue);
	return w_option;
}
*/
function sortList(w_listJsonObject,w_pageTitle){
	
	document.getElementById("sortdiv").innerHTML = "";
	var w_sortOptionDiv = document.getElementById("sortdiv");
	
	var w_select = document.createElement('select');
	w_select.setAttribute("class", "sortSelect");
	w_select.setAttribute("id", "sortSelect");
	
	w_option = document.createElement('option');
	w_option.setAttribute("value", "#");
	var w_optionvalue = document.createTextNode("-Select Sort Type-");
	w_option.appendChild(w_optionvalue);
	w_select.appendChild(w_option);
	
	for ( var j = 0; j < w_listJsonObject.config.length; j++) {
		var w_columnName = w_listJsonObject.config[j]['name'];	
		if(w_columnName == "DefectID" || w_columnName == "Name" || w_columnName =="Priority" || w_columnName=="First Name" || w_columnName == "Last Name" || w_columnName == "Role"){		
			for(var i=0; i<2 ; i++){
				var w_option = document.createElement('option');
				w_option.setAttribute("value", w_columnName);
				if(i==0){
					w_option.setAttribute("id", "asc");
					var w_optionvalue = document.createTextNode(w_columnName+"-Asc");
					w_option.appendChild(w_optionvalue);
				}else{
					w_option.setAttribute("id", "desc");
					var w_optionvalue = document.createTextNode(w_columnName+"-Desc");
					w_option.appendChild(w_optionvalue);
				}
				w_select.appendChild(w_option);
			}	
		}
	}
	
	w_sortOptionDiv.appendChild(w_select);
		
	$('#sortSelect').change(function() {	
		$(this).selected = true;
		function sorting(w_list, key_to_sort_by) {		
		    function sortByKey(a, b) {
		        var x = a[key_to_sort_by];
		        var y = b[key_to_sort_by];
		        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
		    }
		    w_list.sort(sortByKey);
		}	
		if($(this).val() != "#"){
			sorting(w_listJsonObject.data, $(this).val());
		}
		var w_selectedOptionId = $(this).children(":selected").attr("id");
		if(w_selectedOptionId == "desc"){
			w_listJsonObject.data.reverse();
		}
		commondiv.parentElement.removeChild(commondiv);
		renderList(w_listJsonObject, w_pageTitle,"fromSortFunction"); 		
	});	
}

function createForm(w_uList, w_formMode, w_pageTitle) {
	
	commondiv.parentElement.removeChild(commondiv);
	document.getElementById("sortdiv").innerHTML = "";
	document.getElementById("filterdiv").innerHTML = "";
	var w_content = document.getElementById('content');
	var w_list = document.createElement('div');
	w_list.id = "commondiv";
	w_list.setAttribute('class', 'cdiv');
	w_list.setAttribute("align", "center");

	var w_form = document.createElement('form');
	w_form.id = "form";
	w_form.setAttribute('class', 'form');

	var w_table = document.createElement('table');
	w_table.setAttribute("border", "1px");

	var w_formName;
	var w_defectStatus;
	for ( var i = 0; i < w_uList.length; i++) {
		if (w_uList[i].controlType == 'text'
				|| w_uList[i].controlType == 'password'
				|| w_uList[i].controlType == 'date'
				|| w_uList[i].controlType == 'checkbox'
				|| w_uList[i].controlType == 'textarea'
				|| w_uList[i].controlType == 'number'
				|| w_uList[i].controlType == 'email') {

			var w_row2 = document.createElement('tr');
			var w_input = document.createElement('input');

			var w_td = document.createElement('td');
			var w_label = document.createElement('label');
			var w_fieldName = document.createTextNode(w_uList[i].fieldName);

			w_label.appendChild(w_fieldName);
			w_td.appendChild(w_label);
			w_row2.appendChild(w_td);

			var w_td2 = document.createElement('td');
			w_input.setAttribute("type", w_uList[i].controlType);
			w_input.setAttribute("class", w_uList[i].id);
			w_input.setAttribute("id", w_uList[i].id);
			w_input.setAttribute("name", w_uList[i].id);
			w_input.setAttribute("onblur", w_uList[i].onblur);

			if (w_uList[i].status == 'disabled') {
				w_input.setAttribute("disabled", w_uList[i].status);
			}

			if (w_uList[i].fieldName == "Created By" || w_uList[i].fieldName == "Initiator") {
				w_input.setAttribute("value", w_uList[i].value);
			}

			if (w_formMode == "edit") {
				w_input.setAttribute("value", w_uList[i].fieldValue);
			}

			if (w_uList[i].mandatory == "yes") {
				w_input.required = true;
				w_label.setAttribute("class", "required");
			}
			w_td2.appendChild(w_input);
			
			if (w_uList[i].span == "yes") {
				var w_br2 = document.createElement('br');
				w_td2.appendChild(w_br2);
				var w_spanValidationMessage = document.createElement('span');
				w_spanValidationMessage.setAttribute("class", "Invalid"
						+ w_uList[i].id);
				w_spanValidationMessage.setAttribute("id", "Invalid"
						+ w_uList[i].id);
				w_spanValidationMessage.setAttribute("name", "Invalid"
						+ w_uList[i].id);
				w_td2.appendChild(w_spanValidationMessage);
			}

			w_row2.appendChild(w_td2);
			w_table.appendChild(w_row2);
		}

		if (w_uList[i].controlType == 'select' && w_uList[i].fieldName != 'Current User') {
			var w_row2 = document.createElement('tr');

			var w_td = document.createElement('td');
			var w_label = document.createElement('label');
			var w_fieldName = document.createTextNode(w_uList[i].fieldName);
			w_label.appendChild(w_fieldName);
			w_td.appendChild(w_label);
			w_row2.appendChild(w_td);

			var w_td2 = document.createElement('td');
			var w_select = document.createElement('select');
			w_select.setAttribute("class", w_uList[i].id);
			w_select.setAttribute("id", w_uList[i].id);
			w_select.setAttribute("name", w_uList[i].id);
			w_select.setAttribute("onblur", w_uList[i].onblur);
			if (w_uList[i].status == 'disabled') {
				w_select.setAttribute("disabled", w_uList[i].status);
			}

			if (w_uList[i].mandatory == "yes") {
				w_select.required = true;
				w_label.setAttribute("class", "required");
			}
			if (w_uList[i].id == "status") {
				w_defectStatus = w_uList[i].fieldValue;
			}
			
			var w_optionValues = w_uList[i].value.split(",");
			var w_option;
			for ( var w_value in w_optionValues) {

				w_option = document.createElement('option');
				w_option.setAttribute("value", w_optionValues[w_value]);
				if (w_formMode == "edit") {

					if (w_uList[i].fieldValue == w_optionValues[w_value]) {

						w_option.setAttribute("selected", "selected");
					}
				}
				var w_optionvalue = document.createTextNode(w_optionValues[w_value]);
				w_option.appendChild(w_optionvalue);
				w_select.appendChild(w_option);
			}

			w_td2.appendChild(w_select);
			w_row2.appendChild(w_td2);
			w_table.appendChild(w_row2);
		}

		if (w_uList[i].fieldName == 'Current User') {
			var w_row2 = document.createElement('tr');

			var w_td = document.createElement('td');
			var w_fieldName = document.createTextNode(w_uList[i].fieldName);
			w_td.appendChild(w_fieldName);
			w_row2.appendChild(w_td);

			var w_td2 = document.createElement('td');
			var w_select = document.createElement('select');
			w_select.setAttribute("class", w_uList[i].id);
			w_select.setAttribute("id", w_uList[i].id);
			w_select.setAttribute("name", w_uList[i].id);

			if (w_uList[i].status == 'disabled') {
				w_select.setAttribute("disabled", w_uList[i].status);
			}

			var w_defectListJson = w_uList[i].value;
			var w_option;
			for ( var w_i = 0; w_i < w_defectListJson.length; w_i++) {
				w_option = document.createElement('option');
				if (w_formMode == "edit") {
					if (w_uList[i].fieldValue == w_defectListJson[w_i].developerValue) {
						w_option.selected = true;
					}
				}
				w_option.setAttribute("value",w_defectListJson[w_i].developerValue);
				var w_optionvalue = document.createTextNode(w_defectListJson[w_i].developerName);
				w_option.appendChild(w_optionvalue);
				w_select.appendChild(w_option);
			}

			if (w_formMode == "edit") {
				w_option.setAttribute("value", w_uList[i].fieldValue);
			}

			w_td2.appendChild(w_select);
			w_row2.appendChild(w_td2);
			w_table.appendChild(w_row2);
		}

		if (w_uList[i].controlType == 'radio') {
			var w_row2 = document.createElement('tr');

			var w_td = document.createElement('td');
			var w_fieldName = document.createTextNode(w_uList[i].fieldName);
			w_td.appendChild(w_fieldName);
			w_row2.appendChild(w_td);

			var w_td2 = document.createElement('td');

			var w_radioValues = w_uList[i].value.split(",");
			var w_radioInput;
			for ( var w_value in w_radioValues) {
				w_radioInput = document.createElement('input');
				w_radioInput.setAttribute("type", w_uList[i].controlType);
				w_radioInput.setAttribute("class", w_uList[i].id);
				w_radioInput.setAttribute("id", w_uList[i].id);
				w_radioInput.setAttribute("name", w_uList[i].id);
				w_radioInput.setAttribute("value", w_radioValues[w_value]);

				if (w_uList[i].status == "disabled") {
					w_radioInput.setAttribute("disabled", w_uList[i].status);
				}
				if (w_radioValues[w_value] == "N") {
					w_radioInput.setAttribute("checked", "checked");
				}

				if (w_radioValues[w_value] == "Y") {
					if (w_formMode == "edit" && w_uList[i].fieldValue == "Y") {
						w_radioInput.setAttribute("checked", "checked");
					}
					w_radioInput.onclick = function(event) {
						$('#priority').val("Critical");
						document.getElementById("priority").readOnly = true;
						w_radioInput.setAttribute("checked", "checked");
					}
				}

				w_td2.appendChild(w_radioInput);
				var w_radiovalue = document.createTextNode(w_radioValues[w_value]);
				w_td2.appendChild(w_radiovalue);
			}
			w_row2.appendChild(w_td2);
			w_table.appendChild(w_row2);
		}

		if (w_uList[i].controlType == 'hidden') {
			var w_input = document.createElement('input');
			w_input.setAttribute("type", w_uList[i].controlType);
			w_input.setAttribute("class", w_uList[i].id);
			w_input.setAttribute("id", w_uList[i].id);
			w_input.setAttribute("name", w_uList[i].id);
			w_input.setAttribute("value", w_uList[i].value);

			var w_defectname = w_uList[i].defectName;

			if (w_defectname != null) {
				document.getElementById("pagetitlediv").innerHTML = "Edit: "+ w_defectname;
			} else {
				document.getElementById("pagetitlediv").innerHTML = "";
			}
			w_formName = w_uList[i].value;
		}
	}

	w_form.appendChild(w_table);
	w_list.appendChild(w_form);

	var w_nextLine5 = document.createElement('br');
	w_list.appendChild(w_nextLine5);

	var w_formButtonDiv = document.createElement('div');
	w_formButtonDiv.id = "formbuttondiv";
	w_formButtonDiv.setAttribute('class', 'formbuttondiv');

	if ((w_defectStatus == "Open" && w_formMode == "edit")|| w_formMode == "create") {
		var w_btnSave = document.createElement('button');
		w_btnSave.id = "savebtnn";
		w_btnSave.setAttribute('class', 'savebtnn');

		var w_btnNameSave = document.createTextNode("Save");
		w_btnSave.appendChild(w_btnNameSave);
		w_formButtonDiv.appendChild(w_btnSave);

		$(w_btnSave).click(function(a_e) {
					if (w_formName == "CreateDefect") {
								if (document.getElementById('resolved').disabled == false && document.getElementById('resolved').checked == false) {
									alert("Defect has not been resolved yet. So, you cannot save!!!");
								} else if (document.getElementById('resolved').disabled == false && document.getElementById('resolved').checked == true) {
									$("#resolved").val("R");
									document.getElementById('resolved').disabled = true;
									document.getElementById('resolution').disabled = true;
									$("#status").val("Closed");									
									var w_d = getSystemDate();
									$("#dateclosed").val(w_d);
									var w_check = mandatoryFieldsValidation();
									if (w_check) {
										saveDetails(w_formName);
									}
								} else {
									var w_id;
									var w_check = mandatoryFieldsValidation();
									if (w_check) {
										saveDetails(w_formName);
									}
								}
					 } else {
								var w_check = mandatoryFieldsValidation();
								if (w_check) {
									saveDetails(w_formName);
								}
					}
		});
	}

	var w_createFormBackBtn = backBtn();

	if (w_formMode == "create") {
		var w_btnSaveAddNew = document.createElement('button');
		w_btnSaveAddNew.id = "saveaddnewbtnn";
		w_btnSaveAddNew.setAttribute('class', 'saveaddnewbtnn');

		var w_btnNameSave = document.createTextNode("Save and Add New");
		w_btnSaveAddNew.appendChild(w_btnNameSave);
		w_formButtonDiv.appendChild(w_btnSaveAddNew);

		$(w_btnSaveAddNew).click(function(a_e) {
			var w_check = mandatoryFieldsValidation();
			if (w_check) {
				saveDetails(w_formName, "btnSaveAddNew");
			}
		});
		w_createFormBackBtn.setAttribute('onClick', 'createFormBackBtn()');
	}

	if ($("#defectid").val() > 0) {
		w_createFormBackBtn.setAttribute('onClick', 'backBtnOnclick()');
	}

	w_formButtonDiv.appendChild(w_createFormBackBtn);
	w_list.appendChild(w_formButtonDiv);

	var w_nextLine6 = document.createElement('br');
	w_list.appendChild(w_nextLine6);

	var w_nextLine7 = document.createElement('br');
	w_list.appendChild(w_nextLine7);

	w_content.appendChild(w_list);
}

function createFormBackBtn() {

	var w_form = document.getElementsByTagName('form');
	var w_formElem = w_form[0];
	var w_temp;
	var w_ChangeFieldCount = 0;
	var w_tag = w_formElem.getElementsByTagName('input');
	for ( var i = 0; i < w_tag.length; i++) {
		w_temp = w_tag[i];
		if ($("#" + w_temp.id).val() != "") {
			w_ChangeFieldCount++;
		}
	}

	if (w_ChangeFieldCount > 0) {
		var w_confirmBoxValue = confirm("You have not saved changes. Do you want to continue ?");
		if (w_confirmBoxValue == true) {
			commondiv.parentElement.removeChild(commondiv);
			getList();
		}
	} else {
		commondiv.parentElement.removeChild(commondiv);
		getList();
	}
}

function getSystemDate() {
	var w_d = new Date();
	var dd = w_d.getDate();
	var mm = w_d.getMonth() + 1;
	var yyyy = w_d.getFullYear();

	if (dd < 10) {
		dd = '0' + dd
	}
	if (mm < 10) {
		mm = '0' + mm
	}
	w_d = yyyy + '-' + mm + '-' + dd;
	return w_d;
}

function backBtn() {
	var w_backBtn = document.createElement('button');
	w_backBtn.id = "btnn";
	w_backBtn.setAttribute('class', 'btnn');
	w_backBtn.setAttribute('onClick', 'backBtnOnclick()');
	var w_btnname = document.createTextNode("Back");
	w_backBtn.appendChild(w_btnname);
	return w_backBtn;
}

function backBtnOnclick() {
	commondiv.parentElement.removeChild(commondiv);
	getList();
}

function saveDetails(w_formName, w_saveBtnType) {

	document.getElementById('savebtnn').disabled = true;
	var w_query = "insert";

	if ($("#defectid").val() != 0) {
		w_query = "update";
	}

	var w_form = document.getElementsByTagName('form');
	var w_formElem = w_form[0];
	var w_temp;
	var w_element = {};
	var w_tag = w_formElem.getElementsByTagName('input');
	for ( var i = 0; i < w_tag.length; i++) {
		w_temp = w_tag[i];

		if (w_temp.type == "radio") {
			var w_radioChecked = $("input[name='" + w_temp.id + "']:checked")
					.val();
			w_element[w_temp.id] = w_radioChecked;
		} else {
			w_element[w_temp.id] = w_temp.value;
		}

	}

	var w_tag = w_formElem.getElementsByTagName('select');
	for ( var i = 0; i < w_tag.length; i++) {
		w_temp = w_tag[i];
		w_element[w_temp.id] = w_temp.value;
	}

	var w_detailsList = {"detailsData" : [ w_element ]};

	$.ajax({
				type : "POST",
				url : "CommonController",
				dataType : 'json',
				success : function(w_resp) {
					document.getElementById('savebtnn').disabled = false;
					if (w_saveBtnType == "btnSaveAddNew") {
						alert("New Entry Created successfully");
						var w_tag = w_formElem.getElementsByTagName('input');
						for ( var i = 0; i < w_tag.length; i++) {
							w_temp = w_tag[i];
							if (i == 1) {
								w_temp.focus();
							}
							$("#" + w_temp.id).val("");
						}

						var w_tag = w_formElem.getElementsByTagName('select');
						for ( var i = 0; i < w_tag.length; i++) {
							w_temp = w_tag[i];
							$("#" + w_temp.id).val("");
						}

					} else {

						if (w_resp['status'] == "failed") {
							alert("DB Problem: Creation failed!!!!!")
						} else {
						
							$("#btnn").attr('onClick', 'backBtnOnclick()');
							var w_saveaddnewbtnn = document.getElementById('saveaddnewbtnn');
							if (w_saveaddnewbtnn != null) {
								document.getElementById('saveaddnewbtnn').disabled = true;
							}
							if (w_formName == "CreateDefect") {
								document.getElementById("pagetitlediv").innerHTML = "Edit: "+ $("#name").val();
								if (w_query == "insert") {
									alert("Defect Created successfully and opened in edit mode");
								} else {
									alert("Defect updated successfully and opened in edit mode");
								}

								for (w_key in w_resp) {
									$("#" + w_key).val(w_resp[w_key]);
								}

							} else {
								alert("User Created Successfully");
								commondiv.parentElement.removeChild(commondiv);
								getList();
							}
						}
					}
				},
				error : function() {
					window.location.replace("SQLError.html");
				},
				data : {
					action : 'saveDetails',
					json : JSON.stringify(w_detailsList),
					formname : w_formName,
					query : w_query
				}
			});
}
function myFunction() {
	  var input, filter, table, tr, td, i;
	  input = document.getElementById("searchbar");
	  filter = input.value.toUpperCase();
	  table = document.getElementById("myTable");
	  tr = table.getElementsByTagName("tr");
	  for (i = 0; i < tr.length; i++) {
	    td = tr[i].getElementsByTagName("td")[1];
	    if (td) {
	      if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
	        tr[i].style.display = "";
	      } else {
	        tr[i].style.display = "none";
	      }
	    }       
	  }
}