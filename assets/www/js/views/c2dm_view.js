function c2dm_message(e) {
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

app.views.C2DMView = Ext.extend(Ext.Panel, {
	title:"Google C2DM",
    initComponent: function() {
        Ext.apply(this, {
            dockedItems: [{
                xtype: "toolbar",
                title: "Google C2DM",
                items: [
                        new Ext.Button({
                        	text:"Zurück",
                        	ui:"back",
                        	handler:function(btn, evt) {
                        		Ext.getCmp('mainPanel').layout.setActiveItem('home');
                        	}
                        }),
                        ],
            }],
        });
        app.views.HomeView.superclass.initComponent.apply(this, arguments);
    },
	items: [
        new Ext.Button({
        	text:"Registrieren",
        	handler: function(button, tap) { 
        		console.log("call C2DM register")
        		window.plugins.C2DM.register("f.englert@gmail.com", "c2dm_message", C2DM_Success, C2DM_Fail );
        	}
        }),
    ],
});

Ext.reg("c2dm_view", app.views.C2DMView)