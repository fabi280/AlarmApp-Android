//alert( localStorage.site );
gApp = new Array();

gApp.deviceready = false;
gApp.c2dmregid = '';

window.onbeforeunload = function(e) {
	if ( gApp.c2dmregid.length > 0 )
	{
		// The same routines are called for success/fail on the unregister. You can make them unique if you like
		window.plugins.C2DM.unregister( C2DM_Success, C2DM_Fail ); // close the C2DM
	
	}
};

//$(document).bind("mobileinit", function() {

console.log( 'Mobileinit event received' );

document.addEventListener('deviceready', function() {
	// This is the PhoneGap deviceready event. Once this is called PhoneGap is available to be used
	console.log( 'deviceready event received' );
	
	console.log( 'calling C2DM.register, register our email with Google' );
	
	gApp.DeviceReady = true;
	
	// Some Unique stuff here,
	// The first Parm is your Google email address that you were authorized to use C2DM with
	// the Event Processing rountine (2nd parm) we pass in the String name
	// not a pointer to the routine, under the covers a JavaScript call is made so the name is used
	// to generate the function name to call. I didn't know how to call a JavaScript routine from Java
	// The last two parms are used by PhoneGap, they are the callback routines if the call is successful or fails
	//
	// CHANGE: your_c2dm_account@gmail.com
	// TO: what ever your C2DM authorized email account name is
	//
	window.plugins.C2DM.register("f.englert@gmail.com", "C2DM_Event", C2DM_Success, C2DM_Fail );
	
}, false );


//});

function C2DM_Event(e) {
	var li = '';
	console.log('EVENT -> RECEIVED:' + e.event);
	
	switch( e.event )
	{
		case 'registered':
			// the definition of the e variable is json return defined in C2DMReceiver.java
			// In my case on registered I have EVENT and REGID defined
			gApp.c2dmregid = e.regid;
			if ( gApp.c2dmregid.length > 0 )
			{
				console.log('REGISTERED -> REGID:' + e.regid);
				
				// ==============================================================================
				// ==============================================================================
				// ==============================================================================
				//
				// This is where you would code to send the REGID to your server for this device
				//
				// ==============================================================================
				// ==============================================================================
				// ==============================================================================
			}
		
		break
		
		case 'message':
			// the definition of the e variable is json return defined in C2DMReceiver.java
			// In my case on registered I have EVENT, MSG and MSGCNT defined
			
			// You will NOT receive any messages unless you build a HOST server application to send
			// Messages to you, This is just here to show you how it might work
			
			console.log('MESSAGE -> MSG:' + e.msg);
			console.log('MESSAGE -> MSGCNT:' + e.msgcnt);
		
		break;
		
		
		case 'error':
			console.log('ERROR -> MSG:' + e.msg);
		break;
		
		
		
		default:
			console.log('EVENT -> Unknown, an event was received and we do not know what it is');
		break;
	}
}

function C2DM_Success(e) {
	console.log('C2DM_Success -> We have successfully registered and called the C2DM plugin, waiting for C2DM_Event:reistered -> REGID back from Google');
}

function C2DM_Fail(e) {
	console.log('C2DM_Fail -> C2DM plugin failed to register');
	
	console.log('C2DM_Fail -> ' + e.msg);
}