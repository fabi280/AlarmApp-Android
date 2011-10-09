app.views.HomeView = Ext.extend(Ext.Panel, {
    title: "Start",
    items: [
    	new Ext.Button({
    		text:"Geräteinfos",
    		 handler: function(button, tap) {
                	Ext.getCmp('mainPanel').layout.setActiveItem('smartphone_details');
                }
    		}),
    	new Ext.Button({
    		text:"Authentifizieren",
    		handler: function(button, tap) {
    			Ext.getCmp('mainPanel').layout.setActiveItem('authenticate');
    		}
    	}),
    	new Ext.Button({
    		text:"Smartphone erstellen",
    	}),
    ],
    initComponent: function() {
        Ext.apply(this, {
            dockedItems: [{
                xtype: "toolbar",
                title: "Übersicht",
               
            }]
        });
        app.views.HomeView.superclass.initComponent.apply(this, arguments);
    }
});

Ext.reg('home_view', app.views.HomeView);