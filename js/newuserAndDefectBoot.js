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
			console.log("gvchbnm");
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
	
	var w_container = document.createElement('div');
	w_container.setAttribute('class', 'container');
	
	
	var w_navbar = document.createElement('div');
	w_navbar.setAttribute('class', 'navbar navbar-default navbar-fixed-top');
	w_navbar.setAttribute('role', 'navigation');
	
	var w_containerDiv = document.createElement('div');
	w_containerDiv.setAttribute('class', 'container');
	
	var w_navbarHeaderDiv = document.createElement('div');
	w_navbarHeaderDiv.setAttribute('class', 'navbar-header');
	
	var w_toggleButton = document.createElement('button');
	w_toggleButton.setAttribute('type', 'button');
	w_toggleButton.setAttribute('class', 'navbar-toggle');
	w_toggleButton.setAttribute('data-toggle', 'collapse');
	w_toggleButton.setAttribute('data-target', '.navbar-collapse');
	
	var w_iconbarSpan1 = document.createElement('span');
	w_iconbarSpan1.setAttribute('class', 'icon-bar');
	w_toggleButton.appendChild(w_iconbarSpan1);
	
	var w_iconbarSpan2 = document.createElement('span');
	w_iconbarSpan2.setAttribute('class', 'icon-bar');
	w_toggleButton.appendChild(w_iconbarSpan2);
	
	var w_iconbarSpan3 = document.createElement('span');
	w_iconbarSpan3.setAttribute('class', 'icon-bar');
	w_toggleButton.appendChild(w_iconbarSpan3);

	w_navbarHeaderDiv.appendChild(w_toggleButton);
	
	var w_rowDiv = document.createElement('div');
	w_rowDiv.setAttribute('class', 'row');
	
	var w_logocolumnDiv = document.createElement('div');
	w_logocolumnDiv.setAttribute('class', 'col-sm-5');
	
	var w_logoImageLink =  document.createElement('a');
	w_logoImageLink.setAttribute('href', 'baseBoot.html');
	w_logoImageLink.setAttribute('class', 'navbar-brand');
	
	var w_logoImage = document.createElement('img');
	w_logoImage.setAttribute('src', 'media/logo.png');
	w_logoImage.setAttribute('class', 'img-responsive img-rounded logoImageStyle');
	w_logoImageLink.appendChild(w_logoImage);
	
	w_logocolumnDiv.appendChild(w_logoImageLink);
	w_rowDiv.appendChild(w_logocolumnDiv);
	
	var w_nameColumnDiv = document.createElement('div');
	w_nameColumnDiv.setAttribute('class', 'col-sm-7 nameStyle');
	
	var w_name = document.createTextNode("DefectTracker");
	
	w_nameColumnDiv.appendChild(w_name);
	w_rowDiv.appendChild(w_nameColumnDiv);
	
	w_navbarHeaderDiv.appendChild(w_rowDiv);
	w_containerDiv.appendChild(w_navbarHeaderDiv);
	
	var w_loggedInUserDetailsDiv = document.createElement('div');
	w_loggedInUserDetailsDiv.setAttribute('class', 'navbar-collapse collapse');
	w_loggedInUserDetailsDiv.setAttribute('id', 'collapseDiv');
	
	var w_ulList = document.createElement('ul');
	w_ulList.setAttribute('class', 'nav navbar-nav navbar-right');
	
	var w_li = document.createElement('li');
	w_li.setAttribute('class', 'dropdown liStyle');
	
	var w_nameLink =  document.createElement('a');
	w_nameLink.setAttribute('href', '#');
	w_nameLink.setAttribute('class', 'dropdown-toggle');
	w_nameLink.setAttribute('data-toggle', 'dropdown');
	
	var w_userRowDiv = document.createElement('div');
	w_userRowDiv.setAttribute('class', 'row');
	
	var w_col1 = document.createElement('div');
	w_col1.setAttribute('class', 'col-sm-4');
	
	var w_roleLogo = document.createElement('img');
	if (w_loggedInUserDetails['role'] == "Admin") {
		w_roleLogo.setAttribute('src', 'media/adminuser.png');
	} else {
		w_roleLogo.setAttribute('src', 'media/dev_test.png');
	}
	w_roleLogo.setAttribute('title', w_loggedInUserDetails['role']);
	w_roleLogo.id = "rolelogo";
	w_roleLogo.setAttribute('class', 'img-responsive img-circle roleImgStyle');
	w_col1.appendChild(w_roleLogo);
	w_userRowDiv.appendChild(w_col1);
	
	var w_col2 = document.createElement('div');
	w_col2.setAttribute('class', 'col-sm-8');
	var w_userNameSt = document.createElement('strong');
	w_userNameSt.setAttribute('class', 'userName');
	var w_userName = document.createTextNode(w_loggedInUserDetails['name'] + "  ");
	w_userNameSt.appendChild(w_userName);
	w_col2.appendChild(w_userNameSt);
	
	var w_downGlyphSpan = document.createElement('span');
	w_downGlyphSpan.setAttribute('class', 'glyphicon glyphicon-chevron-down userName');
	w_col2.appendChild(w_downGlyphSpan);
	w_userRowDiv.appendChild(w_col2);
	w_nameLink.appendChild(w_userRowDiv);
	
	
	w_li.appendChild(w_nameLink);
	
	var w_ulDropdownMenu = document.createElement('ul');
	w_ulDropdownMenu.setAttribute('class', 'dropdown-menu');
	
	var w_liDropdownMenu = document.createElement('li');
	var w_navbarLogin = document.createElement('div');
	w_navbarLogin.setAttribute('class', 'navbar-login');
	
	var w_navbarLoginRow = document.createElement('div');
	w_navbarLoginRow.setAttribute('class', 'row');
	
	var w_column1 = document.createElement('div');
	w_column1.setAttribute('class', 'col-lg-4');
	var w_pTag = document.createElement('p');
	w_pTag.setAttribute('class', 'text-center');
	var w_userGlyphSpan = document.createElement('span');
	w_userGlyphSpan.setAttribute('class', 'glyphicon glyphicon-user icon-size');
	w_pTag.appendChild(w_userGlyphSpan);
	w_column1.appendChild(w_pTag);
	w_navbarLoginRow.appendChild(w_column1);
	
	var w_column2 = document.createElement('div');
	w_column2.setAttribute('class', 'col-lg-8');
	var w_pTag1 = document.createElement('p');
	w_pTag1.setAttribute('class', 'text-left');
	var w_userNameIn = document.createTextNode(w_loggedInUserDetails['name']);
	w_pTag1.appendChild(w_userNameIn);
	w_column2.appendChild(w_pTag1);
	
	var w_pTag2 = document.createElement('p');
	w_pTag2.setAttribute('class', 'text-left small');
	var w_userEmailId = document.createTextNode(w_loggedInUserDetails['loginId']);
	w_pTag2.appendChild(w_userEmailId);
	w_column2.appendChild(w_pTag2);
	
	var w_pTag3 = document.createElement('p');
	w_pTag3.setAttribute('class', 'text-left small');
	var w_userLoggedInTime = document.createTextNode(w_loggedInUserDetails['loggedInTime']);
	w_pTag3.appendChild(w_userLoggedInTime);
	w_column2.appendChild(w_pTag3);
	w_navbarLoginRow.appendChild(w_column2);
	
	w_navbarLogin.appendChild(w_navbarLoginRow);	
	w_liDropdownMenu.appendChild(w_navbarLogin);
	w_ulDropdownMenu.appendChild(w_liDropdownMenu);
	
	var w_liDivider = document.createElement('li');
	w_liDivider.setAttribute('class', 'divider');
	w_ulDropdownMenu.appendChild(w_liDivider);
	
	var w_list = document.createElement('li');
	var w_logoutUserBtnDiv = document.createElement('div');
	w_logoutUserBtnDiv.setAttribute('class', 'navbar-login navbar-login-session');
	var w_logoutRow = document.createElement('div');
	w_logoutRow.setAttribute('class', 'row');
	
	var w_logoutColumn = document.createElement('div');
	w_logoutColumn.setAttribute('class', 'col-lg-12');
	var w_pLogout = document.createElement('p');
	
	var w_logoutBtn = document.createElement('button');
	w_logoutBtn.id = "logoutbtn";
	w_logoutBtn.setAttribute('class', 'btn btn-primary');
	var w_btnname = document.createTextNode("Logout");
	w_logoutBtn.appendChild(w_btnname);
	w_pLogout.appendChild(w_logoutBtn);
	
	w_logoutColumn.appendChild(w_pLogout);
	w_logoutRow.appendChild(w_logoutColumn);
	w_logoutUserBtnDiv.appendChild(w_logoutRow);
	w_list.appendChild(w_logoutUserBtnDiv);
	w_ulDropdownMenu.appendChild(w_list);
	w_li.appendChild(w_ulDropdownMenu);
	
	w_ulList.appendChild(w_li);
	w_loggedInUserDetailsDiv.appendChild(w_ulList);
	w_containerDiv.appendChild(w_loggedInUserDetailsDiv);
	w_navbar.appendChild(w_loggedInUserDetailsDiv);
	w_container.appendChild(w_navbar);
	w_content.appendChild(w_container);
	
	$(w_logoutBtn).click(function(a_e) {

		var w_logoutValue = confirm("Do you really want to Logout?");
		if (w_logoutValue) {
			$.ajax({
				type : "POST",
				url : "CommonController",
				dataType : 'json',
				success : function() {
					window.location.replace("loginBoot.html");
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
}
	