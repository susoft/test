<%@page session="false" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Media Query Demos</title>
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.2/jquery.mobile-1.4.2.min.css">
<script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="http://code.jquery.com/mobile/1.4.2/jquery.mobile-1.4.2.min.js"></script>
<!-- <script type="text/JavaScript" src="./resources/js/cordova.js"></script> -->

<script type="text/javascript">
function fnClick(meetingCd) {
	//$("#myPopupDialog").popup();
	//$('#myPopupDialog').popup('open');
	console.log(meetingCd);
	
	//$.mobile.changePage( "#download", { role: "diolog" } );
}

function choosePopup(meetingCd, meetingNm) {
	meetingCd_temp = meetingCd;
	meetingNm_temp = meetingNm;
	
	$("#meetingNm").val(meetingNm);
	$("#meetingCd").val(meetingCd);
	
	$("#popupMenu").popup();
	$('#popupMenu').popup('open');
}

function modifyMeeting() {
	var meetingCd = meetingCd_temp;
	console.log(meetingCd);
	
	$.mobile.changePage( "#download", { role: "diolog" } );
	
	//Dialog opened 
	/* $('#download').on("pageshow", function() {
	    alert("Opened");
	}); */
	
	// Dialog closed 
	$('#download').on("pagehide", function() {
		console.log("deleteMeeting");
		deleteMeeting();
	});
}

function deleteMeeting() {
	//$('#popupMenu').popup('close');
	console.log("deleteMeeting");
	$.mobile.changePage( "#dialogResult", { role: "diolog" } );
}

//attaches the "scroll" event
/* $(window).scroll(function (e) {
    var body = document.body,
        scrollTop = this.pageYOffset || body.scrollTop;
    if (body.scrollHeight - scrollTop === this.innerHeight) {
      console.log("► End of scroll");
    }
}); */

$(document).on("pagecreate","#pageone",function(){
	   $(document).scroll(function() {
			console.log("► start of scroll");
			
			var documentHeight = $(document).height();
		    var scrollDifference = $(window).height() + $(window).scrollTop();
		    
		    console.log(documentHeight + "-" +  scrollDifference);
		    
		    if (documentHeight == scrollDifference) {
		    	console.log("► End of scroll");
		    }
		}); 
	  
	});


	var meetingCd_temp;
	var meetingNm_temp;
	
/* // A button will call this function
//
function getPhoto(source) {
  	// Retrieve image file location from specified source
  	navigator.camera.getPicture(onPhotoURISuccess, onFail, { quality: 50, 
    destinationType: destinationType.FILE_URI,
    sourceType: source });
}

var pictureSource;   // picture source
var destinationType; // sets the format of returned value 

//Wait for PhoneGap to connect with the device
//
document.addEventListener("deviceready",onDeviceReady,false);

// PhoneGap is ready to be used!
//
function onDeviceReady() {
    pictureSource=navigator.camera.PictureSourceType;
    destinationType=navigator.camera.DestinationType;
}

//Called when a photo is successfully retrieved
//
function onPhotoDataSuccess(imageData) {
  // Uncomment to view the base64 encoded image data
  // console.log(imageData);

  // Get image handle
  //
  var smallImage = document.getElementById('smallImage');

  // Unhide image elements
  //
  smallImage.style.display = 'block';

  // Show the captured photo
  // The inline CSS rules are used to resize the image
  //
  smallImage.src = "data:image/jpeg;base64," + imageData;
}

// Called when a photo is successfully retrieved
//
function onPhotoURISuccess(imageURI) {
  // Uncomment to view the image file URI 
  // console.log(imageURI);

  // Get image handle
  //
  var largeImage = document.getElementById('largeImage');

  // Unhide image elements
  //
  largeImage.style.display = 'block';

  // Show the captured photo
  // The inline CSS rules are used to resize the image
  //
  largeImage.src = imageURI;
}

//Called if something bad happens.
// 
function onFail(message) {
  alert('Failed because: ' + message);
} */

</script>
</head>

<body>
	<div data-role="page" id="pageone">
		<div data-role="header">
			<a href="#"
				class="ui-btn ui-corner-all ui-shadow ui-icon-home ui-btn-icon-left">Home</a>
			<h1>교류회</h1>
			<a href="#"
				class="ui-btn ui-corner-all ui-shadow ui-icon-search ui-btn-icon-left">Search</a>
		</div>
		
		<div data-role="main" class="ui-content" id="myDiv">
			<ul data-role="listview" data-inset="true">
				
				<li>
					<a href="javascript:fnClick('001');">
						회장 : <br>
						총무 : <br>
						회원수 : <br>
					</a>
					<!-- <a href="javascript:choosePopup('01111', 'test');" data-icon="gear"></a> -->
					<a href="javascript:choosePopup('01111', 'test1114');" data-rel="popup" data-icon="gear"></a>
				</li>
				
				<li>
					<a href="javascript:fnClick('001');">
						회장 : <br>
						총무 : <br>
						회원수 : <br>
					</a>
					<!-- <a href="javascript:choosePopup('01111', 'test');" data-icon="gear"></a> -->
					<a href="javascript:choosePopup('01111', 'test41');" data-rel="popup" data-icon="gear"></a>
				</li>
				
				<li>
					<a href="javascript:fnClick('001');">
						회장 : <br>
						총무 : <br>
						회원수 : <br>
					</a>
					<!-- <a href="javascript:choosePopup('01111', 'test');" data-icon="gear"></a> -->
					<a href="javascript:choosePopup('01111', 'test33');" data-rel="popup" data-icon="gear"></a>
				</li>
				
				
			</ul>

			<div data-role="popup" id="popupMenu">
				<ul data-role="listview" data-inset="true">
					<li data-role="divider" data-theme="a">교류회 관리 목록</li>
					<li><a href="javascript:modifyMeeting();">수정</a></li>
					<li><a href="javascript:deleteMeeting();">삭제</a></li>
				</ul>
			</div>
		</div>
	</div>

	<div data-role="page" id="download" data-dialog="true">
		<div data-role="header">
			<h1>교류회 수정</h1>
		</div>

		<div data-role="main" class="ui-content">
			<label for="meetingNm">교류회명:</label> <input type="text"
					name="meetingNm" id="meetingNm"> <input type="hidden"
					name="meetingCd" id="meetingCd"> <a href="#"
					class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b ui-icon-back ui-btn-icon-right"
					data-rel="back">Go Back</a><a href="javascript:getPhoto(2)"
					class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b ui-icon-back ui-btn-icon-right"
					>photo</a>
					
					<img style="width:60px;height:60px;" id="smallImage" src="" />
    <img style="display:none;" id="largeImage" src="" />
		</div>
	</div>
	
	<div data-role="page" id="dialogResult" data-dialog="true">
			<div data-role="header">
				<h1>교류회 변경 결과</h1>
			</div>
			<div data-role="main" class="ui-content">
				<p id="resultMessage">ㅅㄷㄴㅅ</p>
			</div>
		</div>
</body>
</html>
