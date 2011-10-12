app.views.AuthenticateView = Ext.extend(Ext.Panel, {
    title: "Anmelden",
    items: [
    	
    	new Ext.Panel({
    		margin: "10",
    		html:"<p>Sie müssen Sich annmelden, um den Alarmierungsdienst zu nutzen. " +
    		"Wenn Sie noch keinen Benutzeraccount haben, können Sie einen neuen "+
    		"Account erstellen und anschließend Ihrer Feuerwehr beitreten. " +
    		"Bitte geben Sie zum Anmelden Ihre Emailadresse und Ihr Passwort ein:</p>",
    	}),
    	
    	
    	new Ext.form.FormPanel({
    		items: [
	        {
	            xtype: 'textfield',
	            name : 'email',
	            id   : 'emailfield',
	            label: 'Email'
	        }]
        }),
        
        new Ext.form.FormPanel({
    		items: [
	        {
	            xtype: 'passwordfield',
	            name : 'password',
	            id   : 'passwordfield',
	            label: 'Passwort'
	        }]
        }),
    	
    	new Ext.Panel({
    		layout: {
    			type:'hbox',
    			align: 'stretch',
    			pack: 'center',
    		},
    		items: [
    			new Ext.Button({
    				text:"Anmelden",
    				margin: "5 5 5 5",
    				handler: function(btn, evt) {
    					var email = Ext.getCmp('emailfield').getValue();
    					var password = Ext.getCmp('passwordfield').getValue();
    					
    					var loadingMask = new Ext.LoadMask(Ext.getBody(), {msg: "Bitte warten..."});
    					loadingMask.show();
    					AlarmService.login(email, 
    							password,
    							function(result) {
    									var user = result.user
    									var dept = result.fire_department
    									
    									app.stores.UserInfos.add(
    									{
    										UserId: 	user.id, 
    										Firstname:	user.first_name, 
    										Lastname:	user.last_name,
    										FireDepartment: dept.name,
    										FireDepartmentId: dept.id
    									})
    									
    									app.stores.UserInfos.sync()
    									
    									loadingMask.hide();
    									
    									console.log("Hello " + result.user.first_name + " " + result.user.last_name)
    									console.log("Your id is " + result.user.id)
    									console.log("Your department is " + result.fire_department.name)
    									console.log("Your departments id is " + result.fire_department.id)
    									console.log(Ext.encode(result));
    							},
    							function(result) {
    									loadingMask.hide();
    									alert("Login failed: ");
    									console.log(Ext.encode(result));
    								});
    					console.log("do login for " + email + " with password " + password );
    				}
    			}),
    			new Ext.Button({
    				text:"Account erstellen",
    				margin: "5 5 5 0",
    			}),
    		]
    	}),
    ],
    initComponent: function() {
        Ext.apply(this, {
            dockedItems: [{
               xtype: "toolbar",
               title: "Anmelden",
               items:[
               		new Ext.Button({
               			text:"Zurück",
               			ui:"back",
               			handler: function(btn,evt) {
               				Ext.getCmp('mainPanel').layout.setActiveItem('home');
               			},
               		}),
               ],
            }]
        });
        app.views.AuthenticateView.superclass.initComponent.apply(this, arguments);
    }
});

Ext.reg('authenticate', app.views.AuthenticateView);