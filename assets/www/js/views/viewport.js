app.views.Viewport = Ext.extend(Ext.TabPanel, {
    fullscreen: true,
	id: 'mainPanel',
    initComponent: function() {
    	
    	app.stores.UserInfos.load()
    	console.log("Count " + app.stores.UserInfos.getCount())
    	
    	console.log("Loaded user id from store is " + Ext.encode(app.stores.UserInfos.first().UserId));
    	
        Ext.apply(this, {
            tabBar: {
                dock: 'bottom',
                layout: {
                    pack: 'center'
                }
            },
            items: [
                { xtype: 'authenticate', id:'authenticate'},
                { xtype: 'c2dm_view', id:'c2dm'},
                { xtype: 'home_view', id:"home" },
                { xtype: 'smartphone_details', id:"smartphone_details"},
            ]
        });
        app.views.Viewport.superclass.initComponent.apply(this, arguments);
    }
});
